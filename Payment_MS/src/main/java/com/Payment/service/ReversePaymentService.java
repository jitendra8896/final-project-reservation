package com.Payment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.Payment.constant.Constant;
import com.Payment.model.Payment;
import com.Payment.payload.BookingEvent;
import com.Payment.payload.PaymentEvent;
import com.Payment.repository.PaymentRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ReversePaymentService {

	@Autowired
	private KafkaTemplate<String, BookingEvent> bookingKafkaTemplet;
	
	@Autowired
	private PaymentRepo payRepo;
	
	@KafkaListener(topics = Constant.REVERSE_PAYMENT_CREATED_TOPIC,groupId = Constant.PAYMENT_GROUP_ID)
	public void reversePayment(String event) throws Exception {
		System.out.println("Inside reverse payment for booking -> "+event);
		
		try {
			PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class);
			Optional<Payment> payment = this.payRepo.findById(paymentEvent.getPaymentId());
			payment.ifPresent(e->{
				e.setStatus(Constant.FAILED);
				payRepo.save(e);
			});
			
			
			BookingEvent bookingEvent =new BookingEvent();
			bookingEvent.setBooking_number(paymentEvent.getBooking_number());
			bookingEvent.setType(Constant.BOOKING_REVERSE);
			bookingKafkaTemplet.send(Constant.REVERSE_BOOKING_ORDER_TOPIC, bookingEvent);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("exception while reversing payment");
		}
		
	}
}
