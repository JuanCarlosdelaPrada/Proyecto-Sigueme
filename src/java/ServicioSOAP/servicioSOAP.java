/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioSOAP;

import JPA_Entidades.Inscrito;
import JPA_Entidades.InscritoPK;
import JPA_Entidades.Posicion;
import JPA_Entidades.PosicionPK;
import java.math.BigDecimal;
import java.util.Date;
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
     * @param pruebaId
     * @param usuarioId
     * @param latitud
     * @param longitud
     */
    @WebMethod(operationName = "posicion-nueva")
    public String posicion(@WebParam(name = "pruebaId") String pruebaId,
                        @WebParam(name = "usuarioId") String usuarioId,
                        @WebParam(name = "latitud") double latitud,
                        @WebParam(name = "longitud") double longitud) {
        Inscrito inscrito = em.find(Inscrito.class, new InscritoPK(pruebaId, usuarioId));
        if (inscrito != null && inscrito.getPrueba().getActiva()) {
            PosicionPK posicionPK = new PosicionPK(pruebaId, usuarioId, new Date(), new Date());
            Posicion posicion = new Posicion(posicionPK, new BigDecimal(latitud), new BigDecimal(longitud));  
            persist(posicion);
            return "MIARMITA";
        }
        return "PIXA";
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
