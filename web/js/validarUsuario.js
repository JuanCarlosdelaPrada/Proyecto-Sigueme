function validarUsuario() {
    var fstPassword = $("#fstPassword").val();
    var sndPassword = $("#sndPassword").val();
    var nombre = $("#nombre").val();
    var apellidos = $("#apellidos").val();
    var direccion = $("#direccion").val();
    var fecha_nacimiento = $("#fecha_nacimiento").val();
    var telefono = $("#telefono").val();
    var sexo = $("input[name='sexo']").val();
    var club = $("#club").val();
    var federado = $("input[name='federado']").val(); 
    alert("hola miarma ya las liado");
    return false;
}
/*
 * 	    $.ajax({
11
           type: "POST",
12
           url: url,
13
           data: $("#formulario").serialize(), // Adjuntar los campos del formulario enviado.
14
           success: function(data)
15
           {
16
               $("#respuesta").html(data); // Mostrar la respuestas del script PHP.
17
           }
18
         });
 */