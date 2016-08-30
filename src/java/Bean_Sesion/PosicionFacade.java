/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean_Sesion;

import JPA_Entidades.Posicion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Je¡ZZ¡
 */
@Stateless
public class PosicionFacade extends AbstractFacade<Posicion> {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PosicionFacade() {
        super(Posicion.class);
    }
    
}
