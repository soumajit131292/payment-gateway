package com.example.payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.service.PaypalService;
@RestController
@RequestMapping("/paypal")
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "token" })
public class PayPalController {
	
	@Autowired
    private PaypalService payPalClient;

    
    @PostMapping("/make/payment")
    public Map<String, Object> makePayment(@RequestParam("sum") String sum){
    	System.out.println("in paypal");
        return payPalClient.createPayment(sum);
    }

       @PostMapping("/complete/payment")
    public Map<String, Object> completePayment(@RequestParam("token") String token, @RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId){
       System.out.println("in complement");
       System.out.println(token);
    	  return payPalClient.completePayment(token,paymentId,payerId);
    	 
    }

}
