/**
 * 
 */
package com.acertainbank.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.acertainbank.AccountManager;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;

/**
 * @author Dominik Wagner
 *
 */
public class CertainBank implements AccountManager {
	
	private HashMap<Long, Account> accountMap = null;
	private HashSet<Long> bankSet = null;

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
		} else if (!bankSet.contains(branchId)) {
			throw new InexistentBranchException(branchId);
		} else if (!accountMap.containsKey(Long.parseLong(branchId + ""
				+ accountId))) {
			throw new InexistentAccountException(accountId);
		}
		Account ac = accountMap.get(Long.parseLong(branchId + "" + accountId));
		ac.setAmount(ac.getAmount() + amount);

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

		Account ac = accountMap.get(Long.parseLong(branchId + "" + accountId));
		ac.setAmount(ac.getAmount() - amount);

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

		Account ac = accountMap.get(Long.parseLong(branchId + ""
				+ accountIdOrig));
		ac.setAmount(ac.getAmount() - amount);
		Account acD = accountMap.get(Long.parseLong(branchId + ""
				+ accountIdDest));
		acD.setAmount(acD.getAmount() + amount);

	}

	@Override
	public double calculateExposure(int branchId)
			throws InexistentBranchException {

		if (!bankSet.contains(branchId)) {
			throw new InexistentBranchException(branchId);
		}
		double sum = 0;
		Iterator it = accountMap.entrySet().iterator();
		Map.Entry<Long, Account> pair = (Map.Entry<Long, Account>) it.next();
		long branch = pair.getValue().getBranchID();
		if (branch == branchId) {
			sum = sum + pair.getValue().getAmount();
		}
		return sum;
	}

}
