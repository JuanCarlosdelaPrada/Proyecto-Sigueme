package Controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Herramientas.AES;
import Herramientas.DistanciaDeHaversine;
import Herramientas.ParserGPX;
import JPA_Entidades.Inscrito;
import JPA_Entidades.InscritoPK;
import JPA_Entidades.Ivbytes;
import JPA_Entidades.Posicion;
import JPA_Entidades.Prueba;
import JPA_Entidades.Ruta;
import JPA_Entidades.Usuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Je¡ZZ¡
 */
@WebServlet(urlPatterns = {
    "",
    "/login",
    "/logout",
    "/Rutas",
    "/rutas", 
    "/ruta",
    "/SubirRuta",
    "/subirRuta",
    "/comprobarRutaId",
    "/editar-ruta",
    "/editarRuta",
    "/eliminar-ruta",
    "/Pruebas",
    "/pruebas", 
    "/prueba", 
    "/CrearPrueba",
    "/crearPrueba",
    "/comprobarPruebaId",
    "/editar-prueba",
    "/editarPrueba",
    "/eliminar-prueba",
    "/SeguimientoPruebas",
    "/seguimientoPruebas", 
    "/seguir-prueba",
    "/seguimiento_prueba",
    "/Usuarios",
    "/usuarios",
    "/usuario", 
    "/CrearUsuario",
    "/crearUsuario",
    "/comprobarUsuarioId",
    "/comprobarUsuarioDNI",
    "/editar-usuario",
    "/editarUsuario",
    "/eliminar-usuario",
    "/inscripciones",
    "/inscripciones_",
    "/inscribirse",
    "/editar-inscripcion",
    "/editarInscripcion",
    "/eliminar-inscripcion"
})
@MultipartConfig

public class ControladorAdministracion extends HttpServlet {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    @Resource(name = "myDatasource")
    private DataSource myDatasource;


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session;
        String accion;
        String vista;
        
        request.setCharacterEncoding("UTF-8");
        
        accion = request.getServletPath();
        session = request.getSession();
        
        TypedQuery<Posicion> consultaPosiciones;
        TypedQuery<Inscrito> consultaInscritos;
        TypedQuery<Usuario> consultaUsuarios;
        TypedQuery<Ivbytes> consultaIvBytes;
        TypedQuery<Prueba> consultaPruebas;
        TypedQuery<Ruta> consultaRutas;
        Enumeration<String> parametros;
        SimpleDateFormat formato;
        List<Usuario> usuarios;
        List<Inscrito> inscritos;
        List<Prueba> pruebas;
        List<String> navegacion;
        JsonObject resultado;
        ParserGPX parseador;
        Inscrito inscrito;
        PrintWriter out;
        JsonArray array;
        Ivbytes ivbytes;
        Usuario usuario = null;
        Prueba prueba = null;
        Ruta ruta;
                
        byte[] ivBytes;
        
        int i,
            j,
            cantidad,
            comienzo,
            idraw,
            num_atributos,
            total;
            
        Boolean permiso, 
                errores;
        
        String usuario_id,
               prueba_id,
               ruta_id,
               descripcion, 
               lugar,
               fecha_cel,
               hora_cel,
               fecha_inscrip_min,
               fecha_inscrip_max,
               maximo_inscritos,
               dificultad,
               tabla,
               dir,
               sStart, 
               sAmount, 
               draw, 
               sCol, 
               sdir,
               nombreAtributo,
               contrasena,
               _contrasena,
               nombre,
               apellidos,
               dni,
               direccion,
               fecha_nacimiento,
               telefono,
               sexo,
               club,
               federado,
               miUsuario,
               mensajeError;
        
        String[] enlaces,
                 enlaces2;
        
        Part ficheroGPX;
        
        File destino,
             archivo;
        
        Date fecha_cel_tratada = null,
             fecha_inscrip_min_tratada = null,
             fecha_inscrip_max_tratada = null,
             hora_cel_tratada = null;
        
        OutputStream salida = null;
        
        InputStream contenidoDelFichero = null;
        
