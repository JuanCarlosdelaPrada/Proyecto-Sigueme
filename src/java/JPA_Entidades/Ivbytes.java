/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "ivbytes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ivbytes.findAll", query = "SELECT i FROM Ivbytes i"),
    @NamedQuery(name = "Ivbytes.findByUsuarioId", query = "SELECT i FROM Ivbytes i WHERE i.usuarioId = :usuarioId")})
public class Ivbytes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Lob
    @Column(name = "ivbytes_id")
    private byte[] ivbytesId;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "usuario_id")
    private String usuarioId;
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;

    public Ivbytes() {
    }

    public Ivbytes(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public byte[] getIvbytesId() {
        return ivbytesId;
    }

    public void setIvbytesId(byte[] ivbytesId) {
        this.ivbytesId = ivbytesId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
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
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ivbytes)) {
            return false;
        }
        Ivbytes other = (Ivbytes) object;
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Ivbytes[ usuarioId=" + usuarioId + " ]";
    }
    
}
