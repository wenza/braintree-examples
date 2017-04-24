package com.worstentrepreneur.braintreetest;

import com.braintreegateway.*;

public class BraintreeConfig {
	/*
	Step 1: https://sandbox.braintreegateway.com/merchants
	Step 2: Account->My User->API Keys, Tokenization Keys, Encryption Keys->View Authorizations
	 */
	private static BraintreeGateway gateway = new BraintreeGateway(
			Environment.SANDBOX,
			"merchantId",
			"publicKey",
			"privateKey"
	);
	public static String generateClientToken(){
		return gateway.clientToken().generate();
	}
	public static Result<Transaction> getPayment(TransactionRequest paymentReq){
		return gateway.transaction().sale(paymentReq);
	}
	public static Transaction getTransaction(String txId){
		return gateway.transaction().find(txId);
	}
	public static Result<Customer> createCustomer(CustomerRequest customer){
		return gateway.customer().create(customer);
	}
	public static Result<? extends PaymentMethod> createPaymentMethod(PaymentMethodRequest paymentMethodRequest){
		return gateway.paymentMethod().create(paymentMethodRequest);
	}
	public static Customer getCustomer(String braintreeId){
		return gateway.customer().find(braintreeId);
	}
	public static PaymentMethodGateway getPaymentMethodGateway(){
		return gateway.paymentMethod();
	}
}
