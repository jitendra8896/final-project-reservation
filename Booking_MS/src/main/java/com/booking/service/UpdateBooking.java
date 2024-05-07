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
public class UpdateBooking {

	@Autowired
	private BookingRepo br;
	
	@KafkaListener(topics = Constant.UPDATE_BOOKING_STATUS_TOPICE,groupId = Constant.BOOKING_GROUP_ID)
	public void updateBookingStatus(String event) {
		System.out.println("update the booking status success - "+event);
		
		try {
			BookingEvent bookingEvent = new ObjectMapper().readValue(event, BookingEvent.class);
			
			int booking_number = bookingEvent.getBooking_number();
			Optional<Booking> booking = this.br.findById(booking_number);
			if(booking.isPresent()) {
				Booking booking2 = booking.get();
				booking2.setStatus(Constant.SUCCESS);
				this.br.save(booking2);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception occured while while updating status of booking...");
		}
		
	}
}
