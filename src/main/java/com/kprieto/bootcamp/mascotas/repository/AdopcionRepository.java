package com.kprieto.bootcamp.mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kprieto.bootcamp.mascotas.model.Adopcion;

@Repository
public interface  AdopcionRepository extends JpaRepository<Adopcion, Long> {
    
}
