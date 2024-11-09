package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.enums.BookingStatus;
import com.example.demo.error.ResourceNotFoundException;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.repository.BookingRepository;
import com.example.demo.request.BookingRequest;
import com.example.demo.response.BookingResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public BookingMapper bookingMapper;

    private final Counter customCounter;

    public BookingService(MeterRegistry meterRegistry) {
        // add custom metric counter to count number of create booking
        this.customCounter = meterRegistry.counter("custom.counter", "type", "createBooking");
    }

    public void performAction() {
        customCounter.increment();
    }

    public BookingResponse createBooking(BookingRequest bookingRequest) {
        this.performAction();
        Booking booking = bookingMapper.toEntity(bookingRequest);
        booking.setStatus(BookingStatus.BOOKED);
        Booking newBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(newBooking);
    }

    public BookingResponse findBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return bookingMapper.toDto(booking);
    }

    public Page<Booking> searchBookings(String name, String email, BookingStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return bookingRepository.searchBookings(name, email, status, pageable);
    }

    public BookingResponse updateBooking(Long id, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setCheckInDate(bookingRequest.checkInDate);
        booking.setCheckOutDate(bookingRequest.checkOutDate);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(updatedBooking);
    }

    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
