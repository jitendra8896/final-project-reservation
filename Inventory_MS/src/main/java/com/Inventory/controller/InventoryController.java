package com.Inventory.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.Inventory.constant.Constant;
import com.Inventory.model.BusInventory;
import com.Inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Inventory.payload.ApiResponse;
import com.Inventory.payload.BookingEvent;

import com.Inventory.payload.PaymentEvent;
import com.Inventory.repository.InventoryRepo;


@RestController
@RequestMapping("/api/v1/bus/inventory")
public class InventoryController {

	@Autowired
	private InventoryService invService;
	
	@Autowired
	private InventoryRepo invRepo;
	
	@Autowired
	private KafkaTemplate<String, BookingEvent> bookingKafkaTemplet;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> paymentKafkaTemplet;
	
	
	@KafkaListener(topics = Constant.PAYMENT_CREATED_TOPIC,groupId = Constant.INVENTORY_GROUP_ID)
	public void updateAllocatedSeat(String event) throws Exception {
		System.out.println("inside the inventory to update seat"+event);
		PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class);
		
		try {
			
			String busNumber = paymentEvent.getBusNumber();
			int number_of_seats = paymentEvent.getNumber_of_seats();
			Optional<BusInventory> busInventory = Optional.ofNullable(invRepo.findByBusNumber(busNumber)) ;
			
			if(busInventory.isPresent()) {
				int availableSeat = busInventory.get().getAvailableSeat();
				if(availableSeat-number_of_seats>0) {
					BusInventory inventory = busInventory.get();
					inventory.setAvailableSeat(availableSeat-number_of_seats);
					inventory.setStatus(Constant.SUCCESS);
					invRepo.save(inventory);
					BookingEvent be = new BookingEvent();
					be.setBooking_number(paymentEvent.getBooking_number());
					be.setBusNumber(paymentEvent.getBusNumber());
					bookingKafkaTemplet.send(Constant.UPDATE_BOOKING_STATUS_TOPICE, be);
					
				}else {
					throw new Exception("required seat greater than available seat");	
				}
				
			}else {
				throw new Exception("bus number not found");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			paymentEvent.setType(Constant.PAYMENT_REVERSE);
			paymentKafkaTemplet.send(Constant.REVERSE_PAYMENT_CREATED_TOPIC, paymentEvent);
		}
		
	}
	
	
	
	@PostMapping("/add")
	public ResponseEntity<BusInventory> createBusInventory(@RequestBody BusInventory busInv){
		busInv.setLastUpdatedDate(new Date());
		BusInventory createBusInventory = this.invService.createBusInventory(busInv);
		return new ResponseEntity<>(createBusInventory,HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{invId}")
	public ResponseEntity<BusInventory> updateBus(@RequestBody BusInventory busInv,@PathVariable("invId") Integer invId){
		BusInventory updateBusInventory = this.invService.updateBusInventory(busInv, invId);
		return ResponseEntity.ok(updateBusInventory);
	}
	
	//delete booking
	
	@DeleteMapping("/delete/{invId}")
	public ResponseEntity<?> deleteBus(@PathVariable Integer invId){
		this.invService.deleteBusInventory(invId);
		return new ResponseEntity<>(new ApiResponse("Inventory Delete successfully..", true),HttpStatus.OK);
	}
	//get user
	@GetMapping("/getall")
	public ResponseEntity<List<BusInventory>> getAllBus(){
		List<BusInventory> all = this.invService.getAllBusInventory();
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/get/{invId}")
	public ResponseEntity<BusInventory> getBusById(@PathVariable Integer invId){
		return ResponseEntity.ok(this.invService.getBusInventoryById(invId));
	}
	@GetMapping("/getSeatCount/{busNumber}")
	public String getTotalSeatAvailable(@PathVariable String busNumber) {
		String seatAvailable = this.invService.getTotalSeatAvailable(busNumber);
				
		return seatAvailable;
	}
	
	@GetMapping("/updateBookedSeat/{busNumber}/{totalBookedSeat}")
	public String updateBookedSeat(@PathVariable String busNumber,@PathVariable String totalBookedSeat) {
		String seatAvailable = this.invService.updateBookedSeat(busNumber,totalBookedSeat);
				
		return seatAvailable;
	}

}
