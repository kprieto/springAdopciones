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

import com.kprieto.bootcamp.mascotas.model.TipoMascota;
import com.kprieto.bootcamp.mascotas.repository.TipoMascotaRepository;



@RestController()
@RequestMapping("/api")
public class TipoMascotaRestController {
    
    @Autowired
    private final TipoMascotaRepository tipoMascotaRepository;
    
    public TipoMascotaRestController(TipoMascotaRepository tipoMascotaRepository){
        this.tipoMascotaRepository = tipoMascotaRepository;
    }

    /* Lista todos los tipos de mascotas */
    @GetMapping("/tipoMascotas")
    public ResponseEntity<List<TipoMascota>> listAll() {
        return ResponseEntity.ok(tipoMascotaRepository.findAll());
    }    

    /* Crear un tipo de mascota */
    @PostMapping("/tipoMascotas")
    public ResponseEntity<?> createTipoMascota(@RequestBody TipoMascota nuevoTipoMascota, UriComponentsBuilder ucb) {
        TipoMascota tipoMascotaGuardada = tipoMascotaRepository.save(nuevoTipoMascota);
        URI uriTipoMascota = ucb
                .path("tipoMascotas/{id}")
                .buildAndExpand(tipoMascotaGuardada.getId())
                .toUri();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipo de mascota creado exitosamente.");
        response.put("tipoMascota", tipoMascotaGuardada);
            
        return ResponseEntity.created(uriTipoMascota).body(response);
    }

    /* Obtener un tipo mascota por su ID */
    @GetMapping("/tipoMascotas/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<TipoMascota> tipoMascota = tipoMascotaRepository.findById(id);
        if (tipoMascota.isPresent()){
            return ResponseEntity.ok(tipoMascota.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Tipo Mascota con ID " + id + " no encontrada.");
        
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /* Actualizar un tipo mascota por su ID */
    @PutMapping("/tipoMascotas/{id}")
    public ResponseEntity<?> updateTipoMascota(@RequestBody TipoMascota tipoMascotaActualizada, @PathVariable Long id) {
        return ResponseEntity.ok(tipoMascotaRepository.findById(id)
                .map(tipoMascota -> {
                    tipoMascota.setNombre(tipoMascotaActualizada.getNombre());
                    tipoMascotaRepository.save(tipoMascota);
                    return ResponseEntity.ok().body("Tipo Mascota con ID " + id + " actualizada exitosamente.");
                }).orElseGet(() -> {
                    tipoMascotaRepository.save(tipoMascotaActualizada);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Tipo Mascota con ID " + id + " no encontrada. No se puede actualizar.");
                }));
    }

    /* Eliminar un tipo mascota por su ID */
    @DeleteMapping("/tipoMascotas/{id}") 
    public ResponseEntity<?> deleteTipoMascota(@PathVariable Long id) {
        Optional<TipoMascota> tipoMascota = tipoMascotaRepository.findById(id);
    
        if (tipoMascota.isPresent()) {
            tipoMascotaRepository.deleteById(id);
            return ResponseEntity.ok().body("Tipo Mascota con ID " + id + " eliminada exitosamente.");
        } else {
            return ResponseEntity.ok().body("Tipo Mascota con ID " + id + " no encontrada.");
        }
    }
}
