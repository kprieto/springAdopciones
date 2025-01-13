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

import com.kprieto.bootcamp.mascotas.model.TipoMascota;
import com.kprieto.bootcamp.mascotas.repository.TipoMascotaRepository;


@Controller
@RequestMapping("/tipoMascotas")
public class TipoMascotaViewController {
    
    @Autowired
    private TipoMascotaRepository tipoMascotaRepository;

    // Lista todos los tipos de mascotas
    @GetMapping
    public String listTipoMascotas(Model model) {
        model.addAttribute("tipoMascotas",tipoMascotaRepository.findAll());
        return "tipoMascotas";  // Thymeleaf buscará la plantilla 'tipoMascotas.html'
    }

    // Mostrar formulario de agregar el tipo de mascota
    @GetMapping("/new")
    public String showCreateFormTipoMascota(Model model) {
        model.addAttribute("tipoMascota", new TipoMascota());
        return "registrarTM";
    }

    // Registra el tipo de mascota
    @PostMapping("/registrar")
    public String registrarTipoMascota(
        @ModelAttribute TipoMascota tipoMascota,
        RedirectAttributes redirectAttributes,
        Model model){
        // Verificar si ya existe un tipo de mascota con el nombre ingresado
        Boolean existingTipoMascota = tipoMascotaRepository.existsByNombre(tipoMascota.getNombre());
        if (existingTipoMascota) {
            model.addAttribute("error", "El tipo de mascota ya está registrado.");
            return "registrarTM"; // Retorna al formulario con un mensaje de error
        } else {
            tipoMascotaRepository.save(tipoMascota);
            redirectAttributes.addFlashAttribute("mensaje", "Tipo de Mascota registrada con éxito.");
            return "redirect:/tipoMascotas";  
        }

    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String showFormEdicionTipoMascota(@PathVariable Long id, Model model) {
        TipoMascota tipoMascota = tipoMascotaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("tipoMascota", tipoMascota);
        return "registrarTM";
    }

    // Actualizar un tipo de mascota
    @PostMapping("/actualizar/{id}")
    public String actualizarTipoMascota(@PathVariable Long id, @ModelAttribute("tipoMascota") TipoMascota tipoMascotaActualizado, RedirectAttributes redirectAttributes) {
        TipoMascota tipoMascota = tipoMascotaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
    
        tipoMascota.setNombre(tipoMascotaActualizado.getNombre());
        tipoMascotaRepository.save(tipoMascota);
    
        redirectAttributes.addFlashAttribute("mensaje", "Tipo de Mascota actualizado con éxito.");
        return "redirect:/tipoMascotas";
    }
    
    // Eliminar un tipo de mascota
    @GetMapping("/eliminar/{id}")
    public String eliminarTipoMascota(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        TipoMascota tipoMascota = tipoMascotaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        tipoMascotaRepository.delete(tipoMascota);
    
        redirectAttributes.addFlashAttribute("mensaje", "Tipo de Mascota eliminado con éxito.");
        return "redirect:/tipoMascotas";
    }

}
