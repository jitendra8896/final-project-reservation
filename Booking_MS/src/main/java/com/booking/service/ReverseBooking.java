package com.booking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.booking.constant.Constant;
import com.booking.model.Booking;
import com.booking.payload.BookingEvent;
import com.booking.repository.BookingRepo;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ReverseBooking {

	@Autowired
	private BookingRepo bookingRepo;
	
	@KafkaListener(topics = Constant.REVERSE_BOOKING_ORDER_TOPIC,groupId = Constant.BOOKING_GROUP_ID)
	public void reverseBooking(String event) {
		System.out.println("reverse the booking event - "+event);
		
		try {
			BookingEvent bookingEvent = new ObjectMapper().readValue(event, BookingEvent.class);
			Optional<Booking> booking = bookingRepo.findById(bookingEvent.getBooking_number());
			booking.ifPresent(e->{
				e.setStatus(Constant.FAILED);
				bookingRepo.save(e);
			});
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception occured while reverting the booking");
		}
	}
	
}
