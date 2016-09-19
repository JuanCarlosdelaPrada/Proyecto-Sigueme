<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Crear usuario</title>
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
    <body>
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
        
        <!--Breadcrumb-->
        <%@include file="WEB-INF/jspf/breadcrumb.jspf"%>
        
        <!--Contenido-->
        <div class="page-header">
            <div class="col-sm-offset-1">
                <h1>Crear usuario</h1> 
            </div>
        </div>
        <div class="container-fluid">
            <c:if test="${not empty requestScope.mensajeError}">
                <div class="alert alert-danger col-sm-offset-1 col-sm-10">
                    <strong>Se han producido los siguientes errores:</strong></br>
                    ${requestScope.mensajeError}
                </div>
            </c:if>
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form" method="POST" action="crearUsuario">
                <div class="form-group">
                    <label for="correo" class="col-lg-offset-1 col-lg-2 control-label">Correo electrónico:</label>
                    <div id="usuario" class="col-lg-3">
                        <input type="email" id="correo" name="correo" class="form-control" placeholder="Correo electrónico" value="${requestScope.correo}" maxlength="64" onkeyup="unicoId()" onchange="unicoId()" required>
                        <font id="mensajeUsuario" color="red"></font>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fstPassword" class="col-lg-offset-1 col-lg-2 control-label">Contraseña:</label>
                    <div class="col-lg-3">
                        <input type="password" id="fstPassword" name="fstPassword" class="form-control" value="${requestScope.contrasena}" placeholder="Contraseña" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="sndPassword" class="col-lg-offset-1 col-lg-2 control-label">Validar contraseña:</label>
                    <div class="col-lg-3">
                        <input type="password" id="sndPassword" name="sndPassword" class="form-control" value="${requestScope.valida_contrasena}" placeholder="Validar contraseña" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="nombre" class="col-lg-offset-1 col-lg-2 control-label">Nombre:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" value="${requestScope.nombre}" maxlength="32" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="apellidos" class="col-lg-offset-1 col-lg-2 control-label">Apellidos:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Apellidos" value="${requestScope.apellidos}" maxlength="64" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="dni" class="col-lg-offset-1 col-lg-2 control-label">DNI:</label>
                    <div id="usuarioDNI" class="col-lg-3">
                        <input type="text" class="form-control" id="dni" name="dni" placeholder="DNI" value="${requestScope.dni}" maxlength="9" onkeyup="unicoDNI()" onchange="unicoDNI()" required>
                        <font id="mensajeUsuarioDNI" color="red"></font>
                    </div>
                </div>
                <div class="form-group">
                    <label for="direccion" class="col-lg-offset-1 col-lg-2 control-label">Dirección:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" value="${requestScope.direccion}" maxlength="45" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_nacimiento" class="col-lg-offset-1 col-lg-2 control-label">Fecha de nacimiento:</label>
                    <div class="col-lg-3">
                        <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento" value="${requestScope.fecha_nacimiento}" placeholder="Fecha de nacimiento" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="telefono" class="col-lg-offset-1 col-lg-2 control-label">Teléfono:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" value="${requestScope.telefono}" maxlength="9" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Sexo:</label>
                    <div class="col-lg-3">
                        <div class="radio">
                            <label>
                                <c:choose>
                                    <c:when test="${requestScope.sexo eq 'hombre'}">
                                        <input name="sexo" value="hombre" type="radio" checked>
                                    </c:when>
                                    <c:otherwise>
                                        <input name="sexo" value="hombre" type="radio" checked>
                                    </c:otherwise>
                                </c:choose>
                                Hombre <i class="fa fa-male" aria-hidden="true"></i>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <c:choose>
                                    <c:when test="${requestScope.sexo eq 'mujer'}">
                                        <input name="sexo" value="mujer" type="radio" checked>
                                    </c:when>
                                    <c:otherwise>
                                        <input name="sexo" value="mujer" type="radio">
                                    </c:otherwise>
                                </c:choose>
                                Mujer <i class="fa fa-female" aria-hidden="true"></i>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="club" class="col-lg-offset-1 col-lg-2 control-label">Club al que pertenece:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="club" name="club"value="${requestScope.club}" maxlength="64">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Indique si es federado o no:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${requestScope.federado}">
                                <div class="radio">
                                    <label>
                                        <input name="federado" value="s" type="radio" checked>
                                        Sí
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input name="federado" value="n" type="radio" required>
                                        No
                                    </label>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="radio">
                                    <label>
                                        <input name="federado" value="s" type="radio" required>
                                        Sí
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input name="federado" value="n" type="radio" checked>
                                        No
                                    </label>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-lg-offset-3 col-lg-1">
                        <button type="submit" class="btn btn-primary btn-block">Enviar</button>
                    </div>
                </div>
            </form>
        </div>
            
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script>
            if ("${requestScope.correo}" !== "") {
                unicoId();
            }
            if ("${requestScope.dni}" !== "") {
                unicoDNI();
            }
            function unicoId() {
                $.post(
                    'comprobarUsuarioId',  
                    {usuario_id: $('#correo').val()},
                    function(data){
                        var datos = $(data).attr('mensajeUsuarioId');
                        if ($("#usuario").hasClass("has-error")) {
                            $("#usuario").removeClass("has-error");
                            $("#mensajeUsuario").html("");
                        }
                        else if ($("#usuario").hasClass("has-success")) {
                            $("#usuario").removeClass("has-success");
                        }
                        if (datos !== "") {
                            $("#usuario").addClass("has-error");
                            $("#mensajeUsuario").html(datos);
                        }
                        else if ($('#correo').val() === "") {
                            $("#usuario").addClass("has-error");
                            $("#mensajeUsuario").html("Debe indicar un correo.");
                        }
                        else {
                            $("#usuario").addClass("has-success");
                        }
                    }
                );
            }
            function unicoDNI() {
                $.post(
                    'comprobarUsuarioDNI',  
                    {dni: $('#dni').val()},
                    function(data){
                        var datos = $(data).attr('mensajeUsuarioDNI');
                        if ($("#usuarioDNI").hasClass("has-error")) {
                            $("#usuarioDNI").removeClass("has-error");
                            $("#mensajeUsuarioDNI").html("");
                        }
                        else if ($("#usuarioDNI").hasClass("has-success")) {
                            $("#usuarioDNI").removeClass("has-success");
                        }
                        if (datos !== "") {
                            $("#usuarioDNI").addClass("has-error");
                            $("#mensajeUsuarioDNI").html(datos);
                        }
                        else if ($('#dni').val() === "") {
                            $("#usuarioDNI").addClass("has-error");
                            $("#mensajeUsuarioDNI").html("Debe indicar su DNI.");
                        }
                        else {
                            $("#usuarioDNI").addClass("has-success");
                        }
                    }
                );
            }
        </script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/validarUsuario.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>