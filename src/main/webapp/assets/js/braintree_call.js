var BraintreeAjaxCall = function () {
    var formSelector;
    var form;
    var submit;
    var ajax_path;
    var method;
    var getFormInJSONData = function(){
        var data = {};
        $(formSelector).serializeArray().map(function(x){data[x.name] = x.value;});
        //JSON.stringify( formik.serialize() )
        console.log('pooozoor');
        console.log(JSON.stringify(data));
        console.log(data);
        return JSON.stringify(data);
    }
    var finalizeAction = function(){
        $.ajax({
            url: ajax_path,
            type: method,
            data: getFormInJSONData(),
            dataType: "application/json",
            contentType: "application/json; charset=utf-8",
            success: function (checkout_response) {
                console.log('success');
                console.log(checkout_response);
            },
            error: function(xhr, status, error) {
                console.log(xhr.responseText);
                var err = eval("(" + xhr.responseText + ")");
                console.log(err.Message);
            }
        });
    }

    var initBraintreeForm = function(hostedFieldsInstance){
        // Use the Hosted Fields instance here to tokenize a card
        console.log('selector:'+submit);
        console.log(submit.value);
        submit.removeAttribute('disabled');
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            console.log('submitted');

            hostedFieldsInstance.tokenize(function (tokenizeErr, payload) {
                if (tokenizeErr) {
                    // Handle error in Hosted Fields tokenization
                    console.log(tokenizeErr);
                    return;
                }
                document.querySelector('input[name="payment_method_nonce"]').value = payload.nonce;
                console.log('nonce_value='+(document.querySelector('input[name="payment_method_nonce"]').value));
                finalizeAction();
            });
        }, false);
    }

    var initHostedFields = function(clientInstance){
        braintree.hostedFields.create(getHostedFields(clientInstance), function (hostedFieldsErr, hostedFieldsInstance) {
            if (hostedFieldsErr) {
                console.log(hostedFieldsErr);
                return;
            }
            initBraintreeForm(hostedFieldsInstance);

        });
    }

    var initBraintree = function(client_token){
        console.log(client_token);
        braintree.client.create({
            authorization: client_token
        }, function (clientErr, clientInstance) {
            if (clientErr) {
                console.log(clientErr);
                return;
            }
            initHostedFields(clientInstance);

        });
    }
    var initBraintreeToken = function(){
        $.ajax({
            url: '/client_token',
            type: 'GET',
            dataType: "text",
            success: function (client_token) {
                initBraintree(client_token);
            },
            error: function() {
            }
        });
    }
    var handle = function(formSelectorX,submitBtnSelector,ajaxUrl,method_type){
        formSelector=formSelectorX;
        form = document.querySelector(formSelector);
        submit = document.querySelector(submitBtnSelector);
        ajax_path = ajaxUrl;
        method = method_type;
        initBraintreeToken();
    }
    return {
        init: function (formSelector,submitBtnSelector,ajaxUrl,method_type) {
            handle(formSelector,submitBtnSelector,ajaxUrl,method_type);
        }
    }
}();