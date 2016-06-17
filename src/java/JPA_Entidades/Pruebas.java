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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "pruebas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pruebas.findAll", query = "SELECT p FROM Pruebas p"),
    @NamedQuery(name = "Pruebas.findByPruebaId", query = "SELECT p FROM Pruebas p WHERE p.pruebaId = :pruebaId"),
    @NamedQuery(name = "Pruebas.findByDescripcion", query = "SELECT p FROM Pruebas p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Pruebas.findByActiva", query = "SELECT p FROM Pruebas p WHERE p.activa = :activa"),
    @NamedQuery(name = "Pruebas.findByDireccion", query = "SELECT p FROM Pruebas p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "Pruebas.findByFechaCelebracion", query = "SELECT p FROM Pruebas p WHERE p.fechaCelebracion = :fechaCelebracion"),
    @NamedQuery(name = "Pruebas.findByHoraComienzo", query = "SELECT p FROM Pruebas p WHERE p.horaComienzo = :horaComienzo"),
    @NamedQuery(name = "Pruebas.findByMaximoInscritos", query = "SELECT p FROM Pruebas p WHERE p.maximoInscritos = :maximoInscritos")})
public class Pruebas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "prueba_id")
    private String pruebaId;
    @Size(max = 360)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activa")
    private boolean activa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_celebracion")
    @Temporal(TemporalType.DATE)
    private Date fechaCelebracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_comienzo")
    @Temporal(TemporalType.TIME)
    private Date horaComienzo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maximo_inscritos")
    private int maximoInscritos;
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    @ManyToOne(optional = false)
    private Tracks trackId;

    public Pruebas() {
    }

    public Pruebas(String pruebaId) {
        this.pruebaId = pruebaId;
    }

    public Pruebas(String pruebaId, boolean activa, String direccion, Date fechaCelebracion, Date horaComienzo, int maximoInscritos) {
        this.pruebaId = pruebaId;
        this.activa = activa;
        this.direccion = direccion;
        this.fechaCelebracion = fechaCelebracion;
        this.horaComienzo = horaComienzo;
        this.maximoInscritos = maximoInscritos;
    }

    public String getPruebaId() {
        return pruebaId;
    }

    public void setPruebaId(String pruebaId) {
        this.pruebaId = pruebaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaCelebracion() {
        return fechaCelebracion;
    }

    public void setFechaCelebracion(Date fechaCelebracion) {
        this.fechaCelebracion = fechaCelebracion;
    }

    public Date getHoraComienzo() {
        return horaComienzo;
    }

    public void setHoraComienzo(Date horaComienzo) {
        this.horaComienzo = horaComienzo;
    }

    public int getMaximoInscritos() {
        return maximoInscritos;
    }

    public void setMaximoInscritos(int maximoInscritos) {
        this.maximoInscritos = maximoInscritos;
    }

    public Tracks getTrackId() {
        return trackId;
    }

    public void setTrackId(Tracks trackId) {
        this.trackId = trackId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pruebaId != null ? pruebaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pruebas)) {
            return false;
        }
        Pruebas other = (Pruebas) object;
        if ((this.pruebaId == null && other.pruebaId != null) || (this.pruebaId != null && !this.pruebaId.equals(other.pruebaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controlador.Pruebas[ pruebaId=" + pruebaId + " ]";
    }
    
}
