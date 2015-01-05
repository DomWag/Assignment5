package com.acertainbank.utils;

/**
 * Exception to signal a bank error
 */
public class BankException extends Exception {
	private static final long serialVersionUID = 1L;

	public BankException() {
		super();
	}

	public BankException(String message) {
		super(message);
	}

	public BankException(String message, Throwable cause) {
		super(message, cause);
	}

	public BankException(Throwable ex) {
		super(ex);
	}
}
