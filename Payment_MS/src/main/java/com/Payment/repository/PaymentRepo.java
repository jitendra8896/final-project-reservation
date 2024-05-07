package com.Payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Payment.model.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

	public Payment findByBookingNumber(int bookingNumber);
}
