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
var mobileNav=false;

//Configuration
var shipImg = "nave";
var ahoraDif = "facil";
var lugarAterrizaje = "luna";
var isModifying=false;

var indexConfig = 0;
var arrayConfig = [];


$(document).ready(function () {

    velocidad = $("#speed_Panel");
    altura = $("#height_Panel");
    combustible = $("#fuel_Panel");


    /*EVENTS GAME MOVEMENT*/
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

    /*NAVBAR EVENTS*/
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
            $("#instrctionPanel").hide();
            optionsVisible = false;
        }else{
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

    $(".btn-return").click(function () {
        hideAll();
        start();
    });

    /*PAUSE EVENTS*/
    $("#resumePause").click(function () {
        start();
        $("#pausePanel").hide();
        pauseVisible = false;
    });
    
    $("#restartPause").click(function () {
        restart();
        $("#pausePanel").hide();
        pauseVisible = false;
    });
 
    /*OPTIONS EVENTS*/
    $("#chooseOption").click(function () {
        cargarConfig();
        restart();
        motorOff();
        hideAll();
    });
    
    $("#newConfigOption").click(function () {
        $("#optionPanel").hide();
        $("#configurationPanel").modal({backdrop: "static", keyboard: "false"});      
    });
    
    $("#modifyConfigOption").click(function () {
        putDataConfig();
        isModifying=true;
        $("#optionPanel").hide();
        $("#configurationPanel").modal({backdrop: "static", keyboard: "false"});      
    });
    
    $("#deleteConfigOption").click(function () {
        if(arrayConfig.length>1){
            deleteConfig();  
        }else{
            showAlert("You can't delete this configuration because you need to have at least one saved configuration.");
        }
            
    });
    
    /*CONFIGURATION EVENTS*/
    $("#saveConfig").click(function () {
        $("#configurationPanel").modal('hide');
        $("#optionPanel").show();
        
    });
    $("#cancelConfig").click(function () {
        $("#configurationPanel").modal('hide');
        $("#optionPanel").show();
    });
    
    /*MOBILE EVENTS*/
    $("#mobileRestart").click(function () {
        restart();
    });
    
    $("#mobilePause").click(function () {
            if (mobilePause) {
            start();
            mobilePause = false;
            $("#mobilePause").text("");
    $("#mobilePause").append("<span class=\"glyphicon glyphicon-pause\"></span>");
        } else {
            stop();
            hideAll();
            mobilePause = true;
            $("#mobilePause").text("");
            $("#mobilePause").append("<span class=\"glyphicon glyphicon-play\"></span>");
        }        
    });
    
    
    
    $("#mobileNav").click(function () {
        if (mobileNav) {
            if(!mobilePause){
            start();
        }
            mobileNav = false;
            $("#mobilePause").prop('disabled', false);         
            $("#mobileRestart").prop('disabled', false);
            $(".navbar").css("min-height", "40px");
        } else {
            stop();
            hideAll();
            mobileNav = true;
            $("#mobilePause").prop('disabled', true);
            $("#mobileRestart").prop('disabled', true);
            $(".navbar").css("min-height", "100%");
        }
    });

    //Empezar a mover la nave justo después de cargar la página ---------------------------------------------------
    start();
    $("#okModal").click(function(){
        $("#alertModal").modal('hide');
    });

});

/*GAME*/

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
        isPausa = true;
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
/*MENU*/
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

function restart() {
    restartConfig();
    start();
}

function restartConfig() {
    y = yStart;
    fuel = fuelStart;
    v = 0;
    isFuel = true;
    endGame = false;
    clearInterval(timer);
    timer = null;
    clearInterval(timerFuel);
    timerFuel = null;

    combustible.css("color", "lime");
    combustible.text(fuel.toFixed());
    document.getElementById("naveimg").src = "img/" + shipImg + ".png";
}

/*OPTIONS*/
function cargarConfig() {
    var indexAux = $("#selOptions option:selected").index();
    seleccionarDif(arrayConfig[indexAux][1]);
    seleccionarNave(arrayConfig[indexAux][2]);
    seleccionarLugar(arrayConfig[indexAux][3]);
}

/*CONFIGURATION*/




function showAlert(text){
    $("#pModal").text(text);
    $("#alertModal").modal({backdrop: "static", keyboard: "false"});
}