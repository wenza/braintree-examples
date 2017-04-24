package com.worstentrepreneur.test;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.*;
import com.worstentrepreneur.BraintreeConfig;
import com.worstentrepreneur.test.dto.AddCreditcardDTO;
import com.worstentrepreneur.test.dto.CreateCustomerDTO;
import com.worstentrepreneur.test.dto.CreditcardDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Path("braintree-customer")
public class CustomerApi {
	/*

	1) Create customer with credit card 1
	2) Add credit card 2 and set as default
	3) Remove credit card 1
	4) Add credit card 3 and set as default
	5) Set default credit card 2
	6) Pay with credit card 2
	7) Remove customer

	API:
	/braintree-customer/												POST:CREATE/VIA BRAINTREE AJAX
	/braintree-customer/S0M3H45H/credit-card/							POST:CREATE/VIA BRAINTREE AJAX
	/braintree-customer/S0M3H45H/credit-card/							GET:READ/ VIA AJAX
	/braintree-customer/S0M3H45H/credit-card/S0M3H45H2/					DELETE:DELETE/ VIA AJAX
	/braintree-customer/S0M3H45H/credit-card/S0M3H45H2/					PUT:UPDATE/ VIA AJAX(only making default)
	/braintree-customer/S0M3H45H/credit-card/S0M3H45H2/transaction/		POST:CREATE/ VIA AJAX

	 */
	private Transaction.Status[] TRANSACTION_SUCCESS_STATUSES = new Transaction.Status[] {
			Transaction.Status.AUTHORIZED,
			Transaction.Status.AUTHORIZING,
			Transaction.Status.SETTLED,
			Transaction.Status.SETTLEMENT_CONFIRMED,
			Transaction.Status.SETTLEMENT_PENDING,
			Transaction.Status.SETTLING,
			Transaction.Status.SUBMITTED_FOR_SETTLEMENT
	};

	private static String lastCustomerBraintreeId="65065450";

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object createCustomerWithDefaultCreditCard(CreateCustomerDTO data) {
		System.out.println(data.nonce);
		CustomerRequest customerRequest = new CustomerRequest()
				.phone(data.phone.startsWith("+")?data.phone:"+420"+data.phone)
				.email(data.email)
				.creditCard()
					.cardholderName(data.name)
					.paymentMethodNonce(data.nonce)
					.options()
						.failOnDuplicatePaymentMethod(false)
						.verifyCard(true)
						.makeDefault(true)
						.done()
					.done();
		Result<Customer> result = BraintreeConfig.createCustomer(customerRequest);
		if(result.isSuccess()){
			System.out.println("Customer Braintree ID: "+result.getTarget().getId());
			return result.getTarget().getId();
		}
		return processFailedResult(result);
	}

