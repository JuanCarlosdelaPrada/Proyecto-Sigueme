/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioSOAP;

import Herramientas.AES;
import JPA_Entidades.Inscrito;
import JPA_Entidades.InscritoPK;
import JPA_Entidades.Ivbytes;
import JPA_Entidades.Posicion;
import JPA_Entidades.PosicionPK;
import JPA_Entidades.Usuario;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

/**
 *
 * @author Je¡ZZ¡
 */
@WebService(serviceName = "servicioSOAP")
public class servicioSOAP {
    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;
    
    @Resource
    private javax.transaction.UserTransaction utx;
    
    /**
     * This is a sample web service operation
     * @param usuarioId
     * @param latitud
     * @param longitud
     */
    @WebMethod(operationName = "posicion-nueva")
    public void posicion(@WebParam(name = "usuarioId") String usuarioId,
                        @WebParam(name = "latitud") double latitud,
                        @WebParam(name = "longitud") double longitud) {
        Usuario usuario = em.find(Usuario.class, usuarioId);
        Collection<Inscrito> inscripciones = usuario.getInscritoCollection();
        for(Inscrito inscripcion: inscripciones) {
            if (inscripcion.getPrueba().getActiva()) {
                PosicionPK posicionPK = new PosicionPK(inscripcion.getPrueba().getPruebaId(), usuarioId, new Date(), new Date());
                Posicion posicion = new Posicion(posicionPK, new BigDecimal(latitud), new BigDecimal(longitud));  
                persist(posicion);
            }
        }
    }
    
    /**
     * This is a sample web service operation
     * @param usuarioId
     * @param password
     * @return 
     */
    @WebMethod(operationName = "login")
    public boolean login(@WebParam(name = "usuarioId") String usuarioId,
                    @WebParam(name = "password") String password) {
        Usuario usuario = em.find(Usuario.class, usuarioId);
        Ivbytes ivbytes = usuario.getIvbytes();
        AES.setIvBytes(ivbytes.getIvbytesId());
        return password.equals(AES.decrypt(new String(usuario.getContrasena()))); 
    }
    
    private void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
