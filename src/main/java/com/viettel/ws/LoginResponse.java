package com.viettel.ws;

import com.viettel.core.user.BO.Users;

public class LoginResponse {
	int statusCode = 200;
	String message ="success";
	Users user;
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}


	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	 
	
}