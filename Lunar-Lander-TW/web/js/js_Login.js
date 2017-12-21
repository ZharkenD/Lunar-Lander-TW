//window.onload = function () {
////    $("#login-submit").click(function(){
////        console.log("hi");
////    });
//};

function prueba(){
    console.log("OK");
    var username = $("#usernameLog").val();
    alert(username);
    return false;
}

function login() {

    var url = "Login";
    var mensaje = "Error desconocido";
    var username = $("#usernameLog").val();
    var password = $("#passwordLog").val();
    alert(username);
/*
    if (validar(username) || validar(password)) {
        alert("Ningún campo puede estar vacio");
    } else {

        $.ajax({
            method: "POST",
            url: url,
            data: {usernamePost: username, passwordPost: password},
            success: function (u) {
                if (u["mess"] === ("El usuario no existe, intenta registrarte primero.")) {
                    alert(u["mess"]);
                    
                };
                location.reload();
            },
            error: function (e) {
                if (e["responseJSON"] === undefined)
                    alert(mensaje);
                else
                    alert(e["responseJSON"]["error"]);
            }
        });
    }
    ;*/
}
;


function registro() {

    var url = "Register";
    var mensaje = "Error desconocido";
    var name = $("#name").val();
    var username = $("#username").val();
    var password = $("#password").val();
    if (validar(name) || validar(username) || validar(password)) {
        alert("Ningún campo puede estar vacio");
    } else {

        $.ajax({
            method: "POST",
            url: url,
            data: {namePost: name, usernamePost: username, passwordPost: password},
            success: function (u) {
                alert(u["mess"]);
                location.reload();
            },
            error: function (e) {
                if (e["responseJSON"] === undefined)
                    alert(mensaje);
                else
                    alert(e["responseJSON"]["error"]);
            }
        });
    }
}

function validar(texto) {
    if (texto.length === 0 || texto === null) {
        return true;
    } else {
        return false;
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
