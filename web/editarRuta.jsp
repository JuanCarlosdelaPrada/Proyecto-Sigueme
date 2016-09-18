<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Editar ruta: ${requestScope.ruta.rutaId}</title>
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
                 <h1>Editar ruta: ${requestScope.ruta.rutaId}</h1> 
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
            <form class="form-horizontal" role="form" method="POST" action="editarRuta" enctype="multipart/form-data">
                <input type="hidden" name="ruta_id" value="${requestScope.ruta.rutaId}">
                <div class="form-group">
                    <label for="descripcion" class="col-lg-offset-1 col-lg-2 control-label">Descripción de la ruta:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.descripcion}">
                                <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la ruta" maxlength="360">${requestScope.ruta.descripcion}</textarea>
                            </c:when>
                            <c:otherwise>
                                <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la ruta" maxlength="360">${requestScope.descripcion}</textarea>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Dificultad:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.dificultad}">
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.ruta.dificultad eq 'fácil'}">
                                                <input type="radio" name="dificultad" value="fácil" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="fácil" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Fácil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.ruta.dificultad eq 'moderado'}">
                                                <input type="radio" name="dificultad" value="moderado" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="moderado" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Moderado
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.ruta.dificultad eq 'difícil'}">
                                                <input type="radio" name="dificultad" value="difícil" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="difícil" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Difícil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.ruta.dificultad eq 'muy difícil'}">
                                                <input type="radio" name="dificultad" value="muy difícil" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="muy difícil" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Muy difícil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.ruta.dificultad eq 'sólo expertos'}">
                                                <input type="radio" name="dificultad" value="sólo expertos" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="sólo expertos" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Sólo expertos
                                    </label>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.dificultad eq 'fácil'}">
                                                <input type="radio" name="dificultad" value="fácil" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="fácil" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Fácil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.dificultad eq 'moderado'}">
                                                <input type="radio" name="dificultad" value="moderado" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="moderado" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Moderado
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.dificultad eq 'difícil'}">
                                                <input type="radio" name="dificultad" value="difícil" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="difícil" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Difícil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.dificultad eq 'muy difícil'}">
                                                <input type="radio" name="dificultad" value="muy difícil" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="muy difícil" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Muy difícil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <c:choose>
                                            <c:when test="${requestScope.dificultad eq 'sólo expertos'}">
                                                <input type="radio" name="dificultad" value="sólo expertos" required checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="dificultad" value="sólo expertos" required>
                                            </c:otherwise>
                                        </c:choose>
                                        Sólo expertos
                                    </label>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label for = "GPXactual" class="col-lg-offset-1 col-lg-2 control-label">Archivo GPX actual:</label>
                    <div class="col-lg-3">
                        <input class="btn btn-info" id="GPXactual" name="GPXactual" type="button" value="Ver" onclick="window.open('ficherosGPX/${requestScope.ruta.rutaId}.gpx', '_blank')"/>
                        <a class="btn btn-danger " href="ficherosGPX/${requestScope.ruta.rutaId}.gpx" download>Descargar</a>
                    </div>
                </div>
                <div class="form-group">
                    <label for="ficheroGPX" class="col-lg-3 control-label">Modificar archivo GPX:</label>
                    <div class="col-lg-3">
                        <input type="file" id="ficheroGPX" name="ficheroGPX" class="filestyle" data-iconName="glyphicon glyphicon-upload" data-input="true" data-buttonText="" data-buttonName="btn btn-default" accept=".gpx">
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