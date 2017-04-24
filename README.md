#Braintree examples
This repository is made to provide straight forward examples of PayPal Braintree payment gateway more straight forward for JAVA developers with simple example API.<br>

#HTML files
1_create_customer.html - Provides a way of creating customer with credit card, phone, email, name<br>
2_add_creditcard.html - Adds credit card to customer with customer-braintree-id hardcoded in bottom of file as "braintree_customer_id"<br>
3_list_creditcards.html - List credit cards of customer with customer-braintree-id hardcoded in bottom of file as "braintree_customer_id"<br>
4_remove_creditcard.html - remove credit card of customer with customer-braintree-id and braintree-payment-method-token hardcoded in bottom of file<br>
5_make_default_creditcard.html - makes credit card default - requires customer with customer-braintree-id and braintree-payment-method-token hardcoded in bottom of file<br>
6_create_transaction.html - creates transaction for customer and credit card  with customer-braintree-id and braintree-payment-method-token hardcoded in bottom of file<br>

#Braintree tests REST API
/braintree-customer/												                                POST:CREATE/VIA BRAINTREE AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/							                POST:CREATE/VIA BRAINTREE AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/							                GET:READ/ VIA AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/{payment-braintree-token}/					DELETE:DELETE/ VIA AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/{payment-braintree-token}/					PUT:UPDATE/ VIA AJAX(only making default)<br>
/braintree-customer/{customer-braintree-id}/credit-card/{payment-braintree-token}/transaction/		POST:CREATE/ VIA AJAX<br>

#How to Run
Deploy this app in Wildfly 10.0 in which the app was tested<br>
<br>
go to localhost:8080/{name_of_file}