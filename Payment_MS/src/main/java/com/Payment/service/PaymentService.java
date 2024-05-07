package com.Payment.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;

import com.Payment.util.Util;
import com.Payment.constant.Constant;
import com.Payment.exception.ResourceNotFoundException;
import com.Payment.model.Payment;
import com.Payment.repository.PaymentRepo;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepo paymentRepo;
	
	public Payment createPayment(@RequestBody Payment payment) {
		 
		return this.paymentRepo.save(payment);
	}
	
	public void deletePayment(Integer paymentId) {
		Payment busInventory = this.paymentRepo.findById(paymentId).orElseThrow(()-> new ResourceNotFoundException("inventory", "id", paymentId));
		this.paymentRepo.delete(busInventory);
	}

	public List<Payment> getAllPayment() {
		List<Payment> findAll = this.paymentRepo.findAll();
		return findAll;
	}
	
	public Payment getPaymentById(Integer id) {
		Payment payment = this.paymentRepo.findById(id)
		.orElseThrow(()-> new ResourceNotFoundException("Payment", "Id", id));
		return payment;
	}

	
	public Payment updatePayment(Payment paymentDto, Integer id) {
		Payment payment = this.paymentRepo.findById(id).
				orElseThrow(()->new ResourceNotFoundException("Payment", "Id", id));
		
		if(paymentDto.getBookingNumber()!=0) {
			payment.setBookingNumber(paymentDto.getBookingNumber());
		}
		payment.setPaymentDate(new Date());
		Payment save = this.paymentRepo.save(payment);
		return save;
	}
	
	public String cancelPayment(Integer bookingNumber) {
		Optional<Payment> paymentOPT = Optional.ofNullable(this.paymentRepo.findByBookingNumber(bookingNumber)) ;
		String status=Constant.FAILED;	
		
		if(paymentOPT.isPresent()) {
			Payment payment = paymentOPT.get();
			payment.setStatus(Constant.CANCEL);
			payment.setPaymentDate(new Date());
			this.paymentRepo.save(payment);
			status=Constant.CANCEL;
		}
		
		
		return status;
	}
	
}
