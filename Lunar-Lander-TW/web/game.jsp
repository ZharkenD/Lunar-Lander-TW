<%-- 
    Document   : game
    Created on : 02-ene-2018, 12:34:57
    Author     : Ramon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lunar Lander</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="Game Lunar Lnder">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel='stylesheet' type="text/css" href='css/css_Game.css'>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script src="js/js_Game.js"></script>
    </head>
    <body>
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" id="mobilePause" data-toggle="collapse" data-target="#myNavbar">
                        <span class="glyphicon glyphicon-cog"></span>
                    </button>
                    <a class="navbar-brand">Lunar  <span class="glyphicon glyphicon-star"></span> Lander</a>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li><a id="playNav"><span class="glyphicon glyphicon-play"></span> Play</a></li>
                        <li><a id="instructionNav"><span class="glyphicon glyphicon-align-left"></span> Instructions</a></li>
                        <li><a id="optionNav"><span class="glyphicon glyphicon-wrench"></span> Options</a></li> 
                        <li><a id="rankingNav"><span class="glyphicon glyphicon-stats"></span> Ranking</a></li> 
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a id="aboutNav"><span class="glyphicon glyphicon-info-sign"></span> About</a></li>
                        <li><a id="logoutNav"><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div id="nave">
            <img id="naveimg" src="img/nave.png" alt="img nave">
        </div>

        <div class="row">
            <div class="col-sm-4">
                <div class="panel panel-default" id="info_Panel_Control">
                    <div class="panel-body" id="info_Panel">
                        <ul class="list-group">
                            <li class="list-group-item">Speed: <span id="speed_Panel">0</span> m/s</li>
                            <li class="list-group-item">Fuel: <span id="fuel_Panel">100</span> l</li>
                            <li class="list-group-item">Height: <span id="height_Panel">50</span> m</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-8">
                <img id="earth" src="img/earth.png" alt="img earth">
            </div>
        </div>
        <div class="d">d</div>

        <div class="panel panel-default panel-modal" id="pausePanel">
            <div class="panel-heading">PAUSE</div>
            <div class="panel-body"><div class="btn-group-vertical">
                    <button type="button" class="btn btn-panel">Resume</button>
                    <button type="button" class="btn btn-panel">Restart</button>
                </div></div>
        </div>

        <div class="panel panel-default panel-modal" id="instructionPanel">
            <div class="panel-heading">INSTRUCTIONS</div>
            <div class="panel-body">Panel Content</div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="optionPanel">
            <div class="panel-heading">OPTIONS</div>
            <div class="panel-body">Panel Content</div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>
        
        <div class="panel panel-default panel-modal" id="configurationPanel">
            <div class="panel-heading">CONFIGURATION</div>
            <div class="panel-body">Panel Content</div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="rankingPanel">
            <div class="panel-heading">RANKING</div>
            <div class="panel-body">Panel Content</div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="aboutPanel">
            <div class="panel-heading">ABOUT</div>
            <div class="panel-body">Panel Content</div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>
    </body>
</html>
