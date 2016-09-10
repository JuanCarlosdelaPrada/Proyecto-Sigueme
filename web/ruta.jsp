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
        <title>${requestScope.ruta.rutaId}</title>
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
                <h1>${requestScope.ruta.rutaId}</h1> 
            </div>
        </div>
        
        <div class="container-fluid">
            <div id="map-canvas" class="col-xs-offset-1 col-xs-4">
                <div id="map" style="width:100%;height:400px"></div>
            </div>
            <div id="columna" class="col-xs-6">
                <div id="intro"></div>
                <div id="descripcion" class="panel panel-default" style="padding:0;display:block;clear:both">
                    <div class="panel-heading"><b>Descripción</b></div>
                    <div class="panel-body">${requestScope.ruta.descripcion}</div>
                </div>
                <div id="masInformacion" class="panel panel-default" style="padding:0;display:block;clear:both">
                    <div class="panel-heading"><b>Más información</b></div>
                    <div class="panel-body">
                        <ul>
                            <li>Pertenece a la <u>dificultad</u>: <mark><b>${requestScope.ruta.dificultad}</b></mark>.</li>
                            <li>Tiene una <u>distancia</u> aproximada de <mark><b>${requestScope.distancia} m</b></mark>.</li>
                            <li><u>Archivo GPX</u>:</li>
                        </ul>
                        <div class="text-center">
                            <input class="btn btn-info" id="GPXactual" name="GPXactual" type="button" value="Ver" onclick="window.open('ficherosGPX/${requestScope.ruta.rutaId}.gpx', '_blank')"/>
                            <a class="btn btn-danger " href="ficherosGPX/${requestScope.ruta.rutaId}.gpx" download>Descargar</a>
                        </div>
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
                    $("#columna").removeClass("col-xs-6").addClass("col-xs-12");
                    $("#columna").css("marginLeft", $("#map-canvas").offset().left - 16.75);
                    $("#columna").width($("#map-canvas").width() + 2.5);
                    $("#intro").html("&nbsp");
                }
                else {
                    $("#map-canvas").removeClass("col-xs-12").addClass("col-xs-offset-1 col-xs-4");
                    $("#columna").removeClass("col-xs-12").addClass("col-xs-6");
                    $("#columna").css("marginLeft", "initial");
                    $("#columna").width("");
                    $("#intro").html("");
                }
                
                $("#map").height($("#map").width());
                
                // camino
                var path = ${requestScope.latlng};

                // mapa
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 8,
                    center: path[1],
                    mapTypeId: 'terrain'
                });

                // Incluir los límites
                var bounds = new google.maps.LatLngBounds();
                bounds.extend(new google.maps.LatLng(${ruta.latMin}, ${ruta.longMin}));
                bounds.extend(new google.maps.LatLng(${ruta.latMin}, ${ruta.longMax}));
                bounds.extend(new google.maps.LatLng(${ruta.latMax}, ${ruta.longMin}));
                bounds.extend(new google.maps.LatLng(${ruta.latMax}, ${ruta.longMax}));
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
                    title: 'Comienzo de la ruta'
                });
                new google.maps.Marker({
                    position: path[path.length - 1],
                    map: map,
                    icon: 'markers/finish.png',
                    title: 'Final de la ruta'
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
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>