package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Customer;
import com.example.demo.enums.BookingStatus;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.request.BookingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public CustomerRepository customerRepository;

    public Customer testCustomer;

    @BeforeEach
    public void setup() {
        testCustomer = customerRepository.save(new Customer("Test User", "testuser@example.com"));

        Booking booking1 = new Booking();
        booking1.setStatus(BookingStatus.BOOKED);
        booking1.setCheckInDate(LocalDate.of(2024, 11, 10));
        booking1.setCheckOutDate(LocalDate.of(2024, 11, 15));
        booking1.setCustomer(testCustomer);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setStatus(BookingStatus.CANCELLED);
        booking2.setCheckInDate(LocalDate.of(2024, 11, 5));
        booking2.setCheckOutDate(LocalDate.of(2024, 11, 8));
        booking2.setCustomer(testCustomer);
        bookingRepository.save(booking2);
    }


    @AfterEach
    public void cleanup() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateBooking() throws Exception {
        BookingRequest bookingRequest  = new BookingRequest();
        bookingRequest.customerId = testCustomer.getId();
        bookingRequest.checkInDate = LocalDate.of(2024, 11, 10);
        bookingRequest.checkOutDate = LocalDate.of(2024, 11, 15);
        bookingRequest.status = BookingStatus.BOOKED;

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value(BookingStatus.BOOKED.name()));
    }

    @Test
    public void testGetBooking() throws Exception {
        Booking booking = new Booking();
        booking.setCustomer(testCustomer);
        booking.setCheckInDate(LocalDate.of(2024, 11, 10));
        booking.setCheckOutDate(LocalDate.of(2024, 11, 15));
        booking.setStatus(BookingStatus.BOOKED);
        booking = bookingRepository.save(booking);

        mockMvc.perform(get("/api/bookings/" + booking.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.status").value(BookingStatus.BOOKED.name()));
    }

    @Test
    public void testSearchBookings() throws Exception {
        mockMvc.perform(get("/api/bookings/search")
                        .param("name", "Test User")
                        .param("email", "testuser@example.com")
                        .param("status", "BOOKED")
                        .param("checkInStart", "2024-11-10")
                        .param("checkInEnd", "2024-11-15")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].status").value(BookingStatus.BOOKED.name()))
                .andExpect(jsonPath("$.content[0].customer.name").value("Test User"))
                .andExpect(jsonPath("$.content[0].customer.email").value("testuser@example.com"));
    }

    @Test
    public void testUpdateBooking() throws Exception {
        Booking booking = new Booking();
        booking.setCustomer(testCustomer);
        booking.setCheckInDate(LocalDate.of(2024, 11, 10));
        booking.setCheckOutDate(LocalDate.of(2024, 11, 15));
        booking.setStatus(BookingStatus.BOOKED);
        booking = bookingRepository.save(booking);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.checkInDate = LocalDate.of(2024, 11, 12);
        bookingRequest.checkOutDate = LocalDate.of(2024, 11, 16);

        mockMvc.perform(put("/api/bookings/" + booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkInDate").value("2024-11-12"))
                .andExpect(jsonPath("$.checkOutDate").value("2024-11-16"));
    }

    @Test
    public void testCancelBooking() throws Exception {
        Booking booking = new Booking();
        booking.setCustomer(testCustomer);
        booking.setCheckInDate(LocalDate.of(2024, 11, 10));
        booking.setCheckOutDate(LocalDate.of(2024, 11, 15));
        booking.setStatus(BookingStatus.BOOKED);
        booking = bookingRepository.save(booking);

        mockMvc.perform(delete("/api/bookings/" + booking.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify that the booking status was changed to "Canceled"
        Booking canceledBooking = bookingRepository.findById(booking.getId()).orElse(null);
        assertNotNull(canceledBooking);
        assertEquals(BookingStatus.CANCELLED, canceledBooking.getStatus());
    }
}
