package com.acertainbank.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.acertainbank.utils.BankMessageTag;
import com.acertainbank.utils.BankUtility;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.TransferObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Set;

public class AccountManagerPartionHTTPMessageHandler extends AbstractHandler{
	// TODO Constructor

	@SuppressWarnings("unchecked")
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		BankMessageTag messageTag;
		String numBooksString = null;
		int numBooks = -1;
		String requestURI;
		BookStoreResponse bookStoreResponse = null;

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		requestURI = request.getRequestURI();

		// Need to do request multi-plexing
		if (!BankUtility.isEmpty(requestURI)
				&& requestURI.toLowerCase().startsWith("/stock")) {
			messageTag = BankUtility.convertURItoMessageTag(requestURI
					.substring(6)); // the request is from store
			// manager, more
			// sophisticated security
			// features could be added
			// here
		} else {
			messageTag = BankUtility.convertURItoMessageTag(requestURI);
		}
		// the RequestURI before the switch
		if (messageTag == null) {
			System.out.println("Unknown message tag");
		} else {
			switch (messageTag) {
			case CREDIT:
				String xml = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject to = (TransferObject) BankUtility.deserializeXMLStringToObject(xml);
				try {
					.credit(to.getBranchID(), to.getAccountIdOrg(), to.getAmount());
				} catch (InexistentAccountException iAe){
					.setException(iAe);
				} catch (InexistentBranchException iBe){
					.setException(iBe);
				}
				
			case DEBIT:
				String xml2 = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject to2 = (TransferObject) BankUtility.deserializeXMLStringToObject(xml);
				try {
					.debit(to2.getBranchID(), to2.getAccountIdOrg(), to2.getAmount());
				} catch (InexistentAccountException iAe){
					.setException(iAe);
				} catch (InexistentBranchException iBe){
					.setException(iBe);
				}
				
			case TRANSFER:
				String xmlT = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject toT = (TransferObject) BankUtility.deserializeXMLStringToObject(xml);
				try {
					.transfer(toT.getBranchID(), toT.getAccountIdOrg(), to.getAccountIdDest(), to.getAmount());
				} catch (InexistentAccountException iAe){
					.setException(iAe);
				} catch (InexistentBranchException iBe){
					.setException(iBe);
				}
				
			case CALCULATE:
				String xmlCa = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject toCA = (TransferObject) BankUtility.deserializeXMLStringToObject(xml);
				try {
					.creditCA(toCA.getBranchID());
				} catch (InexistentBranchException iBe){
					.setException(iBe);
				}
			default:
				System.out.println("Unhandled message tag");
				break;
			}
		}
			baseRequest.setHandled(true);


}}
