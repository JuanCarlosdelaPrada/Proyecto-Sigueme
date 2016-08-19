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
        <title>Editar ruta</title>
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
            <div class="col-xs-offset-1">
                <c:choose>
                    <c:when test="${empty requestScope.prueba_id}">
                        <h1>Editar inscripción de ${requestScope.usuario_id}</h1>
                    </c:when>
                    <c:otherwise>
                        <h1>Editar inscripción en ${requestScope.prueba_id}</h1>
                    </c:otherwise> 
                </c:choose>
            </div>
        </div>
        
        <div class="container-fluid">
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form" method="POST" action="editarInscripcion">
                <div class="form-group">
                    <c:choose>
                        <c:when test="${!empty requestScope.prueba_id}">
                            <label for="track_id" class="col-lg-offset-1 col-lg-2 control-label">Correo:</label>
                            <div class="col-lg-3">
                                <input type="text" id="usuario_id" name="usuario_id" class="form-control" placeholder="Correo" maxlength="64" value="${requestScope.inscrito.usuario.usuarioId}" required>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <label for="track_id" class="col-lg-offset-1 col-lg-2 control-label">Nombre de la prueba:</label>
                            <div class="col-lg-3">
                                <input type="text" id="prueba_id" name="prueba_id" class="form-control" placeholder="Nombre de la prueba" maxlength="75" value="${requestScope.inscrito.prueba.pruebaId}" required>
                            </div>
                        </c:otherwise> 
                    </c:choose>
                </div>
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
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>