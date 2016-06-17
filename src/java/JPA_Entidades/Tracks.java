/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "tracks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tracks.findAll", query = "SELECT t FROM Tracks t"),
    @NamedQuery(name = "Tracks.findByTrackId", query = "SELECT t FROM Tracks t WHERE t.trackId = :trackId"),
    @NamedQuery(name = "Tracks.findByDescripcion", query = "SELECT t FROM Tracks t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tracks.findByDistancia", query = "SELECT t FROM Tracks t WHERE t.distancia = :distancia"),
    @NamedQuery(name = "Tracks.findByDificultad", query = "SELECT t FROM Tracks t WHERE t.dificultad = :dificultad"),
    @NamedQuery(name = "Tracks.findByFicheroGPX", query = "SELECT t FROM Tracks t WHERE t.ficheroGPX = :ficheroGPX"),
    @NamedQuery(name = "Tracks.findByMinlatitud", query = "SELECT t FROM Tracks t WHERE t.minlatitud = :minlatitud"),
    @NamedQuery(name = "Tracks.findByMinlongitud", query = "SELECT t FROM Tracks t WHERE t.minlongitud = :minlongitud"),
    @NamedQuery(name = "Tracks.findByMaxlatitud", query = "SELECT t FROM Tracks t WHERE t.maxlatitud = :maxlatitud"),
    @NamedQuery(name = "Tracks.findByMaxlongitud", query = "SELECT t FROM Tracks t WHERE t.maxlongitud = :maxlongitud")})
public class Tracks implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "track_id")
    private String trackId;
    @Size(max = 360)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "distancia")
    private BigDecimal distancia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "dificultad")
    private String dificultad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "ficheroGPX")
    private String ficheroGPX;
    @Basic(optional = false)
    @NotNull
    @Column(name = "minlatitud")
    private BigDecimal minlatitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "minlongitud")
    private BigDecimal minlongitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maxlatitud")
    private BigDecimal maxlatitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maxlongitud")
    private BigDecimal maxlongitud;

    public Tracks() {
    }

    public Tracks(String trackId) {
        this.trackId = trackId;
    }

    public Tracks(String trackId, BigDecimal distancia, String dificultad, String ficheroGPX, BigDecimal minlatitud, BigDecimal minlongitud, BigDecimal maxlatitud, BigDecimal maxlongitud) {
        this.trackId = trackId;
        this.distancia = distancia;
        this.dificultad = dificultad;
        this.ficheroGPX = ficheroGPX;
        this.minlatitud = minlatitud;
        this.minlongitud = minlongitud;
        this.maxlatitud = maxlatitud;
        this.maxlongitud = maxlongitud;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getFicheroGPX() {
        return ficheroGPX;
    }

    public void setFicheroGPX(String ficheroGPX) {
        this.ficheroGPX = ficheroGPX;
    }

    public BigDecimal getMinlatitud() {
        return minlatitud;
    }

    public void setMinlatitud(BigDecimal minlatitud) {
        this.minlatitud = minlatitud;
    }

    public BigDecimal getMinlongitud() {
        return minlongitud;
    }

    public void setMinlongitud(BigDecimal minlongitud) {
        this.minlongitud = minlongitud;
    }

    public BigDecimal getMaxlatitud() {
        return maxlatitud;
    }

    public void setMaxlatitud(BigDecimal maxlatitud) {
        this.maxlatitud = maxlatitud;
    }

    public BigDecimal getMaxlongitud() {
        return maxlongitud;
    }

    public void setMaxlongitud(BigDecimal maxlongitud) {
        this.maxlongitud = maxlongitud;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trackId != null ? trackId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tracks)) {
            return false;
        }
        Tracks other = (Tracks) object;
        if ((this.trackId == null && other.trackId != null) || (this.trackId != null && !this.trackId.equals(other.trackId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Tracks[ trackId=" + trackId + " ]";
    }
    
}
