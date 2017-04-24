//CHECK OUT:
//http://www.ilovephp.net/php/braintree-hosted-fields-integration-with-custom-stylesheetcss-and-validation/

function finalizeCheckout(){
    var serializedData = encodeURI($('#checkout-form').serialize());
    console.log('cl='+serializedData);
    $.ajax({
        url: '/customer_create',
        type: 'GET',
        data: serializedData,
        dataType: "text",
        success: function (checkout_response) {
            console.log(checkout_response);
            //initBraintree(client_token);
        },
        error: function() {
        }
    });
}

function initBraintreeForm(hostedFieldsInstance){
    // Use the Hosted Fields instance here to tokenize a card
    var form = document.querySelector('#checkout-form');
    var submit = document.querySelector('button[type="submit"]');
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

            // Put `payload.nonce` into the `payment_method_nonce` input, and then
            // submit the form. Alternatively, you could send the nonce to your server
            // with AJAX.
            console.log('tokenized');
            document.querySelector('input[name="payment_method_nonce"]').value = payload.nonce;
            //form.submit();
            console.log('nonce_value='+(document.querySelector('input[name="payment_method_nonce"]').value));
            finalizeCheckout();
        });
    }, false);
}

function initHostedFields(clientInstance){
    var options = {
        client: clientInstance,
        fields: {
            number: {
                selector: '#bt_card_number',
                placeholder: '4111 1111 1111 1111'
            },
            cvv: {
                selector: '#bt_cvv',
                placeholder: '123'
            },
            expirationDate: {
                selector: '#bt_exp_date',
                placeholder: '10/2019'
            }
        },
        styles: {
            'input': {


                //form-control
                'display':' block',
                'width':' 100%',
                'height':' 34px',
                'padding':' 6px 12px',
                'font-size':' 14px',
                'line-height':' 1.42857143',
                'color':' #555',
                'background-color':' #fff',
                'background-image':' none',
                'border':' 1px solid #ccc',
                'border-radius':' 4px',
                '-webkit-box-shadow':' inset 0 1px 1px rgba(0,0,0,.075)',
                'box-shadow':' inset 0 1px 1px rgba(0,0,0,.075)',
                '-webkit-transition':' border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s',
                '-o-transition':' border-color ease-in-out .15s,box-shadow ease-in-out .15s',
                'transition':' border-color ease-in-out .15s,box-shadow ease-in-out .15s',


                //'line-height': '20px',
                //'text-align': 'left',
                //'font-size': '12px'//,
                //'font-size': '1rem'

                //line-input
                'background': '0 0',
                'border':' 0',
                'border-bottom':' 1px solid rgba(43, 41, 41, 0.2)',
                '-webkit-border-radius':' 0',
                '-moz-border-radius':' 0',
                '-ms-border-radius':' 0',
                '-o-border-radius':' 0',
                'color':' #151419',
                'box-shadow':' none',
                'padding-left':' 0',
                'padding-right':' 0',
                'margin-top':' 15px',
                'font-size':' 16px',
                'font-size':' 1rem',
                'line-height':' 20px',
                'text-align':' left',
            },
            'input.invalid': {
                'color': 'red'
            },
            'input.valid': {
                'color': 'green'
            }
        }

    };
    braintree.hostedFields.create(options, function (hostedFieldsErr, hostedFieldsInstance) {
        if (hostedFieldsErr) {
            console.log('field error after');
            console.log(hostedFieldsErr);
            // Handle error in Hosted Fields creation
            //TODO:BTFn.showFormErrors(hostedFieldsErr);
            //TODO:$obj.show().closest('.btn_container').find('.loader_img').hide();
            return;
        }
        console.log('ready');
        //TODO:BTFn.showBtForm();

        initBraintreeForm(hostedFieldsInstance);

    });
}

function initBraintree(client_token){
    console.log(client_token);
    braintree.client.create({
        authorization: client_token
    }, function (clientErr, clientInstance) {
        if (clientErr) {
            // Handle error in client creation
            console.log(clientErr);
            return;
        }
        console.log('inited');
        initHostedFields(clientInstance);

    });
}
function initBraintreeToken(){
    $.ajax({
        url: '/client_token',
        type: 'GET',
        dataType: "text",
        success: function (client_token) {
            console.log('reqdone');
            initBraintree(client_token);
        },
        error: function() {
        }
    });
}

(function() {

    $(document).ready(function() {

        initBraintreeToken();
        $('#modal-1').modal();

    });
})();