package com.booking.payload;

import java.util.List;

import org.springframework.http.HttpStatus;


public class Response {

	private String message ;
	private int resultCode;
	private String errorMessage;
	private HttpStatus status;
	private List data;
	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Response(String message, int resultCode, String errorMessage, HttpStatus status, List data) {
		super();
		this.message = message;
		this.resultCode = resultCode;
		this.errorMessage = errorMessage;
		this.status = status;
		this.data = data;
	}
	public Response(String message, int resultCode, List data) {
		super();
		this.message = message;
		this.resultCode = resultCode;
		this.data = data;
	}
	public Response(int resultCode, String errorMessage, HttpStatus status) {
		super();
		this.resultCode = resultCode;
		this.errorMessage = errorMessage;
		this.status = status;
	}
	public Response(int resultCode, Throwable ex) {
	       this();
	       this.resultCode = resultCode;
	       this.errorMessage = ex.getLocalizedMessage();
	   }
	
	public Response(HttpStatus status, Throwable ex) {
	       this();
	       this.status = status;
	       this.errorMessage = ex.getLocalizedMessage();
	   }
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	
	
	
	
}
