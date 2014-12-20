package com.acertainbank.client;

import com.acertainbank.*;

public class AccountManagerProxy implements AccountManager {

	@Override
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (branchId < 0 || ) {
			throw new InexistentBranchException(branchId);
		} else if (accountId < 0 || ) {
			throw new InexistentAccountException(accountId);
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (branchId < 0 || ) {
			throw new InexistentBranchException(branchId);
		} else if (accountId < 0 || ) {
			throw new InexistentAccountException(accountId);
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (branchId < 0 || ) {
			throw new InexistentBranchException(branchId);
		} else if (accountId < 0 || ) {
			throw new InexistentAccountException(accountId);
		}
		// TODO Auto-generated method stub

	}

	@Override
	public double calculateExposure(int branchId)
			throws InexistentBranchException {
		if (branchId < 0 || ){
			throw new InexistentBranchException(branchId);

		}
		// TODO Auto-generated method stub
		return 0;
	}

}
