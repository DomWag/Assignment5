package com.acertainbank.client.workloads;

import com.acertainbank.AccountManager;
import com.acertainbank.business.PartitionFileSystem;

public class WorkloadConfiguration {

	private int amountToCredit;
	private AccountManager accountManager = null;
	private int warmUpRuns = 10;
	private int numActualRuns = 50;	
	private PartitionFileSystem fileSystem = null;
	
	public PartitionFileSystem getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(PartitionFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public int getNumActualRuns() {
		return numActualRuns;
	}

	public void setNumActualRuns(int numActualRuns) {
		this.numActualRuns = numActualRuns;
	}

	public void setWarmUpRuns(int warmUpRuns) {
		this.warmUpRuns = warmUpRuns;
	}

	public int getWarmUpRuns() {
		return warmUpRuns;
	}

	public void setWarmUpRounds(int warmUpRuns) {
		this.warmUpRuns = warmUpRuns;
	}

	public WorkloadConfiguration(AccountManager accountManager){
		
		this.accountManager = accountManager;
	}

	public int getAmountToCredit() {
		return amountToCredit;
	}

	public void setAmountToCredit(int amountToCredit) {
		this.amountToCredit = amountToCredit;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
