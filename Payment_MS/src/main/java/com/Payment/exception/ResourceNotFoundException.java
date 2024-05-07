package com.Payment.exception;

public class ResourceNotFoundException extends RuntimeException {

	String resourceName;
	String fieldName;
	long fieldValue;
	public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
		super(String.format("%s not fount with %s : %s", resourceName,fieldName,fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
}