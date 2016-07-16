$(function () {
    $("input.zip_code").inputmask({
        "mask": "99999-999",
        "clearIncomplete": true,
        greedy: false,
        showMaskOnHover: false
    });
    $("input.phone").inputmask({
        "mask": "(99) 99999999?9[9]",
        "clearIncomplete": false,
        greedy: false,
        showMaskOnHover: false
    });
//    $("input.phone").inputmask("(99) 9999-9999?9").ready(function (event) {
//        var target, phone, element;
//        target = (event.currentTarget) ? event.currentTarget : event.srcElement;
//        phone = target.value.replace(/\D/g, '');
//        element = $(target);
//        element.unmask();
//        if (phone.length > 10) {
//            element.mask("(99) 99999-999?9");
//        } else {
//            element.mask("(99) 9999-9999?9");
//        }
//    });
    $("input.cpf").inputmask({
        "mask": "999.999.999-99",
        "clearIncomplete": true,
        greedy: false,
        showMaskOnHover: false
    });
    $("input.data").inputmask({
        "mask": "99/99/9999",
        "clearIncomplete": true,
        greedy: false,
        showMaskOnHover: false
    });

    $("input.hora").inputmask({
        "mask": "99:99",
        "clearIncomplete": true,
        greedy: false,
        showMaskOnHover: false
    });

    $("input.valor").inputmask({
        "mask": "99,99",
        "clearIncomplete": true,
        greedy: false,
        showMaskOnHover: false
    });

//   
//     $("input.metros").inputmask({
//        "mask": "9!9.99",
//        "clearIncomplete": false,
//        greedy: false,
//        showMaskOnHover: false
//    });


});