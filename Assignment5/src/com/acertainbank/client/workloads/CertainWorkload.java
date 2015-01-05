/**
 * 
 */
package com.acertainbank.client.workloads;


import java.util.HashMap;
import java.util.HashSet;


import com.acertainbank.AccountManager;
import com.acertainbank.business.Account;
import com.acertainbank.business.CertainBankPartition;
import com.acertainbank.business.PartitionFileSystem;



public class CertainWorkload {

	/**
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception {
		
		AccountManager accountManager = null;
		
		long startTimeInNanoSecs = 0;
		long endTimeInNanoSecs = 0;
		double totalTime = 0;
		
		for (int i = 10; i < 210; i = i + 10){
			
			PartitionFileSystem fileSystem = new PartitionFileSystem();
		    fileSystem = createFileSystem(i);
			accountManager = fileSystem;
			startTimeInNanoSecs = System.nanoTime();
			accountManager.credit(1, 1, 10);
			endTimeInNanoSecs = System.nanoTime();
			totalTime = (endTimeInNanoSecs - startTimeInNanoSecs);
			System.out.println("Number of partitions: " + i);
			Double throughput = 1/totalTime;
			System.out.println("Throughput: " + throughput);
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
		fileSystem.addPartition(bankPartition);
		
		for(int i = 0; i < partitionsNumber - 1; i++){
			
			CertainBankPartition emptyPartition = null;
			fileSystem.addPartition(emptyPartition);
		}
		
		return fileSystem;
	}
}
