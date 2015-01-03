/**
 * 
 */
package com.acertainbank.business;

/**
 * This class symbolizes a bank account.
 * @author Dominik Wagner
 *
 */
public class Account {
	

	private int branchID;
	private int accoundID;
	private double amount;
	
	public int getAccoundID() {
		return accoundID;
	}

	public void setAccoundID(int accoundID) {
		this.accoundID = accoundID;
	}

	public void setBranchID(int branchID) {
		this.branchID = branchID;
	}

	public Account(int branchID, int accoundID, double amount) {
		this.branchID = branchID;
		this.accoundID = accoundID;
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof Account)){
			return false;
		}
		
		Account that = (Account) other;
		
		return (this.branchID == (that.branchID)) && (this.accoundID == (that.accoundID) && this.amount == that.amount);
	}
	
	@Override
	public int hashCode(){
		int hashCode = 1;
		hashCode = (int) (hashCode * 37 + this.branchID);
		hashCode = (int) (hashCode * 37 + this.accoundID);
		
		return hashCode;
	}
	
	public Integer getBranchID() {
		return branchID;
	}
}
