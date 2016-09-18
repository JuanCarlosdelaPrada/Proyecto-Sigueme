<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Subir ruta</title>
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
                <h1>Subir ruta</h1> 
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
            <form class="form-horizontal" role="form" method="POST" action="subirRuta" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="track_id" class="col-lg-offset-1 col-lg-2 control-label">Nombre de la ruta:</label>
                    <div id="ruta" class="col-lg-3">
                        <input type="text" id="track_id" name="track_id" class="form-control success" placeholder="Nombre de la ruta" maxlength="75" value="${requestScope.track_id}" onkeyup="unico()" onchange="unico()" required>
                        <font id="mensajeTrack" color="red"></font>
                    </div>
                </div>
                <div class="form-group">
                    <label for="descripcion" class="col-lg-offset-1 col-lg-2 control-label">Descripción de la ruta:</label>
                    <div class="col-lg-3">
                        <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripción de la ruta" maxlength="360">${requestScope.descripcion}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-lg-offset-1 col-lg-2 control-label">Dificultad:</label>
                    <div class="col-lg-3">
                        <c:choose>
                            <c:when test="${empty requestScope.dificultad}">
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="dificultad" value="fácil" required checked>
                                        Fácil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="dificultad" value="moderado" required>
                                        Moderado
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="dificultad" value="difícil" required>
                                        Difícil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="dificultad" value="muy difícil" required>
                                        Muy difícil
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="dificultad" value="sólo expertos" required>
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
                    <label for="ficheroGPX" class="col-lg-offset-1 col-lg-2 control-label">Subir un archivo GPX:</label>
                    <div class="col-lg-3">
                        <input type="file" id="ficheroGPX" name="ficheroGPX" class="filestyle" data-iconName="glyphicon glyphicon-upload" data-input="true" data-buttonText="" data-buttonName="btn btn-default" accept=".gpx" required>
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
        <script>
            if ("${requestScope.track_id}" !== "") {
                unico();
            }
            function unico() {
                $.post(
                    'comprobarRutaId',  
                    {ruta_id: $('#track_id').val()},
                    function(data){
                        var datos = $(data).attr('mensajeRutaId');
                        if ($("#ruta").hasClass("has-error")) {
                            $("#ruta").removeClass("has-error");
                            $("#mensajeTrack").html("");
                        }
                        else if ($("#ruta").hasClass("has-success")) {
                            $("#ruta").removeClass("has-success");
                        }
                        if (datos !== "") {
                            $("#ruta").addClass("has-error");
                            $("#mensajeTrack").html(datos);
                        }
                        else if ($('#track_id').val() === "") {
                            $("#ruta").addClass("has-error");
                            $("#mensajeTrack").html("Debe indicar un nombre de ruta.");
                        }
                        else {
                            $("#ruta").addClass("has-success");
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