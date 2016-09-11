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
@Table(name = "prueba")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prueba.findAll", query = "SELECT p FROM Prueba p"),
    @NamedQuery(name = "Prueba.findByPruebaId", query = "SELECT p FROM Prueba p WHERE p.pruebaId = :pruebaId"),
    @NamedQuery(name = "Prueba.findByRutaId", query = "SELECT p FROM Prueba p WHERE p.rutaId = :rutaId"),
    @NamedQuery(name = "Prueba.findByDescripcion", query = "SELECT p FROM Prueba p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Prueba.findByLugar", query = "SELECT p FROM Prueba p WHERE p.lugar = :lugar"),
    @NamedQuery(name = "Prueba.findByFechaCel", query = "SELECT p FROM Prueba p WHERE p.fechaCel = :fechaCel"),
    @NamedQuery(name = "Prueba.findByHoraCel", query = "SELECT p FROM Prueba p WHERE p.horaCel = :horaCel"),
    @NamedQuery(name = "Prueba.findByFechaInscripMin", query = "SELECT p FROM Prueba p WHERE p.fechaInscripMin = :fechaInscripMin"),
    @NamedQuery(name = "Prueba.findByFechaInscripMax", query = "SELECT p FROM Prueba p WHERE p.fechaInscripMax = :fechaInscripMax"),
    @NamedQuery(name = "Prueba.findByMaximoInscritos", query = "SELECT p FROM Prueba p WHERE p.maximoInscritos = :maximoInscritos"),
    @NamedQuery(name = "Prueba.findByActiva", query = "SELECT p FROM Prueba p WHERE p.activa = :activa"),
    @NamedQuery(name = "Prueba.findByNumero", query = "SELECT p FROM Prueba p WHERE p.numero = :numero"),
    @NamedQuery(name = "Prueba.orderingByNumero", query = "SELECT p FROM Prueba p ORDER BY p.numero DESC")})
public class Prueba implements Serializable {

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
    @Size(min = 1, max = 90)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cel")
    @Temporal(TemporalType.DATE)
    private Date fechaCel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_cel")
    @Temporal(TemporalType.TIME)
    private Date horaCel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inscrip_min")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripMin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inscrip_max")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripMax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maximo_inscritos")
    private int maximoInscritos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activa")
    private boolean activa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numero;
    @JoinColumn(name = "ruta_id", referencedColumnName = "ruta_id")
    @ManyToOne(optional = false)
    private Ruta rutaId;

    public Prueba() {
    }

    public Prueba(String pruebaId) {
        this.pruebaId = pruebaId;
    }

    public Prueba(String pruebaId, String lugar, Date fechaCel, Date horaCel, Date fechaInscripMin, Date fechaInscripMax, int maximoInscritos, boolean activa, int numero) {
        this.pruebaId = pruebaId;
        this.lugar = lugar;
        this.fechaCel = fechaCel;
        this.horaCel = horaCel;
        this.fechaInscripMin = fechaInscripMin;
        this.fechaInscripMax = fechaInscripMax;
        this.maximoInscritos = maximoInscritos;
        this.activa = activa;
        this.numero = numero;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFechaCel() {
        return fechaCel;
    }

    public void setFechaCel(Date fechaCel) {
        this.fechaCel = fechaCel;
    }

    public Date getHoraCel() {
        return horaCel;
    }

    public void setHoraCel(Date horaCel) {
        this.horaCel = horaCel;
    }

    public Date getFechaInscripMin() {
        return fechaInscripMin;
    }

    public void setFechaInscripMin(Date fechaInscripMin) {
        this.fechaInscripMin = fechaInscripMin;
    }

    public Date getFechaInscripMax() {
        return fechaInscripMax;
    }

    public void setFechaInscripMax(Date fechaInscripMax) {
        this.fechaInscripMax = fechaInscripMax;
    }

    public int getMaximoInscritos() {
        return maximoInscritos;
    }

    public void setMaximoInscritos(int maximoInscritos) {
        this.maximoInscritos = maximoInscritos;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Ruta getRutaId() {
        return rutaId;
    }

    public void setRutaId(Ruta rutaId) {
        this.rutaId = rutaId;
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
        if (!(object instanceof Prueba)) {
            return false;
        }
        Prueba other = (Prueba) object;
        if ((this.pruebaId == null && other.pruebaId != null) || (this.pruebaId != null && !this.pruebaId.equals(other.pruebaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Prueba[ pruebaId=" + pruebaId + " ]";
    }
    
}
