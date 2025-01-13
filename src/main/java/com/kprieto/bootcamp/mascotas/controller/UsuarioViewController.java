package com.kprieto.bootcamp.mascotas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kprieto.bootcamp.mascotas.model.Usuario;
import com.kprieto.bootcamp.mascotas.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Lista todos los usuarios
    @GetMapping
    public String listUsuarios(Model model) {
        model.addAttribute("usuarios",usuarioRepository.findAll());
        return "usuarios";  // Thymeleaf buscará la plantilla 'usuarios.html'
    }

    // Mostrar formulario de agregar a un usuario
    @GetMapping("/new")
    public String showCreateFormUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrarU";
    }

    // Registra a un usuario
    @PostMapping("/registrar")
    public String registrarUsuario(
        @ModelAttribute Usuario usuario,
        RedirectAttributes redirectAttributes,
        Model model){
        // Verificar si ya existe un usuario con el nombre ingresado
        Boolean existingUsuario = usuarioRepository.existsByNombreAndApellidoPaternoAndApellidoMaterno(usuario.getNombre(), usuario.getApellidoPaterno(), usuario.getApellidoMaterno());
        if (existingUsuario) {
            model.addAttribute("error", "El usuario ya está registrado.");
            return "registrarU"; // Retorna al formulario con un mensaje de error
        } else {
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado con éxito.");
            return "redirect:/usuarios";  
        }

    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String showFormEdicionUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("usuario", usuario);
        return "registrarU";
    }

    // Actualizar un usuario
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuarioActualizado, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
    
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
        usuario.setCorreoElectronico(usuarioActualizado.getCorreoElectronico());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setEdad(usuarioActualizado.getEdad());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuarioRepository.save(usuario);
    
        redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado con éxito.");
        return "redirect:/usuarios";
    }
    
    // Eliminar un usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        usuarioRepository.delete(usuario);    
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado con éxito.");
        return "redirect:/usuarios";
    }
}
