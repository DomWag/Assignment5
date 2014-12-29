package com.acertainbank.utils;

import java.util.List;

/**
 * Data Structure that we use to communicate objects and error messages
 * from the server to the client.
 * 
 */
public class BankResponse {
	private BankException exception;
	private List<?> list;

	public BankException getException() {
		return exception;
	}

	public void setException(BankException exception) {
		this.exception = exception;
	}

	public BankResponse(BankException exception) {
		this.setException(exception);
	}

	public BankResponse() {
		this.setException(null);
		this.setList(null);
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}