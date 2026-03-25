package com.flight.service.impl;

import com.flight.dto.*;
import com.flight.enums.BookingStatus;
import com.flight.exception.PaymentUnsuccessfulException;
import com.flight.exception.SeatsNotAvailableException;
import com.flight.exception.UserNotFoundException;
import com.flight.model.Booking;
import com.flight.model.Flight;
import com.flight.model.Payment;
import com.flight.model.Refunds;
import com.flight.repository.BookingRepository;
import com.flight.repository.FlightRepository;
import com.flight.repository.PaymentRepository;
import com.flight.repository.RefundRepository;
import com.flight.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final UserService userService;
    private final PaymentServiceFeignClient paymentServiceFeignClient;
    private final AccountServiceFeignClient accountServiceFeignClient;
    private final RefundServiceFeignClient refundServiceFeignClient;
    private final RefundRepository refundRepository;

    @Override
    @Transactional
    public BookingResponse bookFlight(Integer userId, String flightNumber, Integer seatCount) {
        // validating flihgt number
        Flight flight = flightService.getFlightByFlightNumber(flightNumber);

        //check if flight is full
        int flightAvailableSeatCount = flight.getAvailableSeats();
        if(flightAvailableSeatCount < seatCount || flightAvailableSeatCount<0){
            throw new SeatsNotAvailableException("Seats are full for Flight "+ flightNumber);
        }

        // validate userId - validation logic is in getuser() method. For invalid userId, it throw error there.
        UserResponse user = userService.getUser(userId);

        //update the seats after booking
        flightService.updateAvailableSeats(flight.getId(),flight.getAvailableSeats() - seatCount);

        // save booking
        Booking booking = mapToEntity(userId, flightNumber, seatCount);
        Booking saved = bookingRepository.save(booking);

        return mapToResponse(saved, flight.getSeatPrice());
    }

    BookingResponse mapToResponse(Booking saved, Double seatPrice){
        BookingResponse resp = new BookingResponse();
        resp.setBookingId(saved.getBookingId());
        resp.setMessage("Confirmed the Booking by doing Payment.");
        resp.setPrice(saved.getSeatCount() * seatPrice);
        resp.setTotalBookSeats(saved.getSeatCount());
        return resp;
    }

    Booking mapToEntity(Integer userId, String flightNumber, Integer seatCount){
        Booking bookingObj = new Booking();
        bookingObj.setUserId(userId);
        bookingObj.setFlightNumber(flightNumber);
        bookingObj.setSeatCount(seatCount);
        bookingObj.setBookingDateTime(LocalDateTime.now());
        bookingObj.setStatus(BookingStatus.PAYMENT_PENDING);
        return bookingObj;
    }

    @Override
    public Booking getBookingDetailById(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(()-> new RuntimeException("Invalid bookingId: "+ bookingId));
    }

    @Override
    public List<Booking> getUsersBookings(Integer userId) {
        // validate userId
        if(!userService.validateUserId(userId)){
            throw new UserNotFoundException("User Not Found with userId: "+ userId);
        }
        return bookingRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Object doPayment(PaymentDto paymentDto) {
        Payment paymentObj = new Payment( paymentDto.getBookingId(), paymentDto.getPrice(), LocalDateTime.now());
        Payment savedPayment = paymentServiceFeignClient.createNewPayment(paymentObj);

        Booking booking = bookingRepository.findById(paymentDto.getBookingId()).orElseThrow(() ->
                                                                                    new RuntimeException("Booking not found"));

        if( booking.getStatus() == BookingStatus.COMPLETED){
            return "Payment Already done.";
        }else if(booking.getStatus() == BookingStatus.CANCELLED){
            return "booking is Cancelled for booking id:"+ paymentDto.getBookingId();
        }

        //update the booking status to Completed and add paymentId into bookings table.
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setPaymentId(savedPayment.getPaymentId());
        bookingRepository.save(booking);

        // update balance in account table
        int userId = booking.getUserId();
        double amountToPay = paymentDto.getPrice();
        Double withdrawalAmount = accountServiceFeignClient.withdraw(userId, amountToPay);
        System.out.println("Amount "+amountToPay+" Withdraw successfull.");

        return mapToDto(savedPayment.getPaymentId(), withdrawalAmount, paymentDto.getBookingId());
    }

    private PaymentResponse mapToDto(Integer paymentId,Double withdrawalAmount, Integer bookingId){
        PaymentResponse resp = new PaymentResponse();
        resp.setPaymentId(paymentId);
        resp.setPaymentAmount(withdrawalAmount);
        resp.setMessage("Payment is Successful for bookingId: "+ bookingId);
        return resp;
    }

    @Override
    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public RefundResponse cancelFlightBooking(Integer bookingId) {
        // check booking id, if valid then get bookingObj by given id
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                                                                        new RuntimeException("Booking not found with bookingId: "+ bookingId));
        //Update the booking status for that booking to cancel and save it again
        if(booking.getStatus() == BookingStatus.CANCELLED){
            throw new RuntimeException("Flight Booking already cancelled for bookingId: "+ bookingId);
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Update the availability of seat again
        Flight flight = flightService.getFlightByFlightNumber(booking.getFlightNumber());
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatCount());
        flightService.addFlight(flight);

        RefundDto refundDto = new RefundDto();
        refundDto.setBookingId(bookingId);
        refundDto.setUserId(booking.getUserId());
        refundDto.setPaymentId(booking.getPaymentId());
        refundDto.setBookingDateTime(booking.getBookingDateTime());

        RefundResponse refundResponse = refundServiceFeignClient.doRefund(refundDto);
        refundResponse.setBookingStatus(BookingStatus.CANCELLED);
        refundResponse.setMessage("Flight booking cancelled successfully for flight number"+booking.getFlightNumber());

        return refundResponse;
    }


}
