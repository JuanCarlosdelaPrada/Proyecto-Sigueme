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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.json.JSONArray;
import org.json.JSONObject;

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
    "/rutas"
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
        
        accion = request.getServletPath();
        session = request.getSession();
        
        TypedQuery<Usuario> consultaUsuarios;
        Usuario usuario;
        
        String ruta_id,
               descripcion, 
               dificultad;
        
        vista = "";
        switch(accion) {
            case "/login":
                String correo_login = request.getParameter("correo_login");
                String contrasena_login = request.getParameter("contrasena_login");
                byte[]_contrasena_login = contrasena_login.getBytes();
                consultaUsuarios = em.createNamedQuery("Usuario.findByUsuarioId", Usuario.class);
                consultaUsuarios.setParameter("usuario_id", correo_login);
                try { 
                    usuario = consultaUsuarios.getSingleResult();
                    if(Arrays.equals(usuario.getContrasena(), _contrasena_login)) {
                        session.setAttribute("usuario", usuario.getNombre());
                        session.setAttribute("permiso", usuario.getRol());
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
                
                ParserGPX parseador = new ParserGPX(archivo);
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
                Ruta ruta = new Ruta(ruta_id, dificultad, new BigDecimal(distancia), destino + File.separator + ruta_id + ".gpx",
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
                String[] atributos = {"ruta_id", "descripcion", "distancia", "dificultad", "fichero_gpx", "lat_min", "lat_max", "long_min", "long_max"};
                String tabla = "ruta";
                JSONObject resultado = new JSONObject();
                JSONArray array = new JSONArray();
                int cantidad = 10,
                    comienzo = 0,
                    echo = 0,
                    num_atributos = 0;
                
                String dir = "asc",
                       sStart = request.getParameter("iDisplayStart"),
                       sAmount = request.getParameter("iDisplayLength"),
                       sEcho = request.getParameter("sEcho"),
                       sCol = request.getParameter("iSortCol_0"),
                       sdir = request.getParameter("sSortDir_0");
                
                 
                String sdistancia = request.getParameter("sSearch 2"),
                       fichero_gpx = request.getParameter("sSearch_4"),
                       lat_min = request.getParameter("sSearch_5"),
                       lat_max = request.getParameter("sSearch_6"),
                       long_min = request.getParameter("sSearch_7"),
                       long_max = request.getParameter("sSearch 8");
                ruta_id = request.getParameter("sSearch_0");
                descripcion = request.getParameter("sSearch_1");
                dificultad = request.getParameter("sSearch_3");
                
                System.out.println(sdistancia);
                System.out.println(fichero_gpx);
                System.out.println(lat_min);
                System.out.println(lat_max);
                System.out.println(long_min);
                System.out.println(long_max);
                System.out.println(ruta_id);
                System.out.println(descripcion);
                System.out.println(dificultad);
                
                List<String> sArray = new ArrayList<>();
                if (ruta_id != null) {
                    sArray.add(" ruta_id like '%" + ruta_id + "%'");
                }
                if (descripcion != null) {
                    sArray.add(" descripcion like '%" + descripcion + "%'");
                }
                if (sdistancia != null) {
                    sArray.add(" distancia like '%" + sdistancia + "%'");
                }
                if (dificultad != null) {
                    sArray.add(" dificultad like '%" + dificultad + "%'");
                }
                if (fichero_gpx != null) {
                    sArray.add(" fichero_gpx like '%" + fichero_gpx + "%'");
                }
                if (lat_min != null) {
                    sArray.add(" lat_min like '%" + lat_min + "%'");
                }
                if (lat_max != null) {
                    sArray.add(" lat_max like '%" + lat_max + "%'");
                }
                if (long_min != null) {
                    sArray.add(" long_min like '%" + long_min + "%'");
                }
                if (long_max != null) {
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
                if (sEcho != null) {
                    echo = Integer.parseInt(sEcho);
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
                String nombreAtributo = atributos[num_atributos];
                int total = 0;
                
                try (Connection conn = myDatasource.getConnection()) {
                    String sql = "SELECT count(*) FROM " + tabla;
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        total = rs.getInt("count(*)");
                    }
                    int totalAfterFilter = total;
                    String searchSQL = "",
                            searchTerm = request.getParameter("sSearch"),
                            globeSearch = " where (ruta_id like '%" + searchTerm +"%'"
                            + " or descripcion like '%" + searchTerm +"%'"
                            + " or distancia like '%" + searchTerm +"%'"
                            + " or dificultad like '%" + searchTerm +"%'"
                            + " or fichero_gpx like '%" + searchTerm +"%'"
                            + " or lat_min like '%" + searchTerm +"%'"
                            + " or lat_max like '%" + searchTerm +"%'"
                            + " or long_min like '%" + searchTerm +"%'"
                            + " or long_max like '%" + searchTerm + "%'";
                    sql = "SELECT * FROM " + tabla;
                    if(searchTerm != null && !"".equals(individualSearch)){
                        searchSQL = globeSearch + " and " + individualSearch;
                    }
                    else if(!"".equals(individualSearch)){
                        searchSQL = " where " + individualSearch;
                    }else if(searchTerm != null){
                        searchSQL = globeSearch;
                    }
                    System.out.println("individual" + individualSearch);
                    sql += searchSQL;
                    sql += " order by " + nombreAtributo + " " + dir;
                    sql += " limit " + comienzo + ", " + cantidad;
                    System.out.println(sql);
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        JSONArray ja = new JSONArray();
                        ja.put(rs.getString("ruta_id"));
                        ja.put(rs.getString("descripcion"));
                        ja.put(rs.getString("distancia"));
                        ja.put(rs.getString("dificultad"));
                        ja.put(rs.getString("fichero_gpx"));
                        ja.put(rs.getString("lat_min"));
                        ja.put(rs.getString("lat_max"));
                        ja.put(rs.getString("long_min"));
                        ja.put(rs.getString("long_max"));
                        array.put(ja);
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
                    resultado.put("draw", echo);
                    resultado.put("recordsTotal", total);
                    resultado.put("recordsFiltered", totalAfterFilter);
                    resultado.put("data", array);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    PrintWriter out = response.getWriter();
                    out.print(resultado);
                    out.flush();
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