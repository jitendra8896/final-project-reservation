package com.admin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.model.Bus;
import com.admin.repository.AdminRepo;

import com.admin.util.Util;
import com.admin.exception.ResourceNotFoundException;



@Service
public class AdminService {


	@Autowired
	private AdminRepo adminRepo;
	
	
	public Bus createBus(Bus bus) {
		Bus save = this.adminRepo.save(bus);
		return save;
	}
	
	public void deleteBus(Integer busId) {
		Bus bus = this.adminRepo.findById(busId).orElseThrow(()-> new ResourceNotFoundException("bus", "id", busId));
		this.adminRepo.delete(bus);
	}
	
	public List<Bus> getAllBus() {
		List<Bus> findAll = this.adminRepo.findAll();
		return findAll;
	}
	
	public Bus getBusById(Integer busId) {
		Bus bus = this.adminRepo.findById(busId)
		.orElseThrow(()-> new ResourceNotFoundException("bus", "Id", busId));
		return bus;
	}
	
	public Bus getBusByBusNumner(String busNumber) {
		Bus byBusNumber = this.adminRepo.findByBusNumber(busNumber);
		return byBusNumber;
	}
	
	public Bus updateBus(Bus busDto, Integer busId) {
		Bus bus = this.adminRepo.findById(busId).
				orElseThrow(()->new ResourceNotFoundException("booking", "Id", busId));
		bus.setBusNumber(busDto.getBusNumber());
		bus.setDestination(busDto.getDestination());
		bus.setSource(busDto.getSource());
		bus.setPrice(busDto.getPrice());
		Bus save = this.adminRepo.save(bus);
		return save;
	}
}
