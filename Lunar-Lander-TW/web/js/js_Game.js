//ENTORNO
var g = 1.622;
var dt = 0.016683;
var timer=null;
var timerFuel=null;

//NAVE
var y = 23; // Initial height
var v = 0;
var c = 100;
var a = g; 

//MARCADORES
var velocidad = null;
var altura = null;
var combustible = null;


$(document).ready(function(){
	
	velocidad = $("#speed_Panel");
	altura = $("#height_Panel");
	combustible = $("#fuel_Panel");

	
	//Events movement
        $(document).click(function () {
 	  if (a===g){
  		motorOn();
 	  } else {
  		motorOff();
 	  }});
      
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

	
	//Empezar a mover la nave justo después de cargar la página ---------------------------------------------------
	start();
});

//Definición de funciones
function start(){
	timer=setInterval(function(){ moverNave(); }, dt*1000);
}

function stop(){
	clearInterval(timer);
}

function moverNave(){
	//cambiar velocidad y posicion
	v +=a*dt;
	y +=v*dt;
	//actualizar marcadores
	velocidad.text(v);
	altura.text(y);
	
	//mover hasta que top sea un 70% de la pantalla
	if (y<70){ 
		document.getElementById("nave").style.top = y+"%"; 
	} else { 
		stop();
	}
}
function motorOn(){
	//el motor da aceleración a la nave
	a=-g;
	//mientras el motor esté activado gasta combustible
	if (timerFuel===null)
	timerFuel=setInterval(function(){ actualizarFuel(); }, 10);	
}
function motorOff(){
	a=g;
	clearInterval(timerFuel);
	timerFuel=null;
}
function actualizarFuel(){
	//Restamos combustible hasta que se agota
	c-=0.1;
	if (c < 0 ) c = 0;
	combustible.text(c);	
}
