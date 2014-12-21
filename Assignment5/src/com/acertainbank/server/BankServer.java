package com.acertainbank.server;

import com.acertainbank.utils.BankClientConstants;

public class BankServer {
	
	public static void main(String[] args){
	int listen_on_port = 8081;
	// TODO handler umwandeln
	AccountManagerPartionHTTPMessageHandler handler = new AccountManagerPartionHTTPMessageHandler();
	String server_port_string = System.getProperty(BankClientConstants.PROPERTY_KEY_SERVER_PORT);
	if(server_port_string != null) {
		try {
			listen_on_port = Integer.parseInt(server_port_string);
		} catch(NumberFormatException ex) {
			System.err.println(ex);
		}
	}
	if (BankServerUtility.createServer(listen_on_port, handler)) {
		;
	}
}
}

