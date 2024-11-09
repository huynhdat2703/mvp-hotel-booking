package com.example.demo.response;

import com.example.demo.entity.Customer;
import com.example.demo.enums.BookingStatus;

import java.time.LocalDate;

public class BookingResponse {
    public Long id;
    public LocalDate checkInDate;
    public LocalDate checkOutDate;
    public BookingStatus status;
    public Customer customer;
}
