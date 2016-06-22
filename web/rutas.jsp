<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <title>Inicio</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!--Importaciones .css-->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <link href="css/jquery.dataTables.min.css" rel="stylesheet">
        <link href="css/cabecera.css" rel="stylesheet">
        <link href="css/login.css" rel="stylesheet">
    </head>
    <body>
        <!--Cabecera-->       
        <%@include file="WEB-INF/jspf/cabecera.jspf"%>

        <!--Menú-->
        <%@include file="WEB-INF/jspf/menu.jspf"%>
              
        <div class="page-header">
            <div class="col-sm-offset-1">
                <h1>Rutas</h1> 
            </div>
        </div>
        <div class="table-responsive">
            <table id="example" class="table nowrap" width="100%">
                <thead>
                    <tr>
                        <th>Nombre de la ruta</th>
                        <th>Descripcion</th>
                        <th>Distancia</th>
                        <th>Dificultad</th>
                        <th>ficheroGPX</th>
                        <th>minlatitud</th>
                        <th>minlongitud</th>
                        <th>maxlatitud</th>
                        <th>maxlongitud</th>
                    </tr>
                </thead>
                <tfoot>
                    <tr>
                        <th>Nombre de la ruta</th>
                        <th>Descripcion</th>
                        <th>Distancia</th>
                        <th>Dificultad</th>
                        <th>ficheroGPX</th>
                        <th>minlatitud</th>
                        <th>minlongitud</th>
                        <th>maxlatitud</th>
                        <th>maxlongitud</th>
                    </tr>
                </tfoot>
            </table>
        </div>
        
        <!--Pie-->
        <%@include file="WEB-INF/jspf/pie.jspf"%>

        <!--Importaciones .js-->
        <script type="text/javascript" src="js/jQuery/jquery-1.12.3.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/Bootstrap/bootstrap.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/dataTables/jquery.dataTables.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/Stacktable/stacktable.min.js" charset="utf-8"></script>
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {
                $('#example').DataTable({
                    "processing": true,
                    "serverSide": true,
                    "ajax": {
                        "url": "rutas",
                        "type": "POST"
                    },
                    "deferRender": true,
                    "columns": [
                        {"data": "Nombre de la ruta"},
                        {"data": "Descripcion"},
                        {"data": "Distancia"},
                        {"data": "Dificultad"},
                        {"data": "ficheroGPX"},
                        {"data": "minlatitud"},
                        {"data": "minlongitud"},
                        {"data": "maxlatitud"},
                        {"data": "maxlongitud"}
                    ],
                    "language": {
                        "url": "js/Spanish.json"
                    }
                });
                $('#example').stackable();
            });
	</script>
        <script type="text/javascript" src="js/validarLogin.js" charset="utf-8"></script>
        
    </body>
</html>