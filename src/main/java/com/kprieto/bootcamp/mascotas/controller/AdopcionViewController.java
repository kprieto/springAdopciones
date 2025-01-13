package com.kprieto.bootcamp.mascotas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kprieto.bootcamp.mascotas.model.Adopcion;
import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.model.Usuario;
import com.kprieto.bootcamp.mascotas.repository.AdopcionRepository;
import com.kprieto.bootcamp.mascotas.repository.MascotaRepository;
import com.kprieto.bootcamp.mascotas.repository.UsuarioRepository;




@Controller()
@RequestMapping("/adopciones")
public class AdopcionViewController {

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Lista todas las adopciones
    @GetMapping
    public String listAdopcion(Model model) {
        model.addAttribute("adopciones",adopcionRepository.findAll());
        return "adopciones";  // Thymeleaf buscará la plantilla 'adopciones.html'
    }

    // Mostrar formulario de agregar una adopción
    @GetMapping("/new")
    public String showCreateFormAdopcion(Model model) {
        List<Mascota> listaMascotas = mascotaRepository.findByDisponibleTrue();
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        model.addAttribute("listaUsuarios", listaUsuarios);
        model.addAttribute("listaMascotas", listaMascotas);
        model.addAttribute("adopcion", new Adopcion());
        return "registrarAdop";
    }

    // Registra una adopcion
    @PostMapping("/registrar")
    public String registrarAdopcion(
        @ModelAttribute Adopcion adopcion,
        RedirectAttributes redirectAttributes,
        Model model){
        mascotaRepository.actualizarDisponibilidad(adopcion.getMascota().getId());    

        adopcionRepository.save(adopcion);
        redirectAttributes.addFlashAttribute("mensaje", "Adopción registrada con éxito.");
        return "redirect:/adopciones";  

    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String showFormEdicionAdopcion(@PathVariable Long id, Model model) {
        Adopcion adopcion = adopcionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<Mascota> listaMascotas = mascotaRepository.findAll();
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        model.addAttribute("listaUsuarios", listaUsuarios);
        model.addAttribute("listaMascotas", listaMascotas);
        model.addAttribute("adopcion", adopcion);
        return "registrarAdop";
    }

    // Actualizar información de una adopción
    @PostMapping("/actualizar/{id}")
    public String actualizarAdopcion(@PathVariable Long id, @ModelAttribute("adopcion") Adopcion adopcionActualizada, RedirectAttributes redirectAttributes) {
        Adopcion adopcion = adopcionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
    
        adopcion.setFechaAdopcion(adopcionActualizada.getFechaAdopcion());
        adopcion.setMascota(adopcionActualizada.getMascota());
        adopcion.setUsuario(adopcionActualizada.getUsuario());
        adopcionRepository.save(adopcion);
    
        redirectAttributes.addFlashAttribute("mensaje", "Adopción actualizada con éxito.");
        return "redirect:/adopciones";
    }
    
    // Eliminar una adopción
    @GetMapping("/eliminar/{id}")
    public String eliminarAdopcion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Adopcion adopcion = adopcionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        mascotaRepository.actualizarDisponibilidadTrue(adopcion.getMascota().getId());    
        adopcionRepository.delete(adopcion);
    
        redirectAttributes.addFlashAttribute("mensaje", "Adopción eliminada con éxito.");
        return "redirect:/adopciones";
    }
}
