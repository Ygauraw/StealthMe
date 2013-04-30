package com.example.stealthme;

public class Contact {
	
	// private variables
	int _id;
	String _name;
	String _phone_number;
	String _message;
	
	// Empty Constructor
	public Contact() {
		
	}
	// constructor
	public Contact(int id, String name, String _phone_number, String message) {
		this._id = id;
		this._name = name;
		this._phone_number = _phone_number;
		this._message = message;
	}
	
	// constructor
	public Contact(String name, String _phone_number) {
		this._name = name;
		this._phone_number = _phone_number;
	}
	
	// constructor
		public Contact(String name, String _phone_number, String message) {
			this._name = name;
			this._phone_number = _phone_number;
			this._message = message;
			
		}
	
	// getting ID
	public int getID() {
		return this._id;
	}
	
	// setting ID
	public void setID(int id) {
		this._id = id;
	}
	
	// getting name
	public String getName() {
		return this._name;
	}
	
	// setting name
	public void setName(String name) {
		this._name = name;	
	}
	
	// getting phone number
	public String getPhoneNumber() {
		return this._phone_number;
	}
	
	// setting phone number
	public void setPhoneNumber(String phone_number) {
		this._phone_number = phone_number;
	}
	
	// getting message
	public String getMessage() {
		return this._message;
	}
	
	// setting message
	public void setMessage(String message) {
		this._message = message;
	}
}

