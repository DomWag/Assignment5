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
		
		AccountManager accountManager = null;
		
		long startTimeInNanoSecs = 0;
		long endTimeInNanoSecs = 0;
		long totalTime = 0;
		
		for (int i = 0 * 10; i < 200; i++){
			
			PartitionFileSystem fileSystem = new PartitionFileSystem();
		    fileSystem = createFileSystem(i);
			accountManager = fileSystem;
			startTimeInNanoSecs = System.nanoTime();
			accountManager.credit(1, 1, 10);
			endTimeInNanoSecs = System.nanoTime();
			totalTime = (startTimeInNanoSecs - endTimeInNanoSecs);
			System.out.println("Number of partitions: " + i);
			System.out.println("Throughput: " + 1/totalTime);
		}
	}
	
	public static PartitionFileSystem createFileSystem(int partitionsNumber){
		
		CertainBankPartition bankPartition = new CertainBankPartition();
		Account bob = new Account(1, 1, 1000);
		HashMap<Integer, HashSet<Account>> branchAccountMap1 = new HashMap<Integer, HashSet<Account>>();
		HashSet<Account> accountSet1 = new HashSet<Account>();
		accountSet1.add(bob);
		branchAccountMap1.put(1, accountSet1);
		bankPartition.setAccountMap(branchAccountMap1);
		PartitionFileSystem fileSystem = new PartitionFileSystem();
		System.out.println(bankPartition);
		fileSystem.addPartition(bankPartition);
		
		for(int i = 0; i < partitionsNumber - 1; i++){
			
			CertainBankPartition emptyPartition = null;
			fileSystem.addPartition(emptyPartition);
		}
		
		return fileSystem;
	}
}
