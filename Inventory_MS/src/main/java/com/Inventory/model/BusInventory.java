package com.Inventory.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="bus_inventory")
public class BusInventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String busNumber;
	
	private int availableSeat;
	
	private Date lastUpdatedDate;
	
	private String status;

	public BusInventory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BusInventory(int id, String busNumber, int availableSeat, Date lastUpdatedDate) {
		super();
		this.id = id;
		this.busNumber = busNumber;
		this.availableSeat = availableSeat;
		this.lastUpdatedDate = lastUpdatedDate;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	public int getAvailableSeat() {
		return availableSeat;
	}

	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
		
	
}
