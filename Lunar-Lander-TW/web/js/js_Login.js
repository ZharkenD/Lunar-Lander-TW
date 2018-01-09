/**
 * Window onload method
 * @returns {undefined}
 */
window.onload = function () {
    $("#passwordReg").change(function () {
        validatePassword();
    });
    $("#passwordRepeatedReg").keyup(function () {
        validatePassword();
    });
    $("#okModal").click(function () {
        $("#alertModal").modal('hide');
    });
};

/**
 * Login when you press onsubmit on login.
 * @returns {Boolean}
 */
function login() {

    var url = "Login";
    var mensaje = "Unknown error";

    var username = $("#usernameLog").val();
    var password = $("#passwordLog").val();
    var remember = $("#rememberLog").prop('checked');
    var hash = CryptoJS.SHA1(password + "lunar" + username);
    var pwdCrypt = CryptoJS.enc.Hex.stringify(hash);

    $.ajax({
        method: "POST",
        url: url,
        data: {username: username, password: pwdCrypt, remember: remember},
        success: function (u) {
            location.reload();
        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                showAlert(mensaje);
            else
                showAlert(e["responseJSON"]["error"]);
        }
    });

    return false;

}

/**
 * Register when you press onsubmit on register.
 * @returns {Boolean}
 */
function register() {

    var url = "Register";
    var mensaje = "Unknown error";

    var name = $("#nameReg").val();
    var username = $("#usernameReg").val();
    var password = $("#passwordReg").val();
    var email = $("#emailReg").val();
    var hash = CryptoJS.SHA1(password + "lunar" + username);
    var pwdCrypt = CryptoJS.enc.Hex.stringify(hash);

    if (validatePassword()) {
        $.ajax({
            method: "POST",
            url: url,
            data: {name: name, username: username, password: pwdCrypt, email: email},
            success: function (u) {
                if (u["mess"] === "The username is already in use.") {
                    $("#usernameReg").focus();
                }
                if (u["mess"] === "The email is already in use.") {
                    $("#emailReg").focus();
                }
                if (u["mess"] === "User created correctly.") {
                    $("#login-form").delay(100).fadeIn(100);
                    $("#register-form").fadeOut(100);
                    $('#register-form-link').removeClass('active');
                    $('#login-form-link').addClass('active');
                    cleanLogin();
                    cleanRegister();
                }
                showAlert(u["mess"]);
            },
            error: function (e) {
                if (e["responseJSON"] === undefined)
                    showAlert(mensaje);
                else
                    showAlert(e["responseJSON"]["error"]);
            }
        });
    }

    return false;
}

/**
 * Show a personal alert with css
 * @param {type} text
 * @returns {undefined}
 */
function showAlert(text) {
    $("#pModal").text(text);
    $("#alertModal").modal({backdrop: "static", keyboard: "false"});
}

/**
 * Check that the two password fields match in register.
 * @returns {Boolean}
 */
function validatePassword() {
    if ($("#passwordReg").val() !== $("#passwordRepeatedReg").val()) {
        $("#passwordRepeatedReg").each(function () {
            this.setCustomValidity("Passowords don't match");
        });
        return false;
    } else {
        $("#passwordRepeatedReg").each(function () {
            this.setCustomValidity("");
        });
        return true;
    }
}

/**
 * Clean all the fields in the login form
 * @returns {undefined}
 */
function cleanLogin() {
    $("#usernameLog").val("");
    $("#passwordLog").val("");
    $("#rememberLog").prop('checked', false);
}

/**
 * Clean all the fields in the register form
 * @returns {undefined}
 */
function cleanRegister() {
    $("#nameReg").val("");
    $("#usernameReg").val("");
    $("#passwordReg").val("");
    $("#passwordRepeatedReg").val("");
    $("#emailReg").val("");
}

/**
 * Change between tab Login and Sing in.
 * @returns {undefined}
 */
$(function () {

    $('#login-form-link').click(function (e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
        cleanLogin();
        cleanRegister();

    });

    $('#register-form-link').click(function (e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
        cleanLogin();
        cleanRegister();


    });

});
