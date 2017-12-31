window.onload = function () {
    $("#passwordReg").change(function () {
        validatePassword();
    });
    $("#passwordRepeatedReg").keyup(function () {
        validatePassword();
    });
};



function login() {

    var url = "Login";
    var mensaje = "Error desconocido";
    var username = $("#usernameLog").val();
    var password = $("#passwordLog").val();
    var remember = $("#rememberLog").prop('checked');
    var hash = CryptoJS.SHA1(password + "lunar" + username);
    var pwdCrypt = CryptoJS.enc.Hex.stringify(hash);
    alert(pwdCrypt);
    $.ajax({
        method: "POST",
        url: url,
        data: {username: username, password: pwdCrypt, remember: remember},
        success: function (u) {
            if (u["mess"] === ("El usuario no existe, intenta registrarte primero.")) {
                alert(u["mess"]);
            }
            location.reload();
        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                alert(mensaje);
            else
                alert(e["responseJSON"]["error"]);
        }
    });

    return false;

}


function register() {

    var url = "Register";
    var mensaje = "Error desconocido";
    var name = $("#nameReg").val();
    var username = $("#usernameReg").val();
    var password = $("#passwordReg").val();

    var email = $("#emailReg").val();

    //  alert(name+" "+username+" "+password+" "+password2);

    if (validatePassword()) {
        alert("OK");
    }


    return false;
//    if (validar(name) || validar(username) || validar(password)) {
//        alert("Ningún campo puede estar vacio");
//    } else {
//
//        $.ajax({
//            method: "POST",
//            url: url,
//            data: {namePost: name, usernamePost: username, passwordPost: password},
//            success: function (u) {
//                alert(u["mess"]);
//                location.reload();
//            },
//            error: function (e) {
//                if (e["responseJSON"] === undefined)
//                    alert(mensaje);
//                else
//                    alert(e["responseJSON"]["error"]);
//            }
//        });
//    }
}

function validatePassword() {
    if ($("#passwordReg").val() !== $("#passwordRepeatedReg").val()) {
        $("#passwordRepeatedReg").each(function () {
            this.setCustomValidity("errorMessage");
        });
        return false;
    } else {
        $("#passwordRepeatedReg").each(function () {
            this.setCustomValidity("");
        });
        return true;
    }
}

//Funciones de cambiar login / register
$(function () {

    $('#login-form-link').click(function (e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

    $('#register-form-link').click(function (e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

});
