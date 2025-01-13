package com.kprieto.bootcamp.mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kprieto.bootcamp.mascotas.model.Usuario;

@Repository
public interface  UsuarioRepository extends JpaRepository<Usuario, Long>{
    Boolean existsByNombreAndApellidoPaternoAndApellidoMaterno(String nombre, String apellidoPaterno, String apellidoMaterno);
}
