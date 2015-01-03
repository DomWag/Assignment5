/**
 * 
 */
package com.acertainbank.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.acertainbank.AccountManager;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;

/**
 * @author Dominik Wagner
 *
 */
public class CertainBank implements AccountManager {

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private Lock sharedLock = readWriteLock.readLock();
	private Lock exclusiveLock = readWriteLock.writeLock();

	private HashMap<Long, Account> accountMap = null;
	private HashSet<Long> bankSet = null;

	public HashMap<Long, Account> getAccountMap() {
		return accountMap;
	}

	public void setAccountMap(HashMap<Long, Account> accountMap) {
		this.accountMap = accountMap;
	}

	public HashSet<Long> getBankSet() {
		return bankSet;
	}

	public void setBankSet(HashSet<Long> bankSet) {
		this.bankSet = bankSet;
	}

	public CertainBank() {
		accountMap = new HashMap<Long, Account>();
		bankSet = new HashSet<Long>();
	}

	@Override
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (!bankSet.contains((long) branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!accountMap.containsKey(Long.parseLong(branchId + ""
				+ accountId))) {
			throw new InexistentAccountException(accountId);
		}

		exclusiveLock.lock();
		Account ac = accountMap.get(Long.parseLong(branchId + "" + accountId));
		ac.setAmount(ac.getAmount() + amount);
		exclusiveLock.unlock();
	}

	@Override
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (!bankSet.contains(branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!accountMap.containsKey(Long.parseLong(branchId + ""
				+ accountId))) {
			throw new InexistentAccountException(accountId);
		}
		exclusiveLock.lock();
		Account ac = accountMap.get(Long.parseLong(branchId + "" + accountId));
		ac.setAmount(ac.getAmount() - amount);
		exclusiveLock.unlock();

	}

	@Override
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (!bankSet.contains(branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!accountMap.containsKey(Long.parseLong(branchId + ""
				+ accountIdOrig))) {
			throw new InexistentAccountException(accountIdOrig);
		} else if (!accountMap.containsKey(Long.parseLong(branchId + ""
				+ accountIdDest))) {
			throw new InexistentAccountException(accountIdDest);
		}

		exclusiveLock.lock();
		Account ac = accountMap.get(Long.parseLong(branchId + ""
				+ accountIdOrig));
		ac.setAmount(ac.getAmount() - amount);
		Account acD = accountMap.get(Long.parseLong(branchId + ""
				+ accountIdDest));
		acD.setAmount(acD.getAmount() + amount);
		exclusiveLock.unlock();

	}

	@Override
	public double calculateExposure(int branchId)
			throws InexistentBranchException {

		if (!bankSet.contains(branchId)) {
			throw new InexistentBranchException(branchId);
		}
		double sum = 0;

		sharedLock.lock();
		Iterator it = accountMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Account> pair = (Map.Entry<Long, Account>) it
					.next();
			long branch = pair.getValue().getBranchID();
			if (branch == branchId && pair.getValue().getAmount() < 0) {
				sum = sum + pair.getValue().getAmount();
			}
		}
		sharedLock.unlock();
		return sum;
	}

}
