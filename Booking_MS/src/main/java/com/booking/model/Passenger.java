package com.booking.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "passenger")
public class Passenger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	private int passenger_id ;
	

	public Passenger() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Passenger(int passenger_id, Booking booking) {
		super();
		this.passenger_id = passenger_id;
	}



	public int getPassenger_id() {
		return passenger_id;
	}



	public void setPassenger_id(int passenger_id) {
		this.passenger_id = passenger_id;
	}

	
}
