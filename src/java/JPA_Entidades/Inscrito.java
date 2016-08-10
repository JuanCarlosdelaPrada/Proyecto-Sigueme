/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "inscrito")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inscrito.findAll", query = "SELECT i FROM Inscrito i"),
    @NamedQuery(name = "Inscrito.findByPruebaId", query = "SELECT i FROM Inscrito i WHERE i.inscritoPK.pruebaId = :pruebaId"),
    @NamedQuery(name = "Inscrito.findByPruebaIdOrderingByDorsal", query = "SELECT i FROM Inscrito i WHERE i.inscritoPK.pruebaId = :pruebaId ORDER BY i.dorsal"),
    @NamedQuery(name = "Inscrito.findByPruebaIdAndUsuarioId", query = "SELECT i FROM Inscrito i WHERE i.inscritoPK.pruebaId = :pruebaId and i.inscritoPK.usuarioId = :usuarioId"),
    @NamedQuery(name = "Inscrito.findByUsuarioId", query = "SELECT i FROM Inscrito i WHERE i.inscritoPK.usuarioId = :usuarioId"),
    @NamedQuery(name = "Inscrito.findByPagado", query = "SELECT i FROM Inscrito i WHERE i.pagado = :pagado"),
    @NamedQuery(name = "Inscrito.findByDorsal", query = "SELECT i FROM Inscrito i WHERE i.dorsal = :dorsal")})
public class Inscrito implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InscritoPK inscritoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pagado")
    private boolean pagado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dorsal")
    private int dorsal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscrito")
    private Collection<Posicion> posicionCollection;
    @JoinColumn(name = "prueba_id", referencedColumnName = "prueba_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Prueba prueba;
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Inscrito() {
    }

    public Inscrito(InscritoPK inscritoPK) {
        this.inscritoPK = inscritoPK;
    }

    public Inscrito(InscritoPK inscritoPK, boolean pagado, int dorsal) {
        this.inscritoPK = inscritoPK;
        this.pagado = pagado;
        this.dorsal = dorsal;
    }

    public Inscrito(String pruebaId, String usuarioId) {
        this.inscritoPK = new InscritoPK(pruebaId, usuarioId);
    }

    public InscritoPK getInscritoPK() {
        return inscritoPK;
    }

    public void setInscritoPK(InscritoPK inscritoPK) {
        this.inscritoPK = inscritoPK;
    }

    public boolean getPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    @XmlTransient
    public Collection<Posicion> getPosicionCollection() {
        return posicionCollection;
    }

    public void setPosicionCollection(Collection<Posicion> posicionCollection) {
        this.posicionCollection = posicionCollection;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inscritoPK != null ? inscritoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inscrito)) {
            return false;
        }
        Inscrito other = (Inscrito) object;
        if ((this.inscritoPK == null && other.inscritoPK != null) || (this.inscritoPK != null && !this.inscritoPK.equals(other.inscritoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Inscrito[ inscritoPK=" + inscritoPK + " ]";
    }
    
}
