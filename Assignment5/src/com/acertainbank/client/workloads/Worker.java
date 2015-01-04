package com.acertainbank.client.workloads;

import java.util.concurrent.Callable;

import com.acertainbank.business.CertainBankPartition;
import com.acertainbank.business.PartitionFileSystem;
import com.acertainbank.client.workloads.WorkloadConfiguration;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;
import com.acertainbank.client.workloads.WorkerRunResult;

public class Worker implements Callable<WorkerRunResult> {

	private WorkloadConfiguration configuration = null;

	public Worker(WorkloadConfiguration configuration) {
		
		this.configuration = configuration;
	}
	

	private void runTransfer(int branchId, int accountId, PartitionFileSystem fileSystem) throws InexistentAccountException, NegativeAmountException {
		
		try {
			configuration.getFileSystem().credit(branchId, accountId, 1);
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
			
			runTransfer(1, 1, configuration.getFileSystem());
		}
		
		count = 1;
		startTimeInNanoSecs = System.nanoTime();
		
		while (count++ <= configuration.getNumActualRuns()) {
			
			runTransfer(1, 1, configuration.getFileSystem());
			numTransfers++;
		}
		
		endTimeInNanoSecs = System.nanoTime();
		timeForRunsInNanoSecs += (endTimeInNanoSecs - startTimeInNanoSecs);
		
		return new WorkerRunResult(numTransfers,
				timeForRunsInNanoSecs);
		
	}
}
