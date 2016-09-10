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
         <c:choose>
            <c:when test="${sessionScope.correo eq requestScope.usuario.usuarioId}">
                <title>Mi perfil</title>
            </c:when>
            <c:otherwise>
                <title>Perfil de ${requestScope.usuario.usuarioId}</title> 
            </c:otherwise>
        </c:choose>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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

        <!--Contenido-->
        <div class="page-header">
            <div class="col-sm-offset-1">
                <c:choose>
                    <c:when test="${sessionScope.correo eq requestScope.usuario.usuarioId}">
                        <h1>Mi perfil</h1>
                    </c:when>
                    <c:otherwise>
                        <h1>Perfil de ${requestScope.usuario.usuarioId}</h1> 
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="nombre" class="col-lg-offset-1 col-lg-2 control-label">Nombre:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" value="${requestScope.usuario.nombre}" maxlength="32" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="apellidos" class="col-lg-offset-1 col-lg-2 control-label">Apellidos:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Apellidos" value="${requestScope.usuario.apellidos}" maxlength="64" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="dni" class="col-lg-offset-1 col-lg-2 control-label">DNI:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="dni" name="dni" value="${requestScope.usuario.dni}" placeholder="DNI" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="direccion" class="col-lg-offset-1 col-lg-2 control-label">Dirección:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" value="${requestScope.usuario.direccion}" maxlength="45" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_nacimiento" class="col-lg-offset-1 col-lg-2 control-label">Fecha de nacimiento:</label>
                    <div class="col-lg-3">
                        <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento" value="${requestScope.fechaNacimiento}" placeholder="Fecha de nacimiento" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="telefono" class="col-lg-offset-1 col-lg-2 control-label">Teléfono:</label>
                    <div class="col-lg-3">
                        <input type="text" class="form-control" id="telefono" name="telefono" value="${requestScope.usuario.telefono}" placeholder="Teléfono" maxlength="9" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Sexo:</label>
                    <div class="col-lg-3">
                        <div class="radio">
                            <c:choose>
                                <c:when test="${requestScope.usuario.sexo eq 'hombre'}">
                                    Hombre <i class="fa fa-male" aria-hidden="true"></i>
                                </c:when>
                                <c:otherwise>
                                    Mujer <i class="fa fa-female" aria-hidden="true"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="club" class="col-lg-offset-1 col-lg-2 control-label">Club al que pertenece:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.usuario.club}">
                                <input type="text" class="form-control" id="club" name="club" value="Ninguno" maxlength="64" disabled>
                            </c:when>
                            <c:otherwise>
                                <input type="text" class="form-control" id="club" name="club" value="${requestScope.usuario.club}" maxlength="64" disabled>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">¿Es federado? </label>
                    <div class="col-lg-3">
                        <div class="radio">
                            <c:choose>
                                <c:when test="${requestScope.usuario.federado}">
                                    Sí
                                </c:when>
                                <c:otherwise>
                                    No
                                </c:otherwise>
                            </c:choose>
                        </div>
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