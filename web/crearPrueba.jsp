<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<sql:setDataSource dataSource="myDatasource"></sql:setDataSource>
<sql:query var="tracks">
    select * from tracks
</sql:query>
<% synchronized(session){
    if(session.isNew()){
        session.setAttribute("usuario", null);
        session.setAttribute("permiso", null);
    }
}
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Crear prueba</title>
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
                <h1>Crear prueba</h1> 
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <br>
            </div>
            <c:choose>
                <c:when test="${!empty tracks.rows}">
                    <form class="form-horizontal" role="form" method="POST" action="crearPrueba">
                        <div class="form-group">
                            <label for="prueba_id" class="col-lg-offset-1 col-lg-2 control-label">Nombre de la prueba:</label>
                            <div class="col-lg-3">
                                <input type="text" id="prueba_id" name="prueba_id" class="form-control" placeholder="Nombre de la prueba" maxlength="75" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="descripcion" class="col-lg-offset-1 col-lg-2 control-label">Descripción de la prueba:</label>
                            <div class="col-lg-3">
                                <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la prueba" maxlength="360"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-offset-1 col-lg-2 control-label">Selecciona la ruta:</label>
                            <div class="col-lg-3">
                                <select id="track_id" name="track_id">
                                    <c:forEach var="track" items="${tracks.rows}">
                                        <option value="${track.track_id}">${track.track_id}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="direccion" class="col-lg-offset-1 col-lg-2 control-label">Dirección donde comienza la prueba:</label>
                            <div class="col-lg-3">
                                <input type="text" id="direccion" name="direccion" class="form-control" placeholder="Dirección donde comienza la prueba" maxlength="45" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="fecha_celebracion" class="col-lg-offset-1 col-lg-2 control-label">Fecha de celebración:</label>
                            <div class="col-lg-3">
                                <input type="date" id="fecha_celebracion" name="fecha_celebracion" class="form-control" required> 
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="hora_comienzo" class="col-lg-offset-1 col-lg-2 control-label">Hora de comienzo:</label>
                            <div class="col-lg-3">
                                <input type="time" id="hora_comienzo" name="hora_comienzo" class="form-control" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="maximo_inscritos" class="col-lg-offset-1 col-lg-2 control-label">Número máximo de inscritos:</label>
                            <div class="col-lg-3">
                                <input type="number" id="maximo_inscritos" name="maximo_inscritos" class="form-control" min="0" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-offset-3 col-lg-1">
                                <button type="submit" class="btn btn-primary btn-block">Enviar</button>
                            </div>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <p><a href="subirRuta.jsp">Para poder crear una prueba necesita que exista al menos una ruta subida.</a></p>
                </c:otherwise>
            </c:choose>
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