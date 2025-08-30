package com.example.locking.service;

import com.example.locking.entity.Seat;
import com.example.locking.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;

    @Transactional
    public Seat optimisticLockingMechanism(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        System.out.printf("[%s] OptimisticLock: Fetched seatId=%d, movie=%s, booked=%s, version=%d \n",
                Thread.currentThread().getName(), seatId, seat.getMovieName(), seat.isBooked(), seat.getVersion());

        if (seat.isBooked()) {
            throw new RuntimeException("Seat is already booked!");
        }

        seat.setBooked(true);
        return seatRepository.save(seat); // Hibernate increments version here
    }

    @Transactional
    public Seat pessimisticLockingMechanism(Long seatId) {
        Seat seat = seatRepository.findByIdAndLock(seatId) // no need for the version column in pessimistic locking...
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        System.out.printf("[%s] PessimisticLock: Fetched seatId=%d, movie=%s, booked=%s\n   ",
                Thread.currentThread().getName(), seatId, seat.getMovieName(), seat.isBooked());

        if (seat.isBooked()) {
            throw new RuntimeException("Seat is already booked!");
        }

        seat.setBooked(true);
        return seatRepository.save(seat);
    }


}
