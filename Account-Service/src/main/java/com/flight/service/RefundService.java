package com.flight.service;


import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.model.Refunds;

public interface RefundService {
    RefundResponse saveRefundDetails(RefundDto refundDto);
}
