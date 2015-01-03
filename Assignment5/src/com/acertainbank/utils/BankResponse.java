package com.acertainbank.utils;



/**
 * Data Structure that we use to communicate objects and error messages
 * from the server to the client.
 * 
 */
public class BankResponse {
	private BankException exception;
	private InexistentAccountException iAe;
	private InexistentBranchException iBe;
	private NegativeAmountException nAe;
	private double calculation;

	public  Exception getException() {
		return exception;
	}

	public BankResponse(BankException exception) {
		this.setException(exception);
	}

	public BankResponse() {
		this.exception = null;
		this.iAe = null;
		this.iBe = null;
		this.nAe = null;
		this.calculation = 0;
	}

	

	public void setException(BankException exception) {
		this.exception = exception;
	}
	public void setException(InexistentAccountException iAe) {
		this.iAe = iAe;
	}
	public void setException(InexistentBranchException iBe) {
		this.iBe = iBe;
	}
	public void setException(NegativeAmountException nAe) {
		this.nAe = nAe;
	}

	public double getCalculation() {
		return calculation;
	}

	public void setCalculation(double d) {
		this.calculation = d;
	} 
}