package com.example.locking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "movie_name")
    private String movieName;
    private boolean booked;
    @Version
    private Integer version; // Required for optimistic locking; can be removed if using only pessimistic locking
}
