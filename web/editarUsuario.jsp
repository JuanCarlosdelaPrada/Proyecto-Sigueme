<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <c:choose>
            <c:when test="${sessionScope.correo eq requestScope.usuario.usuarioId}">
                <title>Editar mi perfil</title>
            </c:when>
            <c:otherwise>
                <title>Editar usuario: ${requestScope.usuario.usuarioId}</title> 
            </c:otherwise>
        </c:choose>
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
                <c:choose>
                    <c:when test="${sessionScope.correo eq requestScope.usuario.usuarioId}">
                        <h1>Editar mi perfil</h1>
                    </c:when>
                    <c:otherwise>
                        <h1>Editar usuario: ${requestScope.usuario.usuarioId}</h1> 
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form" method="POST" action="editarUsuario">
                <input type="hidden" name="correo" value="${requestScope.usuario.usuarioId}"/>
                <div class="form-group">
                    <label for="newPassword" class="col-lg-offset-1 col-lg-2 control-label">Nueva contraseña:</label>
                    <div class="col-lg-3">
                        <input type="password" id="newPassword" name="newPassword" class="form-control"  placeholder="Nueva contraseña">
                    </div>
                </div>
                <div class="form-group">
                    <label for="validatePassword" class="col-lg-offset-1 col-lg-2 control-label">Validar nueva contraseña:</label>
                    <div class="col-lg-3">
                        <input type="password" id="validatePassword" name="validatePassword" class="form-control"  placeholder="Validar nueva contraseña">
                    </div>
                </div>
                <div class="form-group">
                    <label for="nombre" class="col-lg-offset-1 col-lg-2 control-label">Nombre:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" value="${requestScope.usuario.nombre}" maxlength="32">
                    </div>
                </div>
                <div class="form-group">
                    <label for="apellidos" class="col-lg-offset-1 col-lg-2 control-label">Apellidos:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Apellidos" value="${requestScope.usuario.apellidos}" maxlength="64">
                    </div>
                </div>
                <div class="form-group">
                    <label for="dni" class="col-lg-offset-1 col-lg-2 control-label">DNI:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="dni" name="dni" value="${requestScope.usuario.dni}" placeholder="DNI">
                    </div>
                </div>
                <div class="form-group">
                    <label for="direccion" class="col-lg-offset-1 col-lg-2 control-label">Dirección:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" value="${requestScope.usuario.direccion}" maxlength="45">
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_nacimiento" class="col-lg-offset-1 col-lg-2 control-label">Fecha de nacimiento:</label>
                    <div class="col-lg-3">
                        <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento" value="${requestScope.fechaNacimiento}" placeholder="Fecha de nacimiento">
                    </div>
                </div>
                <div class="form-group">
                    <label for="telefono" class="col-lg-offset-1 col-lg-2 control-label">Teléfono:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="telefono" name="telefono" value="${requestScope.usuario.telefono}" placeholder="Teléfono" maxlength="9">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Sexo:</label>
                    <div class="col-lg-3">
                        <div class="radio">
                            <label>
                                <c:choose>
                                    <c:when test="${requestScope.usuario.sexo eq 'hombre'}">
                                        <input name="sexo" value="hombre" type="radio" checked>
                                    </c:when>
                                    <c:otherwise>
                                        <input name="sexo" value="hombre" type="radio">
                                    </c:otherwise>
                                </c:choose>
                                Hombre <i class="fa fa-male" aria-hidden="true"></i>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <c:choose>
                                    <c:when test="${requestScope.usuario.sexo eq 'mujer'}">
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
                        <input type="text" class="form-control" id="club" name="club" value="${requestScope.usuario.club}" maxlength="64">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Indique si es federado o no:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${requestScope.usuario.federado}">
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
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/validarUsuario.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>