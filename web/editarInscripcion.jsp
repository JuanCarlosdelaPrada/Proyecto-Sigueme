<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <c:choose>
            <c:when test="${empty requestScope.prueba_id}">
                <title>Editar inscripción de ${requestScope.usuario_id} en "${requestScope.inscrito.inscritoPK.pruebaId}"</title>
            </c:when>
            <c:otherwise>
                <title>Editar inscripción en "${requestScope.prueba_id}" de ${requestScope.inscrito.inscritoPK.usuarioId}</title>
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
    </head>
    <body>
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>

        <ul class="breadcrumb" style="margin-bottom:0; background: #FFF7C8; border-radius:0">
            <li><a href="usuarios.jsp">Inscripciones</a></li>
            <li class="active">Editar inscripción</li>
        </ul>
        
        <!--Contenido-->
         <div class="page-header">
            <div class="col-xs-offset-1">
                <c:choose>
                    <c:when test="${empty requestScope.prueba_id}">
                        <h1>Editar inscripción de ${requestScope.usuario_id} en "${requestScope.inscrito.inscritoPK.pruebaId}"</h1>
                    </c:when>
                    <c:otherwise>
                        <h1>Editar inscripción en "${requestScope.prueba_id}" de ${requestScope.inscrito.inscritoPK.usuarioId}</h1>
                    </c:otherwise> 
                </c:choose>
            </div>
        </div>
        
        <div class="container-fluid">
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form" method="POST" action="editarInscripcion">
                <c:choose>
                    <c:when test="${empty requestScope.prueba_id}">
                        <input type="hidden" name="usuarioId" value="${requestScope.usuario_id}"/>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="pruebaId" value="${requestScope.prueba_id}"/>
                    </c:otherwise> 
                </c:choose>
                <input type="hidden" name="prueba_id" value="${requestScope.inscrito.inscritoPK.pruebaId}"/>
                <input type="hidden" name="usuario_id" value="${requestScope.inscrito.inscritoPK.usuarioId}"/>
                <div class="form-group">
                    <label for="dorsal" class="col-lg-offset-1 col-lg-2 control-label">Dorsal:</label>
                    <div class="col-lg-3">
                        <input type="number" id="dorsal" name="dorsal" class="form-control" value="${requestScope.inscrito.dorsal}" min="1" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Pagado:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${requestScope.inscrito.pagado}">
                                <div class="radio">
                                    <label>
                                        <input name="pagado" value="s" type="radio" checked>
                                        Sí
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input name="pagado" value="n" type="radio" required>
                                        No
                                    </label>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="radio">
                                    <label>
                                        <input name="pagado" value="s" type="radio" required>
                                        Sí
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input name="pagado" value="n" type="radio" checked>
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
        <script type="text/javascript" src="js/Bootstrap Filestyle/bootstrap-filestyle.min.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>