/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "ruta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ruta.findAll", query = "SELECT r FROM Ruta r"),
    @NamedQuery(name = "Ruta.findByRutaId", query = "SELECT r FROM Ruta r WHERE r.rutaId = :rutaId"),
    @NamedQuery(name = "Ruta.findByDescripcion", query = "SELECT r FROM Ruta r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "Ruta.findByDificultad", query = "SELECT r FROM Ruta r WHERE r.dificultad = :dificultad"),
    @NamedQuery(name = "Ruta.findByDistancia", query = "SELECT r FROM Ruta r WHERE r.distancia = :distancia"),
    @NamedQuery(name = "Ruta.findByFicheroGpx", query = "SELECT r FROM Ruta r WHERE r.ficheroGpx = :ficheroGpx"),
    @NamedQuery(name = "Ruta.findByLatMin", query = "SELECT r FROM Ruta r WHERE r.latMin = :latMin"),
    @NamedQuery(name = "Ruta.findByLatMax", query = "SELECT r FROM Ruta r WHERE r.latMax = :latMax"),
    @NamedQuery(name = "Ruta.findByLongMin", query = "SELECT r FROM Ruta r WHERE r.longMin = :longMin"),
    @NamedQuery(name = "Ruta.findByLongMax", query = "SELECT r FROM Ruta r WHERE r.longMax = :longMax")})
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "ruta_id")
    private String rutaId;
    @Size(max = 360)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "dificultad")
    private String dificultad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "distancia")
    private BigDecimal distancia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "fichero_gpx")
    private String ficheroGpx;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lat_min")
    private BigDecimal latMin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lat_max")
    private BigDecimal latMax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "long_min")
    private BigDecimal longMin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "long_max")
    private BigDecimal longMax;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rutaId")
    private Collection<Prueba> pruebaCollection;

    public Ruta() {
    }

    public Ruta(String rutaId) {
        this.rutaId = rutaId;
    }

    public Ruta(String rutaId, String dificultad, BigDecimal distancia, String ficheroGpx, BigDecimal latMin, BigDecimal latMax, BigDecimal longMin, BigDecimal longMax) {
        this.rutaId = rutaId;
        this.dificultad = dificultad;
        this.distancia = distancia;
        this.ficheroGpx = ficheroGpx;
        this.latMin = latMin;
        this.latMax = latMax;
        this.longMin = longMin;
        this.longMax = longMax;
    }

    public String getRutaId() {
        return rutaId;
    }

    public void setRutaId(String rutaId) {
        this.rutaId = rutaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public String getFicheroGpx() {
        return ficheroGpx;
    }

    public void setFicheroGpx(String ficheroGpx) {
        this.ficheroGpx = ficheroGpx;
    }

    public BigDecimal getLatMin() {
        return latMin;
    }

    public void setLatMin(BigDecimal latMin) {
        this.latMin = latMin;
    }

    public BigDecimal getLatMax() {
        return latMax;
    }

    public void setLatMax(BigDecimal latMax) {
        this.latMax = latMax;
    }

    public BigDecimal getLongMin() {
        return longMin;
    }

    public void setLongMin(BigDecimal longMin) {
        this.longMin = longMin;
    }

    public BigDecimal getLongMax() {
        return longMax;
    }

    public void setLongMax(BigDecimal longMax) {
        this.longMax = longMax;
    }

    @XmlTransient
    public Collection<Prueba> getPruebaCollection() {
        return pruebaCollection;
    }

    public void setPruebaCollection(Collection<Prueba> pruebaCollection) {
        this.pruebaCollection = pruebaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rutaId != null ? rutaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ruta)) {
            return false;
        }
        Ruta other = (Ruta) object;
        if ((this.rutaId == null && other.rutaId != null) || (this.rutaId != null && !this.rutaId.equals(other.rutaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Ruta[ rutaId=" + rutaId + " ]";
    }
    
}
