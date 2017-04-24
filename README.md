# Braintree examples
This repository is made to provide straight forward examples of PayPal Braintree payment gateway more straight forward for JAVA developers with simple example API.<br>

# HTML files
1_create_customer.html : <br>Provides a way of creating customer with credit card, phone, email, name<br>
2_add_creditcard.html : <br>Adds credit card to customer with customer-braintree-id hardcoded in bottom of file as "braintree_customer_id"<br>
3_list_creditcards.html : <br>List credit cards of customer with customer-braintree-id hardcoded in bottom of file as "braintree_customer_id"<br>
4_remove_creditcard.html : <br>Removes credit card of customer with customer-braintree-id and braintree-payment-method-token hardcoded in bottom of file<br>
5_make_default_creditcard.html : <br>Makes credit card default - requires customer with customer-braintree-id and braintree-payment-method-token hardcoded in bottom of file<br>
6_create_transaction.html : <br>Creates transaction for customer and credit card  with customer-braintree-id and braintree-payment-method-token hardcoded in bottom of file<br>

# Braintree tests REST API
POST:CREATE/VIA BRAINTREE AJAX<br>
/braintree-customer/<br>
POST:CREATE/VIA BRAINTREE AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/<br>
GET:READ/ VIA AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/<br>
DELETE:DELETE/ VIA AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/{payment-braintree-token}/<br>
PUT:UPDATE/ VIA AJAX(only making default)<br>
/braintree-customer/{customer-braintree-id}/credit-card/{payment-braintree-token}/<br>
POST:CREATE/ VIA AJAX<br>
/braintree-customer/{customer-braintree-id}/credit-card/{payment-braintree-token}/transaction/<br>

# How to Run
Deploy this app in Wildfly 10.0 in which the app was tested<br>
<br>
go to localhost:8080/{name_of_file}
