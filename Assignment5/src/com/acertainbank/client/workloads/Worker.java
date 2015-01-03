package com.acertainbank.client.workloads;

import java.util.concurrent.Callable;


import com.acertainbank.business.CertainBank;
import com.acertainbank.client.workloads.WorkloadConfiguration;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;
import com.acertainbank.client.workloads.WorkerRunResult;

public class Worker implements Callable<WorkerRunResult> {

	private WorkloadConfiguration configuration = null;
	private CertainBank bank;
	
	public Worker(WorkloadConfiguration configuration, CertainBank bank) {
		
		this.configuration = configuration;
		this.bank = bank;
	}
	

	private void runTransfer(int branchId, int accountId) throws InexistentAccountException, NegativeAmountException {
		
		try {
			configuration.getAccountManager().credit(branchId, accountId, 1);
		} catch (InexistentBranchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NegativeAmountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public WorkerRunResult call() throws Exception {
		
		int count = 1;
		long startTimeInNanoSecs = 0;
		long endTimeInNanoSecs = 0;
		long timeForRunsInNanoSecs = 0;
		int numTransfers = 0;
		
		while (count++ <= configuration.getWarmUpRuns()) {
			
			runTransfer(1, 1);
		}
		
		count = 1;
		startTimeInNanoSecs = System.nanoTime();
		
		while (count++ <= configuration.getNumActualRuns()) {
			
			runTransfer(1, 1);
			numTransfers++;
		}
		
		endTimeInNanoSecs = System.nanoTime();
		timeForRunsInNanoSecs += (endTimeInNanoSecs - startTimeInNanoSecs);
		
		return new WorkerRunResult(numTransfers,
				timeForRunsInNanoSecs);
		
	}
}
