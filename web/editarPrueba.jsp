<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<sql:setDataSource dataSource="myDatasource"></sql:setDataSource>
<sql:query var="rutas">
    select * from ruta where ruta_id != '${requestScope.prueba.rutaId.rutaId}'
</sql:query>
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
        <title>Editar prueba</title>
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
                <h1>Editar prueba: ${requestScope.prueba.pruebaId}</h1> 
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <br>
            </div>
            <form class="form-horizontal" role="form" method="POST" action="editarPrueba">
                <input type="hidden" name="prueba_id" value="${requestScope.prueba.pruebaId}"/>
                <div class="form-group">
                    <label for="descripcion" class="col-lg-offset-1 col-lg-2 control-label">Descripción de la prueba:</label>
                    <div class="col-lg-3">
                        <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la prueba" maxlength="360">${requestScope.prueba.descripcion}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Selecciona la ruta:</label>
                    <div class="col-lg-3">
                        <select id="ruta_id" name="ruta_id">
                            <option value="${requestScope.prueba.rutaId.rutaId}">${requestScope.prueba.rutaId.rutaId}</option>
                            <c:forEach var="ruta" items="${rutas.rows}">
                                    <option value="${ruta.ruta_id}">${ruta.ruta_id}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lugar" class="col-lg-offset-1 col-lg-2 control-label">Lugar donde comienza la prueba:</label>
                    <div class="col-lg-3">
                        <input type="text" id="lugar" name="lugar" class="form-control" placeholder="Lugar donde comienza la prueba" value="${requestScope.prueba.lugar}" maxlength="45" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_cel" class="col-lg-offset-1 col-lg-2 control-label">Fecha de celebración:</label>
                    <div class="col-lg-3">
                        <input type="date" id="fecha_cel" name="fecha_cel" class="form-control" value="${requestScope.fechaCel}" required> 
                    </div>
                </div>
                <div class="form-group">
                    <label for="hora_cel" class="col-lg-offset-1 col-lg-2 control-label">Hora de celebración:</label>
                    <div class="col-lg-3">
                        <input type="time" id="hora_cel" name="hora_cel" class="form-control" value="${requestScope.horaCel}" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_inscrip_min" class="col-lg-offset-1 col-lg-2 control-label">Fecha apertura inscripción:</label>
                    <div class="col-lg-3">
                        <input type="date" id="fecha_inscrip_min" name="fecha_inscrip_min" class="form-control" value="${requestScope.fechaInscripMin}" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_inscrip_max" class="col-lg-offset-1 col-lg-2 control-label">Fecha límite inscripción:</label>
                    <div class="col-lg-3">
                        <input type="date" id="fecha_inscrip_max" name="fecha_inscrip_max" class="form-control" value="${requestScope.fechaInscripMax}" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="maximo_inscritos" class="col-lg-3 control-label">Número máximo de inscritos:</label>
                    <div class="col-lg-3">
                        <input type="number" id="maximo_inscritos" name="maximo_inscritos" class="form-control" value="${requestScope.prueba.maximoInscritos}" min="1" required>
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