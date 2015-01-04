package com.acertainbank.utils;

import com.acertainbank.business.CertainBankPartition;

/**
 * This class encapsulate the three / four things which are necessary to transfer to the destination.
 * @author Dominik Wagner
 *
 */
public class TransferObject {
	double amount;
	int branchID;
	int accountIdOrg;
	int accountIdDest;
	private CertainBankPartition bankPartition;

	public TransferObject(double amount, int branchID, int accountId, CertainBankPartition bankPartition){
		this.amount = amount;
		this.branchID = branchID;
		this.accountIdOrg = accountId;
		this.bankPartition = bankPartition;
		
	}
	
	
	public TransferObject(double amount, int branchID, int accountIdOrg, int accountIDDest){
		this.amount = amount;
		this.branchID = branchID;
		this.accountIdOrg = accountIdOrg;
		this.accountIdDest = accountIDDest;
		
	}


	public TransferObject(int branchId2) {
		this.branchID = branchId2;
	}


	public double getAmount() {
		return amount;
	}


	public int getBranchID() {
		return branchID;
	}


	public int getAccountIdOrg() {
		return accountIdOrg;
	}


	public int getAccountIdDest() {
		return accountIdDest;
	}
	
	public CertainBankPartition getBankPartition() {
		return bankPartition;
	}


	public void setBankPartition(CertainBankPartition bankPartition) {
		this.bankPartition = bankPartition;
	}

}