	@POST
	@Path("{braintree-customer-id}/credit-card")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addCreditCardToCustomer(@PathParam("braintree-customer-id")String braintreeCustomerId, AddCreditcardDTO data) {
		//String braintreeCustomerId = customerHashId;//TODO:

		PaymentMethodRequest requestPaymentMethod = new PaymentMethodRequest()
				.customerId(braintreeCustomerId)
				.cardholderName("Karel Got")
				.paymentMethodNonce(data.nonce)
				.options()
					.failOnDuplicatePaymentMethod(false)
					.makeDefault(true)
					.verifyCard(true)
					.done();

		Result<? extends PaymentMethod> result = BraintreeConfig.createPaymentMethod(requestPaymentMethod);
		if(result.isSuccess()){
			System.out.println("Credit card with token: "+result.getTarget().getToken()+" added to Customer with Braintree ID: "+result.getTarget().getCustomerId()+" default: "+result.getTarget().isDefault());
			return "{\"token\":\""+result.getTarget().getToken()+"\"}";
		}
		return processFailedResult(result);
	}
	@GET
	@Path("{braintree-customer-id}/credit-card/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CreditcardDTO> getCreditCards(@PathParam("braintree-customer-id")String braintreeCustomerId) {
		Customer customer = BraintreeConfig.getCustomer(braintreeCustomerId);
		String response = "";
		List<CreditcardDTO> ccDTOs = new ArrayList<CreditcardDTO>();
		for(CreditCard cc : customer.getCreditCards()){
			CreditcardDTO ccDTO = new CreditcardDTO(cc.isExpired(),cc.getCreatedAt(),cc.getBin(),
					cc.getLast4(),cc.getCardholderName(),cc.getCardType(),cc.getExpirationMonth(),
					cc.getExpirationYear(),cc.getIssuingBank(),cc.getToken(),cc.isDefault());
			ccDTOs.add(ccDTO);

			response+="name: "+cc.getCardholderName()+" masked:"+cc.getMaskedNumber()+" default:"+cc.isDefault()+" exp_date:"+cc.getExpirationDate()+"\n";
		}
		System.out.println(response);
		return ccDTOs;

	}
	@DELETE
	@Path("{braintree-customer-id}/credit-card/{braintree-payment-token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String removeCreditCard(@PathParam("braintree-payment-token")String braintreePaymentToken) {

		try {
			Result<? extends PaymentMethod> result = BraintreeConfig.getPaymentMethodGateway().delete(braintreePaymentToken);
			if (result.isSuccess()) {
				System.out.println("Credit card with token " + braintreePaymentToken + " removed .");
				return "{}";
			}
			return processFailedResult(result);
		}catch (com.braintreegateway.exceptions.NotFoundException e){
			return "[{\"error\":\"NotFound\"}]";
		}

	}
	@PUT
	@Path("{braintree-customer-id}/credit-card/{braintree-payment-token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String makeCreditCardDefault(@PathParam("braintree-payment-token")String braintreePaymentToken) {

		PaymentMethodRequest updateRequest = new PaymentMethodRequest()
				.options()
					.makeDefault(true)
					.done();

		Result<? extends PaymentMethod> result = BraintreeConfig.getPaymentMethodGateway().update(braintreePaymentToken,updateRequest);//gateway.paymentMethod().update("the_token", updateRequest);
		if(result.isSuccess()){
			System.out.println("Payment method "+result.getTarget().getToken()+" updated to default:"+result.getTarget().isDefault());
			return null;
		}
		return processFailedResult(result);
	}
	@POST
	@Path("{braintree-customer-id}/credit-card/{braintree-payment-token}/transaction/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createPaymentTransaction(
			@Context HttpServletRequest request,
			@PathParam("braintree-payment-token")String braintreeCreditCardToken,
			@PathParam("braintree-customer-id")String braintreeCustomerId){

		System.out.println("CALLED");

		String transactionName = "Order item names";
		BigDecimal amount = new BigDecimal("1.00");

		Random r = new Random();

		TransactionRequest paymentReq = new TransactionRequest()
				//.orderId(r.nextLong()+"")
				.customerId(braintreeCustomerId)
				.paymentMethodToken(braintreeCreditCardToken)
				.amount(amount)
				//TODO:.customField("name",transactionName)
				.options()
					.submitForSettlement(true)
					.done();

		System.out.println("CREATED");
		Result<Transaction> result = BraintreeConfig.getPayment(paymentReq);
		System.out.println("RESULTED");
		if(result.isSuccess()){
			System.out.println("SUCEED");
			try {
				Transaction transaction = BraintreeConfig.getTransaction(result.getTarget().getId());
				CreditCard creditCard = transaction.getCreditCard();
				Customer customer = transaction.getCustomer();

				System.out.println("isSuccess: "+ Arrays.asList(TRANSACTION_SUCCESS_STATUSES).contains(transaction.getStatus()));
				System.out.println("transaction: "+ transaction.getId());
				System.out.println(transaction.getCreditCard().getCardholderName());
				System.out.println("creditCard: "+ creditCard.getMaskedNumber());
				System.out.println("customer: "+ customer.getEmail());

				PaymentMethodRequest updateRequest = new PaymentMethodRequest()
						.options()
						.makeDefault(true)
						.done();

				Result<? extends PaymentMethod> result2 = BraintreeConfig.getPaymentMethodGateway().update(creditCard.getToken(),updateRequest);//gateway.paymentMethod().update("the_token", updateRequest);

			} catch (Exception e) {
				System.err.println("Exception: " + e);
			}

			return null;
		}
		System.out.println("FAILED result:"+result.isSuccess());
		return processFailedResult(result);
	}

	public <T> String processFailedResult(Result<T> result){//Class<Result<T>> r,Result<T> result){
		if(!result.isSuccess()){
			System.err.println("Message:"+result.getMessage());

			//TODO:Message:Gateway Rejected: duplicate
			//TODO:https://articles.braintreepayments.com/control-panel/transactions/duplicate-checking

			String errorString = "";
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
				errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
			}
			System.out.println(errorString);
			for (ValidationError error : result.getErrors().getAllValidationErrors()) {
				System.err.println("Simple Error: " + error.getCode() + ": " + error.getMessage());
			}
			return errorString;
		}
		return null;
	}

}
