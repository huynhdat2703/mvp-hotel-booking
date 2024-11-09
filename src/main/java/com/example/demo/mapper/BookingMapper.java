package com.example.demo.mapper;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Customer;
import com.example.demo.error.ResourceNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.request.BookingRequest;
import com.example.demo.response.BookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    @Autowired
    public CustomerRepository customerRepository;

    public Booking toEntity(BookingRequest bookingRequest) {
        Customer customer = customerRepository.findById(bookingRequest.customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));;

        Booking booking = new Booking();
        booking.setCheckInDate(bookingRequest.checkInDate);
        booking.setCheckOutDate(bookingRequest.checkOutDate);
        booking.setStatus(bookingRequest.status);
        booking.setCustomer(customer);

        return booking;
    }

    public BookingResponse toDto(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.id = booking.getId();
        response.checkInDate = booking.getCheckInDate();
        response.checkOutDate = booking.getCheckOutDate();
        response.status = booking.getStatus();
        response.customer = booking.getCustomer();

        return response;
    }
}
