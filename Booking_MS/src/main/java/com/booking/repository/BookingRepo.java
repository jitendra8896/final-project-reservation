package com.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.model.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

}
