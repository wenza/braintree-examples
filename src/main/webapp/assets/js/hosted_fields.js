function getHostedFields(clientInstance){
    var hosted_fields_options = {
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
    return hosted_fields_options;
}