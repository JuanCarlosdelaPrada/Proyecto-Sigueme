package Controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Herramientas.DistanciaDeHaversine;
import Herramientas.ParserGPX;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

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
    "/pruebas", //--
    "/prueba", //--
    "/usuarios",
    "/usuario" //--
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
        
        TypedQuery<Usuario> consultaUsuarios;
        JsonObject resultado;
        ParserGPX parseador;
        Usuario usuario;
        JsonArray array;
        Ruta ruta;
        
        int cantidad,
            comienzo,
            idraw,
            num_atributos,
            total;
        
        String ruta_id,
               descripcion, 
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
                String correo_login = request.getParameter("correo_login");
                String contrasena_login = request.getParameter("contrasena_login");
                byte[]_contrasena_login = contrasena_login.getBytes();
                consultaUsuarios = em.createNamedQuery("Usuario.findByUsuarioId", Usuario.class);
                consultaUsuarios.setParameter("usuarioId", correo_login);
                try { 
                    usuario = consultaUsuarios.getSingleResult();
                    if(Arrays.equals(usuario.getContrasena(), _contrasena_login)) {
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
                /*
                String string = new String(byte[] bytes, Charset charset);
                //Arrays.toString(bytes);
                byte[] b = string.getBytes();
                byte[] b = string.getBytes(Charset.forName("UTF-8"));
                byte[] b = string.getBytes(StandardCharsets.UTF_8); // Java 7+ only
                */
                
                byte[] contrasena_tratada = contrasena.getBytes(),
                       _contrasena_tratada = _contrasena.getBytes();
                Date fecha_nacimiento_tratada = Date.valueOf(fecha_nacimiento);
                boolean federado_tratado = federado.equals("s");
                String _club = club.equals("")? null : club;
                usuario = new Usuario(usuario_id, contrasena_tratada, false, nombre, apellidos, dni, direccion, fecha_nacimiento_tratada, telefono, sexo, federado_tratado);
                usuario.setClub(_club);
                if(em.find(JPA_Entidades.Usuario.class, usuario_id) == null) {
                    persist(usuario);
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
                
                /*
                    //Creo la tabla correspondiente a las latitudes y longitudes de la ruta.
                    if(!latitudes.isEmpty() && !longitudes.isEmpty() && latitudes.size() == longitudes.size()){
                        try (Connection conn = myDatasource.getConnection()) {
                            Statement st = conn.createStatement();
                            st.execute("CREATE TABLE `"+track_id+"`("
                                    +  "latitud DECIMAL(10, 8) NOT NULL,"
                                    +  "longitud DECIMAL(11, 8) NOT NULL);"
                            );
                            PreparedStatement insercion = conn.prepareStatement("INSERT INTO `"+track_id+"` VALUES (?,?)");
                            for(int i = 0; i < latitudes.size(); i++) {
                                insercion.setDouble(1, latitudes.get(i));
                                insercion.setDouble(2, longitudes.get(i));
                                insercion.executeUpdate();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ControladorAdministracion.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                */
                
                double distancia = DistanciaDeHaversine.getDistancia(latitudes, longitudes);
                
                //String rutaId, String dificultad, BigDecimal distancia, String ficheroGpx, BigDecimal latMin, BigDecimal latMax, BigDecimal longMin, BigDecimal longMax
                //Creo la nueva ruta.
                ruta = new Ruta(ruta_id, dificultad, new BigDecimal(distancia), destino + File.separator + ruta_id + ".gpx",
                       new BigDecimal(minlatitud), new BigDecimal(maxlatitud), new BigDecimal(minlongitud), new BigDecimal(maxlongitud));
                ruta.setDescripcion(descripcion);
                if(em.find(JPA_Entidades.Ruta.class, ruta_id) == null) {
                    persist(ruta);
                }
                /*
                        for (int i = 0; i < track.length(); i++) {
                            JSONObject punto = track.getJSONObject(i);
                            insercion2.setDouble(1, punto.getDouble("lat"));
                            insercion2.setDouble(2, punto.getDouble("lng"));
                            insercion2.executeUpdate();

                }*/
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
                    if (num_atributos < 0 || num_atributos > 9)
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
                            globeSearch = " where (ruta_id like '%" + searchTerm +"%'"
                            + " or descripcion like '%" + searchTerm +"%'"
                            + " or distancia like '%" + searchTerm +"%'"
                            + " or dificultad like '%" + searchTerm +"%'"
                            + " or fichero_gpx like '%" + searchTerm +"%'"
                            + " or lat_min like '%" + searchTerm +"%'"
                            + " or lat_max like '%" + searchTerm +"%'"
                            + " or long_min like '%" + searchTerm +"%'"
                            + " or long_max like '%" + searchTerm + "%')";
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
                    PrintWriter out = response.getWriter();
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
            case "/usuarios":
                String[] columnasU = {"Nombre de la ruta", "Descripcion", "Distancia", "Dificultad", "ficheroGPX", "minlatitud", "minlongitud", "maxlatitud", "maxlongitud", "Mas informacion", "Editar", "Borrar"};
                String[] atributosU = {"ruta_id", "descripcion", "distancia", "dificultad", "fichero_gpx", "lat_min", "lat_max", "long_min", "long_max"};
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
                    if (num_atributos < 0 || num_atributos > 9)
                        num_atributos = 0;
                }
                if (sdir != null) {
                    if (!sdir.equals("asc"))
                        dir = "desc";
                }
                nombreAtributo = atributosU[num_atributos];
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
                            globeSearch = " where (ruta_id like '%" + searchTerm +"%'"
                            + " or descripcion like '%" + searchTerm +"%'"
                            + " or distancia like '%" + searchTerm +"%'"
                            + " or dificultad like '%" + searchTerm +"%'"
                            + " or fichero_gpx like '%" + searchTerm +"%'"
                            + " or lat_min like '%" + searchTerm +"%'"
                            + " or lat_max like '%" + searchTerm +"%'"
                            + " or long_min like '%" + searchTerm +"%'"
                            + " or long_max like '%" + searchTerm + "%')";
                    sql = "SELECT * FROM " + tabla;
                    
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
                        for (int i = 0; i < atributosU.length; i++) {
                            ja.add(columnasU[i], new JsonPrimitive(rs.getString(atributosU[i])));                            
                        }
                        ja.add(columnasU[atributosU.length], new JsonPrimitive("<a href='ruta?"+atributosU[0]+"="+ja.get(columnasU[0]).getAsString()+"'><i class='fa fa-search aria-hidden='true' style='color:#088A08'></i></a>"));
                        
                        Boolean permiso = session.getAttribute("permiso") == null? false: (boolean)session.getAttribute("permiso");
                        if (permiso == true) {
                            ja.add(columnasU[atributosU.length + 1], new JsonPrimitive("<a href='editar-ruta?"+atributosU[0]+"="+ja.get(columnasU[0]).getAsString()+"'><i class='fa fa-pencil-square-o aria-hidden='true' style='color:#8904B1'></i></a>"));
                            ja.add(columnasU[atributosU.length + 2], new JsonPrimitive("<a href='eliminar-ruta?"+atributosU[0]+"="+ja.get(columnasU[0]).getAsString()+"'><i class='fa fa-times aria-hidden='true' style='color:#B40404'></i></a>")); 
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
                    PrintWriter out = response.getWriter();
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