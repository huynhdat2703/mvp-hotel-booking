package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE (:name IS NULL OR b.customer.name LIKE %:name%) " +
            "AND (:email IS NULL OR b.customer.email LIKE %:email%) " +
            "AND (:status IS NULL OR b.status = :status) ")
    Page<Booking> searchBookings(@Param("name") String name,
                                 @Param("email") String email,
                                 @Param("status") BookingStatus status,
                                 Pageable pageable);
}
