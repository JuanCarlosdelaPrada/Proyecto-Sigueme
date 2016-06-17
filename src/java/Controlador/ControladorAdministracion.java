package Controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Herramientas.DistanciaDeHaversine;
import Herramientas.ParserGPX;
import JPA_Entidades.Tracks;
import JPA_Entidades.Usuarios;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

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
        
        TypedQuery<Usuarios> consultaUsuarios;
        Usuarios usuario;
        switch(accion) {
            case "/login":
                String correo_login = request.getParameter("correo_login");
                String contrasena_login = request.getParameter("contrasena_login");
                byte[]_contrasena_login = contrasena_login.getBytes();
                consultaUsuarios = em.createNamedQuery("Usuarios.findByCorreo", Usuarios.class);
                consultaUsuarios.setParameter("correo", correo_login);
                try { 
                    usuario = consultaUsuarios.getSingleResult();
                    if(Arrays.equals(usuario.getPassword(), _contrasena_login)) {
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
                String correo = request.getParameter("correo"),
                       fstPassword = request.getParameter("fstPassword"),
                       sndPassword = request.getParameter("sndPassword"),
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
                /*
                Usuarios(String dni, String correo, byte[] password, boolean rol, String nombre, String apellidos, String direccion, Date fechaNacimiento, String telefono, String sexo, boolean federado
                */
                byte[] _fstPassword = fstPassword.getBytes(),
                       _sndPassword = sndPassword.getBytes();
                Date _fecha_nacimiento = Date.valueOf(fecha_nacimiento);
                boolean _federado = federado.equals("s");
                String _club = club.equals("")? null : club;
                System.out.println(dni);
                System.out.println(correo);
                System.out.println(Arrays.toString(_fstPassword));
                System.out.println(nombre);
                System.out.println(apellidos);
                System.out.println(direccion);
                System.out.println(_fecha_nacimiento.toString());
                System.out.println(telefono);
                System.out.println(sexo);
                System.out.println(federado);
                System.out.println(club);
                usuario = new Usuarios(dni, correo,_fstPassword, false, nombre, apellidos, direccion, _fecha_nacimiento, telefono, sexo, _federado);
                usuario.setClub(_club);
                if(em.find(JPA_Entidades.Usuarios.class, correo) == null) {
                    persist(usuario);
                }
                vista = "inicio.jsp";
                break;
            case "/subirRuta": 
                //Recojo los datos del formulario
                String track_id  = request.getParameter("track_id"),
                       descripcion = request.getParameter("descripcion"),
                       dificultad = request.getParameter("dificultad");
                Part ficheroGPX = request.getPart("ficheroGPX");
                
                //Subo el fichero ".gpx" de la ruta a una carpeta
                OutputStream salida = null;
                InputStream contenidoDelFichero = null;
                PrintWriter writer = response.getWriter();
                File destino = new File("C:\\Users\\Je¡ZZ¡\\Documents\\NetBeansProjects\\Sigueme\\ficherosGPX");
                File archivo = new File(destino + File.separator + track_id + ".gpx");
                try {                   
                    salida = new FileOutputStream(archivo);
                    contenidoDelFichero = ficheroGPX.getInputStream();

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while((read = contenidoDelFichero.read(bytes)) != -1) {
                        salida.write(bytes, 0, read);
                    }
                    writer.println(track_id + ".gpx creado");
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
                
                //Creo la nueva ruta.
                Tracks ruta = new Tracks(track_id, new BigDecimal(distancia), dificultad, destino + File.separator + track_id + ".gpx",
                        new BigDecimal(minlatitud), new BigDecimal(minlongitud), new BigDecimal(maxlatitud), new BigDecimal(maxlongitud));
                
                if(em.find(JPA_Entidades.Tracks.class, track_id) == null) {
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