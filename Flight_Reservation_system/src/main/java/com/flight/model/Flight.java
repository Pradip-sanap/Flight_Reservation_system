package com.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Flights")
public class Flight
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer  id;
    private String flightNumber;
    private String source;
    private String destination;
    private Integer  totalSeats;
    private Integer  availableSeats;
    private Double seatPrice;
}