        vista = "";
        switch(accion) {
            case "":
                List<Prueba> pruebasRecientes = new ArrayList<>();
                pruebas = em.createNamedQuery("Prueba.orderingByNumero", Prueba.class).getResultList();
                i = 0;
                while ( i < pruebas.size() && i < 3) {
                    pruebasRecientes.add(pruebas.get(i));
                    i++;
                }
                request.setAttribute("pruebasRecientes", pruebasRecientes);
                
                vista = "inicio.jsp";
                break;
            case "/login":
                String correo_login = request.getParameter("correo_login");
                String contrasena_login = request.getParameter("contrasena_login");

                usuario = em.find(Usuario.class, correo_login);

                if (usuario != null) {
                    ivbytes= usuario.getIvbytes();

                    AES.setIvBytes(ivbytes.getIvbytesId());
                    System.out.println(AES.decrypt(new String(usuario.getContrasena())));
                    if (contrasena_login.equals(AES.decrypt(new String(usuario.getContrasena())))) {
                        session.setAttribute("correo", usuario.getUsuarioId());
                        session.setAttribute("usuario", usuario.getNombre());
                        session.setAttribute("permiso", usuario.getRol());
                        System.out.println(usuario.getNombre());
                        System.out.println(usuario.getRol());
                        request.setAttribute("Cabecera", "Bienvenid@ "+ usuario.getNombre());
                        request.setAttribute("Cuerpo", "Esperamos que disfrute de su experiencia con nuestro portal.");
                    }
                    else {
                        request.setAttribute("Cabecera", "ERROR");
                        request.setAttribute("Cuerpo", "Lo sentimos, dicha contraseña es incorrecta.");
                    }
                }
                else {
                    request.setAttribute("Cabecera", "ERROR");
                    request.setAttribute("Cuerpo", "Lo sentimos dicho usuario no existe, compruebe que los datos introducidos sean correctos.");
                }
                vista = "";
                break;
            case "/logout":
                usuario_id = (String) session.getAttribute("correo");
                session.setAttribute("correo", null);
                session.setAttribute("usuario", null);
                session.setAttribute("permiso", null);
                request.setAttribute("Cabecera", "Hasta pronto " + usuario_id);
                request.setAttribute("Cuerpo", "Esperamos que el portal haya sido de su agrado.");
                vista = "";
                break;
            case "/Rutas":
                navegacion = new ArrayList<>();
                navegacion.add("Rutas");
                navegacion.add("Rutas");
                session.setAttribute("navegacion", navegacion);
                
                vista = "rutas.jsp";
                break;
            case "/rutas":
                String[] columnas = {"Nombre de la ruta", "Descripcion", "Distancia", "Dificultad", "Mostrar", "Editar", "Borrar"};
                String[] atributos = {"ruta_id", "descripcion", "distancia", "dificultad"};
                tabla = "ruta";
                resultado = new JsonObject();
                array = new JsonArray();
                cantidad = 10;
                comienzo = 0;
                idraw = 0;
                num_atributos = 0;
                
                dir = "asc";
                sStart = request.getParameter("start");
                sAmount = request.getParameter("length"); 
                draw = request.getParameter("draw");
                sCol = request.getParameter("order[0][column]"); 
                sdir = request.getParameter("order[0][dir]"); 
                
                /*
                String sdistancia = request.getParameter("columns[2][search][value]"),
                       fichero_gpx = request.getParameter("columns[4][search][value]"),
                       lat_min = request.getParameter("columns[5][search][value]"),
                       lat_max = request.getParameter("columns[6][search][value]"),
                       long_min = request.getParameter("columns[7][search][value]"),
                       long_max = request.getParameter("columns[8][search][value]");
                ruta_id = request.getParameter("columns[0][search][value]");
                descripcion = request.getParameter("columns[1][search][value]");
                dificultad = request.getParameter("columns[3][search][value]");
                
                List<String> sArray = new ArrayList<>();
                if (!"".equals(ruta_id)) {
                    sArray.add(" ruta_id like '%" + ruta_id + "%'");
                }
                if (!"".equals(descripcion)) {
                    sArray.add(" descripcion like '%" + descripcion + "%'");
                }
                if (!"".equals(sdistancia)) {
                    sArray.add(" distancia like '%" + sdistancia + "%'");
                }
                if (!"".equals(dificultad)) {
                    sArray.add(" dificultad like '%" + dificultad + "%'");
                }
                if (!"".equals(fichero_gpx)) {
                    sArray.add(" fichero_gpx like '%" + fichero_gpx + "%'");
                }
                if (!"".equals(lat_min)) {
                    sArray.add(" lat_min like '%" + lat_min + "%'");
                }
                if (!"".equals(lat_max)) {
                    sArray.add(" lat_max like '%" + lat_max + "%'");
                }
                if (!"".equals(long_min)) {
                    sArray.add(" long_min like '%" + long_min + "%'");
                }
                if (!"".equals(long_max)) {
                    sArray.add(" long_max like '%" + long_max + "%'");
                }
                
                String individualSearch = "";
                if(sArray.size()==1){
                    individualSearch = sArray.get(0);
                }else if(sArray.size() > 1){
                    for(int i = 0; i < sArray.size() - 1; i++){
                        individualSearch += sArray.get(i) + " and ";
                    }
                    individualSearch += sArray.get(sArray.size()-1);
                }
                */
                if (sStart != null) {
                    comienzo = Integer.parseInt(sStart);
                    if (comienzo < 0)
                        comienzo = 0;
                }
                if (sAmount != null) {
                    cantidad = Integer.parseInt(sAmount);
                    if (cantidad < 10 || cantidad > 100)
                        cantidad = 10;
                }
                if (draw != null) {
                    idraw = Integer.parseInt(draw);
                }
                if (sCol != null) {
                    num_atributos = Integer.parseInt(sCol);
                    if (num_atributos < 0 || num_atributos > (atributos.length - 1))
                        num_atributos = 0; 
                }
                if (sdir != null) {
                    if (!sdir.equals("asc"))
                        dir = "desc";
                }
                nombreAtributo = atributos[num_atributos];
                total = 0;
                
                try (Connection conn = myDatasource.getConnection()) {
                    String sql = "SELECT count(*) FROM " + tabla;
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        total = rs.getInt("count(*)");
                    }
                    int totalAfterFilter = total;
                    String searchSQL = "",
                            searchTerm = request.getParameter("search[value]"), 
                            globeSearch = " where (ruta_id like '%" + searchTerm +"%')";
                            /*+ " or descripcion like '%" + searchTerm +"%'"
                            + " or distancia like '%" + searchTerm +"%'"
                            + " or dificultad like '%" + searchTerm +"%'"
                            + " or fichero_gpx like '%" + searchTerm +"%'"
                            + " or lat_min like '%" + searchTerm +"%'"
                            + " or lat_max like '%" + searchTerm +"%'"
                            + " or long_min like '%" + searchTerm +"%'"
                            + " or long_max like '%" + searchTerm + "%')";*/
                    sql = "SELECT * FROM " + tabla;
                    
                    /*if(!"".equals(searchTerm) && !"".equals(individualSearch)){
                        searchSQL = globeSearch + " and " + individualSearch;
                    }
                    else if(!"".equals(individualSearch)){
                        searchSQL = " where " + individualSearch;
                    }else if(!"".equals(searchTerm)){
                        searchSQL = globeSearch;
                    }*/
                    
                    if(!"".equals(searchTerm)){
                        searchSQL = globeSearch;
                    }
                    sql += searchSQL;
                    sql += " order by " + nombreAtributo + " " + dir;
                    sql += " limit " + comienzo + ", " + cantidad;
                    System.out.println(sql);
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (i = 0; i < atributos.length; i++) {
                            if (! atributos[i].equals("distancia")) {
                                ja.add(columnas[i], new JsonPrimitive(rs.getString(atributos[i])));   
                            }
                            else {
                                ja.add(columnas[i], new JsonPrimitive(rs.getString(atributos[i]).replace(".", ",")+" km"));
                            }
                        }
                        ja.add(columnas[atributos.length], new JsonPrimitive("<a href='ruta?"+atributos[0]+"="+ja.get(columnas[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        
                        permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                        if (permiso == true) {
                            ja.add(columnas[atributos.length + 1], new JsonPrimitive("<a href='editar-ruta?"+atributos[0]+"="+ja.get(columnas[0]).getAsString()+"'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnas[atributos.length + 2], new JsonPrimitive("<a href='eliminar-ruta?"+atributos[0]+"="+ja.get(columnas[0]).getAsString()+"'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>")); 
                        }
                        array.add(ja);
                    }
                    
                    String sql2 = "SELECT count(*) FROM " + tabla;
                    if (searchTerm != null) {
                        sql2 += searchSQL;
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            totalAfterFilter = rs2.getInt("count(*)");
                        }
                    }
                    resultado.add("draw", new JsonPrimitive(idraw));
                    resultado.add("recordsTotal", new JsonPrimitive(total));
                    resultado.add("recordsFiltered", new JsonPrimitive(totalAfterFilter));
                    resultado.add("data", array);
                    System.out.println(resultado);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    out = response.getWriter();
                    out.print(resultado);
                    out.flush();
                    conn.close();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "/ruta":
                ruta_id = request.getParameter("ruta_id");
                
                ruta = em.find(Ruta.class, ruta_id);
                if (ruta != null) {
                    File ficheroGpx = new File(ruta.getFicheroGpx());
                    parseador = new ParserGPX(ficheroGpx);
                    JsonArray latlng = parseador.gpxToJson();
                    request.setAttribute("ruta", ruta);
                    request.setAttribute("distancia", ruta.getDistancia().toString().replace(".", ","));
                    request.setAttribute("latlng", latlng);
                }
                
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                request.setAttribute("navegacion", breadcrumb(navegacion, "ruta?ruta_id=" + ruta_id, "Ruta"));
                
                vista = "ruta.jsp";
                break;
            case "/CrearUsuario":
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "CrearUsuario", "Crear usuario"));
                
                vista = "crearUsuario.jsp";
                break;
            case "/crearUsuario":
                permiso = session.getAttribute("permiso") == null? false: (Boolean) session.getAttribute("permiso");
                if (session.getAttribute("correo") == null || permiso) {
                    usuario_id = request.getParameter("correo");
                    contrasena = request.getParameter("fstPassword");
                    _contrasena = request.getParameter("sndPassword");
                    nombre = request.getParameter("nombre");
                    apellidos = request.getParameter("apellidos");
                    dni = request.getParameter("dni");
                    direccion = request.getParameter("direccion");
                    fecha_nacimiento = request.getParameter("fecha_nacimiento");
                    telefono = request.getParameter("telefono");
                    sexo = request.getParameter("sexo");
                    club = request.getParameter("club");
                    federado = request.getParameter("federado");
                    
                    formato = new SimpleDateFormat("yyyy-MM-dd");

                    //Encriptamos la contraseña con AES-256
                    String contrasena_tratada = AES.encrypt(contrasena);
                    System.out.println("contraseña "+contrasena_tratada);
                    ivBytes = AES.getIvBytes();
                    Date fecha_nacimiento_tratada = null;
                    try {
                        fecha_nacimiento_tratada = formato.parse(fecha_nacimiento);
                    } catch (ParseException ex) {
                        Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    boolean federado_tratado = federado.equals("s");
                    String _club = club.equals("")? null : club;
                    
                    errores = false;
                    mensajeError = "";
                    if (em.find(Usuario.class, usuario_id) != null) {
                        mensajeError += "- Dicho correo electrónico ya se encuentra vinculado a una cuenta.";
                        errores = true;
                    }
                    if (!telefono.matches("\\d{9}")) {
                        if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- El número de teléfono tiene que respetar el formato y tener 9 dígitos.";
                        errores = true;
                    }
                    if (!contrasena.equals(_contrasena)) {
                         if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- Las contraseñas introducidas no coinciden.";
                        errores = true;
                    }
                    if (!usuario_id.matches("^[a-zA-Z0-9_\\.\\-]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-\\.]+$")) {
                         if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- El correo dado no tiene formato de correo electrónico.";
                        errores = true;
                    }
                    if (!em.createNamedQuery("Usuario.findByDni", Usuario.class).setParameter("dni", dni).getResultList().isEmpty()) {
                         if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- Dicho DNI ya se encuentra asociado a una cuenta.";
                        errores = true;
                    }
                    if (!dni.matches("\\d{8}\\D")) {
                         if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- El DNI no cumple con el formato deseado, es decir, 8 dígitos y una letra.";
                        errores = true;
                    }
                    if (!errores) {
                        usuario = new Usuario(usuario_id, contrasena_tratada.getBytes(), false, nombre, apellidos, dni, direccion, fecha_nacimiento_tratada, telefono, sexo, federado_tratado);
                        usuario.setClub(_club);
                        ivbytes = new Ivbytes(usuario_id);
                        ivbytes.setIvbytesId(ivBytes);
                        if (em.find(JPA_Entidades.Usuario.class, usuario_id) == null) {
                            persist(usuario);
                            persist(ivbytes);
                        }
                        if (permiso) {
                            request.setAttribute("mensajeCreacion", "El usuario ha sido <i>creado</i> satisfactoriamente.");
                            vista = "Usuarios";
                        }
                        else {
                            request.setAttribute("Cabecera", "¡Enhorabuena!");
                            request.setAttribute("Cuerpo", "Su usuario ha sido creado satisfactoriamente.");
                            vista = "";
                        }
                    }
                    else {
                        request.setAttribute("correo", usuario_id);
                        request.setAttribute("contrasena", contrasena);
                        request.setAttribute("valida_contrasena", _contrasena);
                        request.setAttribute("nombre", nombre);
                        request.setAttribute("apellidos", apellidos);
                        request.setAttribute("dni", dni);
                        request.setAttribute("direccion", direccion);
                        request.setAttribute("fecha_nacimiento", fecha_nacimiento);
                        request.setAttribute("telefono", telefono);
                        request.setAttribute("sexo", sexo);
                        request.setAttribute("club", club);
                        request.setAttribute("federado", federado);
                        request.setAttribute("mensajeError", mensajeError);
                        vista = "CrearUsuario";
                    }
                }
                else {
                    System.out.println("NO TIENE SUFICIENTES PERMISOS");
                    vista = "";
                }
                break;
            case "/comprobarUsuarioId":
                usuario_id = request.getParameter("usuario_id");
                resultado = new JsonObject();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Cache-Control", "no-store");
                out = response.getWriter();
                if (em.find(Usuario.class, usuario_id) != null) {
                    resultado.add("mensajeUsuarioId", new JsonPrimitive("Dicho correo electrónico ya esta siendo usado."));
                }
                else {
                    resultado.add("mensajeUsuarioId", new JsonPrimitive(""));
                }
                out.print(resultado);
                out.flush();
                return;
            case "/comprobarUsuarioDNI":
                dni = request.getParameter("dni");
                resultado = new JsonObject();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Cache-Control", "no-store");
                out = response.getWriter();
                if (!em.createNamedQuery("Usuario.findByDni", Usuario.class).setParameter("dni", dni).getResultList().isEmpty()) {
                    resultado.add("mensajeUsuarioDNI", new JsonPrimitive("Dicho DNI ya esta siendo usado."));
                }
                else {
                    resultado.add("mensajeUsuarioDNI", new JsonPrimitive(""));
                }
                out.print(resultado);
                out.flush();
                return;
            case "/editarUsuario":
                usuario_id = request.getParameter("correo");
                usuario = em.find(Usuario.class, usuario_id);
                
                contrasena = request.getParameter("newPassword");
                _contrasena = request.getParameter("validatePassword");
                nombre = request.getParameter("nombre");
                apellidos = request.getParameter("apellidos");
                direccion = request.getParameter("direccion");
                fecha_nacimiento = request.getParameter("fecha_nacimiento");
                telefono = request.getParameter("telefono");
                sexo = request.getParameter("sexo");
                club = request.getParameter("club");
                federado = request.getParameter("federado");
                
                    
                formato = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha_nacimiento_tratada = null;
                try {
                    fecha_nacimiento_tratada = formato.parse(fecha_nacimiento);
                } catch (ParseException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                boolean federado_tratado = federado.equals("s");

                String _club = club.equals("")? null : club;
                
                errores = false;
                mensajeError = "";
                if (!telefono.matches("\\d{9}")) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- El número de teléfono tiene que respetar el formato y tener 9 dígitos.";
                    errores = true;
                }
                if (!contrasena.equals(_contrasena)) {
                     if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- Las contraseñas introducidas no coinciden.";
                    errores = true;
                }
                if (!errores) {
                    if(!contrasena.equals("")) {
                        //Encriptamos la contraseña con AES-256
                        String contrasena_tratada = AES.encrypt(contrasena);
                        ivBytes = AES.getIvBytes();
                        usuario.setContrasena(contrasena_tratada.getBytes());
                        ivbytes = usuario.getIvbytes();
                        ivbytes.setIvbytesId(ivBytes);
                        merge(ivbytes);
                    }
                    usuario.setNombre(nombre);
                    usuario.setApellidos(apellidos);
                    usuario.setDireccion(direccion);
                    usuario.setFechaNacimiento(fecha_nacimiento_tratada);
                    usuario.setTelefono(telefono);
                    usuario.setSexo(sexo);
                    usuario.setClub(_club);
                    usuario.setFederado(federado_tratado);
                    merge(usuario);

                    permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");

                    if (permiso && !session.getAttribute("correo").equals(usuario_id)) {
                        request.setAttribute("mensajeCreacion", "El usuario ha sido <i>editado</i> satisfactoriamente.");
                        vista = "Usuarios";
                    }
                    else {
                        System.out.println("CAMBIO REALIZADO CON EXITO");
                        request.setAttribute("Cabecera", "Información");
                        request.setAttribute("Cuerpo", "Sus cambios se han realizado con éxito.");
                        vista = "";
                    }
                }
                else {
                    request.setAttribute("contrasena", contrasena);
                    request.setAttribute("contrasena_validada", _contrasena);
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("apellidos", apellidos);
                    request.setAttribute("direccion", direccion);
                    request.setAttribute("fecha_nacimiento", fecha_nacimiento);
                    request.setAttribute("telefono", telefono);
                    request.setAttribute("sexo", sexo);
                    request.setAttribute("club", club);
                    request.setAttribute("federado", federado);
                    request.setAttribute("mensajeError", mensajeError);
                    vista = "editar-usuario?usuario_id=" + usuario_id;
                }
                break;
            case "/SubirRuta":
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "SubirRuta", "Subir ruta"));
                vista = "subirRuta.jsp";
                break;
            case "/subirRuta": 
                //Recojo los datos del formulario
                ruta_id  = request.getParameter("track_id");
                descripcion = request.getParameter("descripcion");
                dificultad = request.getParameter("dificultad");
                ficheroGPX = request.getPart("ficheroGPX");
                destino = new File("C:\\Users\\Je¡ZZ¡\\Documents\\NetBeansProjects\\Sigueme\\web\\ficherosGPX");
                archivo = new File(destino + File.separator + ruta_id + ".gpx");
                
                errores = false;
                mensajeError = "";
                if (em.find(Ruta.class, ruta_id) != null) {
                    mensajeError = "- Ya existe una ruta con dicho nombre.";
                    errores = true;
                }
                if (!ficheroGPX.getSubmittedFileName().endsWith(".gpx")) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- El fichero subido no tiene la extensión apropiada. Utilice un fichero con extensión <i>.gpx</i>.";
                    errores = true;
                }
                if ((destino + File.separator + ruta_id + ".gpx").length() > 120) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La longitud del nombre del fichero es demasiado larga o bien el nombre de la ruta es demasiado largo y el archivo a generar por el servidor no puede ser creado.";
                    errores = true;
                }
                
