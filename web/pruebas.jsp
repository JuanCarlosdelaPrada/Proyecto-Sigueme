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
        <title>Pruebas</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
    </head>
    <body>
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
              
        <div class="page-header">
            <div class="col-xs-offset-1">
                <h1>Pruebas</h1> 
            </div>
        </div>
        
        <div class="container-fluid">   <!--table-bordered-->
            <div class="col-xs-offset-1 col-xs-10">
                <table id="example" class="table table-striped dt-responsive" cellspacing="0" width="100%">
                    <thead>
                        <tr>
                            <th>Nombre de la prueba</th>
                            <th>Descripción</th>
                            <th>Ruta</th>
                            <th>Lugar</th>
                            <th>Fecha celebración</th>
                            <th>Hora celebración</th>
                            <th>Fecha apertura inscripción</th>
                            <th>Fecha límite inscripción</th>
                            <th>Nº Máximo inscritos</th>
                            <c:if test="${empty sessionScope.permiso or !sessionScope.permiso}">
                                <th>Inscribirse</th>
                            </c:if>
                            <th>Más información</th>
                            <c:if test="${sessionScope.permiso}">
                                <th>Editar</th>
                                <th>Borrar</th>
                                <th>Ver inscripciones</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tfoot>
                         <tr>
                            <th>Nombre de la prueba</th>
                            <th>Descripción</th>
                            <th>Ruta</th>
                            <th>Lugar</th>
                            <th>Fecha celebración</th>
                            <th>Hora celebración</th>
                            <th>Fecha apertura inscripción</th>
                            <th>Fecha límite inscripción</th>
                            <th>Nº Máximo inscritos</th>
                            <c:if test="${empty sessionScope.permiso or !sessionScope.permiso}">
                                <th>Inscribirse</th>
                            </c:if>
                            <th>Más información</th>
                            <c:if test="${sessionScope.permiso}">
                                <th>Editar</th>
                                <th>Borrar</th>
                                <th>Ver inscripciones</th>
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
                        "dom": "<'row'<'col-sm-6'l><'col-sm-6'f>>"+"<'row'<'col-sm-12'tr>>"+"<'row'<'col-sm-5'i><'col-sm-7'p>>",
                        "processing": true,
                        "serverSide": true,
                        "ajax": {
                            "url": "pruebas",
                            "type": "POST"
                        },
                        "deferRender": true,
                        "columns": [
                            {"data": "Nombre de la prueba"}
                            ,{"data": "Descripcion"}
                            ,{"data": "Ruta"}
                            ,{"data": "Lugar"}
                            ,{"data": "Fecha celebracion"}
                            ,{"data": "Hora celebracion"}
                            ,{"data": "Fecha apertura inscripcion"}
                            ,{"data": "Fecha limite inscripcion"}
                            ,{"data": "Nº Maximo inscritos"}
                             <c:if test="${empty sessionScope.permiso or !sessionScope.permiso}">
                                ,{"data": "Inscribirse"}
                            </c:if>
                            ,{"data": "Mas informacion"}
                            <c:if test="${sessionScope.permiso}"> 
                                ,{"data": "Editar"}
                                ,{"data": "Borrar"}
                                ,{"data": "Ver inscripciones"}
                            </c:if>
                        ],
                        "language": {
                            "url": "js/Spanish.json"
                        }
                        <c:if test="${sessionScope.permiso}">
                            ,dom: 'lBfrtip'
                            ,buttons: [
                                {
                                    text: "Añadir prueba",
                                    action: function() {window.location.href = "crearPrueba.jsp";}
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