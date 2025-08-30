package com.example.locking.controller;


import com.example.locking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/optimistic")
    private void testOptimisticLocking(@RequestParam Long id) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("[OPTIMISTIC] " + Thread.currentThread().getName() + " started booking seat " + id);
                bookingService.optimisticLockingMechanism(id);
                System.out.println("[OPTIMISTIC] " + Thread.currentThread().getName() + " ✅ successfully booked seat " + id);
            } catch (Exception e) {
                System.out.println("[OPTIMISTIC] " + Thread.currentThread().getName() + " ❌ failed to book seat " + id + " -> " + e.getMessage());
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("[OPTIMISTIC] " + Thread.currentThread().getName() + " started booking seat " + id);
                bookingService.optimisticLockingMechanism(id);
                System.out.println("[OPTIMISTIC] " + Thread.currentThread().getName() + " ✅ successfully booked seat " + id);
            } catch (Exception e) {
                System.out.println("[OPTIMISTIC] " + Thread.currentThread().getName() + " ❌ failed to book seat " + id + " -> " + e.getMessage());
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }


    @GetMapping("/pessimistic")
    private void testPessimisticLocking(@RequestParam Long id) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("[PESSIMISTIC] " + Thread.currentThread().getName() + " started booking seat " + id);
                bookingService.pessimisticLockingMechanism(id);
                System.out.println("[PESSIMISTIC] " + Thread.currentThread().getName() + " ✅ successfully booked seat " + id);
            } catch (Exception e) {
                System.out.println("[PESSIMISTIC] " + Thread.currentThread().getName() + " ❌ failed to book seat " + id + " -> " + e.getMessage());
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("[PESSIMISTIC] " + Thread.currentThread().getName() + " started booking seat " + id);
                bookingService.pessimisticLockingMechanism(id);
                System.out.println("[PESSIMISTIC] " + Thread.currentThread().getName() + " ✅ successfully booked seat " + id);
            } catch (Exception e) {
                System.out.println("[PESSIMISTIC] " + Thread.currentThread().getName() + " ❌ failed to book seat " + id + " -> " + e.getMessage());
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
