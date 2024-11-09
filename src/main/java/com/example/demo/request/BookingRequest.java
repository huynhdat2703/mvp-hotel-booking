package com.example.demo.request;

import com.example.demo.enums.BookingStatus;

import java.time.LocalDate;

public class BookingRequest {
    public LocalDate checkInDate;
    public LocalDate checkOutDate;
    public BookingStatus status;
    public Long customerId;
}
