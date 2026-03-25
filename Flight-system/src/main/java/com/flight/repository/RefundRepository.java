package com.flight.repository;

import com.flight.model.Refunds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refunds, Integer> {
}
