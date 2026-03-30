package com.flight.service.Impl;

import com.flight.dto.PaymentDto;
import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.exception.PaymentNotFoundException;
import com.flight.exception.RefundProcessingException;
import com.flight.model.Payment;
import com.flight.model.Refunds;
import com.flight.repository.RefundRepository;
import com.flight.service.AccountService;
import com.flight.service.PaymentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Refund Test cases")
class RefundServiceImplTest {
    @Mock
    private  RefundRepository refundRepository;
    @Mock
    private  PaymentService paymentService;
    @Mock
    private  AccountService accountService;

    @InjectMocks
    private RefundServiceImpl refundService;

    private RefundDto refundDto;
    private PaymentDto payment;

    @BeforeEach
    void setUp(){
        refundDto = new RefundDto();
        refundDto.setBookingId(101);
        refundDto.setPaymentId(201);
        refundDto.setUserId(301);
        refundDto.setBookingDateTime(LocalDateTime.now().minusHours(5));

        payment = new PaymentDto();
        payment.setPaymentId(201);
        payment.setAmount(1000.0);
        payment.setBookingId(101);
    }

    @AfterEach
    void cleanUp(){

    }

    @Test
    @DisplayName("Should give full refund for Cancellation within 24 hours")
    void test1(){
       // Arrange
        when(paymentService.getPaymentByPaymentId(201)).thenReturn(payment);
        when(accountService.deposit(301, 1000.0)).thenReturn(1000.0);
        when(refundRepository.save(any(Refunds.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RefundResponse response = refundService.doRefund(refundDto);

        // Assert
        assertEquals("Refund Successful", response.getStatus());
        assertEquals(1000.0, response.getRefundAmount());

        // Verify
        verify(paymentService).getPaymentByPaymentId(201);
        verify(accountService).deposit(301, 1000.0);
        verify(refundRepository).save(any(Refunds.class));
    }

    @Test
    void shouldGiveHalfRefundWhenAfter24Hours() {
        // Arrange
        refundDto.setBookingDateTime(LocalDateTime.now().minusDays(2));
        when(paymentService.getPaymentByPaymentId(201)).thenReturn(payment);
        when(accountService.deposit(301, 500.0)).thenReturn(500.0);
        when(refundRepository.save(any(Refunds.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RefundResponse response = refundService.doRefund(refundDto);

        // Assert
        assertEquals(500.0, response.getRefundAmount());

        // Verify
        verify(accountService).deposit(301, 500.0);
    }
    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {
        // Arrange
        when(paymentService.getPaymentByPaymentId(201)).thenThrow(new PaymentNotFoundException("Payment not found"));
        //assert
        assertThrows(RefundProcessingException.class, () -> {
            refundService.doRefund(
                    refundDto);
        });

        verify(paymentService).getPaymentByPaymentId(201);
        verifyNoInteractions(accountService);
        verifyNoInteractions(refundRepository);
    }

    @Test
    void shouldThrowExceptionWhenDepositFails() {
        when(paymentService.getPaymentByPaymentId(201))
                .thenReturn(payment);
        when(accountService.deposit(anyInt(), anyDouble()))
                .thenThrow(new RefundProcessingException("Deposit failed"));

        //assert
        assertThrows(RefundProcessingException.class, () -> {
            refundService.doRefund(refundDto);
        });

        verify(paymentService).getPaymentByPaymentId(201);
        verify(accountService).deposit(anyInt(), anyDouble());
        verifyNoInteractions(refundRepository);
    }

}