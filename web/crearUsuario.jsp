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
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form" method="POST" action="crearUsuario">
                <div class="form-group">
                    <label for="correo" class="col-lg-offset-1 col-lg-2 control-label">Correo electrónico:</label>
                    <div class="col-lg-3">
                        <input type="email" id="correo" name="correo" class="form-control" placeholder="Correo electrónico" maxlength="64">
                    </div>
                </div>
                <div class="form-group">
                    <label for="fstPassword" class="col-lg-offset-1 col-lg-2 control-label">Contraseña:</label>
                    <div class="col-lg-3">
                        <input type="password" id="fstPassword" name="fstPassword" class="form-control" placeholder="Contraseña">
                    </div>
                </div>
                <div class="form-group">
                    <label for="sndPassword" class="col-lg-offset-1 col-lg-2 control-label">Validar contraseña:</label>
                    <div class="col-lg-3">
                        <input type="password" id="sndPassword" name="sndPassword" class="form-control"  placeholder="Validar contraseña">
                    </div>
                </div>
                <div class="form-group">
                    <label for="nombre" class="col-lg-offset-1 col-lg-2 control-label">Nombre:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" maxlength="32">
                    </div>
                </div>
                <div class="form-group">
                    <label for="apellidos" class="col-lg-offset-1 col-lg-2 control-label">Apellidos:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Apellidos" maxlength="64">
                    </div>
                </div>
                <div class="form-group">
                    <label for="dni" class="col-lg-offset-1 col-lg-2 control-label">DNI:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="dni" name="dni" placeholder="DNI">
                    </div>
                </div>
                <div class="form-group">
                    <label for="direccion" class="col-lg-offset-1 col-lg-2 control-label">Dirección:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" maxlength="45">
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_nacimiento" class="col-lg-offset-1 col-lg-2 control-label">Fecha de nacimiento:</label>
                    <div class="col-lg-3">
                        <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento" placeholder="Fecha de nacimiento">
                    </div>
                </div>
                <div class="form-group">
                    <label for="telefono" class="col-lg-offset-1 col-lg-2 control-label">Teléfono:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" maxlength="9">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Sexo:</label>
                    <div class="col-lg-3">
                        <div class="radio">
                            <label>
                                <input name="sexo" value="hombre" type="radio" checked>
                                Hombre <i class="fa fa-male" aria-hidden="true"></i>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input name="sexo" value="mujer" type="radio">
                                Mujer <i class="fa fa-female" aria-hidden="true"></i>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="club" class="col-lg-offset-1 col-lg-2 control-label">Club al que pertenece:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="club" name="club" maxlength="64">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Indique si es federado o no:</label>
                    <div class="col-lg-3">
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
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/validarUsuario.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>