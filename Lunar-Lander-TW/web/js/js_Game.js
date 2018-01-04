//Globals
var g = 1.622;
var dt = 0.016683;
var timer = null;
var timerFuel = null;
var speedImpact = 5;
var distance = 50;
var yStart = 23;
var landing = yStart + distance;
var isFuel = true;

//Spaceship
var y = yStart;
var v = 0;
var fuelStart = 100;
var actualFuel = fuelStart;
var a = g;

//Panel controllers
var velocidad = null;
var altura = null;
var combustible = null;

//Menu controllers
var isPause = false;//----------------------------------------------------------------y la de abajo
var endGame = false;
var pauseVisible = false;
var instructionsVisible = false;
var optionsVisible = false;//-----------------------------------------------------------------
var configVisible = false;
var rankingVisible = false;
var aboutVisible = false;
var mobilePause = false;

//Configuration
var shipImg = "nave";
var ahoraDif = "facil";
var lugarAterrizaje = "luna";

var indexConfig = 0;
var arrayConfig = [];


$(document).ready(function () {

    velocidad = $("#speed_Panel");
    altura = $("#height_Panel");
    combustible = $("#fuel_Panel");


    //Events movement
    document.ontouchstart = function () {
        motorOn();
    };

    document.ontouchend = function () {
        motorOff();
    };
    //encender/apagar al apretar/soltar una tecla
    $(document).keydown(function keyCode(event) {
        var x = event.keyCode;
        if (x === 32) {
            motorOn();
        }
    });
    $(document).keyup(function keyCode(event) {
        var x = event.keyCode;
        if (x === 32) {
            motorOff();
        }
    });

    document.onkeypress = function keyCode(event) {
        var x = event.keyCode;
        if ((x === 112 || x === 80) && !pauseVisible && !isPause) {
            stop();
            hideAll();
            $("#pausePanel").show();
            pauseVisible = true;
        } else if ((x === 112 || x === 80) && pauseVisible) {
            start();
            $("#pausePanel").hide();
            pauseVisible = false;
        }
    };

    $("#playNav").click(function () {
        if (pauseVisible) {
            start();
            $("#pausePanel").hide();
            pauseVisible = false;

        } else {
            stop();
            hideAll();
            $("#pausePanel").show();
            pauseVisible = true;
        }
    });

    $("#instructionNav").click(function () {
        if (instructionsVisible) {
            start();
            $("#instrctionPanel").hide();
            instructionsVisible = false;

        } else {
            stop();
            hideAll();
            $("#instructionPanel").show();
            instructionsVisible = true;
        }
    });

    $("#optionNav").click(function () {
        if (optionsVisible) {
            start();
            $("#optionPanel").hide();
            optionsVisible = false;

        } else {
            stop();
            hideAll();
            $("#optionPanel").show();
            optionsVisible = true;
        }
    });

    $("#rankingNav").click(function () {
        if (rankingVisible) {
            start();
            $("#rankingPanel").hide();
            rankingVisible = false;

        } else {
            stop();
            hideAll();
            $("#rankingPanel").show();
            rankingVisible = true;
        }
    });

    $("#aboutNav").click(function () {
        if (aboutVisible) {
            start();
            $("#aboutPanel").hide();
            aboutVisible = false;

        } else {
            stop();
            hideAll();
            $("#aboutPanel").show();
            aboutVisible = true;
        }
    });

    $("#mobilePause").click(function () {
        if (mobilePause) {
            start();
            mobilePause = false;

        } else {
            stop();
            hideAll();
            mobilePause = true;
        }
    });
    
    $(".btn-return").click(function(){
        hideAll();
        start();
    });

    //Empezar a mover la nave justo después de cargar la página ---------------------------------------------------
    start();
});

//Definición de funciones
function start() {
    isPause = false;
    timer = setInterval(function () {
        moverNave();
    }, dt * 1000);
    $("#playNav").text("");
    $("#playNav").append("<span class=\"glyphicon glyphicon-pause\"></span> Pause");

}

function stop() {
    isPause = true;
    clearInterval(timer);
    motorOff();

    if (endGame) {
        $("#playNav").text("");
        $("#playNav").append("<span class=\"glyphicon glyphicon-play\"></span> Play");
    } else {
        $("#playNav").text("");
        $("#playNav").append("<span class=\"glyphicon glyphicon-play\"></span> Resume");
    }

}

function moverNave() {
    v += a * dt;
    velocidad.text(v.toFixed(2));
    if (v < speedImpact) {
        velocidad.css("color", "lime");
    } else if (v < (speedImpact * 1.5)) {
        velocidad.css("color", "orange");
    } else {
        velocidad.css("color", "red");
    }

    y += v * dt;
    altura.text((landing - y).toFixed(2));

    if (y < (yStart - 30)) {
        endGame = true;
        isPause = true;
        //mensajeSobreAltura();----------------------------------------------------------
        motorOff();
        stop();
    } else if (y < landing) {
        $("#nave").css("top", y + "%");
    } else {
        altura.text(0);
        hayPausa = true;
        //comprobarAterrizaje();----------------------------------------------------------------------------
        motorOff();
        stop();
    }

}
function motorOn() {
    if (actualFuel > 0 && !isPause && !endGame) {
        a = -g;
        if (timerFuel === null)
            timerFuel = setInterval(function () {
                actualizarFuel();
            }, 10);
        document.getElementById("naveimg").src = "img/" + shipImg + "p.png";
    }

}
function motorOff() {
    if (!endGame) {
        a = g;
        clearInterval(timerFuel);
        timerFuel = null;
        document.getElementById("naveimg").src = "img/" + shipImg + ".png";
    }
}
function actualizarFuel() {
    if (isFuel && !endGame) {
        actualFuel -= 0.1;
        if (actualFuel <= 0) {
            isFuel = false;
            actualFuel = 0;
            motorOff();
        }

        if (actualFuel <= fuelStart / 5) {
            combustible.css("color", "red");
        } else if (actualFuel <= fuelStart / 2) {
            combustible.css("color", "orange");
        }
        combustible.text(actualFuel.toFixed(2));
    }

}

function hideAll() {
    $("#pausePanel").hide();
    $("#instructionPanel").hide();
    $("#optionPanel").hide();
    $("#configurationPanel").hide();
    $("#rankingPanel").hide();
    $("#aboutPanel").hide();
    pauseVisible = false;
    instructionsVisible = false;
    optionsVisible = false;
    configVisible = false;
    rankingVisible = false;
    aboutVisible = false;
}