package com.booking.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.booking.constant.Constant;
import com.booking.exception.ResourceNotFoundException;
import com.booking.model.Booking;
import com.booking.payload.BookingEvent;
import com.booking.repository.BookingRepo;
import com.booking.util.Util;
import com.netflix.discovery.converters.Auto;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Flux;

@Service
public class BookingService {

	@Autowired
	private BookingRepo bookingRepo;
	
	@Autowired
	private WebClient webclient;
	
	@Autowired
	@Qualifier("Inventory")
	private RestClient restClient;
	
	@Autowired
	@Qualifier("Payment")
	private RestClient restClient1;
	
	@Autowired
	private KafkaTemplate<String, BookingEvent> kafkaTemplet;
	
	
	public Booking createBooking(Booking booking) {
		booking.setBooking_date(new Date());
		Booking bookingSave = null;
		try {
			String busNumber = booking.getBusNumber();
			String avalSeatCount = restClient.get().uri("/api/v1/bus/inventory/getSeatCount/"+busNumber).retrieve().body(String.class);
			//String string = webclient.get().uri("/api/v1/bus/inventory/getSeatCount").retrieve().bodyToMono(String.class).block();
			
			int seatCount = Integer.parseInt(avalSeatCount);
			if(seatCount>booking.getNumber_of_seats()) {
				try {
					booking.setStatus(Constant.PENDING);
					bookingSave = this.bookingRepo.save(booking);
					BookingEvent bookingEvent = new BookingEvent();
					bookingEvent.setBooking_number(bookingSave.getBooking_number());
					bookingEvent.setBusNumber(busNumber);
					bookingEvent.setNumber_of_seats(booking.getNumber_of_seats());
					bookingEvent.setType(Constant.BOOKING_CREATED);
					
					kafkaTemplet.send(Constant.BOOKING_ORDER_TOPIC, bookingEvent);
					System.out.println("booking topic produce successfully");
				} catch (Exception e) {
					booking.setStatus(Constant.FAILED);
					bookingSave = this.bookingRepo.save(booking);
				}
				
			}else {
				booking.setStatus(Constant.FAILED);
				bookingSave = this.bookingRepo.save(booking);
			}
		} catch (Exception e) {
			booking.setStatus(Constant.FAILED);
			bookingSave = this.bookingRepo.save(booking);
			System.out.println("getting error while fetch calling inventory service through webclient");
		}
		
		
		return bookingSave;
	}
	
	public String cancelBooking(int bookingNumber) {
		Optional<Booking> booking = this.bookingRepo.findById(bookingNumber);
		String status =Constant.FAILED;
		if(booking.isPresent()) {
			Booking booking2 = booking.get();
			int busNumber = booking2.getBooking_number();
			int seats = booking2.getNumber_of_seats();
			booking2.setStatus(Constant.CANCEL);
			this.bookingRepo.save(booking2);
			try {
														   
				String getStatus = restClient1.get().uri("/api/v1/payment/cancel/"+bookingNumber).retrieve().body(String.class);
				
				String update = restClient.get().uri("/api/v1/bus/inventory/updateBookedSeat/"+busNumber+"/"+seats).retrieve().body(String.class);
				status=getStatus+" and "+update+" successfully...";
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				status =Constant.FAILED;
			}
		}
		return status;
		
		
	}
	
	public Booking updateBooking(Booking bookingDto, Integer bookingId) {
		Booking booking = this.bookingRepo.findById(bookingId).
				orElseThrow(()->new ResourceNotFoundException("booking", "Id", bookingId));
		
		if(!Util.isNullEmpty(bookingDto.getDestination())) {
			booking.setDestination(bookingDto.getDestination());
		}
		if(!Util.isNullEmpty(bookingDto.getSource())) {
			booking.setSource(bookingDto.getSource());
		}
		if(!Util.isNullEmpty(bookingDto.getStatus())) {
			booking.setStatus(bookingDto.getStatus());
		}
		if(!Util.isNullEmpty(bookingDto.getBusNumber())) {
			booking.setBusNumber(bookingDto.getBusNumber());
		}
		if(bookingDto.getNumber_of_seats()!=0) {
			booking.setNumber_of_seats(bookingDto.getNumber_of_seats());
		}
		
		booking.setPassengerList(bookingDto.getPassengerList());
		
		Booking save = this.bookingRepo.save(booking);
		return save;
	}
	
	public void deleteBooking(Integer bookingId) {
		Booking booking = this.bookingRepo.findById(bookingId).orElseThrow(()-> new ResourceNotFoundException("booking", "id", bookingId));
		this.bookingRepo.delete(booking);
	}
	
	public List<Booking> getAllBooking() {
		List<Booking> findAll = this.bookingRepo.findAll();
		return findAll;
	}
	
	public Booking getBookingById(Integer bookingId) {
		Booking booking = this.bookingRepo.findById(bookingId)
		.orElseThrow(()-> new ResourceNotFoundException("booking", "Id", bookingId));
		return booking;
	}
	
}
