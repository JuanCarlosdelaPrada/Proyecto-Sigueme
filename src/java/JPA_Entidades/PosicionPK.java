/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Je¡ZZ¡
 */
@Embeddable
public class PosicionPK implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;

    public PosicionPK() {
    }

    public PosicionPK(String pruebaId, String usuarioId, Date fecha, Date hora) {
        this.pruebaId = pruebaId;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.hora = hora;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pruebaId != null ? pruebaId.hashCode() : 0);
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        hash += (fecha != null ? fecha.hashCode() : 0);
        hash += (hora != null ? hora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosicionPK)) {
            return false;
        }
        PosicionPK other = (PosicionPK) object;
        if ((this.pruebaId == null && other.pruebaId != null) || (this.pruebaId != null && !this.pruebaId.equals(other.pruebaId))) {
            return false;
        }
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        if ((this.hora == null && other.hora != null) || (this.hora != null && !this.hora.equals(other.hora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.PosicionPK[ pruebaId=" + pruebaId + ", usuarioId=" + usuarioId + ", fecha=" + fecha + ", hora=" + hora + " ]";
    }
    
}
