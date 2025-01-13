package com.kprieto.bootcamp.mascotas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kprieto.bootcamp.mascotas.model.Mascota;

@Repository
public interface  MascotaRepository extends JpaRepository<Mascota, Long> {
    Boolean existsByNombre(String nombre);
    List<Mascota> findByDisponibleTrue();
    List<Mascota> findByDisponibleFalse();

    
    @Transactional
    @Modifying 
    @Query("UPDATE Mascota m SET m.disponible = false WHERE m.id = :id") 
    void actualizarDisponibilidad(@Param("id") Long id);

    @Transactional
    @Modifying 
    @Query("UPDATE Mascota m SET m.disponible = true WHERE m.id = :id") 
    void actualizarDisponibilidadTrue(@Param("id") Long id);
}

