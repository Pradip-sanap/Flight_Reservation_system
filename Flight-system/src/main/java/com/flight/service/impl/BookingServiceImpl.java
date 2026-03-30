package com.flight.service.impl;

import com.flight.dto.*;
import com.flight.dto.response.BookingResponse;
import com.flight.dto.response.PaymentResponse;
import com.flight.dto.response.RefundResponse;
import com.flight.dto.response.UserResponse;
import com.flight.enums.BookingStatus;
import com.flight.exception.*;
import com.flight.model.Booking;
import com.flight.model.Flight;
//import com.flight.model.Payment;
import com.flight.repository.BookingRepository;
//import com.flight.repository.RefundRepository;
import com.flight.service.*;
import com.flight.service.feignClients.AccountServiceFeignClient;
import com.flight.service.feignClients.PaymentServiceFeignClient;
import com.flight.service.feignClients.RefundServiceFeignClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final UserService userService;
    private final PaymentServiceFeignClient paymentServiceFeignClient;
    private final AccountServiceFeignClient accountServiceFeignClient;
    private final RefundServiceFeignClient refundServiceFeignClient;
    private final RefundRetryService refundRetryService;

    @Override
    @Transactional
    public BookingResponse bookFlight(Integer userId, String flightNumber, Integer seatCount) {
        log.info("Booking request received | userId: {}, flightNumber: {}, seatCount: {}", userId, flightNumber, seatCount);

        Flight flight = flightService.getFlightByFlightNumber(flightNumber);
        log.debug("Validated flight details by flightId={}", flightNumber);
        //check if flight is full
        int flightAvailableSeatCount = flight.getAvailableSeats();
        if (flightAvailableSeatCount < seatCount || flightAvailableSeatCount < 0) {
            log.error("Seats are already full for flight no={}", flightNumber);
            throw new SeatsNotAvailableException("Seats are full for Flight " + flightNumber);
        }

        // validate userId - validation logic is in getuser() method. For invalid userId, it throw error there.
        UserResponse user = userService.getUser(userId);
        log.debug("Validated user details for userId={}", userId);

        //update the seats after booking
        flightService.updateAvailableSeats(flight.getId(), flight.getAvailableSeats() - seatCount);

        // save booking
        Booking booking = mapToEntity(userId, flightNumber, seatCount);
        Booking saved = bookingRepository.save(booking);
        log.debug("Flight booking details save succesfully");

        return mapToResponse(saved, flight.getSeatPrice());
    }

    @Override
    public Booking getBookingDetailById(Integer bookingId) {
        log.info("Fetching booking details | bookingId: {}", bookingId);
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.error("Booking not found | bookingId: {}", bookingId);
            return new RuntimeException("Invalid bookingId: " + bookingId);
        });
    }

    @Override
    public List<Booking> getUsersBookings(Integer userId) {
        log.info("Fetching bookings for user | userId: {}", userId);
        // validate userId
        if (!userService.validateUserId(userId)) {
            log.warn("User validation failed | userId: {}", userId);
            throw new UserNotFoundException("User Not Found with userId: " + userId);
        }
        return bookingRepository.findAllByUserId(userId).orElseThrow(() -> {
            log.error("No booking found for user with userId={}", userId);
            return new RuntimeException("Booking not found");
        });
    }

    @Override
    @Transactional
    public Object doPayment(PaymentDto paymentDto) {
        log.info("Payment request received | bookingId: {}, amount: {}", paymentDto.getBookingId(), paymentDto.getAmount());

        Booking booking = getBookingDetailsById(paymentDto.getBookingId());
        log.debug("Validated the booking with bookingId={}", paymentDto.getBookingId());
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            return "Payment Already done.";
        } else if (booking.getStatus() == BookingStatus.CANCELLED) {
            return "booking is Cancelled for booking id:" + paymentDto.getBookingId();
        }

        paymentDto.setDateTime(LocalDateTime.now());
        PaymentDto savedPayment = paymentServiceFeignClient.createNewPayment(paymentDto).orElseThrow(() -> {
            log.error("Something wend wrong during payment processing in Bank Account service");
            return new RuntimeException("Payment not process. Please try again later");
        });

        //update the booking status to Completed and add paymentId into bookings table.
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setPaymentId(savedPayment.getPaymentId());
        bookingRepository.save(booking);

        // update balance in account table
        int userId = booking.getUserId();
        double amountToPay = paymentDto.getAmount();
        Double withdrawalAmount = accountServiceFeignClient.withdraw(userId, amountToPay).orElseThrow(() -> {
            log.error("Something went wrong during withdrawal in Account Service");
            return new RuntimeException("Something went wrong. Please try again later");
        });

        log.info("Payment is successful for bookingId={}", paymentDto.getBookingId());
        return mapToDto(savedPayment.getPaymentId(), withdrawalAmount, paymentDto.getBookingId());
    }

    public Booking getBookingDetailsById(int bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.error("Booking details not found for bookingId={}", bookingId);
            return new BookingDetailsNotFoundException("Booking details not found");
        });
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }


    @Override
    @Transactional
    public RefundResponse cancelFlightBooking(Integer bookingId) {
        log.info("Received request to cancel flight booking with ID: {}", bookingId);
        // check booking id, if valid then get bookingObj by given id
        Booking booking = getBookingDetailsById(bookingId);
        log.debug("Booking details validated for bookingId={}", bookingId);
        //Update the booking status for that booking to cancel and save it again
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            log.error("Flight booking already cancelled for bookingId={}", bookingId);
            throw new InvalidBookingStateException("Flight Booking already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        Booking savedBooking = saveBooking(booking);
        log.debug("Booking status updated to cancelled successfully");

        // Update the availability of seat again
        Flight flight = flightService.getFlightByFlightNumber(booking.getFlightNumber());
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatCount());
        flightService.addFlight(flight);
        log.debug("Flight seats availability updated again");

        RefundDto refundDto = new RefundDto();
        refundDto.setBookingId(bookingId);
        refundDto.setUserId(booking.getUserId());
        refundDto.setPaymentId(booking.getPaymentId());
        refundDto.setBookingDateTime(booking.getBookingDateTime());

        // call Refund service.
        RefundResponse refundResponse = refundRetryService.processRefund(refundDto);
        if (refundResponse.getStatus().equals("Unsuccessful")) {
            log.error("Refund service is not available");
            throw new RefundServiceDownError("Refund Service Unavailable");
        }
        refundResponse.setBookingStatus(BookingStatus.CANCELLED);
        refundResponse.setMessage("Flight booking cancelled successfully for flight number" + booking.getFlightNumber());

        return refundResponse;
    }

    BookingResponse mapToResponse(Booking saved, Double seatPrice) {
        BookingResponse resp = new BookingResponse();
        resp.setBookingId(saved.getBookingId());
        resp.setMessage("Confirmed the Booking by doing Payment.");
        resp.setPrice(saved.getSeatCount() * seatPrice);
        resp.setTotalBookSeats(saved.getSeatCount());
        return resp;
    }

    Booking mapToEntity(Integer userId, String flightNumber, Integer seatCount) {
        Booking bookingObj = new Booking();
        bookingObj.setUserId(userId);
        bookingObj.setFlightNumber(flightNumber);
        bookingObj.setSeatCount(seatCount);
        bookingObj.setBookingDateTime(LocalDateTime.now());
        bookingObj.setStatus(BookingStatus.PAYMENT_PENDING);
        return bookingObj;
    }

    private PaymentResponse mapToDto(Integer paymentId, Double withdrawalAmount, Integer bookingId) {
        PaymentResponse resp = new PaymentResponse();
        resp.setPaymentId(paymentId);
        resp.setPaymentAmount(withdrawalAmount);
        resp.setMessage("Payment is Successful for bookingId: " + bookingId);
        return resp;
    }
}
