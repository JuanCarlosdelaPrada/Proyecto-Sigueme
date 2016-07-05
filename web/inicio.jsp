<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% synchronized(session){
    if(session.isNew()){
        session.setAttribute("correo", null);
        session.setAttribute("usuario", null);
        session.setAttribute("permiso", null);
    }
}
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Inicio</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!--Importaciones .css-->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <link href="css/cabecera.css" rel="stylesheet">
        <link href="css/login.css" rel="stylesheet">
    </head>
    <style>
        .carousel-inner > .item > img,
        .carousel-inner > .item > a > img {
            width: 75%;
            margin: auto;
        }
  </style>
    <body>
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
              
        <!--Jumbotron-->
        <div class="container-fluid" style="margin-top:0.35%; margin-left:-0.65%; margin-right:-0.85%"> <!-- border-color: purple; border-style: solid-->
            <div class="text-justify jumbotron" style="padding-top: 0.25%; padding-bottom: 0.15%;background-color:  violet; color: purple">
                <h3 style="font-size: 135%"><u><b>Proyecto "Sígueme"</b></u></h3>
                <p style="font-size: 115%">
                    &nbsp;&nbsp;&nbsp;&nbsp;El proyecto "Sígueme" surge como necesidad de realizar un seguimiento de las personas
                   que llevan a cabo una actividad deportiva al aire libre en un evento determinado.
                ${requestScope.usuario}
                </p>
                
                <blockquote class="blockquote-reverse text-right" style="margin-top: -1.5%;padding-top: 0.25%; padding-bottom: 0.15%">
                    <footer style="color: darkorchid; font-size: 75%">
                        Desarrollador de la aplicación web
                    </footer>
                </blockquote> 
                
            </div>
        </div>
                
        <!--Carousel-->
        <div id="myCarousel" class="carousel slide center-block" data-ride="carousel" style="width:80%">
            <!-- Indicators -->
            <ol class="carousel-indicators">
                <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                <li data-target="#myCarousel" data-slide-to="1"></li>
                <li data-target="#myCarousel" data-slide-to="2"></li>
                <li data-target="#myCarousel" data-slide-to="3"></li>
                <li data-target="#myCarousel" data-slide-to="4"></li>
                <li data-target="#myCarousel" data-slide-to="5"></li>
            </ol>

            <!-- Wrapper for slides -->
            <div class="carousel-inner" role="listbox">
                <div class="item active">
                    <img class="img-responsive" src="img/carousel/MountainBike.jpg" alt="Chania" style="width:65%;height:250px">
                    <div class="carousel-caption">
                        <h3>Mountain bike</h3>
                    </div>
                </div>
                
                <div class="item">
                    <img class="img-responsive" src="img/carousel/Esqui.jpg" alt="Flower" style="width:65%;height:250px">
                    <div class="carousel-caption">
                        <h3>Esquí de fondo</h3>
                    </div>
                </div>
                
                <div class="item">
                    <img class="img-responsive" src="img/carousel/Senderismo.jpg" alt="Chania" style="width:65%;height:250px">
                    <div class="carousel-caption">
                        <h3>Senderismo</h3>
                    </div>
                </div>

                <div class="item">
                    <img class="img-responsive" src="img/carousel/Piraguismo.jpg" alt="Flower" style="width:65%;height:250px">
                    <div class="carousel-caption">
                        <h3>Piraguismo</h3>
                    </div>
                </div>
                
                <div class="item">
                    <img class="img-responsive" src="img/carousel/Running.jpg" alt="Flower" style="width:65%;height:250px">
                    <div class="carousel-caption">
                        <h3>Running</h3>
                    </div>
                </div>
                <div class="item">
                    <div class="jumbotron center-block" style="background-color:black;width:65%;height:250px;margin-bottom:0"></div>
                    <div class="carousel-caption" style="top: 50%;transform: translateY(-55%);">
                       <h3>y muchos más <br>...</h3>
                    </div>
                </div>
            </div>

            <!-- Left and right controls -->
            <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Atrás</span>
            </a>
            <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Siguiente</span>
            </a>
        </div>
                
        <!--Noticias-->
        <div class="container-fluid ">
            <h2>Pruebas publicadas recientemente</h2>            
            <div class="row">
              <div class="col-md-4">
                <a href="pulpitrock.jpg" class="thumbnail">
                  <p>Pulpit Rock: A famous tourist attraction in Forsand, Ryfylke, Norway.</p>    
                  <img src="pulpitrock.jpg" alt="Pulpit Rock" style="width:150px;height:150px">
                </a>
              </div>
              <div class="col-md-4">
                <a href="moustiers-sainte-marie.jpg" class="thumbnail">
                  <p>Moustiers-Sainte-Marie: Considered as one of the "most beautiful villages of France".</p>
                  <img src="moustiers-sainte-marie.jpg" alt="Moustiers Sainte Marie" style="width:150px;height:150px">
                </a>
              </div>
              <div class="col-md-4">
                <a href="cinqueterre.jpg" class="thumbnail">
                  <p>The Cinque Terre: A rugged portion of coast in the Liguria region of Italy.</p>      
                  <img src="cinqueterre.jpg" alt="Cinque Terre" style="width:150px;height:150px">
                </a>
              </div>
            </div>
        </div>
        
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>