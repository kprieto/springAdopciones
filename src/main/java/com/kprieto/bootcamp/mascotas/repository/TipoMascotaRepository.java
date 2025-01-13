package com.kprieto.bootcamp.mascotas.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kprieto.bootcamp.mascotas.model.TipoMascota;

@Repository
public interface TipoMascotaRepository extends JpaRepository<TipoMascota, Long> {
    Boolean existsByNombre(String nombre);
}
