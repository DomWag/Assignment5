/**
 * 
 */
package com.acertainbank.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.acertainbank.AccountManager;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;

/**
 * @author Dominik Wagner and Caleb Larson
 *
 */
public class CertainBankPartition implements AccountManager {

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private Lock sharedLock = readWriteLock.readLock();
	private Lock exclusiveLock = readWriteLock.writeLock();
	
	private HashMap<Integer, HashSet<Account>> branchAccountMap = null;
	
	public CertainBankPartition() {
		branchAccountMap = new HashMap<Integer, HashSet<Account>>();
	}

	@Override
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (!branchAccountMap.containsKey(branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!isAccountInSet(branchAccountMap.get(branchId), accountId)) {
			throw new InexistentAccountException(accountId);
		}

		exclusiveLock.lock();
		
		for (Account a: branchAccountMap.get(branchId)){
			
			if (a.getAccoundID() == accountId){
				a.setAmount(a.getAmount() + amount);
			}
		}
		
		exclusiveLock.unlock();
	}

	@Override
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (!branchAccountMap.containsKey(branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!isAccountInSet(branchAccountMap.get(branchId), accountId)) {
			throw new InexistentAccountException(accountId);
		}
		
		exclusiveLock.lock();
		
		for (Account a: branchAccountMap.get(branchId)){
			
			if (a.getAccoundID() == accountId){
				a.setAmount(a.getAmount() - amount);
			}
		}
		
		exclusiveLock.unlock();

	}

	@Override
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (!branchAccountMap.containsKey(branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!isAccountInSet(branchAccountMap.get(branchId), accountIdOrig)) {
			throw new InexistentAccountException(accountIdOrig);
		}  else if (!isAccountInSet(branchAccountMap.get(branchId), accountIdDest)) {
			throw new InexistentAccountException(accountIdDest);
		}

		exclusiveLock.lock();
		Account origin = null;
		Account destination = null;
		
		for (Account a: branchAccountMap.get(branchId)){
			
			if (a.getAccoundID() == accountIdOrig){
				origin = a;
			}
		}
		
		for (Account a: branchAccountMap.get(branchId)){
			
			if (a.getAccoundID() == accountIdDest){
				destination = a;
			}
		}
		origin.setAmount(origin.getAmount() - amount);
		destination.setAmount(destination.getAmount() + amount);
		
		exclusiveLock.unlock();	
	}

	@Override
	public double calculateExposure(int branchId)
			throws InexistentBranchException {

		if (!branchAccountMap.containsKey(branchId)) {
			throw new InexistentBranchException(branchId);
		}
		double sum = 0;

		sharedLock.lock();
		
		for (Account a: branchAccountMap.get(branchId)){
			
			if (a.getAmount() < 0){
				
				sum += a.getAmount();
			}	
		}
		
		sum = Math.abs(sum); // absolute value
		
		sharedLock.unlock();
		return sum; 
	}
	
	public boolean isAccountInSet(HashSet<Account> accounts, int accountId){
		
		for (Account a: accounts){
			
			if (a.getAccoundID() == accountId){
				
				return true;
			}
		}
		
		return false;
		
	}

	public HashMap<Integer, HashSet<Account>> getBranchAccountMap() {
		return branchAccountMap;
	}

	public void setAccountMap(HashMap<Integer, HashSet<Account>> branchAccountMap) {
		this.branchAccountMap = branchAccountMap;
	}

}
