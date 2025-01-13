package com.kprieto.bootcamp.mascotas.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kprieto.bootcamp.mascotas.model.Adopcion;
import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.repository.AdopcionRepository;
import com.kprieto.bootcamp.mascotas.repository.MascotaRepository;


@RestController()
@RequestMapping("/api")
public class AdopcionRestController {
    
    @Autowired
    private final AdopcionRepository adopcionRepository;

    @Autowired
    private final MascotaRepository mascotaRepository;
    
    public AdopcionRestController(AdopcionRepository adopcionRepository, MascotaRepository mascotaRepository){
        this.adopcionRepository = adopcionRepository;
        this.mascotaRepository = mascotaRepository;
    }

    /* Lista todas las adopciones */
    @GetMapping("/adopciones")
    public ResponseEntity<List<Adopcion>> listAll() {
        return ResponseEntity.ok(adopcionRepository.findAll());
    }    

    /* Crear una adopción */
    @PostMapping("/adopciones")
    public ResponseEntity<?> createAdopcion(@RequestBody Adopcion nuevaAdopcion, UriComponentsBuilder ucb) {
        // Verificar si la mascota está asociada a la adopción
        Mascota mascota = nuevaAdopcion.getMascota();
        if (mascota == null || mascota.getId() == null) {
            return ResponseEntity.badRequest().body("La adopción debe estar asociada a una mascota válida.");
        }

        // Actualizar la disponibilidad de la mascota
        mascota.setDisponible(false);
        mascotaRepository.save(mascota);
        
        Adopcion adopcionGuardada = adopcionRepository.save(nuevaAdopcion);
        URI uriAdopcion = ucb
                .path("adopciones/{id}")
                .buildAndExpand(adopcionGuardada.getId())
                .toUri();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Adopción creada exitosamente.");
        response.put("adopcion", adopcionGuardada);
            
        return ResponseEntity.created(uriAdopcion).body(response);
    }

    /* Obtener una adopción por su ID */
    @GetMapping("/adopciones/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Adopcion> adopcion = adopcionRepository.findById(id);
        if (adopcion.isPresent()){
            return ResponseEntity.ok(adopcion.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Adopción con ID " + id + " no encontrada.");
        
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /* Actualizar una adopción por su ID */
    @PutMapping("/adopciones/{id}")
    public ResponseEntity<?> updateAdopcion(@RequestBody Adopcion adopcionActualizada, @PathVariable Long id) {
        return ResponseEntity.ok(adopcionRepository.findById(id)
                .map(adopcion -> {
                    adopcion.setFechaAdopcion(adopcionActualizada.getFechaAdopcion());
                    adopcion.setMascota(adopcionActualizada.getMascota());
                    adopcion.setUsuario(adopcionActualizada.getUsuario());
                    adopcionRepository.save(adopcion);
                    return ResponseEntity.ok().body("Adopción con ID " + id + " actualizada exitosamente.");
                }).orElseGet(() -> {
                    adopcionRepository.save(adopcionActualizada);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Adopción con ID " + id + " no encontrada. No se puede actualizar.");
                }));
    }

    /* Eliminar una adopcion por su ID */
    @DeleteMapping("/adopciones/{id}") 
    public ResponseEntity<?> deleteAdopcion(@PathVariable Long id) {
        Optional<Adopcion> adopcion = adopcionRepository.findById(id);
    
        if (adopcion.isPresent()) {
            adopcionRepository.deleteById(id);
            return ResponseEntity.ok().body("Adopción con ID " + id + " eliminada exitosamente.");
        } else {
            return ResponseEntity.ok().body("Adopción con ID " + id + " no encontrada.");
        }
    }
}
