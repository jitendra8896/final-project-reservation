package com.Payment.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Payment.constant.Constant;
import com.Payment.model.Payment;
import com.Payment.payload.ApiResponse;
import com.Payment.payload.BookingEvent;
import com.Payment.payload.PaymentEvent;

import com.Payment.service.PaymentService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> paymentKafkaTemplet;
	
	@Autowired
	private KafkaTemplate<String, BookingEvent> bookingKafkaTemplet;
	
	
	@KafkaListener(topics = Constant.BOOKING_ORDER_TOPIC,groupId = Constant.PAYMENT_GROUP_ID)
	public void processPayment(String event) throws Exception {
		System.out.println("processing payment event->"+event);
		
		BookingEvent bookingEvent = new ObjectMapper().readValue(event, BookingEvent.class);
		int booking_number = bookingEvent.getBooking_number();
		String bus_number = bookingEvent.getBusNumber();
		int number_of_seats = bookingEvent.getNumber_of_seats();
		Payment payment = new Payment();
		payment.setBookingNumber(booking_number);
		payment.setPaymentDate(new Date());
		payment.setStatus(Constant.SUCCESS);
		
		try {
			Payment createPayment = paymentService.createPayment(payment);
			
			PaymentEvent payEvent = new PaymentEvent();
			
			payEvent.setBusNumber(bus_number);
			payEvent.setPaymentId(createPayment.getPaymentId());
			payEvent.setNumber_of_seats(number_of_seats);
			payEvent.setBooking_number(booking_number);
			payEvent.setType(Constant.PAYMENT_CREATED);
			
			paymentKafkaTemplet.send(Constant.PAYMENT_CREATED_TOPIC, payEvent);
			
		} catch (Exception e) {
			// TODO: handle exception
			payment.setStatus(Constant.FAILED);
			paymentService.createPayment(payment);
			bookingKafkaTemplet.send(Constant.REVERSE_BOOKING_ORDER_TOPIC, bookingEvent);
			
		}
		
	}
	
	
	
	
	@PostMapping("/add")
	public ResponseEntity<Payment> createPayment(@RequestBody Payment payment){
		payment.setPaymentDate(new Date());
		Payment createPayment = this.paymentService.createPayment(payment);
		return new ResponseEntity<>(createPayment,HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{pId}")
	public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment,@PathVariable("pId") Integer pId){
		Payment updatePayment= this.paymentService.updatePayment(payment, pId);
		return ResponseEntity.ok(updatePayment);
	}
	
	@GetMapping("/cancel/{bookingNumber}")
	public String updatePayment(@PathVariable("bookingNumber") Integer bookingNumber){
		String cancelPayment = this.paymentService.cancelPayment(bookingNumber);
		return cancelPayment;
	}
	
	//delete booking
	
	@DeleteMapping("/delete/{pId}")
	public ResponseEntity<?> deletePayment(@PathVariable Integer pId){
		this.paymentService.deletePayment(pId);
		return new ResponseEntity<>(new ApiResponse("Payment Delete successfully..", true),HttpStatus.OK);
	}
	//get user
	@GetMapping("/getall")
	public ResponseEntity<List<Payment>> getAllBus(){
		List<Payment> all = this.paymentService.getAllPayment();
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/get/{pId}")
	public ResponseEntity<Payment> getBusById(@PathVariable Integer pId){
		return ResponseEntity.ok(this.paymentService.getPaymentById(pId));
	}

}
