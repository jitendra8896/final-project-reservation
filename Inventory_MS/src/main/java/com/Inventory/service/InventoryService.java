package com.Inventory.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.Inventory.model.BusInventory;
import com.Inventory.repository.InventoryRepo;
import com.Inventory.util.*;
import com.Inventory.exception.ResourceNotFoundException;


@Service
public class InventoryService {

	@Autowired
	private InventoryRepo inventoryRepo;
	
	public BusInventory createBusInventory(@RequestBody BusInventory busInventory) {
		 
		return this.inventoryRepo.save(busInventory);
	}
	
	public void deleteBusInventory(Integer inventoryId) {
		BusInventory busInventory = this.inventoryRepo.findById(inventoryId).orElseThrow(()-> new ResourceNotFoundException("inventory", "id", inventoryId));
		this.inventoryRepo.delete(busInventory);
	}

	public List<BusInventory> getAllBusInventory() {
		List<BusInventory> findAll = this.inventoryRepo.findAll();
		return findAll;
	}
	
	public BusInventory getBusInventoryById(Integer id) {
		BusInventory busInventory = this.inventoryRepo.findById(id)
		.orElseThrow(()-> new ResourceNotFoundException("BusInventory", "Id", id));
		return busInventory;
	}

	public String getTotalSeatAvailable(String busNumber) {
		int count=0;
		Optional<BusInventory> busOptional = Optional.ofNullable(this.inventoryRepo.findByBusNumber(busNumber));
	    if(busOptional.isPresent()) {
	    	count=busOptional.get().getAvailableSeat();
	    }
		String c = String.valueOf(count);
		return c;
	}
	
	public String updateBookedSeat(String busNumber,String totalBookedSeat) {
		int bookedSeat=Integer.parseInt(totalBookedSeat);
		Optional<BusInventory> busOptional = Optional.ofNullable(this.inventoryRepo.findByBusNumber(busNumber));
	    if(busOptional.isPresent()) {
	    	BusInventory busInventory = busOptional.get();
	    	int avaSeat =busInventory.getAvailableSeat();
	    	busInventory.setAvailableSeat(avaSeat+bookedSeat);
	    	this.inventoryRepo.save(busInventory);
	    }
		
		return "Seat Updated";
	}
	
	public BusInventory updateBusInventory(BusInventory invDto, Integer id) {
		BusInventory busInventory = this.inventoryRepo.findById(id).
				orElseThrow(()->new ResourceNotFoundException("BusInventory", "Id", id));
		if(!Util.isNullEmpty(invDto.getBusNumber())) {
			busInventory.setBusNumber(invDto.getBusNumber());
		}
		if(invDto.getAvailableSeat()!=0) {
		busInventory.setAvailableSeat(invDto.getAvailableSeat());
		}
		busInventory.setLastUpdatedDate(new Date());
		BusInventory save = this.inventoryRepo.save(busInventory);
		return save;
	}

}
