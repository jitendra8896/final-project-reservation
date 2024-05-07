package com.Payment.payload;

public class BookingEvent {

	private int booking_number;
	
	private String busNumber;
	
	private int number_of_seats;
	
	private String type;

	public int getBooking_number() {
		return booking_number;
	}

	public void setBooking_number(int booking_number) {
		this.booking_number = booking_number;
	}

	public String getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	public int getNumber_of_seats() {
		return number_of_seats;
	}

	public void setNumber_of_seats(int number_of_seats) {
		this.number_of_seats = number_of_seats;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

		
	
}
