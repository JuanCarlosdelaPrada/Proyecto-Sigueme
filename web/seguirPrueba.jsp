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
        <title>${requestScope.prueba.pruebaId}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='img/favicon.ico' rel='shortcut icon' type='image/x-icon'>
        
        <!--Importaciones .css-->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <link href="css/cabecera.css" rel="stylesheet">
        <link href="css/login.css" rel="stylesheet">
    </head>
    <body onresize="map_initialize()">
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
        
        <ul class="breadcrumb" style="margin-bottom:0; background: #FFF7C8; border-radius:0">
            <li><a href="seguimientoPruebas.jsp">Seguimiento de pruebas</a></li>
            <li class="active">Seguir prueba</li>
        </ul>
        
        <div class="page-header">
            <div class="col-sm-offset-1">
                <h1>${requestScope.prueba.pruebaId}</h1> 
            </div>
        </div>
        
        <div class="container-fluid">
            <div id="map-canvas" class="col-xs-offset-1 col-xs-6">
                <div id="map" style="width:100%;height:400px"></div>
            </div>
            <div id="columna" class="col-xs-4">
                <div id="intro"></div>
                <div id="listado_competidores" class="panel panel-default" style="padding:0">
                    <div class="panel-heading"><b>Competidores</b></div>
                    <div class="panel-body">
                        Selecciona un competidor:</br>
                        <div class="text-center">
                            <div class="fom-group">
                                <input id="seleccion" list="inscritos" class="form-control">
                                <datalist id="inscritos">
                                    <c:forEach var="inscrito" items="${inscritos}">
                                        <option value="${inscrito.inscritoPK.usuarioId}"></option>
                                    </c:forEach>
                                </datalist>
                            </div>
                            <div class="fom-group" style="margin-top:0.5%">
                                <a id="buscar" class="form-control btn btn-default">Buscar</a>
                            </div>
                            <div id="mensaje1" style="margin-top:0.5%;display: none">
                                <a id="msj1" class="close">&times;</a>
                                <div id="text1"></div>
                            </div>
                        </div>
                        <hr/>
                        Selección múltiple de competidores (mantén shift para seleccionar más de uno):</label>
                        <div class="text-center">
                            <div class="fom-group">
                                <select id="seleccionMultiple" class="form-control" multiple>
                                    <c:forEach var="inscrito" items="${inscritos}">
                                        <option value="${inscrito.inscritoPK.usuarioId}">${inscrito.dorsal}. ${inscrito.inscritoPK.usuarioId}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="fom-group" style="margin-top:0.5%">
                                <a id="mostrarSeleccionados" class="form-control btn btn-default">Mostrar seleccionados</a>
                            </div>
                            <div id="mensaje2" style="margin-top:0.5%;display: none">
                                <a id="msj2" class="close">&times;</a>
                                <div id="text2"></div>
                            </div>
                            <hr/>
                            <div class="fom-group" style="margin-top:0.5%">
                                <a id="mostrarTodos" class="form-control btn btn-default">Mostrar todos</a>
                            </div>
                            <div id="mensaje3" class="alert alert-success" style="margin-top:0.5%;display: none">
                                <a id="msj3" class="close">&times;</a>
                                <div>La búsqueda ha sido realizada exitósamente.</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="opciones" class="panel panel-default" style="padding:0">
                    <div class="panel-heading"><b>Opciones</b></div>
                    <div class="panel-body">
                        <div class="checkbox">
                            <label>
                                <input id="inicio" type="checkbox" onclick="show_start();">Mostrar inicio
                            </label>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input id="fin" type="checkbox" onclick="show_finish();">Mostrar meta
                            </label>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input id="circuito" type="checkbox" onclick="show_way();">Mostrar circuito
                            </label>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input id="nombres" type="checkbox" onclick="show_names();">Mostrar nombres
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row"></br></div>
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>
        
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script type="text/javascript">
            var path = ${requestScope.latlng};
            var inicio;
            var fin;
            var circuito; 
            var markers = [];
            var infowindows = [];
            var competidores = [];
            var map;

            $("#inicio").data("clicked",false);
            $("#fin").data("clicked",false);
            $("#circuito").data("clicked",false);
            $("#nombres").data("clicked",false);
            
            $("#msj1").click(function(){
                $("#mensaje1").hide();
            });
            $("#msj2").click(function(){
                $("#mensaje2").hide();
            });
            $("#msj3").click(function(){
                $("#mensaje3").hide();
            });
            
            $("#buscar").click(function() {
                competidores = [];
                competidores.push($("#seleccion").val());
                $("#seleccion").val("");
                message("#mensaje1", "#text1", false);
            });

            $("#mostrarSeleccionados").click(function() {
                competidores = [];
                $('#seleccionMultiple option').each(function() {
                    if(this.selected) {
                        competidores.push(this.value);
                        this.selected = false;
                    }
                });
                message("#mensaje2", "#text2", false);
            }); 

            $("#mostrarTodos").click(function() {
                competidores = [];
                message("#mensaje3", "", true); 
            }); 
            
            function message(message, text, shDir) {
                delete_message(message);
                if ($(message).css("display") === "block") {
                    $(message).fadeOut("fast", function() {
                        if (!shDir) {
                            create_message(message, text);
                        }
                        else {
                            $(message).show();
                        }
                    });
                }
                else {
                    if (!shDir) {
                        create_message(message, text);
                    }
                    else {
                        $(message).show();
                    }
                }
            }
            
            function create_message(message, text) {
                if ($(message).hasClass("alert alert-danger")) {
                    $(message).removeClass("alert alert-danger");
                }
                else if ($(message).hasClass("alert alert-success")) {
                    $(message).removeClass("alert alert-success");
                }
                if (competidores[0] === "" || competidores.length === 0) {
                    $(message).addClass("alert alert-danger");
                    $(text).html('<strong>Error</strong>: no se ha definido ningún competidor.');
                }
                else {
                    $(message).addClass("alert alert-success");
                    $(text).html('La búsqueda ha sido realizada exitósamente.');
                }
                $(message).show();
            }
            
            function delete_message(message) {
                var mensajes = ["#mensaje1", "#mensaje2", "#mensaje3"];
                for (var i = 0; i < mensajes.length; i++) {
                    if (mensajes[i] !== message) {
                        $(mensajes[i]).hide();
                    }
                }
            }
            
            //############### Google Map Initialize ##############
            function map_initialize() {
                if (window.innerWidth <= 767) {
                    $("#map-canvas").removeClass("col-xs-offset-1 col-xs-6").addClass("col-xs-12");
                    $("#columna").removeClass("col-xs-4").addClass("col-xs-12");
                    $("#columna").css("marginLeft", $("#map-canvas").offset().left - 15.5);
                    $("#columna").width($("#map-canvas").width() + 1);
                    $("#intro").html("&nbsp");
                }
                else {
                    $("#map-canvas").removeClass("col-xs-12").addClass("col-xs-offset-1 col-xs-6");
                    $("#columna").removeClass("col-xs-12").addClass("col-xs-4");
                    $("#columna").css("marginLeft", "initial");
                    $("#columna").width("");
                    $("#intro").html("");
                }
                
                $("#map").height($("#map").width());
                
                map = new google.maps.Map(document.getElementById("map"), {
                    zoom: 8, //zoom level, 0 = earth view to higher value
                    center: path[1],
                    mapTypeId: 'terrain',
                    panControl: true, //enable pan Control
                    zoomControl: true, //enable zoom control
                    zoomControlOptions: {
                        style: google.maps.ZoomControlStyle.SMALL //zoom control size
                    },
                    scaleControl: true // enable scale control
                });

                var bounds = new google.maps.LatLngBounds();
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMin}, ${prueba.rutaId.longMin}));
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMin}, ${prueba.rutaId.longMax}));
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMax}, ${prueba.rutaId.longMin}));
                bounds.extend(new google.maps.LatLng(${prueba.rutaId.latMax}, ${prueba.rutaId.longMax}));
                map.fitBounds(bounds);

                if ($("#inicio").data("clicked") === true) {
                    inicio = new google.maps.Marker({
                        position: path[0],
                        map: map,
                        icon: 'markers/start-flag.png',
                        title: 'Inicio de la prueba'
                    });
                }
                if ($("#fin").data("clicked") === true) {
                    fin = new google.maps.Marker({
                        position: path[path.length - 1],
                        map: map,
                        icon: 'markers/finish.png',
                        title: 'Meta'
                    });
                }
                if ($("#circuito").data("clicked") === true) {
                    circuito = new google.maps.Polyline({
                        path: path,
                        strokeColor: '#0000CC',
                        opacity: 0.4,
                        map: map
                    });
                }

                //Load Markers from the XML File, Check (map_process.php) //CAMBIADO POR SEGUIMIENTO PRUEBA
                window.setInterval(create_markers, 2500);

                /*
                 //drop a new marker on right click
                 google.maps.event.addListener(map, 'rightclick', function(event) {
                 //Edit form to be displayed with new marker
                 var EditForm = '<p><div class="marker-edit">'+
                 '<form action="ajax-save.php" method="POST" name="SaveMarker" id="SaveMarker">'+
                 '<label for="pName"><span>Place Name :</span><input type="text" name="pName" class="save-name" placeholder="Enter Title" maxlength="40" /></label>'+
                 '<label for="pDesc"><span>Description :</span><textarea name="pDesc" class="save-desc" placeholder="Enter Address" maxlength="150"></textarea></label>'+
                 '<label for="pType"><span>Type :</span> <select name="pType" class="save-type"><option value="restaurant">Rastaurant</option><option value="bar">Bar</option>'+
                 '<option value="house">House</option></select></label>'+
                 '</form>'+
                 '</div></p><button name="save-marker" class="save-marker">Save Marker Details</button>';

                 //call create_marker() function
                 create_marker(event.latLng, 'New Marker', EditForm, true, true, true, "http://PATH-TO-YOUR-WEBSITE-ICON/icons/pin_green.png");
                 });  
                 */
            }

            function create_markers() {
                if (competidores === null) {
                    $.post("seguimiento_prueba",{prueba_id: "${requestScope.prueba.pruebaId}"}, function(data) {
                        if (markers.length !== 0) {
                            delete_names();
                            delete_markers();
                        }
                        $(data).find("marker").each(function () {
                            //Get user input values for the marker from the form
                            var title = $(this).attr('dorsal');
                            var name = $(this).attr('usuario_id');
                            //var address   = '<p>'+ $(this).attr('address') +'</p>';
                            var point = new google.maps.LatLng(parseFloat($(this).attr('latitud')), parseFloat($(this).attr('longitud')));
                            //call create_marker() function for xml loaded maker
                            create_marker(point, name, title, false, false, false/*, "http://PATH-TO-YOUR-WEBSITE-ICON/icons/pin_blue.png"*/);
                        });
                    });
                }
                else {
                    $.post("seguimiento_prueba",{prueba_id: "${requestScope.prueba.pruebaId}", 'competidores_id[]': competidores }, function(data) {
                        if (markers.length !== 0) {
                            delete_names();
                            delete_markers();
                        }
                        $(data).find("marker").each(function () {
                            //Get user input values for the marker from the form
                            var title = $(this).attr('dorsal');
                            var name = $(this).attr('usuario_id');
                            //var address   = '<p>'+ $(this).attr('address') +'</p>';
                            var point = new google.maps.LatLng(parseFloat($(this).attr('latitud')), parseFloat($(this).attr('longitud')));
                            //call create_marker() function for xml loaded maker
                            create_marker(point, name, title, false, false, false/*, "http://PATH-TO-YOUR-WEBSITE-ICON/icons/pin_blue.png"*/);
                        });
                    });
                }
            }

            function delete_markers() {
                for (var i = markers.length; i > 0; i--) {
                    markers[i - 1].setMap(null);
                }
                markers = [];
            }

            //############### Create Marker Function ##############
            function create_marker(MapPos, MapTitle, Title, InfoOpenDefault, DragAble, Removable/*, iconPath*/) {
                var colores = [
                    'FF0000', //Rojo
                    'FFFC00', //Amarillo
                    '2BFF00', //Verde
                    'FFB300', //Naranja claro
                    '00FFFC', //Azul claro
                    'FF00D1', //Rosa
                    'B700FF', //Violeta
                    'C9FF00', //Verde amarillento
                    'FF7C00', //Naranja fuerte
                    '00B3FF' //Azul
                ];

                var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + colores[(Title - 1) % colores.length],
                    new google.maps.Size(21, 34),
                    new google.maps.Point(0,0),
                    new google.maps.Point(10, 34));

                //new marker
                var marker = new google.maps.Marker({
                    position: MapPos,
                    map: map,
                    draggable: DragAble,
                    //animation: google.maps.Animation.DROP,
                    title: Title,
                    icon: pinImage
                });
                markers.push(marker);
                //Content structure of info Window for the Markers
                var contentString = $('<div class="marker-info-win">' +
                        '<div class="marker-inner-win">'+
                        '<span class="info-content">' +
                        '<p>' + MapTitle + '</p>' +
                        //'<h1 class="marker-heading">' + MapTitle + '</h1>' +
                        //MapDesc+ 
                        '</span>'+
                        //'</span><button name="remove-marker" class="remove-marker" title="Remove Marker">Remove Marker</button>' +
                        '</div></div>');


                //Create an infoWindow
                var infowindow = new google.maps.InfoWindow({
                    //set the content of infoWindow
                    content: contentString[0],
                    position: marker.getPosition(),
                    pixelOffset: new google.maps.Size(0, -marker.getIcon().size.height)

                });
                //alert(marker.getIcon().size.height);
                if ($("#nombres").data("clicked") === true) {
                    infowindow.open(map/*, marker*/);
                }

                infowindows.push(infowindow);
            }

            function show_way() {
                if ($("#circuito").data('clicked') === false) {
                    circuito = new google.maps.Polyline({
                            path: path,
                            strokeColor: '#0000CC',
                            opacity: 0.4,
                            map: map
                    });
                    $("#circuito").data('clicked', true);
                }
                else {
                    circuito.setMap(null);
                    $("#circuito").data('clicked', false);
                }
            }

            function show_start() {
                if ($("#inicio").data('clicked') === false) {
                    inicio = new google.maps.Marker({
                        position: path[0],
                        map: map,
                        icon: 'markers/start-flag.png',
                        title: 'Inicio de la prueba'
                    });
                    $("#inicio").data('clicked', true);
                }
                else {
                    inicio.setMap(null);
                    $("#inicio").data('clicked', false);
                }
            }

            function show_finish() {
                if ($("#fin").data('clicked') === false) {
                    fin = new google.maps.Marker({
                        position: path[path.length - 1],
                        map: map,
                        icon: 'markers/finish.png',
                        title: 'Meta'
                    });
                    $("#fin").data('clicked', true);
                }
                else {
                    fin.setMap(null);
                    $("#fin").data('clicked', false);
                }
            }

            function show_names() {
                if ($("#nombres").data('clicked') === false) {
                    for(var i = 0; i < infowindows.length; i++) {
                        infowindows[i].open(map, markers[i]);
                    }
                    $("#nombres").data('clicked', true);
                }
                else {
                    delete_names();
                    $("#nombres").data('clicked', false);
                }
            }

            function delete_names() {
                for(var i = 0; i < infowindows.length; i++) {
                    infowindows[i].close();
                }
                infowindows = [];
            }
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBO-SUTN3pwBYm44vcHwrrEU28ScOR0F5s&signed_in=false&callback=map_initialize"
            async defer></script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>
