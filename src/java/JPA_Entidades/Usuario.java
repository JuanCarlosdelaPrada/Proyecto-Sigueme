/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA_Entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Je¡ZZ¡
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByUsuarioId", query = "SELECT u FROM Usuario u WHERE u.usuarioId = :usuarioId"),
    @NamedQuery(name = "Usuario.findByRol", query = "SELECT u FROM Usuario u WHERE u.rol = :rol"),
    @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario.findByApellidos", query = "SELECT u FROM Usuario u WHERE u.apellidos = :apellidos"),
    @NamedQuery(name = "Usuario.findByDni", query = "SELECT u FROM Usuario u WHERE u.dni = :dni"),
    @NamedQuery(name = "Usuario.findByDireccion", query = "SELECT u FROM Usuario u WHERE u.direccion = :direccion"),
    @NamedQuery(name = "Usuario.findByFechaNacimiento", query = "SELECT u FROM Usuario u WHERE u.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Usuario.findByTelefono", query = "SELECT u FROM Usuario u WHERE u.telefono = :telefono"),
    @NamedQuery(name = "Usuario.findBySexo", query = "SELECT u FROM Usuario u WHERE u.sexo = :sexo"),
    @NamedQuery(name = "Usuario.findByClub", query = "SELECT u FROM Usuario u WHERE u.club = :club"),
    @NamedQuery(name = "Usuario.findByFederado", query = "SELECT u FROM Usuario u WHERE u.federado = :federado")})
public class Usuario implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "contrasena")
    private byte[] contrasena;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Ivbytes ivbytes;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "usuario_id")
    private String usuarioId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private boolean rol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "dni")
    private String dni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "sexo")
    private String sexo;
    @Size(max = 64)
    @Column(name = "club")
    private String club;
    @Basic(optional = false)
    @NotNull
    @Column(name = "federado")
    private boolean federado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<Posicion> posicionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<Inscrito> inscritoCollection;

    public Usuario() {
    }

    public Usuario(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario(String usuarioId, byte[] contrasena, boolean rol, String nombre, String apellidos, String dni, String direccion, Date fechaNacimiento, String telefono, String sexo, boolean federado) {
        this.usuarioId = usuarioId;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.sexo = sexo;
        this.federado = federado;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }


    public boolean getRol() {
        return rol;
    }

    public void setRol(boolean rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public boolean getFederado() {
        return federado;
    }

    public void setFederado(boolean federado) {
        this.federado = federado;
    }

    @XmlTransient
    public Collection<Posicion> getPosicionCollection() {
        return posicionCollection;
    }

    public void setPosicionCollection(Collection<Posicion> posicionCollection) {
        this.posicionCollection = posicionCollection;
    }

    @XmlTransient
    public Collection<Inscrito> getInscritoCollection() {
        return inscritoCollection;
    }

    public void setInscritoCollection(Collection<Inscrito> inscritoCollection) {
        this.inscritoCollection = inscritoCollection;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA_Entidades.Usuario[ usuarioId=" + usuarioId + " ]";
    }


    public Ivbytes getIvbytes() {
        return ivbytes;
    }

    public void setIvbytes(Ivbytes ivbytes) {
        this.ivbytes = ivbytes;
    }

    public byte[] getContrasena() {
        return contrasena;
    }

    public void setContrasena(byte[] contrasena) {
        this.contrasena = contrasena;
    }
    
}
