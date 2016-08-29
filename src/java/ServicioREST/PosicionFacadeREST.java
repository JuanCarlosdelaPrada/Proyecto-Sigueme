/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioREST;

import JPA_Entidades.Posicion;
import JPA_Entidades.PosicionPK;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Je¡ZZ¡
 */
@Stateless
@Path("jpa_entidades.posicion")
public class PosicionFacadeREST extends AbstractFacade<Posicion> {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    private PosicionPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;pruebaId=pruebaIdValue;usuarioId=usuarioIdValue;fecha=fechaValue;hora=horaValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        JPA_Entidades.PosicionPK key = new JPA_Entidades.PosicionPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> pruebaId = map.get("pruebaId");
        if (pruebaId != null && !pruebaId.isEmpty()) {
            key.setPruebaId(pruebaId.get(0));
        }
        java.util.List<String> usuarioId = map.get("usuarioId");
        if (usuarioId != null && !usuarioId.isEmpty()) {
            key.setUsuarioId(usuarioId.get(0));
        }
        java.util.List<String> fecha = map.get("fecha");
        if (fecha != null && !fecha.isEmpty()) {
            key.setFecha(new java.util.Date(fecha.get(0)));
        }
        java.util.List<String> hora = map.get("hora");
        if (hora != null && !hora.isEmpty()) {
            key.setHora(new java.util.Date(hora.get(0)));
        }
        return key;
    }

    public PosicionFacadeREST() {
        super(Posicion.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Posicion entity) {
        super.create(entity);
    }
    
    @POST
    @Path("nueva-posicion")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void posicion(@FormParam("name") String name,
                         @FormParam("message") String message) {
        System.out.println("name: "+ name);
        System.out.println("message: "+ message);
        //super.create(entity);
    }
    
    public static void main(String[] args) {
        String BASE_URI = "http://localhost:8080/Sigueme/servicio-rest/jpa_entidades.posicion/";
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost post = new HttpPost(BASE_URI + "nueva-posicion");

            // Setup form data
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("name", "blive1"));
            nameValuePairs.add(new BasicNameValuePair("message", "d30a62033c24df68bb091a958a68a169"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute request
            HttpResponse response = httpclient.execute(post);

            // Check response status and read data
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String data = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Posicion entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        JPA_Entidades.PosicionPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Posicion find(@PathParam("id") PathSegment id) {
        JPA_Entidades.PosicionPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Posicion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Posicion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
