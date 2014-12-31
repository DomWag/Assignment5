package com.acertainbank.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.acertainbank.business.CertainBank;
import com.acertainbank.utils.BankMessageTag;
import com.acertainbank.utils.BankResponse;
import com.acertainbank.utils.BankUtility;
import com.acertainbank.utils.InexistentAccountException;
import com.acertainbank.utils.InexistentBranchException;
import com.acertainbank.utils.NegativeAmountException;
import com.acertainbank.utils.TransferObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AccountManagerPartionHTTPMessageHandler extends AbstractHandler{
	private CertainBank myBank = null;
	public AccountManagerPartionHTTPMessageHandler(CertainBank bank) {
		myBank = bank;
	}
	// TODO Constructor

	@SuppressWarnings("unchecked")
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		BankMessageTag messageTag;
		String numBooksString = null;
		int numBooks = -1;
		String requestURI;
		BankResponse bankResponse = null;

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
				bankResponse = new BankResponse();

				String xml = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject to = (TransferObject) BankUtility.deserializeXMLStringToObject(xml);
				try {
					myBank.credit(to.getBranchID(), to.getAccountIdOrg(), to.getAmount());
				}  catch (NegativeAmountException nAe){
					bankResponse.setException(nAe);
				} catch (InexistentAccountException iAe){
					bankResponse.setException(iAe);
				} catch (InexistentBranchException iBe){
					bankResponse.setException(iBe);
				}
				
				String xmlString = BankUtility.serializeObjectToXMLString(bankResponse);
				response.getWriter().println(xmlString);
				break;
			case DEBIT:
				bankResponse = new BankResponse();

				String xml2 = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject to2 = (TransferObject) BankUtility.deserializeXMLStringToObject(xml2);
				try {
					myBank.debit(to2.getBranchID(), to2.getAccountIdOrg(), to2.getAmount());
				} catch (NegativeAmountException nAe){
					bankResponse.setException(nAe);
				} catch (InexistentAccountException iAe){
					bankResponse.setException(iAe);
				} catch (InexistentBranchException iBe){
					bankResponse.setException(iBe);
				}
				
				String xmlString2 = BankUtility.serializeObjectToXMLString(bankResponse);
				response.getWriter().println(xmlString2);
				break;
				
			case TRANSFER:
				bankResponse = new BankResponse();

				String xmlT = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject toT = (TransferObject) BankUtility.deserializeXMLStringToObject(xmlT);
				try {
					myBank.transfer(toT.getBranchID(), toT.getAccountIdOrg(), toT.getAccountIdDest(), toT.getAmount());
				} catch (NegativeAmountException nAe){
					bankResponse.setException(nAe);
				} catch (InexistentAccountException iAe){
					bankResponse.setException(iAe);
				} catch (InexistentBranchException iBe){
					bankResponse.setException(iBe);
				}
				
				String xmlString3 = BankUtility.serializeObjectToXMLString(bankResponse);
				response.getWriter().println(xmlString3);
				break;
				
			case CALCULATE:
				bankResponse = new BankResponse();
				String xmlCa = BankUtility.extractPOSTDataFromRequest(request);
				TransferObject toCA = (TransferObject) BankUtility.deserializeXMLStringToObject(xmlCa);
				try {
					bankResponse.setCalculation(myBank.calculateExposure(toCA.getBranchID()));
				} catch (InexistentBranchException iBe){
					bankResponse.setException(iBe);
				}
				
				String xmlString4 = BankUtility.serializeObjectToXMLString(bankResponse);
				response.getWriter().println(xmlString4);
				break;
			default:
				System.out.println("Unhandled message tag");
				break;
			}
		}
			baseRequest.setHandled(true);
	}
}
