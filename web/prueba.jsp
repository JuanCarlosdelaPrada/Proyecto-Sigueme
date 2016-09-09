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
        <title>${prueba.pruebaId}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!--Importaciones .css-->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <link href="css/cabecera.css" rel="stylesheet">
        <link href="css/login.css" rel="stylesheet">
    </head>
    <body onresize="initMap()">
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
              
        <div class="page-header">
            <div class="col-sm-offset-1">
                <h1>${prueba.pruebaId}</h1> 
            </div>
        </div>
        
        <div class="container-fluid">
            <div id="map-canvas" class="col-xs-offset-1 col-xs-4">
                <div id="map" style="width:100%;height:400px"></div>
            </div>
            <div class="col-xs-6"> 
                
            <div id="intro"></div>
            <div id="descripcion" class="panel panel-default col-xs-12" style="padding:0;display:block;clear:both">
                <div class="panel-heading"><b>Descripción</b></div>
                <div class="panel-body">${prueba.descripcion}</div>
            </div>
            <div id="intro2"></div>
            <div id="datosInteres" class="panel panel-default" style="padding:0;display:block;clear:both">
                <div class="panel-heading"><b>Datos de interés</b></div>
                <div class="panel-body">
                    Se celebrará en ${prueba.lugar} el día ${fechaCel} a las ${horaCel}. Fechas a tener en cuenta:
                    <ul>
                        <li>Apertura del plazo de inscripciones ${fechaInscripMin}.</li>
                        <li>Cierre del plazo de inscripciones ${fechaInscripMax}.</li>
                    </ul>
                    <c:choose>
                        <c:when test="${periodoInscripcion and not maximoinscritos}">
                            <c:choose>
                                <c:when test="${usuario ne null}">
                                    <input class="btn btn-success" type="button" value="Inscribirse" onclick="window.open('inscribirse?prueba_id=${prueba.pruebaId}')"/></br>
                                </c:when>
                                <c:otherwise>
                                    ¿Desea inscribirse?</br>
                                    <input class="btn btn-success" type="button" value="¡Registrate ya!" onclick="window.open('crearUsuario.jsp', '_self')"/></br>
                                </c:otherwise>
                            </c:choose>
                            ABIERTO EL PLAZO DE INSCRIPCIONES</br>
                        </c:when>
                        <c:when test="${not periodoInscripcion}">
                            <input class="btn btn-default" type="button" value="Inscribirse" disabled/></br>
                            <font color="orange">ACTUALMENTE EL PLAZO DE INSCRIPCIONES ESTÁ CERRADO</font></br>
                        </c:when>      
                        <c:when test="${maximoinscritos}">
                            <input class="btn btn-default" type="button" value="Inscribirse" disabled/></br>
                            <font color="red">EL CUPO DE PLAZAS ESTÁ CUBIERTO</font></br>
                        </c:when> 
                        <c:otherwise>
                            <input class="btn btn-default" type="button" value="Inscribirse" disabled/></br>
                        </c:otherwise>
                    </c:choose>
                    <mark>Nota</mark>: el número máximo de plazas son ${prueba.maximoInscritos}.
                </div>
            </div>
            <div id="intro3"></div>
            <div id="masInformacion" class="panel panel-default" style="padding:0;display:block;clear:both">
                <div class="panel-heading"><b>Más información</b></div>
                <div class="panel-body">
                    Si desea obtener más información acerca de la ruta asociada a dicha prueba proceda a pulsar el siguiente botón:</br>
                    <a class="btn btn-info col-xs-offset-5" href="ruta?ruta_id=${prueba.rutaId.rutaId}">Mostrar ruta</a>
                </div>
            </div>
                
            </div>
        </div>
        <div class="row"></br></div>
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script>
            function initMap() {
                if (window.innerWidth <= 767) {
                    $("#map-canvas").removeClass("col-xs-offset-1 col-xs-4").addClass("col-xs-12");
                    $("#descripcion").removeClass("col-xs-6").addClass("col-xs-12");
                    $("#descripcion").css("marginLeft", $("#map-canvas").offset().left);
                    $("#descripcion").width($("#map-canvas").width() - 2);
                    $("#intro").html("&nbsp");
                }
                else {
                    $("#map-canvas").removeClass("col-xs-12").addClass("col-xs-offset-1 col-xs-4");
                    $("#descripcion").removeClass("col-xs-12").addClass("col-xs-6");
                    $("#descripcion").css("marginLeft", "initial");
                    $("#descripcion").width("");
                    $("#intro").html("");
                }
                
                $("#map").height($("#map").width());
                
                // camino
                var path = ${latlng};

                // mapa
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 8,
                    center: path[1],
                    mapTypeId: 'terrain'
                });

                // Incluir los límites
                var bounds = new google.maps.LatLngBounds();
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMin}, ${prueba.rutaId.longMin}));
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMin}, ${prueba.rutaId.longMax}));
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMax}, ${prueba.rutaId.longMin}));
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMax}, ${prueba.rutaId.longMax}));
                map.fitBounds(bounds);

                // Dibujar el camino
                new google.maps.Polyline({
                    path: path,
                    strokeColor: '#0000CC',
                    opacity: 0.4,
                    map: map
                });
                
                // Dibujar inicio y fin
                new google.maps.Marker({
                    position: path[0],
                    map: map,
                    icon: 'markers/start-flag.png',
                    title: 'Comienzo de la prueba'
                });
                new google.maps.Marker({
                    position: path[path.length - 1],
                    map: map,
                    icon: 'markers/finish.png',
                    title: 'Final de la prueba'
                });
                /*
                //Distancia
                var path_coords = new google.maps.MVCArray();
                for (var i = 0; i < path.length; i++) {
                    path_coords.push(new google.maps.LatLng(path[i]));
                }
                alert(google.maps.geometry.spherical.computeLength(path_coords));
                */
            }
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?libraries=geometry&key=AIzaSyBO-SUTN3pwBYm44vcHwrrEU28ScOR0F5s&signed_in=false&callback=initMap"
            async defer></script>
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <c:if test="${usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>