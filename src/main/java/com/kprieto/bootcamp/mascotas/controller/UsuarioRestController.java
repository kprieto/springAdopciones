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

import com.kprieto.bootcamp.mascotas.model.Usuario;
import com.kprieto.bootcamp.mascotas.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class UsuarioRestController {
    @Autowired
    private final UsuarioRepository usuarioRepository;

    public UsuarioRestController(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    /* Lista todos los usuarios */
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listAll() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }    

    /* Crear un usuario */
    @PostMapping("/usurios")
    public ResponseEntity<?> createAdopcion(@RequestBody Usuario nuevoUsuario, UriComponentsBuilder ucb) {

        Usuario usuarioGuardada = usuarioRepository.save(nuevoUsuario);
        URI uriUsuario = ucb
                .path("usuarios/{id}")
                .buildAndExpand(usuarioGuardada.getId())
                .toUri();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario creado exitosamente.");
        response.put("adopcion", usuarioGuardada);
            
        return ResponseEntity.created(uriUsuario).body(response);
    }

    /* Obtener un usuario por su ID */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Usuario con ID " + id + " no encontrado.");
        
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /* Actualizar un usuario por su ID */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> updateUsuario(@RequestBody Usuario usuarioActualizado, @PathVariable Long id) {
        return ResponseEntity.ok(usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
                    usuario.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
                    usuario.setCorreoElectronico(usuarioActualizado.getCorreoElectronico());
                    usuario.setDireccion(usuarioActualizado.getDireccion());
                    usuario.setEdad(usuarioActualizado.getEdad());
                    usuario.setTelefono(usuarioActualizado.getTelefono());
                    usuarioRepository.save(usuario);
                    return ResponseEntity.ok().body("Usuario con ID " + id + " actualizado exitosamente.");
                }).orElseGet(() -> {
                    usuarioRepository.save(usuarioActualizado);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuario con ID " + id + " no encontrado. No se puede actualizar.");
                }));
    }

    /* Eliminar un usuario por su ID */
    @DeleteMapping("/usuarios/{id}") 
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
    
        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().body("Usuarui con ID " + id + " eliminado exitosamente.");
        } else {
            return ResponseEntity.ok().body("Usuario con ID " + id + " no encontrado.");
        }
    }
}
