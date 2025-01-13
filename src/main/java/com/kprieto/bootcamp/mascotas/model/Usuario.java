package com.kprieto.bootcamp.mascotas.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private Integer edad;
    private String direccion;
    private String telefono;

    public Usuario() {
    }

    public Usuario(String nombre, String apellidoMaterno, String apellidoPaterno, String correoElectronico, String direccion, Integer edad, String telefono) {
        this.apellidoMaterno = apellidoMaterno;
        this.apellidoPaterno = apellidoPaterno;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.edad = edad;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(nombre, usuario.nombre) && Objects.equals(apellidoPaterno, usuario.apellidoPaterno)
        && Objects.equals(apellidoMaterno, usuario.apellidoMaterno) && Objects.equals(correoElectronico, usuario.correoElectronico) && Objects.equals(edad, usuario.edad)
        && Objects.equals(direccion, usuario.direccion) && Objects.equals(telefono, usuario.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellidoPaterno, apellidoMaterno, correoElectronico, direccion, edad, telefono);
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno
                + ", apellidoMaterno=" + apellidoMaterno + ", correoElectronico=" + correoElectronico + ", edad=" + edad
                + ", direccion=" + direccion + ", telefono=" + telefono + "]";
    }

    



}
