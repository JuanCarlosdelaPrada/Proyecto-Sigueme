<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<%
    ArrayList<String> nav = new ArrayList<String>();
    nav.add("/Sigueme");
    nav.add("Inicio");
    session.setAttribute("navegacion", nav);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Inicio</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='img/favicon.ico' rel='shortcut icon' type='image/x-icon'>
        
        <!--Importaciones .css-->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <link href="css/cabecera.css" rel="stylesheet">
        <link href="css/login.css" rel="stylesheet">
        <link href="css/breadcrumb.css" rel="stylesheet">
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
        
        <!--Breadcrumb-->
        <%@include file="WEB-INF/jspf/breadcrumb.jspf"%>
        
        <div class="container">
            <!-- Trigger the modal with a button -->
            <button type="button" class="btn btn-info btn-lg" id="myBtn" style="display:none"></button>

            <!-- Modal -->
            <div class="modal fade" id="myModal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">${requestScope.Cabecera}</h4>
                        </div>
                        <div class="modal-body">
                            <p>${requestScope.Cuerpo}</p>
                        </div>
                    </div>
              </div>
            </div>
        </div>
        
        <!--Jumbotron-->
        <div class="container-fluid" style="margin-top: 0.35%; margin-left:-0.65%; margin-right:-0.85%"> <!-- border-color: purple; border-style: solid-->
            <div class="text-justify jumbotron" style="padding-top: 0.25%; padding-bottom: 0.15%;background-color:  violet; color: purple">
                <h3 style="font-size: 135%"><u><b>Proyecto "Sígueme"</b></u></h3>
                <p style="font-size: 115%">
                    &nbsp;&nbsp;&nbsp;&nbsp;El proyecto "Sígueme" surge como necesidad de realizar un seguimiento de las personas
                   que llevan a cabo una actividad deportiva al aire libre en un evento determinado.
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
        <c:choose>
            <c:when test="${not empty requestScope.pruebasRecientes}">
                <div class="container-fluid ">
                    <h2>Pruebas publicadas recientemente</h2>
                    <div class="row">
                        <c:forEach var="prueba" items="${requestScope.pruebasRecientes}">
                            <div class="col-md-4">
                                <a href="prueba?prueba_id=${prueba.pruebaId}" class="thumbnail">
                                  <p>${prueba.pruebaId}: ${prueba.descripcion}</p>    
                                  <img src="img/logo_web.png" alt="${prueba.pruebaId}" style="width:150px;height:150px">
                                </a>
                          </div>
                        </c:forEach>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row" style="margin-top:0.65%">
                    </br>
                </div>
            </c:otherwise>
        </c:choose>
        
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>
        
        <!--Importaciones .js-->
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script>
            <c:if test="${not empty requestScope.Cabecera}">
                $(document).ready(function(){
                    $("#myModal").modal();
                });
            </c:if>
        </script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>