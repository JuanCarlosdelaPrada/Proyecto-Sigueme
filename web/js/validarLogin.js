var correo_exp = /^[a-zA-Z0-9_\.\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$/;

function validarLogin(){
    var correcto = true;
    if( $("#correo_login").val() === "" || !correo_exp.test($("#correo_login").val())){
        $("#correo_login_div").attr("class", "form-group has-error has-feedback");
        correcto = false;
    }
    if( $("#contrasena_login").val() === "" ) {
        $("#contrasena_login_div").attr("class", "form-group has-error has-feedback");
        correcto = false;
    }
    return correcto;
};

function validarCorreo() {
    if( $("#correo_login").val() !== "" && correo_exp.test($("#correo_login").val())){
        $("#correo_login_div").attr("class", "form-group");
        return false;
    }
}
    
function validarContrasena(){
    if( $("#contrasena_login").val() !== "" ){
        $("#contrasena_login_div").attr("class", "form-group");
        return false;
   }
}