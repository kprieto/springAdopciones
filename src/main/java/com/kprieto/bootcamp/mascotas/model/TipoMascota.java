package com.kprieto.bootcamp.mascotas.model;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class TipoMascota {
    
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "tipoMascota")
    private String nombre;
    
    public TipoMascota() {
    }

    public TipoMascota(String tipoMascota) {
        this.nombre = tipoMascota;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String tipoMascota) {
        this.nombre = tipoMascota;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }


    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoMascota tipoMascota = (TipoMascota) o;
        return Objects.equals(id, tipoMascota.id) && Objects.equals(tipoMascota, tipoMascota.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @Override
    public String toString() {
        return "TipoMascota [id=" + id + ", tipoMascota=" + nombre + "]";
    }
}
