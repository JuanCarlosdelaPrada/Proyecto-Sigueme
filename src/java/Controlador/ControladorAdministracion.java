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
import java.io.FileWriter;
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
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
    "/crearUsuario",
    "/login",
    "/logout",
    "/subirRuta",
    "/crearPrueba",
    "/rutas", 
    "/ruta",
    "/pruebas", 
    "/prueba", //--
    "/seguimientoPruebas", 
    "/seguir-prueba",
    "/seguimiento_prueba",
    "/usuarios",
    "/usuario", //--
    "/inscribirse",
    "/inscripciones",
    "/inscripciones_"
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
        
        TypedQuery<Inscrito> consultaInscritos;
        TypedQuery<Usuario> consultaUsuarios;
        TypedQuery<Ivbytes> consultaIvBytes;
        SimpleDateFormat formato;
        JsonObject resultado;
        ParserGPX parseador;
        Inscrito inscrito;
        PrintWriter out;
        JsonArray array;
        Ivbytes ivbytes;
        Usuario usuario;
        Prueba prueba;
        Ruta ruta;
                
        byte[] ivBytes;
        
        int cantidad,
            comienzo,
            idraw,
            num_atributos,
            total;
        
        String prueba_id,
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
               nombreAtributo;
        
        vista = "";
        switch(accion) {
            case "/login":
                try {
                    String correo_login = request.getParameter("correo_login");
                    String contrasena_login = request.getParameter("contrasena_login");
                    
                    consultaUsuarios = em.createNamedQuery("Usuario.findByUsuarioId", Usuario.class);
                    consultaUsuarios.setParameter("usuarioId", correo_login);
                    
                    usuario = consultaUsuarios.getSingleResult();
                    
                    consultaIvBytes = em.createNamedQuery("Ivbytes.findByUsuarioId", Ivbytes.class);
                    consultaIvBytes.setParameter("usuarioId", correo_login);
                    
                    ivbytes = consultaIvBytes.getSingleResult();
                    AES.setIvBytes(ivbytes.getIvbytesId());
                    if (contrasena_login.equals(AES.decrypt(new String(usuario.getContrasena())))) {
                        session.setAttribute("correo", usuario.getUsuarioId());
                        session.setAttribute("usuario", usuario.getNombre());
                        session.setAttribute("permiso", usuario.getRol());
                        System.out.println(usuario.getNombre());
                        System.out.println(usuario.getRol());
                    }
                    else {
                         System.out.println( "<p class='incorrecto'>Contraseña incorrecta, inténtelo de nuevo.</p>");
                        // request.setAttribute("msg1", msg);
                    }
                }
                catch(Exception ex) {
                    System.out.println("<p class='incorrecto'>Dicho usuario no existe en nuestro sistema.</p>");
                }
                vista= "inicio.jsp";
                break;
            case "/logout":
                session.setAttribute("correo", null);
                session.setAttribute("usuario", null);
                session.setAttribute("permiso", null);
                session.invalidate();
                vista = "inicio.jsp";
                break;
            case "/crearUsuario":
                String usuario_id = request.getParameter("correo"),
                       contrasena = request.getParameter("fstPassword"),
                       _contrasena = request.getParameter("sndPassword"),
                       nombre = request.getParameter("nombre"),
                       apellidos = request.getParameter("apellidos"),
                       dni = request.getParameter("dni"),
                       direccion = request.getParameter("direccion"),
                       fecha_nacimiento = request.getParameter("fecha_nacimiento"),
                       telefono = request.getParameter("telefono"),
                       sexo = request.getParameter("sexo"),
                       club = request.getParameter("club"),
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
                usuario = new Usuario(usuario_id, contrasena_tratada.getBytes(), false, nombre, apellidos, dni, direccion, fecha_nacimiento_tratada, telefono, sexo, federado_tratado);
                usuario.setClub(_club);
                ivbytes = new Ivbytes(usuario_id);
                ivbytes.setIvbytesId(ivBytes);
                if(em.find(JPA_Entidades.Usuario.class, usuario_id) == null) {
                    persist(usuario);
                    persist(ivbytes);
                }
                vista = "inicio.jsp";
                break;
            case "/subirRuta": 
                //Recojo los datos del formulario
                ruta_id  = request.getParameter("track_id");
                descripcion = request.getParameter("descripcion");
                dificultad = request.getParameter("dificultad");
                Part ficheroGPX = request.getPart("ficheroGPX");
                
                //Subo el fichero ".gpx" de la ruta a una carpeta
                OutputStream salida = null;
                InputStream contenidoDelFichero = null;
                PrintWriter writer = response.getWriter();
                File destino = new File("C:\\Users\\Je¡ZZ¡\\Documents\\NetBeansProjects\\Sigueme\\ficherosGPX");
                File archivo = new File(destino + File.separator + ruta_id + ".gpx");
                try {                   
                    salida = new FileOutputStream(archivo);
                    contenidoDelFichero = ficheroGPX.getInputStream();

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while((read = contenidoDelFichero.read(bytes)) != -1) {
                        salida.write(bytes, 0, read);
                    }
                    writer.println(ruta_id + ".gpx creado");
                } catch (FileNotFoundException fne) {
                    writer.println("Puede que no especificases el fichero a subir o que "
                            + "estes intentando subir un archivo a una localizacion protegida "
                            + "o inexistente.");
                    writer.println("<br> ERROR: " + fne.getMessage());
                } finally {
                    if (salida != null) {
                        salida.close();
                    }
                    if (contenidoDelFichero != null) {
                        contenidoDelFichero.close();
                    }
                    if (writer != null) {
                        writer.close();
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
                
                //Creo la nueva ruta.
                ruta = new Ruta(ruta_id, dificultad, new BigDecimal(distancia), destino + File.separator + ruta_id + ".gpx",
                       new BigDecimal(minlatitud), new BigDecimal(maxlatitud), new BigDecimal(minlongitud), new BigDecimal(maxlongitud));
                ruta.setDescripcion(descripcion);
                if(em.find(JPA_Entidades.Ruta.class, ruta_id) == null) {
                    persist(ruta);
                }
                vista = "inicio.jsp";
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
                Date fecha_cel_tratada = null,
                     fecha_inscrip_min_tratada = null,
                     fecha_inscrip_max_tratada = null,
                     hora_cel_tratada = null;
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
                prueba = new Prueba(prueba_id, lugar, fecha_cel_tratada, hora_cel_tratada, fecha_inscrip_min_tratada, fecha_inscrip_max_tratada, maximo_inscritos_tratada, false);
                ruta = em.find(JPA_Entidades.Ruta.class, ruta_id);
                prueba.setRutaId(ruta);
                prueba.setDescripcion(descripcion);
                if (em.find(JPA_Entidades.Prueba.class, prueba_id) == null) {
                    persist(prueba);
                }
                try (Connection conn = myDatasource.getConnection()) {
                    PreparedStatement stm = conn.prepareStatement("CREATE EVENT " + prueba_id + " ON SCHEDULE AT ? DO UPDATE seguimiento_trayectoria.prueba SET activa = 1 WHERE prueba_id = ?");
                    stm.setString(1, fecha_cel + " " + hora_cel);
                    stm.setString(2, prueba_id);
                    System.out.println(stm.toString());
                    stm.execute();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                vista = "inicio.jsp";
                break;
            case "/rutas":
                String[] columnas = {"Nombre de la ruta", "Descripcion", "Distancia", "Dificultad", "ficheroGPX", "minlatitud", "minlongitud", "maxlatitud", "maxlongitud", "Mas informacion", "Editar", "Borrar"};
                String[] atributos = {"ruta_id", "descripcion", "distancia", "dificultad", "fichero_gpx", "lat_min", "lat_max", "long_min", "long_max"};
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
                    if (num_atributos < 0 || num_atributos > 8)
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
                        for (int i = 0; i < atributos.length; i++) {
                            ja.add(columnas[i], new JsonPrimitive(rs.getString(atributos[i])));                            
                        }
                        ja.add(columnas[atributos.length], new JsonPrimitive("<a href='ruta?"+atributos[0]+"="+ja.get(columnas[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        
                        Boolean permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
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
                    request.setAttribute("latlng", latlng);
                }
                vista = "ruta.jsp";
                break;
            case "/pruebas":
                String[] columnasP = {"Nombre de la prueba", "Descripcion", "Ruta", "Lugar", "Fecha celebracion", "Hora celebracion", "Fecha apertura inscripcion", "Fecha limite inscripcion", "Nº Maximo inscritos", "Inscribirse", "Mas informacion", "Editar", "Borrar", "Ver inscripciones"};
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
                    if (num_atributos < 0 || num_atributos > 8)
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
                    Boolean permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso"),
                            user = session.getAttribute("usuario") != null;
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (int i = 0; i < atributosP.length; i++) {
                            ja.add(columnasP[i], new JsonPrimitive(rs.getString(atributosP[i])));
                        }
                        if (!permiso) {
                            if (user) {
                                ja.add(columnasP[atributosP.length], new JsonPrimitive("<a href='inscribirse?"+atributosP[0]+"="+ja.get(columnasP[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                            }
                            else {
                                ja.add(columnasP[atributosP.length], new JsonPrimitive("<a href='crearUsuario.jsp'>Registrate ya!</a>"));
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
                    Boolean permiso = session.getAttribute("permiso") == null ? false : (boolean) session.getAttribute("permiso");
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (int i = 0; i < atributosU.length; i++) {
                            String u;
                            if (!columnasU[i].equals("Federado") && !columnasU[i].equals("Rol")) {
                                u = rs.getString(atributosU[i]);
                            } else if (columnasU[i].equals("Federado")) {
                                u = rs.getString(atributosU[i]).equals("1") ? "Sí" : "No";
                            } else {
                                u = rs.getString(atributosU[i]).equals("1") ? "Administrador" : "Usuario";
                            }
                            ja.add(columnasU[i], new JsonPrimitive(u != null ? u : ""));

                        }
                        ja.add(columnasU[atributosU.length], new JsonPrimitive("<a href='usuario?" + atributosU[0] + "=" + ja.get(columnasU[0]).getAsString() + "'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));

                        if (permiso) {
                            ja.add(columnasU[atributosU.length + 1], new JsonPrimitive("<a href='editar-usuario?" + atributosU[0] + "=" + ja.get(columnasU[0]).getAsString() + "'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnasU[atributosU.length + 2], new JsonPrimitive("<a href='eliminar-usuario?" + atributosU[0] + "=" + ja.get(columnasU[0]).getAsString() + "'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>"));
                            ja.add(columnasU[atributosU.length + 3], new JsonPrimitive("<a href='inscripciones?"+atributosU[0]+"="+ja.get(columnasU[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
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
                        for (int i = 0; i < atributosS.length; i++) {
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
                    File ficheroGpx = new File(prueba.getRutaId().getFicheroGpx());
                    parseador = new ParserGPX(ficheroGpx);
                    JsonArray latlng = parseador.gpxToJson();
                    request.setAttribute("prueba", prueba);
                    request.setAttribute("latlng", latlng);
                }
                vista = "seguirPrueba.jsp";
                break;
            case "/seguimiento_prueba":
                prueba_id = request.getParameter("prueba_id");
                /*
                try (Connection con = myDatasource.getConnection()) {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM posicion WHERE prueba_id = ? AND hora BETWEEN DATE_SUB(CURTIME(), INTERVAL 10 SECOND) AND CURTIME() ORDER BY hora DESC;");
                    ps.setString(1, prueba_id);
                    ResultSet posiciones = ps.executeQuery();
                    Set<String> usuarios = new HashSet<>();
                    while(posiciones.next()) {
                        
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                }
                */
                TypedQuery<Posicion> consultaPosiciones = em.createNamedQuery("Posicion.findGPSResults", Posicion.class);
                consultaPosiciones.setParameter("pruebaId", prueba_id);
                List<Posicion> posiciones = consultaPosiciones.getResultList();
                
                Set<Integer> usuarios  = new HashSet<>();
                
                for (Posicion posicion: posiciones) {
                    if (!usuarios.contains(posicion.getInscrito().getDorsal())) {
                        System.out.println(posicion.getInscrito().getDorsal());
                        usuarios.add(posicion.getInscrito().getDorsal());
                    }
                }
                
                Element markers = new Element("markers");
                Document doc = new Document(markers);
                doc.setRootElement(markers);
                
                final String[] atributes = {"name", "address", "lat", "lng", "type"};
               
                Element marker = new Element("marker");
                marker.setAttribute(new Attribute("name", "ola k ase"));
                marker.setAttribute(new Attribute("address", "pepin"));
                marker.setAttribute(new Attribute("lat", "47.624561"));
                marker.setAttribute(new Attribute("lng", "-122.356445"));
                marker.setAttribute(new Attribute("type", "bar"));
                
                /*para otros elementos y añadir texto sería:
                    Element staff2 = new Element("staff");
                    staff2.setAttribute(new Attribute("id", "2"));
                    staff2.addContent(new Element("firstname").setText("low"));
                    staff2.addContent(new Element("lastname").setText("yin fong"));
                    staff2.addContent(new Element("nickname").setText("fong fong"));
                    staff2.addContent(new Element("salary").setText("188888"));
                */
                doc.getRootElement().addContent(marker);
                
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
                List<Inscrito> inscritos = consultaInscritos.getResultList();
                InscritoPK inscritoPK = new InscritoPK(prueba_id, session.getAttribute("correo").toString());
                if (em.find(Inscrito.class, inscritoPK) == null) {
                    if (inscritos.size() < prueba.getMaximoInscritos()) {
                        if (inscritos.isEmpty()) {
                            inscrito = new Inscrito(inscritoPK, false, 1);
                        }
                        else {
                            int i = 0;
                            boolean hueco = false;
                            while (!hueco && (i + 1) < inscritos.size()) {
                                if (inscritos.get(i + 1).getDorsal() - inscritos.get(i).getDorsal() != 1)
                                    hueco = true;
                                i++;
                            }
                            if (!hueco) {
                                inscrito = new Inscrito(new InscritoPK(prueba_id, (String) session.getAttribute("correo")), false, inscritos.size() + 1);
                            }
                            else {
                                inscrito = new Inscrito(new InscritoPK(prueba_id, (String) session.getAttribute("correo")), false, i + 1);
                            }
                        }
                        System.out.println(inscrito.toString());
                        persist(inscrito);
                    }
                    else {
                        System.out.println("YA ESTÁ EL CUPO DE INSCRITOS CUBIERTO");
                    }
                }
                else {
                    System.out.println("YA ESTÁ INSCRITO");
                }
                break;
            case "/inscripciones":
                prueba_id = request.getParameter("prueba_id");
                usuario_id = request.getParameter("usuario_id");
                request.setAttribute("prueba_id", prueba_id);
                request.setAttribute("usuario_id", usuario_id);
                vista = "inscripciones.jsp";
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
                    Boolean permiso = session.getAttribute("permiso") == null ? false : (boolean) session.getAttribute("permiso"),
                            user = session.getAttribute("usuario") != null;
                    while (rs.next()) {
                        JsonObject ja = new JsonObject();
                        for (int i = 0; i < atributosI.size(); i++) {
                            if (!"Pagado".equals(columnasI.get(i))) {
                                ja.add(columnasI.get(i), new JsonPrimitive(rs.getString(atributosI.get(i))));
                            }
                            else {
                                ja.add(columnasI.get(i), new JsonPrimitive(rs.getString(atributosI.get(i)).equals("0")? "No": "Sí"));
                            }
                        }
                        if (permiso) {
                            ja.add(columnasI.get(atributosI.size()), new JsonPrimitive("<a href='editar-inscripcion?" + atributosI.get(0) + "=" + ja.get(columnasI.get(0)).getAsString() + "'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnasI.get(atributosI.size() + 1), new JsonPrimitive("<a href='eliminar-inscripcion?" + atributosI.get(0) + "=" + ja.get(columnasI.get(0)).getAsString() + "'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>"));
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
                vista = "inicio.jsp";
                break;
        };
        RequestDispatcher rd = request.getRequestDispatcher(vista);
        rd.forward(request, response);
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
}