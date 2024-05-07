package com.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.admin.service.AdminService;
import com.admin.payload.ApiResponse;
import com.admin.model.Bus;

@RestController
@RequestMapping("/api/v1/admin/bus")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/add")
	public ResponseEntity<Bus> createBus(@RequestBody Bus bus){
		Bus createBus = this.adminService.createBus(bus);
		return new ResponseEntity<>(createBus,HttpStatus.CREATED);
	}
	
	//put booking
		@PutMapping("/update/{busId}")
		public ResponseEntity<Bus> updateBus(@RequestBody Bus bus,@PathVariable("busId") Integer busId){
			Bus updateBus = this.adminService.updateBus(bus, busId);
			return ResponseEntity.ok(updateBus);
		}
		
		//delete booking
		
		@DeleteMapping("/delete/{busId}")
		public ResponseEntity<?> deleteBus(@PathVariable Integer busId){
			this.adminService.deleteBus(busId);
			return new ResponseEntity<>(new ApiResponse("Booking Delete successfully..", true),HttpStatus.OK);
		}
		//get user
		@GetMapping("/getall")
		public ResponseEntity<List<Bus>> getAllBus(){
			List<Bus> allBooking = this.adminService.getAllBus();
			return ResponseEntity.ok(allBooking);
		}
		
		@GetMapping("/getbyid/{busId}")
		public ResponseEntity<Bus> getBusById(@PathVariable Integer busId){
			return ResponseEntity.ok(this.adminService.getBusById(busId));
		}
		
		@GetMapping("/getbynumber/{busNumber}")
		public ResponseEntity<Bus> getBusByNumber(@PathVariable String busNumber){
			return ResponseEntity.ok(this.adminService.getBusByBusNumner(busNumber));
		}
}
