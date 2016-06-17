$(document).ready(function () {
    var error = true;
    var correo_exp = /^[a-zA-Z0-9_\.\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$/;
    $("#boton_login").click(function (){
        if( $("#correo_login").val() === "" || !correo_exp.test($("#correo_login").val())){
            $("#correo_login_div").attr("class", "form-group has-error");
        }
        else {
            error = false;
        }

        if( $("#contraseña_login").val() === "" ) {
            $("#contraseña_login_div").attr("class", "form-group has-error");
        }
        else {
            error = false;
        }
        
        if(!error) {
            $.ajax({
            // la URL para la petición
            url : "login",

            // la información a enviar
            // (también es posible utilizar una cadena de datos)
            data : $("#datos_login").serialize(),

            // especifica si será una petición POST o GET
            type : 'POST',

            // el tipo de información que se espera de respuesta
            //dataType : 'json',

            // código a ejecutar si la petición es satisfactoria;
            // la respuesta es pasada como argumento a la función
            success : function(result) {
                alert("hola"+result);
            },

            // código a ejecutar si la petición falla;
            // son pasados como argumentos a la función
            // el objeto de la petición en crudo y código de estatus de la petición
            error : function(xhr, status) {
                alert('Disculpe, existió un problema');
            }
        });
        }
        else {
            return false;
        }
    });
    $("#correo_login").keyup(function(){
        if( $(this).val() !== "" && correo_exp.test($(this).val())){
            $("#correo_login_div").attr("class", "form-group");
            return false;
        }
    });
    $("#contraseña_login").keyup(function(){
        if( $(this).val() !== "" ){
            $("#contraseña_login_div").attr("class", "form-group");
            return false;
        }
    });
});