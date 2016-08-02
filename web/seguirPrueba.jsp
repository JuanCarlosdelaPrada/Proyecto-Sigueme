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
              
        <div class="page-header">
            <div class="col-sm-offset-1">
                <h1>${requestScope.prueba.pruebaId}</h1> 
            </div>
        </div>
        
        <div class="container-fluid">
            <div id="map-canvas" class="col-xs-offset-1 col-xs-6">
                <div id="map" style="width:100%;height:400px"></div>
            </div>
            <div id="intro"></div>
            <div id="listado_competidores" class="panel panel-default col-xs-4" style="padding:0">
                <div class="panel-heading">Listado competidores</div>
                <div class="panel-body">
                    <label for="sel2">Mutiple select list (hold shift to select more than one):</label>
                    <select multiple class="form-control" id="sel2">
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                    </select>
                </div>
            </div>
            <div id="intro"></div>
            <div id="opciones" class="panel panel-default col-xs-4" style="padding:0">
                <div class="panel-heading">Opciones</div>
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
                var map;
                
                $("#inicio").data("clicked",false);
                $("#fin").data("clicked",false);
                $("#circuito").data("clicked",false);
                $("#nombres").data("clicked",false);
                
                //############### Google Map Initialize ##############
                function map_initialize() {
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
                    $.get("seguimiento_prueba?prueba_id=${requestScope.prueba.pruebaId}", function (data) {
                        if (markers.length !== 0) {
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

                function delete_markers() {
                    for (var i = 0; i < markers.length; i++) {
                        markers[i].setMap(null);
                        if ($("#nombres").data("clicked") === true) {
                            infowindows[i].close();
                        }
                    }
                    markers = [];
                    if ($("#nombres").data("clicked") === true) {
                        infowindows = [];
                    }
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
                            '<div class="marker-inner-win"><span class="info-content">' +
                            '<p>' + MapTitle + '</p>' +
                            //'<h1 class="marker-heading">' + MapTitle + '</h1>' +
                            //MapDesc+ 
                            //'</span><button name="remove-marker" class="remove-marker" title="Remove Marker">Remove Marker</button>' +
                            '</div></div>');


                    //Create an infoWindow
                    var infowindow = new google.maps.InfoWindow();
                    //set the content of infoWindow
                    infowindow.setContent(contentString[0]);
                    
                    infowindows.push(infowindow);
                    
                    if ($("#nombres").data("clicked") === true) {
                        infowindow.open(map, marker);
                    }
                    /*
                     //Find remove button in infoWindow
                     var removeBtn   = contentString.find('button.remove-marker')[0];

                     //Find save button in infoWindow
                     var saveBtn     = contentString.find('button.save-marker')[0];

                     //add click listner to remove marker button
                     google.maps.event.addDomListener(removeBtn, "click", function(event) {
                     //call remove_marker function to remove the marker from the map
                     remove_marker(marker);
                     });

                     if(typeof saveBtn !== 'undefined') //continue only when save button is present
                     {
                     //add click listner to save marker button
                     google.maps.event.addDomListener(saveBtn, "click", function(event) {
                     var mReplace = contentString.find('span.info-content'); //html to be replaced after success
                     var mName = contentString.find('input.save-name')[0].value; //name input field value
                     var mDesc  = contentString.find('textarea.save-desc')[0].value; //description input field value
                     var mType = contentString.find('select.save-type')[0].value; //type of marker

                     if(mName =='' || mDesc =='')
                     {
                     alert("Please enter Name and Description!");
                     }else{
                     //call save_marker function and save the marker details
                     save_marker(marker, mName, mDesc, mType, mReplace);
                     }
                     });
                     }
                     */
                    //add click listner to save marker button 
                    /*
                    google.maps.event.addListener(marker, 'click', function () {
                        infowindow.open(map, marker); // click on marker opens info window 
                    });

                    if (InfoOpenDefault) //whether info window should be open by default
                    {
                        infowindow.open(map, marker);
                    }*/
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
                /*
                 //############### Remove Marker Function ##############
                 function remove_marker(Marker) {
                 determine whether marker is draggable 
                 new markers are draggable and saved markers are fixed 
                 if(Marker.getDraggable()) 
                 {
                 Marker.setMap(null); //just remove new marker
                 }
                 else
                 {
                 //Remove saved marker from DB and map using jQuery Ajax
                 var mLatLang = Marker.getPosition().toUrlValue(); //get marker position
                 var myData = {del : 'true', latlang : mLatLang}; //post variables
                 $.ajax({
                 type: "POST",
                 url: "map_process.php",
                 data: myData,
                 success:function(data){
                 Marker.setMap(null); 
                 alert(data);
                 },
                 error:function (xhr, ajaxOptions, thrownError){
                 alert(thrownError); //throw any errors
                 }
                 });
                 }
                 }

                 //############### Save Marker Function ##############
                 function save_marker(Marker, mName, mAddress, mType, replaceWin) {
                 //Save new marker using jQuery Ajax
                 var mLatLang = Marker.getPosition().toUrlValue(); //get marker position
                 var myData = {name : mName, address : mAddress, latlang : mLatLang, type : mType }; //post variables
                 console.log(replaceWin);        
                 $.ajax({
                 type: "POST",
                 url: "map_process.php",
                 data: myData,
                 success:function(data){
                 replaceWin.html(data); //replace info window with new html
                 Marker.setDraggable(false); //set marker to fixed
                 Marker.setIcon('http://PATH-TO-YOUR-WEBSITE-ICON/icons/pin_blue.png'); //replace icon
                 },
                 error:function (xhr, ajaxOptions, thrownError){
                 alert(thrownError); //throw any errors
                 }
                 });
                 }
                 });*/
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBO-SUTN3pwBYm44vcHwrrEU28ScOR0F5s&signed_in=false&callback=map_initialize"
            async defer></script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>
