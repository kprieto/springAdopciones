package com.kprieto.bootcamp.mascotas.controller;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.repository.MascotaRepository;

@RestController
@RequestMapping("/api")
public class MascotaRestController {

    // Ruta donde se guardarán las imágenes
    @Value("${ruta.imagenes}")
    private String uploadDirectory;

    @Autowired
    private final MascotaRepository mascotaRepository;
    public MascotaRestController(MascotaRepository mascotaRepository){
        this.mascotaRepository = mascotaRepository;
    }

    /* Lista todas las mascotas disponibles */
    @GetMapping("/mascotas")
    public ResponseEntity<List<Mascota>> listAll() {
        return ResponseEntity.ok(mascotaRepository.findByDisponibleTrue());
    }    

    /* Crear una mascota */
    @PostMapping("/mascotas")
    public ResponseEntity<?> createMascota(@RequestBody Mascota nuevaMascota, 
            UriComponentsBuilder ucb) {
                // @RequestParam("imagen") MultipartFile imagen,
         // Validar que el archivo no esté vacío
        // if (imagen.isEmpty()) {
        //     return ResponseEntity.badRequest().body("La foto de la mascota es obligatoria.");
        // }


        // byte [] bytesImg = imagen.getBytes();
        // Path rutaCompleta = Paths.get(ruta + File.separator + imagen.getOriginalFilename());
        // Files.write(rutaCompleta, bytesImg);
        // nuevaMascota.setFoto(imagen.getOriginalFilename());// Campo para almacenar la ruta de la foto
        Mascota mascotaGuardada = mascotaRepository.save(nuevaMascota);

        // Construir la URI de la nueva mascota
        URI uriMascota = ucb
                .path("/mascotas/{id}")
                .buildAndExpand(mascotaGuardada.getId())
                .toUri();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Mascota creada exitosamente.");
                response.put("mascota", mascotaGuardada);
                    
        return ResponseEntity.created(uriMascota).body(response);


    }

    /* Obtener una mascota por su ID */
    @GetMapping("/mascotas/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Mascota> mascota = mascotaRepository.findById(id);
        if (mascota.isPresent()){
            return ResponseEntity.ok(mascota.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Mascota con ID " + id + " no encontrada");
        
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /* Actualizar una mascota por su ID */
    @PutMapping("/mascotas/{id}")
    public ResponseEntity<?> updateMascota(@RequestBody Mascota mascotaActualizada, @PathVariable Long id) {
        return ResponseEntity.ok(mascotaRepository.findById(id)
                .map(mascota -> {
                    mascota.setNombre(mascotaActualizada.getNombre());
                    mascota.setEdad(mascotaActualizada.getEdad());
                    mascota.setDisponible(mascotaActualizada.getDisponible());
                    mascota.setTipoMascota(mascotaActualizada.getTipoMascota());
                    mascota.setFoto(mascotaActualizada.getFoto());
                    mascota.setTiempo(mascotaActualizada.getTiempo());
                    mascota.setRaza(mascotaActualizada.getRaza());
                    mascota.setVacunado(mascotaActualizada.getVacunado());
                    mascotaRepository.save(mascota);
                    return ResponseEntity.ok().body("Mascota con ID " + id + " actualizada exitosamente.");
                }).orElseGet(() -> {
                    mascotaRepository.save(mascotaActualizada);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Mascota con ID " + id + " no encontrada. No se puede actualizar.");
                }));
    }

    /* Eliminar una mascota por su ID */
    @DeleteMapping("/mascotas/{id}") 
    public ResponseEntity<?> deleteMascota(@PathVariable Long id) {
        Optional<Mascota> mascota = mascotaRepository.findById(id);
    
        if (mascota.isPresent()) {
            mascotaRepository.deleteById(id);
            return ResponseEntity.ok().body("Mascota con ID " + id + " eliminada exitosamente.");
        } else {
            return ResponseEntity.ok().body("Mascota con ID " + id + " no encontrada.");
        }
    }

}
