<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="WEB-INF/jspf/sesion.jspf"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Rutas</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='img/favicon.ico' rel='shortcut icon' type='image/x-icon'>
        
        <!--Importaciones .css-->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet"> 
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <!-- 
                    CON ESTA LÍNEA NO SE MEZCLAN LOS ESTILOS
        <link href="css/jquery.dataTables.min.css" rel="stylesheet"> 
        -->
        <!-- <link href="css/responsive.dataTables.min.css" rel="stylesheet">-->
        <link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
        <link href="css/responsive.bootstrap.min.css" rel="stylesheet">
        <link href="css/buttons.dataTables.min.css" rel="stylesheet">
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
        
        <div class="page-header">
            <div class="col-xs-offset-1">
                <h1>Rutas</h1> 
            </div>
        </div>
        <c:if test="${not empty requestScope.mensajeCreacion}">
            <div class="alert alert-success col-sm-offset-1 col-sm-10">
                <strong>Información:</strong> ${requestScope.mensajeCreacion}
            </div>
        </c:if>
        <div class="container-fluid">   <!--table-bordered-->
            <div class="col-xs-offset-1 col-xs-10">
                <table id="example" class="table table-striped dt-responsive" cellspacing="0" width="100%">
                    <thead>
                        <tr>
                            <th>Nombre de la ruta</th>
                            <th>Descripción</th>
                            <th>Distancia</th>
                            <th>Dificultad</th>
                            <th>Mostrar</th>
                            <c:if test="${sessionScope.permiso}">
                                <th>Editar</th>
                                <th>Borrar</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th>Nombre de la ruta</th>
                            <th>Descripción</th>
                            <th>Distancia</th>
                            <th>Dificultad</th>
                            <th>Mostrar</th>
                            <c:if test="${sessionScope.permiso}">
                                <th>Editar</th>
                                <th>Borrar</th>
                            </c:if>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        <div class="row">
            </br>   
        </div>
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/dataTables/jquery.dataTables.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/dataTables/dataTables.responsive.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/dataTables/dataTables.bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/dataTables/responsive.bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/dataTables/dataTables.buttons.min.js" charset="utf-8"></script>
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {
               var tabla =  $('#example')
                    .addClass('nowrap')
                    .DataTable({
                        responsive: true,
                       // "dom": "<'row'<'col-sm-6'l><'col-sm-6'f>>"+"<'row'<'col-sm-12'tr>>"+"<'row'<'col-sm-5'i><'col-sm-7'p>>",
                        "scrollX": true,
                        "processing": true,
                        "serverSide": true,
                        "ajax": {
                            "url": "rutas",
                            "type": "POST"
                        },
                        "deferRender": true,
                        "columns": [
                            {"data": "Nombre de la ruta"}
                            ,{"data": "Descripcion"}
                            ,{"data": "Distancia"}
                            ,{"data": "Dificultad"}
                            ,{"data": "Mostrar"}
                            <c:if test="${sessionScope.permiso}"> 
                                ,{"data": "Editar"}
                                ,{"data": "Borrar"}
                            </c:if>
                        ],
                        "language": {
                            "url": "js/Spanish.json"
                        }
                        <c:if test="${sessionScope.permiso}">
                            ,dom: 'lBfrtip'
                            ,buttons: [
                                {
                                    text: "Añadir ruta",
                                    action: function() {window.location.href = "SubirRuta";}
                                }
                            ]    
                       </c:if>
                    });
            });
	</script>
        <c:if test="${sessionScope.usuario ne null}">
            <script type="text/javascript" src="js/menuUsuario.js" charset="utf-8"></script>
        </c:if>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
        
    </body>
</html>