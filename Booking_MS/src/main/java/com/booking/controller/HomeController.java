package com.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;

import com.booking.model.Booking;
import com.booking.payload.ApiResponse;
import com.booking.service.BookingService;

@RestController
@RequestMapping("/api/v1/booking")
public class HomeController {

	@Autowired
	private BookingService bookingService;
	
	//add booking
	@PostMapping("/add")
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking){
		Booking createBooking = this.bookingService.createBooking(booking);
		return new ResponseEntity<>(createBooking,HttpStatus.CREATED);
	}
	
	@GetMapping("/cancel/{bookingNumber}")
	public ResponseEntity<?> cancelBooking(@PathVariable int bookingNumber){
		String cancelBooking = this.bookingService.cancelBooking(bookingNumber);
		return new ResponseEntity<>(new ApiResponse(cancelBooking, true),HttpStatus.OK);
		
	}
	//put booking
	@PutMapping("/update/{bookingId}")
	public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking,@PathVariable("bookingId") Integer bookingId){
		Booking updateBooking = this.bookingService.updateBooking(booking, bookingId);
		return ResponseEntity.ok(updateBooking);
	}
	
	//delete booking
	
	@DeleteMapping("/delete/{bookingId}")
	public ResponseEntity<?> deleteBooking(@PathVariable Integer bookingId){
		this.bookingService.deleteBooking(bookingId);
		return new ResponseEntity<>(new ApiResponse("Booking Delete successfully..", true),HttpStatus.OK);
	}
	//get user
	@GetMapping("/getall")
	public ResponseEntity<List<Booking>> getAllBooking(){
		List<Booking> allBooking = this.bookingService.getAllBooking();
		return ResponseEntity.ok(allBooking);
	}
	@GetMapping("/get/{bookingId}")
	public ResponseEntity<Booking> getBookingById(@PathVariable Integer bookingId){
		return ResponseEntity.ok(this.bookingService.getBookingById(bookingId));
	}
}
