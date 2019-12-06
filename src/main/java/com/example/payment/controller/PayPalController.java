package com.example.payment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
    private final PaypalService payPalClient;
    @Autowired
    PayPalController(PaypalService payPalClient){
        this.payPalClient = payPalClient;
    }

    
    @PostMapping("/make/payment")
    public Map<String, Object> makePayment(@RequestParam("sum") String sum){
        return payPalClient.createPayment(sum);
    }

       @PostMapping("/complete/payment")
    public Map<String, Object> completePayment(HttpServletRequest request,@RequestParam("token") String token, @RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId){
       System.out.println("in complement");
       System.out.println(token);
    	   return payPalClient.completePayment(request);
    }

}
