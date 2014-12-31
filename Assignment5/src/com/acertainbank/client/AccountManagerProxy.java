package com.acertainbank.client;


import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange.ContentExchange;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.acertainbank.*;
import com.acertainbank.utils.BankClientConstants;
import com.acertainbank.utils.BankException;
import com.acertainbank.utils.BankMessageTag;
import com.acertainbank.utils.BankUtility;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;
import com.acertainbank.utils.TransferObject;

public class AccountManagerProxy implements AccountManager {
	
	protected HttpClient client;
	protected String serverAddress;

	/**
	 * Initialize the client object
	 */
	public AccountManagerProxy(String serverAddress) throws Exception {
		setServerAddress(serverAddress);
		client = new HttpClient();
		client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		client.setMaxConnectionsPerAddress(BankClientConstants.CLIENT_MAX_CONNECTION_ADDRESS); // max
																									// concurrent
																									// connections
																									// to
																									// every
																									// address
		client.setThreadPool(new QueuedThreadPool(
				BankClientConstants.CLIENT_MAX_THREADSPOOL_THREADS)); // max
																			// threads
		client.setTimeout(BankClientConstants.CLIENT_MAX_TIMEOUT_MILLISECS); // seconds
																					// timeout;
																					// if
																					// no
																					// server
																					// reply,
																					// the
																					// request
																					// expires
		client.start();
	}
	

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	
	public void stop() {
		try {
			client.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (branchId < 0 ) {
			throw new InexistentBranchException(branchId);
		} else if (accountId < 0 ) {
			throw new InexistentAccountException(accountId);
		}
		
		ContentExchange exchange = new ContentExchange();
		// TODO the server adress is not fix, it has to look up
		String urlString = serverAddress + "/" + BankMessageTag.CREDIT;
		TransferObject to = new TransferObject(amount, branchId, accountId);
		String toXMLstring = BankUtility
				.serializeObjectToXMLString(to);
		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(toXMLstring);
		exchange.setRequestContent(requestContent);
		try {
			BankUtility.SendAndRecv(this.client, exchange);
		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (branchId < 0 ) {
			throw new InexistentBranchException(branchId);
		} else if (accountId < 0 ) {
			throw new InexistentAccountException(accountId);
		}
		
		ContentExchange exchange = new ContentExchange();
		// TODO the server adress is not fix, it has to look up
		String urlString = serverAddress + "/" + BankMessageTag.CREDIT;
		TransferObject to = new TransferObject(amount, branchId, accountId);
		String toXMLstring = BankUtility
				.serializeObjectToXMLString(to);
		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(toXMLstring);
		exchange.setRequestContent(requestContent);
		try {
			BankUtility.SendAndRecv(this.client, exchange);
		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		if (amount < 0) {
			throw new NegativeAmountException(amount);
		} else if (branchId < 0  ) {
			throw new InexistentBranchException(branchId);
		} else if (accountIdOrig < 0 || accountIdDest < 0) {
			throw new InexistentAccountException(accountIdOrig);
		}
		
		ContentExchange exchange = new ContentExchange();
		// TODO the server adress is not fix, it has to look up
		String urlString = serverAddress + "/" + BankMessageTag.CREDIT;
		TransferObject to = new TransferObject(amount, branchId, accountIdOrig, accountIdDest);
		String toXMLstring = BankUtility
				.serializeObjectToXMLString(to);
		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(toXMLstring);
		exchange.setRequestContent(requestContent);
		try {
			BankUtility.SendAndRecv(this.client, exchange);
		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public double calculateExposure(int branchId) // The bottom of this doens't look right. 
			throws InexistentBranchException {
		if (branchId < 0 ){
			throw new InexistentBranchException(branchId);

		}
		ContentExchange exchange = new ContentExchange();
		// TODO the server adress is not fix, it has to look up
		String urlString = serverAddress + "/" + BankMessageTag.CALCULATE;
		TransferObject to = new TransferObject(branchId);
		String toXMLstring = BankUtility
				.serializeObjectToXMLString(to);
		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(toXMLstring);
		exchange.setRequestContent(requestContent);
		List<Double> exposure = null;
		
		try {
			exposure = (List<Double>) BankUtility.SendAndRecv(this.client, exchange);
		} catch (InexistentAccountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exposure.get(0);
	}

}
