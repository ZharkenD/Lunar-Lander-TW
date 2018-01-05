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
                    <button type="button" class="navbar-toggle" id="mobileNav" data-toggle="collapse" data-target="#myNavbar">
                        <span class="glyphicon glyphicon-cog"></span>
                    </button>
                    <button type="button" class="navbar-toggle" id="mobilePause" data-toggle="collapse" data-target="#myNavbar">
                        <span class="glyphicon glyphicon-pause"></span>
                    </button>
                    <button type="button" class="navbar-toggle" id="mobileRestart">
                        <span class="glyphicon glyphicon-repeat"></span>
                    </button>
                    <a class="navbar-brand">Lunar  <span class="glyphicon glyphicon-star"></span> Lander</a>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li><a class="pc" id="playNav"><span class="glyphicon glyphicon-play"></span> Play</a></li>
                        <li><a id="instructionNav"><span class="glyphicon glyphicon-align-left"></span> Instructions</a></li>
                        <li><a id="optionNav"><span class="glyphicon glyphicon-wrench"></span> Options</a></li> 
                        <li><a id="rankingNav"><span class="glyphicon glyphicon-stats"></span> Ranking</a></li>
                        <li><a id="usersNav"><span class="glyphicon glyphicon-user"></span> Users Online</a></li> 
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
        <div class="d"></div>

        <div class="panel panel-default panel-modal" id="pausePanel">
            <div class="panel-heading"><h2>PAUSE</h2></div>
            <div class="panel-body"><div class="btn-group-vertical">
                    <button type="button" class="btn btn-panel" id="resumePause">Resume</button>
                    <button type="button" class="btn btn-panel" id="restartPause">Restart</button>
                </div></div>
        </div>

        <div class="panel panel-default panel-modal" id="instructionPanel">
            <div class="panel-heading"><h2>INSTRUCTIONS</h2></div>
            <div class="panel-body"><div class="well well-lg">

                    <p> The objective of Lunar Lander is to land the ship on the surface of the Moon without crashing. </p>
                    <p> You can propel the ship <span class = "pc"> using the Space Bar </span> <span class = "movil"> by touching the screen </span> to reduce its speed and prevent it from exploding. But watch carefully! Gasoline is limited and you could run out of it.
                    </p>
                    <p class = "pc"> You can pause the game by pressing P at any time. </p>
                    <p class = "pc"> Use the mouse to navigate the top menu. </p>
                    <p> Good Luck Astronaut! </p>
                </div></div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="optionPanel">
            <div class="panel-heading"><h2>OPTIONS</h2></div>
            <div class="panel-body">
                <div class="well">
                    <p>Welcome </p>
                    <p>Please, choose your configuration</p>
                </div>
                <select class="form-control" id="selOptions">
                    <option id="1">ddd</option>
                    <option id="22">d2dd</option>
                    <option id="333">d3dd</option>
                </select>
                <br>
                <button type="button" class="btn btn-panel" id="chooseOption">Choose this configuration</button>
                <br>
                <div class="btn-group-vertical">
                    <button type="button" class="btn btn-panel" id="newConfigOption">New configuration</button>
                    <button type="button" class="btn btn-panel" id="modifyConfigOption">Modifiy selected configuration</button>
                    <button type="button" class="btn btn-panel" id="deleteConfigOption">Delete selected configuration</button>
                </div>
            </div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="configurationPanel">
            <div class="panel-heading"><h2>CONFIGURATION</h2></div>
            <div class="panel-body">
                <div class="form-group">
                    <input type="text" name="configName" id="configName" class="form-control" placeholder="Configuration Name" value="" maxlength="30" pattern=".{4,}" title="Minimum 4 characters" required>
                </div>
                <h3>Difficulty</h3>
                <div class="btn-group btn-group-justified">
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel btn-active" id="easyConfig">Easy</button>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel" id="mediumConfig">Medium</button>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel" id="hardConfig">Hard</button>
                    </div>
                </div>
                <div class="well well-sm">
                    <p id="infoLevel">EASY: You have 100 liters of fuel and you must land less than 5 m/s.</p>
                </div>
                <h3>Spaceship</h3>
                <div class="btn-group btn-group-justified">
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel btn-active" id="standardConfig">Standard</button>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel" id="ufoConfig">UFO</button>
                    </div>
                </div>
                <h3>Landing</h3>
                <div class="btn-group btn-group-justified">
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel btn-active" id="moonConfig">Moon</button>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-panel" id="marsConfig">Mars</button>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <div class="btn-group btn-group-justified">
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary" id="saveConfig">Save</button>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary" id="cancelConfig">Cancel</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="panel panel-default panel-modal" id="rankingPanel">
            <div class="panel-heading"><h2>SCORES</h2></div>
            <div class="panel-body">
                <!-- Tab links -->
                <div class="tab">
                    <button class="tablinks" onclick="openTab(event, 'mybest')">My best scores</button>
                    <button class="tablinks" onclick="openTab(event, 'worldbest')">Best world scores</button>
                    <button class="tablinks" onclick="openTab(event, 'topten')">Top 10 most played</button>
                </div>

                <!-- Tab content -->
                <div id="mybest" class="tabcontent">
                    <div class="container">           
                        <table class="table table-condensed">
                            <thead>
                                <tr>
                                   <th>Configuration Name</th>
                                    <th>Difficult</th>
                                    <th>Score</th>
                                </tr>
                            </thead>
                            <tbody id="mybestbody">

                            </tbody>
                        </table>
                    </div>

                </div>

                <div id="worldbest" class="tabcontent">
                    <div class="container">           
                        <table class="table table-condensed">
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Difficult</th>
                                    <th>Score</th>
                                </tr>
                            </thead>
                            <tbody id="worldbestbody">
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="topten" class="tabcontent">
                    <div class="container">           
                        <table class="table table-condensed">
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Games Played</th>
                                </tr>
                            </thead>
                            <tbody id="toptenbody">
                                
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="usersPanel">
            <div class="panel-heading"><h2>USERS ONLINE</h2></div>
            <div class="panel-body"><ul class="list-group" id="list-g-users">
                </ul></div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="panel panel-default panel-modal" id="aboutPanel">
            <div class="panel-heading"><h2>ABOUT</h2></div>
            <div class="panel-body"><div class="well well-lg">
                    <p> IES Francesc Borja Moll - 2º DAM - Access to Database</p>
                    <p> This project has been producd out by the following people: </p>
                    <p> - Ángel Barceló </p>
                    <p> - Carlos Enrique Dorst </p>
                    <p> - Ramón Moreno </p>
                    <p> <span class="glyphicon glyphicon-copyright-mark"></span> All rights reserved  </p>
                </div></div>
            <div class="panel-footer"><button type="button" class="btn btn-default btn-return">Return</button></div>
        </div>

        <div class="modal fade" id="alertModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">     
                    <div class="modal-body text-center">
                        <div id="parrafosAbout">
                            <p id="pModal"></p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="form-control btn" id="okModal">OK</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="panel panel-default panel-modal" id="endPanel">
            <div class="panel-heading"><h2 id="hEnd">.</h2></div>
            <div class="panel-body"><div class="well well-lg">
                    <p id="pEnd"> . </p>
                    <p> Your final score is <span id="scoreEnd"></span>. </p>
                </div></div>
            <div class="panel-footer"><button type="button" class="btn btn-default" id="playEnd">Play Again</button></div>
        </div>

    </body>
</html>
