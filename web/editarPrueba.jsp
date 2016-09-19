<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<sql:setDataSource dataSource="myDatasource"></sql:setDataSource>
<c:choose>
    <c:when test="${empty requestScope.ruta_id}">
        <sql:query var="rutas">
            select * from ruta where ruta_id != '${requestScope.prueba.rutaId.rutaId}'
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
        <title>Editar prueba: ${requestScope.prueba.pruebaId}</title>
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
                <h1>Editar prueba: ${requestScope.prueba.pruebaId}</h1> 
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
            <form class="form-horizontal" role="form" method="POST" action="editarPrueba">
                <input type="hidden" name="prueba_id" value="${requestScope.prueba.pruebaId}"/>
                <div class="form-group">
                    <label for="descripcion" class="col-lg-offset-1 col-lg-2 control-label">Descripción de la prueba:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestSCope.descripcion}">
                                <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la prueba" maxlength="360">${requestScope.prueba.descripcion}</textarea>
                            </c:when>
                            <c:otherwise>
                                <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la prueba" maxlength="360">${requestScope.descripcion}</textarea>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Selecciona la ruta:</label>
                    <div class="col-lg-3">
                        <select id="ruta_id" name="ruta_id">
                            <c:choose>
                                <c:when test="${empty requestScope.ruta_id}">
                                    <option value="${requestScope.prueba.rutaId.rutaId}">${requestScope.prueba.rutaId.rutaId}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${requestScope.ruta_id}">${requestScope.ruta_id}</option>
                                </c:otherwise>
                            </c:choose>
                            
                            <c:forEach var="ruta" items="${rutas.rows}">
                                    <option value="${ruta.ruta_id}">${ruta.ruta_id}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lugar" class="col-lg-offset-1 col-lg-2 control-label">Lugar donde comienza la prueba:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.lugar}">
                                <input type="text" id="lugar" name="lugar" class="form-control" placeholder="Lugar donde comienza la prueba" value="${requestScope.prueba.lugar}" maxlength="45" required>
                            </c:when>
                            <c:otherwise>
                                <input type="text" id="lugar" name="lugar" class="form-control" placeholder="Lugar donde comienza la prueba" value="${requestScope.lugar}" maxlength="45" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_cel" class="col-lg-offset-1 col-lg-2 control-label">Fecha de celebración:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.fecha_cel}">
                                <input type="date" id="fecha_cel" name="fecha_cel" class="form-control" value="${requestScope.fechaCel}" required>
                            </c:when>
                            <c:otherwise>
                                <input type="date" id="fecha_cel" name="fecha_cel" class="form-control" value="${requestScope.fecha_cel}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label for="hora_cel" class="col-lg-offset-1 col-lg-2 control-label">Hora de celebración:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.hora_cel}">
                                <input type="time" id="hora_cel" name="hora_cel" class="form-control" value="${requestScope.horaCel}" required>
                            </c:when>
                            <c:otherwise>
                                <input type="time" id="hora_cel" name="hora_cel" class="form-control" value="${requestScope.hora_cel}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_inscrip_min" class="col-lg-offset-1 col-lg-2 control-label">Fecha apertura inscripción:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.fecha_inscrip_min}">
                                <input type="date" id="fecha_inscrip_min" name="fecha_inscrip_min" class="form-control" value="${requestScope.fechaInscripMin}" required>
                            </c:when>
                            <c:otherwise>
                                <input type="date" id="fecha_inscrip_min" name="fecha_inscrip_min" class="form-control" value="${requestScope.fecha_inscrip_min}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fecha_inscrip_max" class="col-lg-offset-1 col-lg-2 control-label">Fecha límite inscripción:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.fecha_inscrip_max}">
                                <input type="date" id="fecha_inscrip_max" name="fecha_inscrip_max" class="form-control" value="${requestScope.fechaInscripMax}" required>
                            </c:when>
                            <c:otherwise>
                                <input type="date" id="fecha_inscrip_max" name="fecha_inscrip_max" class="form-control" value="${requestScope.fecha_inscrip_max}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label for="maximo_inscritos" class="col-lg-3 control-label">Número máximo de inscritos:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.maximoInscritos}">
                                <input type="number" id="maximo_inscritos" name="maximo_inscritos" class="form-control" value="${requestScope.prueba.maximoInscritos}" min="1" required>
                            </c:when>
                            <c:otherwise>
                                <input type="number" id="maximo_inscritos" name="maximo_inscritos" class="form-control" value="${requestScope.maximoInscritos}" min="1" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                 <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">¿Desea activar la prueba?</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty activa}">
                                <div class="radio-inline">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.prueba.activa eq true}">
                                                <input name="activa" value="s" type="radio" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input name="activa" value="s" type="radio">
                                            </c:otherwise>
                                        </c:choose>
                                        Sí
                                    </label>
                                </div>
                                <div class="radio-inline">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.prueba.activa eq false}">
                                                <input name="activa" value="n" type="radio" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input name="activa" value="n" type="radio">
                                            </c:otherwise>
                                        </c:choose>
                                        No
                                    </label>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="radio-inline">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.activa eq true}">
                                                <input name="activa" value="s" type="radio" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input name="activa" value="s" type="radio">
                                            </c:otherwise>
                                        </c:choose>
                                        Sí
                                    </label>
                                </div>
                                <div class="radio-inline">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.activa eq false}">
                                                <input name="activa" value="n" type="radio" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input name="activa" value="n" type="radio">
                                            </c:otherwise>
                                        </c:choose>
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