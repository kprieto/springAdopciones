package com.kprieto.bootcamp.mascotas.model;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idTipoMascota", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoMascota tipoMascota; 
    private Integer edad;
    private Boolean disponible;
    private String foto;
    private String tiempo;
    private String raza;
    private Boolean vacunado;

    public Mascota() {
    }

    public Mascota(String nombre, TipoMascota tipoMascota, Integer edad, Boolean disponible, String foto, String tiempo, String raza, Boolean vacunado) {
        this.nombre = nombre;
        this.tipoMascota = tipoMascota;
        this.edad = edad;
        this.disponible = disponible;
        this.foto = foto;
        this.tiempo = tiempo;
        this.raza = raza;
        this.vacunado = vacunado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public Integer getEdad() {
        return edad;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public String getFoto() {
        return foto;
    }

    public String getTiempo() {
        return tiempo;
    }

    public String getRaza() {
        return raza;
    }

    public Boolean getVacunado() {
        return vacunado;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setVacunado(Boolean vacunado) {
        this.vacunado = vacunado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoMascota(TipoMascota tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return Objects.equals(id, mascota.id) && Objects.equals(nombre, mascota.nombre) && Objects.equals(tipoMascota, mascota.tipoMascota)
        && Objects.equals(edad, mascota.edad) && Objects.equals(disponible, mascota.disponible) && Objects.equals(foto, mascota.foto)
        && Objects.equals(tiempo, mascota.tiempo) && Objects.equals(raza, mascota.raza) && Objects.equals(vacunado, mascota.vacunado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, tipoMascota, edad, disponible, foto, tiempo, raza, vacunado);
    }

    public String convertirBooleano(Boolean valor) {
        return valor != null && valor ? "Si" : "No";
    }

    @Override
    public String toString() {
        return "Mascota [id=" + id + ", nombre=" + nombre + ", tipoMascota=" + tipoMascota + ", edad=" + edad
                + ", disponible=" + convertirBooleano(disponible) + ", foto=" + foto + ", tiempo=" + tiempo + ", raza=" + raza
                + ", vacunado=" + convertirBooleano(vacunado) + "]";
    }



    
    
}
