<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<sql:setDataSource dataSource="myDatasource"></sql:setDataSource>
<c:choose>
    <c:when test="${empty requestScope.ruta_id}">
        <sql:query var="rutas">
            select * from ruta
        </sql:query>
    </c:when>
    <c:otherwise>
        <sql:query var="rutas">
            select * from ruta where ruta_id != '${requestScope.ruta_id}'
        </sql:query>
    </c:otherwise>
</c:choose>
<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Crear prueba</title>
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
                <h1>Crear prueba</h1> 
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
            <c:choose>
                <c:when test="${!empty rutas.rows}">
                    <form class="form-horizontal" role="form" method="POST" action="crearPrueba">
                        <div class="form-group">
                            <label for="prueba_id" class="col-lg-offset-1 col-lg-2 control-label">Nombre de la prueba:</label>
                            <div id="prueba" class="col-lg-3">
                                <input type="text" id="prueba_id" name="prueba_id" class="form-control" placeholder="Nombre de la prueba" maxlength="75" value="${requestScope.prueba_id}" onkeyup="unico()" onchange="unico()" required>
                                <font id="mensajePrueba" color="red"></font>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="descripcion" class="col-lg-offset-1 col-lg-2 control-label">Descripción de la prueba:</label>
                            <div class="col-lg-3">
                                <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la prueba" maxlength="360">${requestScope.descripcion}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-offset-1 col-lg-2 control-label">Selecciona la ruta:</label>
                            <div class="col-lg-3">
                                <select id="ruta_id" name="ruta_id">
                                    <c:if test="${not empty ruta_id}">
                                        <option value="${requestScope.ruta_id}">${requestScope.ruta_id}</option>
                                    </c:if>
                                    <c:forEach var="ruta" items="${rutas.rows}">
                                        <option value="${ruta.ruta_id}">${ruta.ruta_id}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lugar" class="col-lg-offset-1 col-lg-2 control-label">Lugar donde comienza la prueba:</label>
                            <div class="col-lg-3">
                                <input type="text" id="lugar" name="lugar" class="form-control" placeholder="Lugar donde comienza la prueba" value="${requestScope.lugar}" maxlength="45" required>
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
                                <input type="number" id="maximo_inscritos" name="maximo_inscritos" class="form-control" value="${requestScope.maximoInscritos}" min="1" required>
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
                    <div class="alert alert-warning col-sm-offset-1 col-sm-10">
                        <strong>ATENCIÓN</strong>: para poder crear una prueba necesita que exista al menos una ruta subida. Si desea subir una ruta proceda a pulsar el siguiente botón:</br></br>
                        <div class="text-center">
                            <a class="btn btn-info" href="SubirRuta">Subir ruta</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script>
            if ("${requestScope.prueba_id}" !== "") {
                unico();
            }
            function unico() {
                $.post(
                    'comprobarPruebaId',  
                    {prueba_id: $('#prueba_id').val()},
                    function(data){
                        var datos = $(data).attr('mensajePruebaId');
                        if ($("#prueba").hasClass("has-error")) {
                            $("#prueba").removeClass("has-error");
                            $("#mensajePrueba").html("");
                        }
                        else if ($("#prueba").hasClass("has-success")) {
                            $("#prueba").removeClass("has-success");
                        }
                        if (datos !== "") {
                            $("#prueba").addClass("has-error");
                            $("#mensajePrueba").html(datos);
                        }
                        else if ($('#track_id').val() === "") {
                            $("#prueba").addClass("has-error");
                            $("#mensajePrueba").html("Debe indicar el nombre de la prueba.");
                        }
                        else {
                            $("#prueba").addClass("has-success");
                        }
                    }
                );
            }
        </script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/Bootstrap Filestyle/bootstrap-filestyle.min.js" charset="utf-8"></script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
    </body>
</html>