                if (!errores) {
                    try {                   
                        salida = new FileOutputStream(archivo);
                        contenidoDelFichero = ficheroGPX.getInputStream();

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while((read = contenidoDelFichero.read(bytes)) != -1) {
                            salida.write(bytes, 0, read);
                        }
                    } catch (FileNotFoundException fne) {
                    } finally {
                        if (salida != null) {
                            salida.close();
                        }
                        if (contenidoDelFichero != null) {
                            contenidoDelFichero.close();
                        }
                    }         

                    parseador = new ParserGPX(archivo);
                    Vector<Double> latitudes = parseador.getLatitudes();
                    Vector<Double> longitudes = parseador.getLongitudes();
                    double minlatitud = parseador.getMinlat();
                    double minlongitud = parseador.getMinlon();
                    double maxlatitud = parseador.getMaxlat();
                    double maxlongitud = parseador.getMaxlon();

                    double distancia = DistanciaDeHaversine.getDistancia(latitudes, longitudes);

                    System.out.println((destino + File.separator + ruta_id + ".gpx").length());

                    //Creo la nueva ruta.
                    ruta = new Ruta(ruta_id, dificultad, new BigDecimal(distancia), destino + File.separator + ruta_id + ".gpx",
                           new BigDecimal(minlatitud), new BigDecimal(maxlatitud), new BigDecimal(minlongitud), new BigDecimal(maxlongitud));
                    ruta.setDescripcion(descripcion);
                    if(em.find(JPA_Entidades.Ruta.class, ruta_id) == null) {
                        persist(ruta);
                    }
                    request.setAttribute("mensajeCreacion", "La ruta ha sido <i>creada</i> satisfactoriamente.");
                    vista = "Rutas";
                }
                else {
                    request.setAttribute("track_id", ruta_id);
                    request.setAttribute("descripcion", descripcion);
                    request.setAttribute("dificultad", dificultad);
                    request.setAttribute("mensajeError", mensajeError);
                    vista = "SubirRuta";
                }
                break;
            case "/comprobarRutaId":
                ruta_id = request.getParameter("ruta_id");
                resultado = new JsonObject();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Cache-Control", "no-store");
                out = response.getWriter();
                if (em.find(Ruta.class, ruta_id) != null) {
                    resultado.add("mensajeRutaId", new JsonPrimitive("Dicha ruta ya existe."));
                }
                else {
                    resultado.add("mensajeRutaId", new JsonPrimitive(""));
                }
                out.print(resultado);
                out.flush();
                return;
            case "/CrearPrueba":
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "CrearPrueba", "Crear prueba"));
                
                vista = "crearPrueba.jsp";
                break;
            case "/crearPrueba":
                prueba_id = request.getParameter("prueba_id");
                ruta_id = request.getParameter("ruta_id");
                descripcion = request.getParameter("descripcion");
                lugar = request.getParameter("lugar");
                fecha_cel = request.getParameter("fecha_cel");
                hora_cel = request.getParameter("hora_cel");
                fecha_inscrip_min = request.getParameter("fecha_inscrip_min");
                fecha_inscrip_max = request.getParameter("fecha_inscrip_max");
                maximo_inscritos = request.getParameter("maximo_inscritos");
                formato = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    fecha_cel_tratada = formato.parse(fecha_cel);
                    fecha_inscrip_min_tratada = formato.parse(fecha_inscrip_min);
                    fecha_inscrip_max_tratada = formato.parse(fecha_inscrip_max);
                    formato = new SimpleDateFormat("hh:mm");
                    hora_cel_tratada = formato.parse(hora_cel);
                } catch (ParseException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                int maximo_inscritos_tratada = Integer.parseInt(maximo_inscritos);
                
                errores = false;
                mensajeError = "";
                if (em.find(Prueba.class, prueba_id) != null) {
                    mensajeError += "- Ya existe una prueba con dicho nombre.";
                    errores = true;
                }
                if (!fecha_inscrip_min_tratada.before(new Date())) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La fecha de apertura de inscripciones debe no haber pasado aún.";
                    errores = true;
                }
                if (fecha_inscrip_min_tratada.after(fecha_inscrip_max_tratada)) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La fecha de apertura de inscripciones debe ser menor o igual que la fecha de cierre del plazo de inscripciones.";
                    errores = true;
                }
                if (fecha_inscrip_max_tratada.after(fecha_cel_tratada) || fecha_inscrip_max_tratada.equals(fecha_cel_tratada)) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La fecha de celebración de la prueba debe ser mayor que la fecha de cierre del plazo de inscripciones.";
                    errores = true;
                }
                if (!errores) {
                    prueba = new Prueba(prueba_id, lugar, fecha_cel_tratada, hora_cel_tratada, fecha_inscrip_min_tratada, fecha_inscrip_max_tratada, maximo_inscritos_tratada, false, em.createNamedQuery("Prueba.findAll", Prueba.class).getResultList().size());
                    ruta = em.find(JPA_Entidades.Ruta.class, ruta_id);
                    prueba.setRutaId(ruta);
                    prueba.setDescripcion(descripcion);
                    if (em.find(JPA_Entidades.Prueba.class, prueba_id) == null) {
                        persist(prueba);
                    }
                    /*
                    try (Connection conn = myDatasource.getConnection()) {
                        PreparedStatement stm = conn.prepareStatement("CREATE EVENT " + prueba_id + " ON SCHEDULE AT ? DO UPDATE seguimiento_trayectoria.prueba SET activa = 1 WHERE prueba_id = ?");
                        stm.setString(1, fecha_cel + " " + hora_cel);
                        stm.setString(2, prueba_id);
                        System.out.println(stm.toString());
                        stm.execute();
                    } catch (SQLException ex) {
                        Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    */
                    request.setAttribute("mensajeCreacion", "La prueba ha sido <i>creada</i> satisfactoriamente.");
                    vista = "Pruebas";
                }
                else { 
                    request.setAttribute("prueba_id", prueba_id);
                    request.setAttribute("ruta_id", ruta_id);
                    request.setAttribute("descripcion", descripcion);
                    request.setAttribute("lugar", lugar);
                    request.setAttribute("fechaCel", fecha_cel);
                    request.setAttribute("horaCel", hora_cel);
                    request.setAttribute("fechaInscripMin", fecha_inscrip_min);
                    request.setAttribute("fechaInscripMax", fecha_inscrip_max);
                    request.setAttribute("maximoInscritos", maximo_inscritos);
                    request.setAttribute("mensajeError", mensajeError);
                    vista = "CrearPrueba";
                }
                break;
            case "/comprobarPruebaId":
                prueba_id = request.getParameter("prueba_id");
                resultado = new JsonObject();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Cache-Control", "no-store");
                out = response.getWriter();
                if (em.find(Prueba.class, prueba_id) != null) {
                    resultado.add("mensajePruebaId", new JsonPrimitive("Dicha prueba ya existe."));
                }
                else {
                    resultado.add("mensajePruebaId", new JsonPrimitive(""));
                }
                out.print(resultado);
                out.flush();
                return;
            case "/editarPrueba":
                prueba_id = request.getParameter("prueba_id");
                prueba = em.find(Prueba.class, prueba_id);
                
                ruta_id = request.getParameter("ruta_id");
                ruta = em.find(JPA_Entidades.Ruta.class, ruta_id);
                
                descripcion = request.getParameter("descripcion");
                lugar = request.getParameter("lugar");
                
                fecha_cel = request.getParameter("fecha_cel");
                hora_cel = request.getParameter("hora_cel");
                fecha_inscrip_min = request.getParameter("fecha_inscrip_min");
                fecha_inscrip_max = request.getParameter("fecha_inscrip_max");
                formato = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    fecha_cel_tratada = formato.parse(fecha_cel);
                    fecha_inscrip_min_tratada = formato.parse(fecha_inscrip_min);
                    fecha_inscrip_max_tratada = formato.parse(fecha_inscrip_max);
                    formato = new SimpleDateFormat("hh:mm");
                    hora_cel_tratada = formato.parse(hora_cel);
                } catch (ParseException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                maximo_inscritos = request.getParameter("maximo_inscritos");
                int maximoInscritos = Integer.parseInt(maximo_inscritos);
                
                boolean activa = request.getParameter("activa").equals("s");
                
                errores = false;
                mensajeError = "";
                if (!fecha_inscrip_min_tratada.before(new Date())) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La fecha de apertura de inscripciones debe no haber pasado aún.";
                    errores = true;
                }
                if (fecha_inscrip_min_tratada.after(fecha_inscrip_max_tratada)) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La fecha de apertura de inscripciones debe ser menor o igual que la fecha de cierre del plazo de inscripciones.";
                    errores = true;
                }
                if (fecha_inscrip_max_tratada.after(fecha_cel_tratada) || fecha_inscrip_max_tratada.equals(fecha_cel_tratada)) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- La fecha de celebración de la prueba debe ser mayor que la fecha de cierre del plazo de inscripciones.";
                    errores = true;
                }
                
                if (maximoInscritos < em.createNamedQuery("Inscrito.findByPruebaId", Inscrito.class).setParameter("pruebaId", prueba_id).getResultList().size()) {
                    if (errores) {
                        mensajeError += "</br>";
                    }
                    mensajeError += "- El número de usuarios inscritos es mayor al número máximo de inscripciones admitidas.";
                    errores = true;
                }
                if (!errores) {
                    prueba.setRutaId(ruta);
                    prueba.setDescripcion(descripcion);
                    prueba.setLugar(lugar);
                    prueba.setFechaCel(fecha_cel_tratada);
                    prueba.setFechaInscripMin(fecha_inscrip_min_tratada);
                    prueba.setFechaInscripMax(fecha_inscrip_max_tratada);
                    prueba.setHoraCel(hora_cel_tratada);
                    prueba.setMaximoInscritos(maximoInscritos);
                    prueba.setActiva(activa);
                    merge(prueba);
                    request.setAttribute("mensajeCreacion", "La prueba ha sido <i>editada</i> satisfactoriamente.");
                    vista = "Pruebas";
                }
                else {
                    request.setAttribute("descripcion", descripcion);
                    request.setAttribute("ruta_id", ruta_id);
                    request.setAttribute("lugar", lugar);
                    request.setAttribute("fecha_cel", fecha_cel);
                    request.setAttribute("hora_cel", hora_cel);
                    request.setAttribute("fecha_inscrip_min", fecha_inscrip_min);
                    request.setAttribute("fecha_inscrip_max", fecha_inscrip_max);
                    request.setAttribute("maximoInscritos", maximo_inscritos);
                    request.setAttribute("activa", activa);
                    request.setAttribute("mensajeError", mensajeError);
                    vista = "editar-prueba?prueba_id=" + prueba_id;
                }
                break;
            case "/editar-ruta":
                ruta_id = request.getParameter("ruta_id");
                ruta = em.find(Ruta.class, ruta_id);
                permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                if (ruta != null && permiso) {
                    request.setAttribute("ruta", ruta);
                }
                
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "editar-ruta?ruta_id=" + ruta_id, "Editar ruta"));
                
                vista = "editarRuta.jsp";
                break;
            case "/editarRuta":
                ruta_id = request.getParameter("ruta_id");
                ruta = em.find(Ruta.class, ruta_id);
                
                descripcion = request.getParameter("descripcion");
                dificultad = request.getParameter("dificultad");
                ficheroGPX = request.getPart("ficheroGPX");
                
                destino = new File("C:\\Users\\Je¡ZZ¡\\Documents\\NetBeansProjects\\Sigueme\\web\\ficherosGPX");
                archivo = new File(destino + File.separator + ruta_id + ".gpx");
                    
                errores = false;
                mensajeError = "";
                System.out.println("MI FICHERO "+ ficheroGPX);
                if (!ficheroGPX.getSubmittedFileName().equals("")) {
                    if (!ficheroGPX.getSubmittedFileName().endsWith(".gpx")) {
                        if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- El fichero subido no tiene la extensión apropiada. Utilice un fichero con extensión <i>.gpx</i>.";
                        errores = true;
                    }
                    if ((destino + File.separator + ruta_id + ".gpx").length() > 120) {
                        if (errores) {
                            mensajeError += "</br>";
                        }
                        mensajeError += "- La longitud del nombre del fichero es demasiado larga o bien el nombre de la ruta es demasiado largo y el archivo a generar por el servidor no puede ser creado.";
                        errores = true;
                    }
                }
                if (!errores) {
                    ruta.setDescripcion(descripcion);
                    ruta.setDificultad(dificultad);
                    
                    if (!ficheroGPX.getSubmittedFileName().equals("")) {
                        File ficheroGpx = new File(ruta.getFicheroGpx());

                        ficheroGpx.delete();

                        try {                   
                            salida = new FileOutputStream(archivo);
                            contenidoDelFichero = ficheroGPX.getInputStream();

                            int read = 0;
                            byte[] bytes = new byte[1024];

                            while((read = contenidoDelFichero.read(bytes)) != -1) {
                                salida.write(bytes, 0, read);
                            }
                        } catch (FileNotFoundException fne) {
                        } finally {
                            if (salida != null) {
                                salida.close();
                            }
                            if (contenidoDelFichero != null) {
                                contenidoDelFichero.close();
                            }
                        }
                        parseador = new ParserGPX(archivo);

                        ruta.setLatMin(new BigDecimal(parseador.getMinlat()));
                        ruta.setLongMin(new BigDecimal(parseador.getMinlon()));
                        ruta.setLatMax(new BigDecimal(parseador.getMaxlat()));
                        ruta.setLongMax(new BigDecimal(parseador.getMaxlon()));
                        ruta.setDistancia(new BigDecimal(DistanciaDeHaversine.getDistancia(parseador.getLatitudes(), parseador.getLongitudes())));
                    }
                    merge(ruta);
                    request.setAttribute("mensajeCreacion", "La ruta ha sido <i>editada</i> satisfactoriamente.");
                    vista = "Rutas";
                }
                else {
                    request.setAttribute("descripcion", descripcion);
                    request.setAttribute("dificultad", dificultad);
                    request.setAttribute("mensajeError", mensajeError);
                    vista = "editar-ruta?ruta_id=" + ruta_id;
                }
                break;
            case "/eliminar-ruta":
                ruta_id = request.getParameter("ruta_id");
                ruta = em.find(Ruta.class, ruta_id);
                if (ruta != null) {
                    consultaPruebas = em.createNamedQuery("Prueba.findByRutaId", Prueba.class);
                    consultaPruebas.setParameter("rutaId", ruta);
                    pruebas = consultaPruebas.getResultList();
                    if (!pruebas.isEmpty()) {
                        for (Prueba pr : pruebas) {
                            consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaId", Inscrito.class);
                            consultaInscritos.setParameter("pruebaId", pr.getPruebaId());
                            inscritos = consultaInscritos.getResultList();
                            if (!inscritos.isEmpty()) {
                                for (Inscrito ins: inscritos) {
                                    consultaPosiciones = em.createNamedQuery("Posicion.findByPruebaId", Posicion.class);
                                    consultaPosiciones.setParameter("pruebaId", ins.getPrueba().getPruebaId());
                                    List<Posicion> posiciones = consultaPosiciones.getResultList();
                                    for (Posicion ps : posiciones) {
                                        delete(ps);
                                    }
                                    delete(ins);
                                }
                            }
                            delete(pr);
                        } 
                    }
                    File ficheroGpx = new File(ruta.getFicheroGpx());
                    ficheroGpx.delete();
                    delete(ruta);
                    
                    request.setAttribute("mensajeCreacion", "La ruta ha sido <i>eliminada</i> satisfactoriamente.");
                    vista = "Rutas";
                }
                else {
                    vista = "";
                }
                break;
            case "/Pruebas":
                navegacion = new ArrayList<>();
                navegacion.add("Pruebas");
                navegacion.add("Pruebas");
                session.setAttribute("navegacion", navegacion);
                
                vista = "pruebas.jsp";
                break;
            case "/pruebas":
                String[] columnasP = {"Nombre de la prueba", "Descripcion", "Ruta", "Lugar", "Fecha celebracion", "Hora celebracion", "Fecha apertura inscripcion", "Fecha limite inscripcion", "Nº Maximo inscritos", "Inscribirse", "Mostrar", "Editar", "Borrar", "Ver inscripciones"};
                String[] atributosP = {"prueba_id", "descripcion", "ruta_id", "lugar", "fecha_cel", "hora_cel", "fecha_inscrip_min", "fecha_inscrip_max", "maximo_inscritos"};
                tabla = "prueba";
                resultado = new JsonObject();
                array = new JsonArray();
                cantidad = 10;
                comienzo = 0;
                idraw = 0;
                num_atributos = 0;
                
                dir = "asc";
                sStart = request.getParameter("start"); 
                sAmount = request.getParameter("length"); 
                draw = request.getParameter("draw");
                sCol = request.getParameter("order[0][column]"); 
                sdir = request.getParameter("order[0][dir]"); 
                if (sStart != null) {
                    comienzo = Integer.parseInt(sStart);
                    if (comienzo < 0)
                        comienzo = 0;
                }
                if (sAmount != null) {
                    cantidad = Integer.parseInt(sAmount);
                    if (cantidad < 10 || cantidad > 100)
                        cantidad = 10;
                }
                if (draw != null) {
                    idraw = Integer.parseInt(draw);
                }
                if (sCol != null) {
                    num_atributos = Integer.parseInt(sCol);
                    if (num_atributos < 0 || num_atributos > (atributosP.length - 1))
                        num_atributos = 0;
                }
                if (sdir != null) {
                    if (!sdir.equals("asc"))
                        dir = "desc";
                }
                nombreAtributo = atributosP[num_atributos];
                total = 0;
                try (Connection conn = myDatasource.getConnection()) {
                    String sql = "SELECT count(*) FROM " + tabla;
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        total = rs.getInt("count(*)");
                    }
                    int totalAfterFilter = total;
                    String searchSQL = "",
                            searchTerm = request.getParameter("search[value]"), 
                            globeSearch = " where (prueba_id like '%" + searchTerm +"%')";
                    sql = "SELECT * FROM " + tabla;
                    
                    if(!"".equals(searchTerm)){
                        searchSQL = globeSearch;
                    }
                    sql += searchSQL;
                    sql += " order by " + nombreAtributo + " " + dir;
                    sql += " limit " + comienzo + ", " + cantidad;
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                    Boolean user = session.getAttribute("usuario") != null;
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (i = 0; i < atributosP.length; i++) {
                            if (!(atributosP[i].equals("fecha_cel") || atributosP[i].equals("hora_cel") || atributosP[i].equals("fecha_inscrip_min") || atributosP[i].equals("fecha_inscrip_max"))) {
                                ja.add(columnasP[i], new JsonPrimitive(rs.getString(atributosP[i])));
                            }
                            else if (atributosP[i].equals("fecha_cel") || atributosP[i].equals("fecha_inscrip_min") || atributosP[i].equals("fecha_inscrip_max")) {
                                formato = new SimpleDateFormat("dd/MM/yyyy");
                                ja.add(columnasP[i], new JsonPrimitive(formato.format(rs.getDate(atributosP[i]))));
                            }
                            else if (atributosP[i].equals("hora_cel")){
                                System.out.println(rs.getTime(atributosP[i]).toString());
                                formato = new SimpleDateFormat("HH:mm"); 
                                ja.add(columnasP[i], new JsonPrimitive(formato.format(rs.getTime(atributosP[i])) + " h"));
                            }
                        }
                        if (!permiso) {
                            if (user) {
                                ja.add(columnasP[atributosP.length], new JsonPrimitive("<a href='inscribirse?"+atributosP[0]+"="+ja.get(columnasP[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                            }
                            else {
                                ja.add(columnasP[atributosP.length], new JsonPrimitive("<a href='CrearUsuario'>¡Registrate ya!</a>"));
                            }
                        }
                        ja.add(columnasP[atributosP.length + 1], new JsonPrimitive("<a href='prueba?"+atributosP[0]+"="+ja.get(columnasP[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        
                        if (permiso) {
                            ja.add(columnasP[atributosP.length + 2], new JsonPrimitive("<a href='editar-prueba?"+atributosP[0]+"="+ja.get(columnasP[0]).getAsString()+"'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnasP[atributosP.length + 3], new JsonPrimitive("<a href='eliminar-prueba?"+atributosP[0]+"="+ja.get(columnasP[0]).getAsString()+"'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>")); 
                            ja.add(columnasP[atributosP.length + 4], new JsonPrimitive("<a href='inscripciones?"+atributosP[0]+"="+ja.get(columnasP[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        }
                        array.add(ja);
                    }
                    String sql2 = "SELECT count(*) FROM " + tabla;
                    if (searchTerm != null) {
                        sql2 += searchSQL;
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            totalAfterFilter = rs2.getInt("count(*)");
                        }
                    }
                    resultado.add("draw", new JsonPrimitive(idraw));
                    resultado.add("recordsTotal", new JsonPrimitive(total));
                    resultado.add("recordsFiltered", new JsonPrimitive(totalAfterFilter));
                    resultado.add("data", array);
                    System.out.println(resultado);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    out = response.getWriter();
                    out.print(resultado);
                    out.flush();
                    conn.close();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "/prueba":
                prueba_id = request.getParameter("prueba_id");
                prueba = em.find(Prueba.class, prueba_id);
                if (prueba != null) {
                    request.setAttribute("prueba", prueba);
                    
                    File ficheroGpx = new File(prueba.getRutaId().getFicheroGpx());
                    parseador = new ParserGPX(ficheroGpx);
                    JsonArray latlng = parseador.gpxToJson();
                    request.setAttribute("latlng", latlng);
                    
                    Date fechaActual = new Date();
                    Calendar fechaMaxima = Calendar.getInstance();
                    fechaMaxima.setTime(prueba.getFechaInscripMax());
                    fechaMaxima.add(Calendar.DATE, 1);
                    request.setAttribute("periodoInscripcion",(fechaActual.compareTo(prueba.getFechaInscripMin()) >= 0 && fechaActual.compareTo(fechaMaxima.getTime()) <= 0));
                    
                    consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaId", Inscrito.class);
                    consultaInscritos.setParameter("pruebaId", prueba.getPruebaId());
                    inscritos = consultaInscritos.getResultList();
                    request.setAttribute("maximoinscritos", inscritos.size() == prueba.getMaximoInscritos());
                    
                    formato = new SimpleDateFormat("dd/MM/yyyy");
                    request.setAttribute("fechaCel", formato.format(prueba.getFechaCel()));
                    request.setAttribute("fechaInscripMin", formato.format(prueba.getFechaInscripMin()));
                    request.setAttribute("fechaInscripMax", formato.format(prueba.getFechaInscripMax()));
                    
                    formato = new SimpleDateFormat("HH:mm");
                    request.setAttribute("horaCel", formato.format(prueba.getHoraCel()) + " h");
                }
                
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "prueba?prueba_id=" + prueba_id, "Prueba"));
                
                vista = "prueba.jsp";
                break;
            case "/editar-prueba":
                prueba_id = request.getParameter("prueba_id");
                prueba = em.find(Prueba.class, prueba_id);
                permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                if (prueba != null && permiso) {
                    SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
                    request.setAttribute("fechaCel", formateador.format(prueba.getFechaCel()));
                    request.setAttribute("fechaInscripMin", formateador.format(prueba.getFechaInscripMin()));
                    request.setAttribute("fechaInscripMax", formateador.format(prueba.getFechaInscripMax()));
                    formateador = new SimpleDateFormat("HH:mm");
                    request.setAttribute("horaCel", formateador.format(prueba.getHoraCel()));
                    request.setAttribute("prueba", prueba);
                }
                
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "editar-prueba?prueba_id=" + prueba_id, "Editar prueba"));
                
                vista = "editarPrueba.jsp";
                break;
            case "/eliminar-prueba":
                prueba_id = request.getParameter("prueba_id");
                prueba = em.find(Prueba.class, prueba_id);
                if (prueba != null) {
                    consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaId", Inscrito.class);
                    consultaInscritos.setParameter("pruebaId", prueba.getPruebaId());
                    inscritos = consultaInscritos.getResultList();
                    if (!inscritos.isEmpty()) {
                        for (Inscrito ins: inscritos) {
                            consultaPosiciones = em.createNamedQuery("Posicion.findByPruebaId", Posicion.class);
                            consultaPosiciones.setParameter("pruebaId", ins.getPrueba().getPruebaId());
                            List<Posicion> posiciones = consultaPosiciones.getResultList();
                            for (Posicion ps : posiciones) {
                                delete(ps);
                            }
                            delete(ins);
                        }
                    }
                    delete(prueba);
                    request.setAttribute("mensajeCreacion", "La prueba ha sido <i>eliminada</i> satisfactoriamente.");
                    vista = "Pruebas";
                }
                else {
                    System.out.println("La prueba no existe.");
                    vista = "";
                }
                break;
            case "/Usuarios":
                navegacion = new ArrayList<>();
                navegacion.add("Usuarios");
                navegacion.add("Usuarios");
                session.setAttribute("navegacion", navegacion);
                
                vista = "usuarios.jsp";
                break;
            case "/usuarios":
                String[] columnasU = {"Correo", "Contrasena", "Rol", "Nombre", "Apellidos", "DNI", "Direccion", "Fecha Nacimiento", "Telefono", "Sexo", "Club", "Federado", "Mas informacion", "Editar", "Borrar", "Ver inscripciones"};
                String[] atributosU = {"usuario_id", "contrasena", "rol", "nombre", "apellidos", "dni", "direccion", "fecha_nacimiento", "telefono", "sexo", "club", "federado"};
                tabla = "usuario";
                resultado = new JsonObject();
                array = new JsonArray();
                cantidad = 10;
                comienzo = 0;
                idraw = 0;
                num_atributos = 0;

                dir = "asc";
                sStart = request.getParameter("start");
                sAmount = request.getParameter("length");
                draw = request.getParameter("draw");
                sCol = request.getParameter("order[0][column]");
                sdir = request.getParameter("order[0][dir]");
                if (sStart != null) {
                    comienzo = Integer.parseInt(sStart);
                    if (comienzo < 0) {
                        comienzo = 0;
                    }
                }
                if (sAmount != null) {
                    cantidad = Integer.parseInt(sAmount);
                    if (cantidad < 10 || cantidad > 100) {
                        cantidad = 10;
                    }
                }
                if (draw != null) {
                    idraw = Integer.parseInt(draw);
                }
                if (sCol != null) {
                    num_atributos = Integer.parseInt(sCol);
                    if (num_atributos < 0 || num_atributos > 11) {
                        num_atributos = 0;
                    }
                }
                if (sdir != null) {
                    if (!sdir.equals("asc")) {
                        dir = "desc";
                    }
                }
                nombreAtributo = atributosU[num_atributos];
                total = 0;
                try (Connection conn = myDatasource.getConnection()) {
                    String sql = "SELECT count(*) FROM " + tabla;
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        total = rs.getInt("count(*)");
                    }
                    int totalAfterFilter = total;
                    String searchSQL = "",
                            searchTerm = request.getParameter("search[value]"),
                            globeSearch = " where (usuario_id like '%" + searchTerm + "%')";
                            /*+ " or contrasena like '%" + searchTerm + "%'"
                            + " or rol like '%" + searchTerm + "%'"
                            + " or nombre like '%" + searchTerm + "%'"
                            + " or apellidos like '%" + searchTerm + "%'"
                            + " or dni like '%" + searchTerm + "%'"
                            + " or direccion like '%" + searchTerm + "%'"
                            + " or fecha_nacimiento like '%" + searchTerm + "%'"
                            + " or telefono like '%" + searchTerm + "%'"
                            + " or sexo like '%" + searchTerm + "%'"
                            + " or club like '%" + searchTerm + "%'"
                            + " or federado like '%" + searchTerm + "%')";*/
                    sql = "SELECT * FROM " + tabla;

                    if (!"".equals(searchTerm)) {
                        searchSQL = globeSearch;
                    }
                    sql += searchSQL;
                    sql += " order by " + nombreAtributo + " " + dir;
                    sql += " limit " + comienzo + ", " + cantidad;
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    permiso = session.getAttribute("permiso") == null ? false : (boolean) session.getAttribute("permiso");
                    while (rs.next()) {
                        boolean rol = false;
                        JsonObject ja = new JsonObject();
                        for (i = 0; i < atributosU.length; i++) {
                            String u;
                            if (!columnasU[i].equals("Federado") && !columnasU[i].equals("Rol")) {
                                u = rs.getString(atributosU[i]);
                            } else if (columnasU[i].equals("Federado")) {
                                u = rs.getString(atributosU[i]).equals("1") ? "Sí" : "No";
                            } else {
                                u = rs.getString(atributosU[i]).equals("1") ? "Administrador" : "Usuario";
                                if (u.equals("Administrador")) {
                                    rol = true;
                                }
                            }
                            ja.add(columnasU[i], new JsonPrimitive(u != null ? u : ""));
                        }
                        
                        ja.add(columnasU[atributosU.length], new JsonPrimitive("<a href='usuario?" + atributosU[0] + "=" + ja.get(columnasU[0]).getAsString() + "'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));

                        if (permiso) {
                            ja.add(columnasU[atributosU.length + 1], new JsonPrimitive("<a href='editar-usuario?" + atributosU[0] + "=" + ja.get(columnasU[0]).getAsString() + "'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnasU[atributosU.length + 2], new JsonPrimitive("<a href='eliminar-usuario?" + atributosU[0] + "=" + ja.get(columnasU[0]).getAsString() + "'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>"));
                            if(!rol) {
                                ja.add(columnasU[atributosU.length + 3], new JsonPrimitive("<a href='inscripciones?"+atributosU[0]+"="+ja.get(columnasU[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                            }
                            else {
                                ja.add(columnasU[atributosU.length + 3], new JsonPrimitive("<font color='red'>No disponible para administradores</font>"));
                            }
                        }
                        array.add(ja);
                    }
                    String sql2 = "SELECT count(*) FROM " + tabla;
                    if (searchTerm != null) {
                        sql2 += searchSQL;
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            totalAfterFilter = rs2.getInt("count(*)");
                        }
                    }
                    resultado.add("draw", new JsonPrimitive(idraw));
                    resultado.add("recordsTotal", new JsonPrimitive(total));
                    resultado.add("recordsFiltered", new JsonPrimitive(totalAfterFilter));
                    resultado.add("data", array);
                    System.out.println(resultado);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    out = response.getWriter();
                    out.print(resultado);
                    out.flush();
                    conn.close();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "/usuario":
                usuario_id = request.getParameter("usuario_id");
                usuario = em.find(Usuario.class, usuario_id);
                permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                if (usuario != null && (permiso || session.getAttribute("correo").equals(usuario_id))) {
                    SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
                    request.setAttribute("fechaNacimiento", formateador.format(usuario.getFechaNacimiento()));
                    request.setAttribute("usuario", usuario);
                    
                    navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                    session.setAttribute("navegacion", breadcrumb(navegacion, "usuario?usuario_id=" + usuario_id, "Usuario"));
                
                    vista = "usuario.jsp";
                }
                else {
                    vista = "";
                }
                break;
            case "/editar-usuario":
                usuario_id = request.getParameter("usuario_id");
                usuario = em.find(Usuario.class, usuario_id);
                permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                if (usuario != null && (permiso || session.getAttribute("correo").equals(usuario_id))) {
                    SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
                    request.setAttribute("fechaNacimiento", formateador.format(usuario.getFechaNacimiento()));
                    request.setAttribute("usuario", usuario);
                    
                    navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                    session.setAttribute("navegacion", breadcrumb(navegacion, "editar-usuario?usuario_id=" + usuario_id, "Editar usuario"));
                    
                    vista = "editarUsuario.jsp";
                }
                else {
                    vista = "";
                }
                break;
            case "/eliminar-usuario":
                String sesion_usuario = (String) session.getAttribute("correo");
                Boolean sesion_permiso = session.getAttribute("permiso") == null ? false: (Boolean) session.getAttribute("permiso");
                if (!"".equals(sesion_usuario)) {
                    usuario_id = request.getParameter("usuario_id");
                    usuario = em.find(Usuario.class, usuario_id);
                    if (usuario != null && (sesion_usuario.equals(usuario_id) || sesion_permiso)) {
                        consultaInscritos = em.createNamedQuery("Inscrito.findByUsuarioId", Inscrito.class);
                        consultaInscritos.setParameter("usuarioId", usuario.getUsuarioId());
                        inscritos = consultaInscritos.getResultList();
                        if (!inscritos.isEmpty()) {
                            for (Inscrito ins: inscritos) {
                                consultaPosiciones = em.createNamedQuery("Posicion.findByPruebaId", Posicion.class);
                                consultaPosiciones.setParameter("pruebaId", ins.getPrueba().getPruebaId());
                                List<Posicion> posiciones = consultaPosiciones.getResultList();
                                for (Posicion ps : posiciones) {
                                    delete(ps);
                                }
                                delete(ins);
                            }
                        }
                        consultaIvBytes = em.createNamedQuery("Ivbytes.findByUsuarioId", Ivbytes.class);
                        consultaIvBytes.setParameter("usuarioId", usuario.getUsuarioId());
                        delete(consultaIvBytes.getSingleResult());
                        delete(usuario);
                        if (sesion_usuario.equals(usuario_id)) {
                            vista = "logout";
                        }
                        else {
                            request.setAttribute("mensajeCreacion", "El usuario ha sido <i>eliminado</i> satisfactoriamente.");
                            vista = "Usuarios";
                        }
                    }
                    else if (!sesion_usuario.equals(usuario_id)) {
                        System.out.println("No tiene suficientes permisos para eliminar dicho usuario.");
                        vista = "";
                    }
                    else {
                        System.out.println("El usuario no existe.");
                        vista = "";
                    }
                }
                else {
                    System.out.println("Se ha accedido a una zona restringida");
                    vista = "";
                }
                break;
            case "/SeguimientoPruebas":
                navegacion = new ArrayList<>();
                navegacion.add("SeguimientoPruebas");
                navegacion.add("Seguimiento de pruebas");
                session.setAttribute("navegacion", navegacion);
                
                vista = "seguimientoPruebas.jsp";
                break;
            case "/seguimientoPruebas":
                String[] columnasS = {"Nombre de la prueba", "Ver", "Mas informacion"};
                String[] atributosS = {"prueba_id"};
                tabla = "prueba";
                resultado = new JsonObject();
                array = new JsonArray();
                cantidad = 10;
                comienzo = 0;
                idraw = 0;
                num_atributos = 0;

                dir = "asc";
                sStart = request.getParameter("start");
                sAmount = request.getParameter("length");
                draw = request.getParameter("draw");
                sCol = request.getParameter("order[0][column]");
                sdir = request.getParameter("order[0][dir]");
                if (sStart != null) {
                    comienzo = Integer.parseInt(sStart);
                    if (comienzo < 0) {
                        comienzo = 0;
                    }
                }
                if (sAmount != null) {
                    cantidad = Integer.parseInt(sAmount);
                    if (cantidad < 10 || cantidad > 100) {
                        cantidad = 10;
                    }
                }
                if (draw != null) {
                    idraw = Integer.parseInt(draw);
                }
                if (sCol != null) {
                    num_atributos = Integer.parseInt(sCol);
                    if (num_atributos < 0 || num_atributos > 0) {
                        num_atributos = 0;
                    }
                }
                if (sdir != null) {
                    if (!sdir.equals("asc")) {
                        dir = "desc";
                    }
                }
                nombreAtributo = atributosS[num_atributos];
                total = 0;
                try (Connection conn = myDatasource.getConnection()) {
                    String sql = "SELECT count(*) FROM " + tabla + " WHERE activa = 1";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        total = rs.getInt("count(*)");
                    }
                    int totalAfterFilter = total;
                    String searchSQL = "",
                            searchTerm = request.getParameter("search[value]"),
                            globeSearch = " where (prueba_id like '%" + searchTerm + "%'"
                            + " and activa = 1)";
                    sql = "SELECT * FROM " + tabla;
                    if (!"".equals(searchTerm)) {
                        searchSQL = globeSearch;
                    } else {
                        searchSQL = " WHERE activa = 1";
                    }
                    sql += searchSQL;
                    sql += " order by " + nombreAtributo + " " + dir;
                    sql += " limit " + comienzo + ", " + cantidad;
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    Boolean user = session.getAttribute("usuario") != null;
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (i = 0; i < atributosS.length; i++) {
                            ja.add(columnasS[i], new JsonPrimitive(rs.getString(atributosS[i])));
                        }
                        ja.add(columnasS[atributosS.length], new JsonPrimitive("<a href='seguir-prueba?" + atributosS[0] + "=" + ja.get(columnasS[0]).getAsString() + "'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        ja.add(columnasS[atributosS.length + 1], new JsonPrimitive("<a href='prueba?"+atributosS[0]+"="+ja.get(columnasS[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        array.add(ja);
                    }
                    String sql2 = "SELECT count(*) FROM " + tabla;
                    if (searchTerm != null) {
                        sql2 += searchSQL;
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        System.out.println(sql2);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            totalAfterFilter = rs2.getInt("count(*)");
                        }
                    }
                    resultado.add("draw", new JsonPrimitive(idraw));
                    resultado.add("recordsTotal", new JsonPrimitive(total));
                    resultado.add("recordsFiltered", new JsonPrimitive(totalAfterFilter));
                    resultado.add("data", array);
                    System.out.println(resultado);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    out = response.getWriter();
                    out.print(resultado);
                    out.flush();
                    conn.close();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "/seguir-prueba":
                prueba_id = request.getParameter("prueba_id");
                prueba = em.find(Prueba.class, prueba_id);
                if (prueba != null) {
                    request.setAttribute("prueba", prueba);
                    
                    File ficheroGpx = new File(prueba.getRutaId().getFicheroGpx());
                    parseador = new ParserGPX(ficheroGpx);
                    JsonArray latlng = parseador.gpxToJson();
                    request.setAttribute("latlng", latlng);
                    
                    consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaIdPagadoOrderingByDorsal", Inscrito.class);
                    consultaInscritos.setParameter("pruebaId", prueba.getPruebaId());
                    inscritos = consultaInscritos.getResultList();
                    request.setAttribute("inscritos", inscritos);
                }
                
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                session.setAttribute("navegacion", breadcrumb(navegacion, "seguir-prueba?prueba_id=" + prueba_id, "Seguir prueba"));
                
                vista = "seguirPrueba.jsp";
                break;
            case "/seguimiento_prueba":
                prueba_id = request.getParameter("prueba_id");
                String[] competidores_id = request.getParameterValues("competidores_id[]");
                try (Connection con = myDatasource.getConnection()) {
                    String consulta =   "SELECT i.dorsal, p.* "+
                                        "FROM posicion p INNER JOIN inscrito i "+
                                            "ON (p.usuario_id = i.usuario_id and p.prueba_id = i.prueba_id) "+
                                        "WHERE ("+
                                            "p.prueba_id = ? "+
                                            "AND "+
                                            "hora BETWEEN DATE_SUB(CURTIME(), INTERVAL 10 SECOND) "+
                                            "AND "+
                                            "CURTIME()";
                    if (competidores_id != null) {
                        consulta += " AND (";
                        for (i = 0; i < competidores_id.length; i++) {
                            if (competidores_id.length > 1 && i < (competidores_id.length - 1)) {
                                consulta += "p.usuario_id = '" + competidores_id[i] + "' OR ";
                            }
                            else {
                                consulta += "p.usuario_id = '" + competidores_id[i] + "')";
                            }
                        }
                    }
                    consulta += ") "+
                                "ORDER BY hora DESC;";
                    PreparedStatement ps = con.prepareStatement(consulta);
                    ps.setString(1, prueba_id);
                    ResultSet posiciones = ps.executeQuery();
                    Set<Integer> dorsales = new HashSet<>();
                    Element markers = new Element("markers");
                    Document doc = new Document(markers);
                    doc.setRootElement(markers);
                    
                    String[] attributes = {"dorsal", "usuario_id", "latitud", "longitud"};
                    while(posiciones.next()) {
                        int dorsal = posiciones.getInt("dorsal");
                        if(dorsales.add(dorsal)) {
                            System.out.println(dorsal);
                            Element marker = new Element("marker");
                            for (String attribute: attributes) {
                                marker.setAttribute(new Attribute(attribute, posiciones.getString(attribute)));
                            }
                            doc.getRootElement().addContent(marker);
                        }
                    }
                    // new XMLOutputter().output(doc, System.out);
                    XMLOutputter xmlOutput = new XMLOutputter();

                    // display nice nice
                    xmlOutput.setFormat(Format.getPrettyFormat());

                    response.setContentType("text/xml");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-type", "application/xhtml+xml");
                    out = response.getWriter();
                    xmlOutput.output(doc, out);
                    out.flush();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                
                
                /*para otros elementos y añadir texto sería:
                    Element staff2 = new Element("staff");
                    staff2.setAttribute(new Attribute("id", "2"));
                    staff2.addContent(new Element("firstname").setText("low"));
                    staff2.addContent(new Element("lastname").setText("yin fong"));
                    staff2.addContent(new Element("nickname").setText("fong fong"));
                    staff2.addContent(new Element("salary").setText("188888"));
                */
                
                /*
                System.out.println(resultado);
                    
                    
                    out.print(resultado);
                    
                */
                
                return;
            case "/inscribirse":
                prueba_id = request.getParameter("prueba_id");
                prueba = em.find(Prueba.class, prueba_id);
                consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaIdOrderingByDorsal", Inscrito.class);
                consultaInscritos.setParameter("pruebaId", prueba_id);
                inscritos = consultaInscritos.getResultList();
                InscritoPK inscritoPK = new InscritoPK(prueba_id, session.getAttribute("correo").toString());
                if (prueba != null && em.find(Inscrito.class, inscritoPK) == null) {
                    Date fechaActual = new Date();
                    Calendar fechaMaxima = Calendar.getInstance();
                    fechaMaxima.setTime(prueba.getFechaInscripMax());
                    fechaMaxima.add(Calendar.DATE, 1);
                    if ((fechaActual.compareTo(prueba.getFechaInscripMin()) >= 0 && fechaActual.compareTo(fechaMaxima.getTime()) <= 0) && inscritos.size() < prueba.getMaximoInscritos()) {
                        if (inscritos.isEmpty()) {
                            inscrito = new Inscrito(inscritoPK, false, 1);
                        }
                        else {
                            i = 0;
                            boolean hueco = false;
                            while (!hueco && i < inscritos.size()) {
                                if ((i + 1) != inscritos.get(i).getDorsal())
                                    hueco = true;
                                i++;
                            }
                            if (!hueco) {
                                inscrito = new Inscrito(new InscritoPK(prueba_id, (String) session.getAttribute("correo")), false, inscritos.size() + 1);
                            }
                            else {
                                inscrito = new Inscrito(new InscritoPK(prueba_id, (String) session.getAttribute("correo")), false, i);
                            }
                        }
                        persist(inscrito);
                        
                        request.setAttribute("Cabecera", "¡Enhorabuena!");
                        request.setAttribute("Cuerpo", "Se acaba de inscribir en " + prueba_id + ".");
                    }
                    else if (!(fechaActual.compareTo(prueba.getFechaInscripMin()) >= 0 && fechaActual.compareTo(prueba.getFechaInscripMax()) <= 0)) {
                        System.out.println("TIENE QUE CUMPLIR EL PLAZO DE INSCRIPCIONES");
                        
                        request.setAttribute("Cabecera", "Error");
                        request.setAttribute("Cuerpo", "Tiene que cumplir el plazo de inscripciones.");
                    }
                    else {
                        System.out.println("YA ESTÁ EL CUPO DE INSCRITOS CUBIERTO");
                        
                        request.setAttribute("Cabecera", "Error");
                        request.setAttribute("Cuerpo", "El cupo de inscritos ya se encuentra cubierto.");
                    }
                }
                else if (prueba == null) {
                    System.out.println("DICHA PRUEBA NO EXISTE");

                    request.setAttribute("Cabecera", "Información");
                    request.setAttribute("Cuerpo", "Dicha prueba no existe.");
                }
                else {
                    request.setAttribute("Cabecera", "Error");
                    request.setAttribute("Cuerpo", "Ya se encuentra inscrito en dicha prueba.");
                    System.out.println("YA ESTÁ INSCRITO");
                }
                break;
            case "/inscripciones":
                prueba_id = request.getParameter("prueba_id");
                usuario_id = request.getParameter("usuario_id");
                request.setAttribute("prueba_id", prueba_id);
                request.setAttribute("usuario_id", usuario_id);
                
                navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                if (prueba_id == null) {
                    session.setAttribute("navegacion", breadcrumb(navegacion, "inscripciones?usuario_id=" + usuario_id, "Inscripciones"));
                }
                else {
                    session.setAttribute("navegacion", breadcrumb(navegacion, "inscripciones?prueba_id=" + prueba_id, "Inscripciones"));
                }
                
                vista = "inscripciones.jsp";
                break;
            case "/editarInscripcion":
                prueba_id = request.getParameter("prueba_id");
                usuario_id = request.getParameter("usuario_id");
                inscrito = em.find(Inscrito.class, new InscritoPK(prueba_id, usuario_id));
                
                int dorsal = Integer.parseInt(request.getParameter("dorsal"));
                boolean pagado = request.getParameter("pagado").equals("s");
                if (inscrito.getDorsal() == dorsal || em.createNamedQuery("Inscrito.findByDorsal", Inscrito.class).setParameter("dorsal", dorsal).getResultList().isEmpty()) {
                    inscrito.setDorsal(dorsal);
                    inscrito.setPagado(pagado);
                    merge(inscrito);
                    String identificador = request.getParameterNames().nextElement().equals("usuarioId")? "usuario_id": "prueba_id";
                    request.setAttribute(identificador, request.getParameter(request.getParameterNames().nextElement()));
                    request.setAttribute("mensajeCreacion", "La inscripción ha sido <i>editada</i> satisfactoriamente.");
                    vista = "inscripciones";
                }
                else {
                    request.setAttribute("Cabecera", "Error");
                    request.setAttribute("Cuerpo", "Ya hay un usuario con dicho dorsal.");
                    vista = "";
                }
                break;
            case "/editar-inscripcion":
                prueba_id = request.getParameter("prueba_id");
                usuario_id = request.getParameter("usuario_id");
                parametros = request.getParameterNames();
                if (parametros.hasMoreElements() && !"".equals(prueba_id) && !"".equals(usuario_id))
                {
                    String identificador = parametros.nextElement();
                    consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaIdAndUsuarioId", Inscrito.class);
                    consultaInscritos.setParameter("usuarioId", usuario_id);
                    consultaInscritos.setParameter("pruebaId", prueba_id);
                    inscrito = consultaInscritos.getSingleResult();
                    if (inscrito != null) {
                        request.setAttribute("inscrito", inscrito);
                        request.setAttribute(identificador, request.getParameter(identificador));
                        
                        navegacion = (ArrayList<String>) session.getAttribute("navegacion");
                        if ("usuario_id".equals(identificador)) {
                            session.setAttribute("navegacion", breadcrumb(navegacion, "editar-inscripcion?usuario_id=" + usuario_id + "&amp;prueba_id=" + prueba_id, "Editar inscripción"));
                        }
                        else {
                            session.setAttribute("navegacion", breadcrumb(navegacion, "editar-inscripcion?prueba_id=" + prueba_id + "&amp;usuario_id=" + usuario_id, "Editar inscripción"));
                        }
                        
                        vista = "editarInscripcion.jsp";
                    }
                    else {
                        System.out.println("Se produjo un error dicha inscripción no existe");
                        vista = "";
                    }
                }
                else {
                    vista = "";
                }
                break;
            case "/eliminar-inscripcion":
                prueba_id = request.getParameter("prueba_id");
                usuario_id = request.getParameter("usuario_id");
                parametros = request.getParameterNames();
                if (parametros.hasMoreElements() && !"".equals(prueba_id) && !"".equals(usuario_id))
                {
                    String identificador = parametros.nextElement();
                    prueba = em.find(Prueba.class, prueba_id);
                    usuario = em.find(Usuario.class, usuario_id);

                    if (prueba != null && usuario != null) {
                        consultaInscritos = em.createNamedQuery("Inscrito.findByPruebaIdAndUsuarioId", Inscrito.class);
                        consultaInscritos.setParameter("usuarioId", usuario.getUsuarioId());
                        consultaInscritos.setParameter("pruebaId", prueba.getPruebaId());
                        inscrito = consultaInscritos.getSingleResult();
                        if (inscrito != null) {
                            consultaPosiciones = em.createNamedQuery("Posicion.findByPruebaIdAndUsuarioId", Posicion.class);
                            consultaPosiciones.setParameter("usuarioId", usuario.getUsuarioId());
                            consultaPosiciones.setParameter("pruebaId", prueba.getPruebaId());
                            List<Posicion> posiciones = consultaPosiciones.getResultList();
                            for (Posicion ps : posiciones) {
                                delete(ps);
                            }
                            delete(inscrito);
                        }
                        else {
                            System.out.println("Se produjo un error dicha inscripción no existe");
                        }
                    }
                    else {
                        System.out.println("Se produjo un error, dicha prueba o dicho usuario no existen.");
                    }
                    request.setAttribute(identificador, request.getParameter(identificador));
                    request.setAttribute("mensajeCreacion", "La inscripción ha sido <i>eliminada</i> satisfactoriamente.");
                    vista = "inscripciones";
                }
                else {
                    vista = "";
                }
                break;
            case "/inscripciones_":
                prueba_id = request.getParameter("prueba_id");
                usuario_id = request.getParameter("usuario_id");
                List<String> columnasI;
                List<String> atributosI;
                String campo,
                       criterio;
                if (!"".equals(prueba_id)) {
                    columnasI = Arrays.asList(new String[]{"Correo", "Dorsal", "Pagado", "Editar", "Borrar"});
                    atributosI = Arrays.asList(new String[]{"usuario_id", "dorsal", "pagado"});
                    criterio = "prueba_id";
                    campo = prueba_id;
                }
                else {
                    columnasI = Arrays.asList(new String[]{"Nombre de la prueba", "Dorsal", "Pagado", "Editar", "Borrar"});
                    atributosI = Arrays.asList(new String[]{"prueba_id", "dorsal", "pagado"});
                    criterio = "usuario_id";
                    campo = usuario_id;
                }
                tabla = "inscrito";
                resultado = new JsonObject();
                array = new JsonArray();
                cantidad = 10;
                comienzo = 0;
                idraw = 0;
                num_atributos = 0;

                dir = "asc";
                sStart = request.getParameter("start");
                sAmount = request.getParameter("length");
                draw = request.getParameter("draw");
                sCol = request.getParameter("order[0][column]");
                sdir = request.getParameter("order[0][dir]");
                if (sStart != null) {
                    comienzo = Integer.parseInt(sStart);
                    if (comienzo < 0) {
                        comienzo = 0;
                    }
                }
                if (sAmount != null) {
                    cantidad = Integer.parseInt(sAmount);
                    if (cantidad < 10 || cantidad > 100) {
                        cantidad = 10;
                    }
                }
                if (draw != null) {
                    idraw = Integer.parseInt(draw);
                }
                if (sCol != null) {
                    num_atributos = Integer.parseInt(sCol);;
                    if (num_atributos < 0 || num_atributos > 2) {
                        num_atributos = 0;
                    }
                }
                if (sdir != null) {
                    if (!sdir.equals("asc")) {
                        dir = "desc";
                    }
                }
                nombreAtributo = atributosI.get(num_atributos);
                total = 0;
                try (Connection conn = myDatasource.getConnection()) {
                    String sql = "SELECT count(*) FROM " + tabla + " WHERE " + criterio + " = '" + campo + "'";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        total = rs.getInt("count(*)");
                    }
                    int totalAfterFilter = total;
                    String searchSQL = "",
                            searchTerm = request.getParameter("search[value]"),
                            globeSearch = " WHERE (" + atributosI.get(0) + " like '%" + searchTerm + "%'"
                            + " and " + criterio + " = '" + campo + "')";
                    sql = "SELECT * FROM " + tabla;
                    if (!"".equals(searchTerm)) {
                        searchSQL = globeSearch;
                    } else {
                        searchSQL = " WHERE " + criterio + " = '" + campo + "'";
                    }
                    sql += searchSQL;
                    sql += " order by " + nombreAtributo + " " + dir;
                    sql += " limit " + comienzo + ", " + cantidad;
                    System.out.println(sql);
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    permiso = session.getAttribute("permiso") == null ? false : (boolean) session.getAttribute("permiso");
                    Boolean user = session.getAttribute("usuario") != null;
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (i = 0; i < atributosI.size(); i++) {
                            if (!"Pagado".equals(columnasI.get(i))) {
                                ja.add(columnasI.get(i), new JsonPrimitive(rs.getString(atributosI.get(i))));
                            }
                            else {
                                ja.add(columnasI.get(i), new JsonPrimitive(rs.getString(atributosI.get(i)).equals("0")? "No": "Sí"));
                            }
                        }
                        if (permiso) {
                            ja.add(columnasI.get(atributosI.size()), new JsonPrimitive("<a href='editar-inscripcion?" + criterio + "=" + campo + "&amp;" + atributosI.get(0) + "=" + ja.get(columnasI.get(0)).getAsString() + "'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnasI.get(atributosI.size() + 1), new JsonPrimitive("<a href='eliminar-inscripcion?" + criterio + "=" + campo + "&amp;" + atributosI.get(0) + "=" + ja.get(columnasI.get(0)).getAsString() + "'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>"));
                        }                        
                        array.add(ja);
                    }
                    String sql2 = "SELECT count(*) FROM " + tabla;
                    if (searchTerm != null) {
                        sql2 += searchSQL;
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            totalAfterFilter = rs2.getInt("count(*)");
                        }
                    }
                    resultado.add("draw", new JsonPrimitive(idraw));
                    resultado.add("recordsTotal", new JsonPrimitive(total));
                    resultado.add("recordsFiltered", new JsonPrimitive(totalAfterFilter));
                    resultado.add("data", array);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    out = response.getWriter();
                    out.print(resultado);
                    out.flush();
                    conn.close();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                vista = "";
                break;
        };
        RequestDispatcher rd = request.getRequestDispatcher(vista);
        rd.forward(request, response);
    }
    
    public List<String> breadcrumb(List<String> navegacion, String ruta, String nombre) {
        if (!navegacion.contains(ruta)) {
            navegacion.add(ruta);
            navegacion.add(nombre);
        }
        else {
            int i = navegacion.lastIndexOf(ruta);
            for (int j = navegacion.size(); j > (i + 2); j--) {
                navegacion.remove(j - 1);
            }
        }
        return navegacion;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public void merge(Object object) {
        try {
            utx.begin();
            em.merge(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public void delete(Object object) {
        try {
            utx.begin();
            em.remove(em.merge(object));
            utx.commit();
        }
        catch (Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}