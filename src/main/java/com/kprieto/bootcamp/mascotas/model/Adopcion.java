package com.kprieto.bootcamp.mascotas.model;

import java.util.Date;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Adopcion {
    
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idMascota", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Mascota mascota;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaAdopcion;

    public Adopcion() {
    }

    public Adopcion(Date fechaAdopcion, Mascota mascota, Usuario usuario) {
        this.fechaAdopcion = fechaAdopcion;
        this.mascota = mascota;
        this.usuario = usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setFechaAdopcion(Date fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public Long getId() {
        return id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Date getFechaAdopcion() {
        return fechaAdopcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopcion adopcionMascota = (Adopcion) o;
        return Objects.equals(id, adopcionMascota.id) && Objects.equals(mascota, adopcionMascota.mascota) && Objects.equals(usuario, adopcionMascota.usuario)
        && Objects.equals(fechaAdopcion, adopcionMascota.fechaAdopcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mascota, usuario, fechaAdopcion);
    }

    @Override
    public String toString() {
        return "Adopcion [id=" + id + ", mascota=" + mascota + ", usuario=" + usuario + ", fechaAdopcion="
                + fechaAdopcion + "]";
    }

}