/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Je¡ZZ¡
 */
@Embeddable
public class InscritoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "prueba_id")
    private String pruebaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "usuario_id")
    private String usuarioId;

    public InscritoPK() {
    }

    public InscritoPK(String pruebaId, String usuarioId) {
        this.pruebaId = pruebaId;
        this.usuarioId = usuarioId;
    }

    public String getPruebaId() {
        return pruebaId;
    }

    public void setPruebaId(String pruebaId) {
        this.pruebaId = pruebaId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pruebaId != null ? pruebaId.hashCode() : 0);
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InscritoPK)) {
            return false;
        }
        InscritoPK other = (InscritoPK) object;
        if ((this.pruebaId == null && other.pruebaId != null) || (this.pruebaId != null && !this.pruebaId.equals(other.pruebaId))) {
            return false;
        }
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.InscritoPK[ pruebaId=" + pruebaId + ", usuarioId=" + usuarioId + " ]";
    }
    
}
