var CleanBraintreeAjaxCall = function () {
    var formSelector;
    var form;
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

    var handle = function(formSelectorX,ajaxUrl,method_type){
        formSelector=formSelectorX;
        form = document.querySelector(formSelector);
        ajax_path = ajaxUrl;
        method = method_type;
        finalizeAction();
    }
    return {
        init: function (formSelector,ajaxUrl,method_type) {
            handle(formSelector,ajaxUrl,method_type);
        }
    }
}();