package com.acertainbank.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.HttpExchange.ContentExchange;

import com.acertainbank.client.BankClientConstants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class BankUtility {

	/**
	 * Convert a request URI to the message tags supported in CertainBank
	 * 
	 * @param requestURI
	 * @return
	 */
	public static BankMessageTag convertURItoMessageTag(String requestURI) {

		try {
			BankMessageTag messageTag = BankMessageTag
					.valueOf(requestURI.substring(1).toUpperCase());
			return messageTag;
		} catch (IllegalArgumentException ex) {
			; // Enum type matching failed so non supported message
		} catch (NullPointerException ex) {
			; // RequestURI was empty
		}
		return null;
	}

	/**
	 * Serializes an object to an xml string
	 * 
	 * @param object
	 * @return
	 */
	public static String serializeObjectToXMLString(Object object) {
		String xmlString;
		XStream xmlStream = new XStream(new StaxDriver());
		xmlString = xmlStream.toXML(object);
		return xmlString;
	}

	/**
	 * De-serializes an xml string to object
	 * 
	 * @param xmlObject
	 * @return
	 */
	public static Object deserializeXMLStringToObject(String xmlObject) {
		Object dataObject = null;
		XStream xmlStream = new XStream(new StaxDriver());
		dataObject = xmlStream.fromXML(xmlObject);
		return dataObject;
	}


	/**
	 * Manages the sending of an exchange through the client, waits for the
	 * response and unpacks the response
	 * 
	 * @param client
	 * @param exchange
	 * @return A List<Book> for a get function, otherwise null
	 * @throws BookStoreException
	 */
	public static double SendAndRecv(HttpClient client,
			ContentExchange exchange) throws InexistentAccountException, InexistentBranchException, BankException {
		int exchangeState;
		try {
			client.send(exchange);
		} catch (IOException ex) {
			throw new BankException(
					BankClientConstants.strERR_CLIENT_REQUEST_SENDING, ex);
		}

		try {
			exchangeState = exchange.waitForDone(); // block until the response
													// is available
		} catch (InterruptedException ex) {
			throw new BankException(
					BankClientConstants.strERR_CLIENT_REQUEST_SENDING, ex);
		}

		if (exchangeState == HttpExchange.STATUS_COMPLETED) {
			try {
				BankResponse bankResponse = (BankResponse) BankUtility
						.deserializeXMLStringToObject(exchange
								.getResponseContent().trim());
				Exception ex = bankResponse.getException();
				if (ex != null) {
					try {
						throw ex;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return bankResponse.getCalculation();
				
			} catch (UnsupportedEncodingException ex) {
				throw new BankException(
						BankClientConstants.strERR_CLIENT_RESPONSE_DECODING,
						ex);
			}
		} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
			throw new BankException(
					BankClientConstants.strERR_CLIENT_REQUEST_EXCEPTION);
		} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
			throw new BankException(
					BankClientConstants.strERR_CLIENT_REQUEST_TIMEOUT);
		} else {
			throw new BankException(
					BankClientConstants.strERR_CLIENT_UNKNOWN);
		}
	}

	/**
	 * Checks if a string is empty or null
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return ((str == null) || str.isEmpty());
	}
	
	
	
	
	/**
	 * Returns the message of the request as a string
	 * 
	 * @param request
	 * @return xml string
	 * @throws IOException
	 */
	public static String extractPOSTDataFromRequest(HttpServletRequest request)
			throws IOException {
		Reader reader = request.getReader();
		int len = request.getContentLength();

		// Request must be read into a char[] first
		char res[] = new char[len];
		reader.read(res);
		reader.close();
		return new String(res);
	}
}
