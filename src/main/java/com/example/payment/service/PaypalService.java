package com.example.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {
	String clientId = "AeAnj6xj9uyWemXMzOq-6ZcT3zxkakvT4uz7VAl7tLynVtMG2aZ0JJkDq3SMKw93Z380_IFSV2gGQtQj";
	String clientSecret = "EPkzML2k-n02ElDvPKrMkHR7vMw_kf3IL6Fuu6hKlvtDr_7tvjKg7AMXCnYeMoMqJgiUmezUL76f2mZL";



	public Map<String, Object> createPayment(String sum) {
		Map<String, Object> response = new HashMap<String, Object>();
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal(sum);
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		System.out.println("before cancel");
		redirectUrls.setCancelUrl("http://localhost:4200/cancel");
		System.out.println("before confirm");
		redirectUrls.setReturnUrl("http://localhost:4200");
		payment.setRedirectUrls(redirectUrls);
		Payment createdPayment;
		try {
			String redirectUrl = "";
			System.out.println("1st");
			APIContext context = new APIContext(clientId, clientSecret, "sandbox");
			System.out.println("2nd");
			createdPayment = payment.create(context);
			System.out.println(createdPayment);
			System.out.println("3rd");
			if (createdPayment != null) {
				List<Links> links = createdPayment.getLinks();
				for (Links link : links) {
					if (link.getRel().equals("approval_url")) {
						redirectUrl = link.getHref();
						break;
					}
				}
				System.out.println(redirectUrl);
				response.put("status", "success");
				response.put("redirect_url", redirectUrl);
			}
		} catch (PayPalRESTException e) {
			System.out.println("Error happened during payment creation!");
		}
		return response;
	}

	public Map<String, Object> completePayment(HttpServletRequest req) {
		System.out.println("in complete payments");
		Map<String, Object> response = new HashMap<String,Object>();
		Payment payment = new Payment();
		String accessToken=req.getParameter("token");
		
		payment.setId(req.getParameter("paymentId"));
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(req.getParameter("payerId"));
		try {
			APIContext context = new APIContext(clientId, clientSecret, "sandbox");
			Payment createdPayment = payment.execute(context, paymentExecution);
			System.out.println(createdPayment);
			if (createdPayment != null) {
				System.out.println("hi");
				response.put("status", "success");
				response.put("payment", createdPayment.toString());
			}
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());
		}
		System.out.println(response);
		return response;
	}
}