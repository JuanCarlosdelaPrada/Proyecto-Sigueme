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
        <title>Usuarios</title>
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
    </head>
    <body>
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
              
        <ul class="breadcrumb" style="margin-bottom:0; background: #FFF7C8; border-radius:0">
            <li class="active">Usuarios</li>
        </ul>
        
        <div class="page-header">
            <div class="col-xs-offset-1">
                <h1>Usuarios</h1> 
            </div>
        </div>
        
        <div class="container-fluid">   <!--table-bordered-->
            <div class="col-xs-offset-1 col-xs-10">
                <table id="example" class="table table-striped dt-responsive" cellspacing="0" width="100%">
                    <thead>
                        <tr>
                            <th>Correo</th>
                            <th>Contraseña</th>
                            <th>Rol</th>
                            <th>Nombre</th>
                            <th>Apellidos</th>
                            <th>DNI</th>
                            <th>Dirección</th>
                            <th>Fecha Nacimiento</th>
                            <th>Teléfono</th>
                            <th>Sexo</th>
                            <th>Club</th>
                            <th>Federado</th>
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
                            <th>Correo</th>
                            <th>Contraseña</th>
                            <th>Rol</th>
                            <th>Nombre</th>
                            <th>Apellidos</th>
                            <th>DNI</th>
                            <th>Dirección</th>
                            <th>Fecha Nacimiento</th>
                            <th>Teléfono</th>
                            <th>Sexo</th>
                            <th>Club</th>
                            <th>Federado</th>
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
                            "url": "usuarios",
                            "type": "POST"
                        },
                        "deferRender": true,
                        "columns": [
                            {"data": "Correo"}
                            ,{"data": "Contrasena"}
                            ,{"data": "Rol"}
                            ,{"data": "Nombre"}
                            ,{"data": "Apellidos"}
                            ,{"data": "DNI"}
                            ,{"data": "Direccion"}
                            ,{"data": "Fecha Nacimiento"}
                            ,{"data": "Telefono"}
                            ,{"data": "Sexo"}
                            ,{"data": "Club"}
                            ,{"data": "Federado"}
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
                                    text: "Añadir usuario",
                                    action: function() {window.location.href = "crearUsuario.jsp";}
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