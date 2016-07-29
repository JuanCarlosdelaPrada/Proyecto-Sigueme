/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "posicion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Posicion.findAll", query = "SELECT p FROM Posicion p"),
    @NamedQuery(name = "Posicion.findByPruebaId", query = "SELECT p FROM Posicion p WHERE p.posicionPK.pruebaId = :pruebaId"),
    @NamedQuery(name = "Posicion.findByUsuarioId", query = "SELECT p FROM Posicion p WHERE p.posicionPK.usuarioId = :usuarioId"),
    @NamedQuery(name = "Posicion.findByFecha", query = "SELECT p FROM Posicion p WHERE p.posicionPK.fecha = :fecha"),
    @NamedQuery(name = "Posicion.findByHora", query = "SELECT p FROM Posicion p WHERE p.posicionPK.hora = :hora"),
    @NamedQuery(name = "Posicion.findByLatitud", query = "SELECT p FROM Posicion p WHERE p.latitud = :latitud"),
    @NamedQuery(name = "Posicion.findByLongitud", query = "SELECT p FROM Posicion p WHERE p.longitud = :longitud")})
public class Posicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PosicionPK posicionPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitud")
    private BigDecimal latitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitud")
    private BigDecimal longitud;
    @JoinColumns({
        @JoinColumn(name = "prueba_id", referencedColumnName = "prueba_id", insertable = false, updatable = false),
        @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Inscrito inscrito;

    public Posicion() {
    }

    public Posicion(PosicionPK posicionPK) {
        this.posicionPK = posicionPK;
    }

    public Posicion(PosicionPK posicionPK, BigDecimal latitud, BigDecimal longitud) {
        this.posicionPK = posicionPK;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Posicion(String pruebaId, String usuarioId, Date fecha, Date hora) {
        this.posicionPK = new PosicionPK(pruebaId, usuarioId, fecha, hora);
    }

    public PosicionPK getPosicionPK() {
        return posicionPK;
    }

    public void setPosicionPK(PosicionPK posicionPK) {
        this.posicionPK = posicionPK;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Inscrito getInscrito() {
        return inscrito;
    }

    public void setInscrito(Inscrito inscrito) {
        this.inscrito = inscrito;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (posicionPK != null ? posicionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posicion)) {
            return false;
        }
        Posicion other = (Posicion) object;
        if ((this.posicionPK == null && other.posicionPK != null) || (this.posicionPK != null && !this.posicionPK.equals(other.posicionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Posicion[ posicionPK=" + posicionPK + " ]";
    }
    
}
