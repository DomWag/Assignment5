/**
 * 
 */
package com.acertainbank.client.workloads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.acertainbank.AccountManager;
import com.acertainbank.business.Account;
import com.acertainbank.business.CertainBankPartition;
import com.acertainbank.business.PartitionFileSystem;
import com.acertainbank.client.AccountManagerProxy;
import com.acertainbank.client.BankClientConstants;


/**
 * 
 * CertainWorkload class runs the workloads by different workers concurrently.
 * It configures the environment for the workers using WorkloadConfiguration
 * objects and reports the metrics
 * 
 */
public class CertainWorkload {

	/**
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception {
		//if non local test you have to run this, but NOTE every time restart the server
		// also you have to manually increase the parameter and switch the boolean to false
		// in the configuration adjust warmup to 100, actual runs to 200
		//execution(10);
		
		//use this if local test
		for(int i = 10; i<=200; i = i +10){
			execution(i);
		}
	}
	

	public static void execution(int clientNumber) throws Exception {
		int numConcurrentWorkloadThreads = clientNumber;
		String serverAddress = "http://localhost:8081";
		boolean localTest = false;
		List<WorkerRunResult> workerRunResults = new ArrayList<WorkerRunResult>();
		List<Future<WorkerRunResult>> runResults = new ArrayList<Future<WorkerRunResult>>();

		// Initialize the RPC interfaces if its not a localTest, the variable is
		// overridden if the property is set
		String localTestProperty = System
			.getProperty(BankClientConstants.PROPERTY_KEY_LOCAL_TEST);
		localTest = (localTestProperty != null) ? Boolean
				.parseBoolean(localTestProperty) : localTest;


		AccountManager accountManager = null;
		
		if (localTest) {
			
			PartitionFileSystem fileSystem = new PartitionFileSystem();
			accountManager = fileSystem;
		
		} else {
			accountManager = new AccountManagerProxy(serverAddress + "/accountManager");
		}

		// Generate data in the bookstore before running the workload
		
		PartitionFileSystem fileSystem = new PartitionFileSystem();
		initializeBankData(fileSystem);
		
	
		ExecutorService exec = Executors
				.newFixedThreadPool(numConcurrentWorkloadThreads);

		for (int i = 0; i < numConcurrentWorkloadThreads; i++) {
			WorkloadConfiguration config = new WorkloadConfiguration(accountManager);
			config.setFileSystem(fileSystem);
			Worker workerTask = new Worker(config);
			// Keep the futures to wait for the result from the thread
			runResults.add(exec.submit(workerTask));
		}

		// Get the results from the threads using the futures returned
		for (Future<WorkerRunResult> futureRunResult : runResults) {
			WorkerRunResult runResult = futureRunResult.get(); // blocking call
			workerRunResults.add(runResult);
		}

		exec.shutdownNow(); // shutdown the executor

		// Finished initialization, stop the clients if not localTest
		if (!localTest) {
			((AccountManagerProxy) accountManager).stop();
		}

		reportMetric(workerRunResults);
	}
	/**
	 * Computes the metrics and prints them
	 * 
	 * @param workerRunResults
	 */
	public static void reportMetric(List<WorkerRunResult> workerRunResults) {
		// TODO: You should aggregate metrics and output them for plotting here
		
		double totalTime = 0;
		double totalTransfers = 0;
		
		for (WorkerRunResult workerRunResult: workerRunResults){
			totalTime += workerRunResult.getElapsedTimeInNanoSecs();
			totalTransfers += workerRunResult.getNumTransfers();
		}
		
		double latency = totalTime/workerRunResults.size(); // latency = average time to generate a response
		double throughput = totalTransfers/totalTime; // throughput = average successful interactions per time period
		//System.out.println("Please notice, the time is in ns! Factor 1ns = 1,0*10^-9 s");
		//System.out.println("total time "+totalTime);
		System.out.println("workerRunResults: "+workerRunResults.size());
		System.out.println("Latency: "+latency);
		System.out.println("Throughput: "+throughput);
	}
	
	public static void initializeBankData(PartitionFileSystem fileSystem)  {
		
		CertainBankPartition bankPartition1 = new CertainBankPartition();
		CertainBankPartition bankPartition2 = new CertainBankPartition();
		CertainBankPartition bankPartition3 = new CertainBankPartition();
		
		HashMap<Integer, HashSet<Account>> branchAccountMap1 = new HashMap<Integer, HashSet<Account>>();
		Account bob = new Account(1, 1, 1000);
		HashSet<Account> accountSet1 = new HashSet<Account>();
		accountSet1.add(bob);
		branchAccountMap1.put(1, accountSet1);
		bankPartition1.setAccountMap(branchAccountMap1);
		
		HashMap<Integer, HashSet<Account>> branchAccountMap2 = new HashMap<Integer, HashSet<Account>>();
		Account ted = new Account(2, 1, 500);
		HashSet<Account> accountSet2 = new HashSet<Account>();
		accountSet2.add(ted);
		branchAccountMap1.put(1, accountSet2);
		bankPartition2.setAccountMap(branchAccountMap2);
		
		HashMap<Integer, HashSet<Account>> branchAccountMap3 = new HashMap<Integer, HashSet<Account>>();
		Account larry = new Account(3, 1, 200);
		HashSet<Account> accountSet3 = new HashSet<Account>();
		accountSet3.add(larry);
		branchAccountMap1.put(1, accountSet3);
		bankPartition3.setAccountMap(branchAccountMap3);
		
		fileSystem.addPartition(bankPartition1);
		fileSystem.addPartition(bankPartition2);
		fileSystem.addPartition(bankPartition3);	
	}
}
