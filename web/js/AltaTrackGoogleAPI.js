// This example creates an interactive map which constructs a polyline based on
// user clicks. Note that the polyline only appears once its path property
// contains two LatLng coordinates.

var poly;
var map;
var elemRec = new Array();
var markers = new Array();
var markersRec = new Array();

function initialize(){
    initMap();
    initAutocomplete();
}

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 5,
        center: {lat: 40.25, lng: 3.45}  // Center the map on Chicago, USA.
    });

    // Add a listener for the click event
    map.addListener('click', addLatLng);

    poly = new google.maps.Polyline({
        strokeColor: '#ff0000',
        strokeOpacity: 1.0,
        strokeWeight: 3
    });
    poly.setMap(map);           
}

function initAutocomplete() {
    // Create the search box and link it to the UI element.
    var input = document.getElementById("ubicacion");
    var searchBox = new google.maps.places.SearchBox(input);

    // Bias the SearchBox results towards current map's viewport.
    map.addListener('bounds_changed', function() {
        searchBox.setBounds(map.getBounds());
    });

    // [START region_getplaces]
    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();
        if (places.length === 0) {
              return;
        }
        // For each place, get the icon, name and location.
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) {
            var icon = {
                url: place.icon,
                size: new google.maps.Size(71, 71),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(17, 34),
                scaledSize: new google.maps.Size(25, 25)
            };
            if (place.geometry.viewport) {
                // Only geocodes have viewport.
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });
        map.fitBounds(bounds);
    });
    // [END region_getplaces]
}

// Handles click events on a map, and adds a new point to the Polyline.
function addLatLng(event) {
    var path = poly.getPath();

    // Because path is an MVCArray, we can simply append a new coordinate
    // and it will automatically appear.
    path.push(event.latLng);

    // Add a new marker at the new plotted point on the polyline.
    var marker = new google.maps.Marker({
        position: event.latLng,
        title: '#' + path.getLength(),
        map: map
    });
    markers.push(marker);
    $("#track").val(JSON.stringify(path.getArray()));
    $("#distancia").val(google.maps.geometry.spherical.computeLength(path));
}

function addLine() {
    var path = poly.getPath();
    if (elemRec.length !== 0) {
        path.push(elemRec.pop());
        markers.push(markersRec.pop());
        markers[markers.length-1].setMap(map);
        $("#track").val(JSON.stringify(path.getArray()));
        $("#distancia").val(google.maps.geometry.spherical.computeLength(path));
    }
}

function removeLine(){
    var path = poly.getPath();
    if (path.getLength() > 1) {
        elemRec.push(path.pop());
        markers[markers.length - 1].setMap(null);
        markersRec.push(markers.pop());
        $("#track").val(JSON.stringify(path.getArray()));
        $("#distancia").val(google.maps.geometry.spherical.computeLength(path));
    } 
}

function removeAll(){
    var path = poly.getPath();
    path.clear();
    /*
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }*/
    $.each(markers, function(index, value) {
        value.setMap(null);
    });
    elemRec = [];
    markers = [];
    $("#track").val(JSON.stringify(path.getArray()));
    $("#distancia").val(google.maps.geometry.spherical.computeLength(path));
}

document.write("<script async defer src='https://maps.googleapis.com/maps/api/js?key=AIzaSyBO-SUTN3pwBYm44vcHwrrEU28ScOR0F5s&signed_in=false&libraries=geometry,places&callback=initialize'></script>");