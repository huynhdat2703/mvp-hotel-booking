package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.enums.BookingStatus;
import com.example.demo.error.ResourceNotFoundException;
import com.example.demo.request.BookingRequest;
import com.example.demo.response.BookingResponse;
import com.example.demo.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    public BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
        logger.info("Start create booking. Payload: {}", bookingRequest);
        return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        logger.info("Start get booking. id: {}", id);
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Booking>> searchBookings(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        logger.info("Start search booking");
        if (page < 0) page = 0;
        if (size <= 0) size = 3;

        Page<Booking> bookings = bookingService.searchBookings(name, email, status, page, size);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        logger.info("Start update booking. id: {}, data: {}", id, bookingRequest);
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        logger.info("Start cancel booking. id: {}", id);
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
