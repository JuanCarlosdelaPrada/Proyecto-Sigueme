<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% synchronized(session){
    if(session.isNew()){
        session.setAttribute("usuario", null);
        session.setAttribute("permiso", null);
    }
}
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Ruta</title>
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
        </div>
        <div class="row"></br></div>
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script>
            function initMap() {
                if (window.innerWidth <= 767) alert("que cabron");
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
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>