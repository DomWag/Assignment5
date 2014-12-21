/**
 * 
 */
package com.acertainbank.business;

/**
 * This class symbolize a bank account.
 * @author Dominik Wagner
 *
 */
public class Account {
	

	int branchID;
	int accoundID;
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	double amount;
	
	/**
	 * 
	 * @param branchID
	 * @param accoundID
	 * @param amount
	 */
	public Account(int branchID, int accoundID, double amount) {
		this.branchID = branchID;
		this.accoundID = accoundID;
		this.amount = amount;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof Account)){
			return false;
		}
		
		Account that = (Account) other;
		
		return (this.branchID == (that.branchID)) && (this.accoundID == (that.accoundID));
	}
	
	@Override
	public int hashCode(){
		int hashCode = 1;
		hashCode = (int) (hashCode * 37 + this.branchID);
		hashCode = (int) (hashCode * 37 + this.accoundID);
		
		return hashCode;
	}
	
	public long getBranchID() {
		return branchID;
	}

}
