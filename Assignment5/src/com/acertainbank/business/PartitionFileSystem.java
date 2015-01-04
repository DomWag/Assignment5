package com.acertainbank.business;

import java.util.HashSet;

import com.acertainbank.AccountManager;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;

public class PartitionFileSystem implements AccountManager {
	
	private HashSet<CertainBankPartition> fileSystem;

	public PartitionFileSystem() {

		this.fileSystem = null;
	}

	public HashSet<CertainBankPartition> getFileSystem() {
		return fileSystem;
	}

	public void setFilesystem(HashSet<CertainBankPartition> filesystem) {
		this.fileSystem = filesystem;
	}
	
	public void addPartition (CertainBankPartition toBeAddedPartition){
		
		fileSystem.add(toBeAddedPartition);
	}

	@Override
	public void credit(int branchId, int accountId, double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		
		for (CertainBankPartition bankPartition : fileSystem){
			
			if (bankPartition.getBranchAccountMap().containsKey(branchId)){
				
				bankPartition.credit(branchId, accountId, amount);
			}
		}
		
	}

	@Override
	public void debit(int branchId, int accountId, double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		
		for (CertainBankPartition bankPartition : fileSystem){
			
			if (bankPartition.getBranchAccountMap().containsKey(branchId)){
				
				bankPartition.debit(branchId, accountId, amount);
			}
		}
		
	}

	@Override
	public void transfer(int branchId, int accountIdOrig, int accountIdDest, double amount)
			throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		
		for (CertainBankPartition bankPartition : fileSystem){
			
			if (bankPartition.getBranchAccountMap().containsKey(branchId)){
				
				bankPartition.transfer(branchId, accountIdOrig, accountIdDest, amount);
			}
		}
		
	}

	@Override
	public double calculateExposure(int branchId) throws InexistentBranchException {
		
		for (CertainBankPartition bankPartition : fileSystem){
			
			if (bankPartition.getBranchAccountMap().containsKey(branchId)){
				
				return bankPartition.calculateExposure(branchId);
			}
		}
		return 0;
	}
}
