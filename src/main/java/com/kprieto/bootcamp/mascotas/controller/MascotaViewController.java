package com.kprieto.bootcamp.mascotas.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.model.TipoMascota;
import com.kprieto.bootcamp.mascotas.repository.MascotaRepository;
import com.kprieto.bootcamp.mascotas.repository.TipoMascotaRepository;

@Controller
@RequestMapping("/mascotas")
public class MascotaViewController {
    

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private TipoMascotaRepository tipoMascotaRepository;

    // Ruta donde se guardarán las imágenes
    @Value("${ruta.imagenes}")
    private String uploadDirectory;

    @GetMapping
    public String listMascotas(Model model) {
        model.addAttribute("mascotas", mascotaRepository.findByDisponibleTrue());
        return "mascotas";  // Thymeleaf buscará la plantilla 'mascotas.html'
    }

    @GetMapping("/new")
    public String showCreateFormMascota(Model model) {
        List<TipoMascota> listaMascotas = tipoMascotaRepository.findAll();
        model.addAttribute("listaMascotas", listaMascotas);
        model.addAttribute("mascota", new Mascota());
        return "registrarM";
    }

    // Registrar a la mascota
    @PostMapping("/registrar")
    public String registrarMascota(
            @ModelAttribute Mascota mascota,
            @RequestParam("image") MultipartFile imagen,
            RedirectAttributes redirectAttributes,
            Model model) {
         // Verificar si ya existe una mascota con el nombre ingresado
    
        Boolean existingMascota = mascotaRepository.existsByNombre(mascota.getNombre());
        if (existingMascota) {
            model.addAttribute("error", "La mascota ya está registrada.");
            return "registrarM"; // Retorna al formulario con un mensaje de error
        } else {
                if (!imagen.isEmpty()){
                    Path directorioImagenes = Paths.get(uploadDirectory);
                    String ruta = directorioImagenes.toFile().getAbsolutePath();
                    try {
                        byte [] bytesImg = imagen.getBytes();
                        Path rutaCompleta = Paths.get(ruta + File.separator + imagen.getOriginalFilename());
                        Files.write(rutaCompleta, bytesImg);
                        mascota.setFoto(imagen.getOriginalFilename());
                        mascotaRepository.save(mascota);
                        redirectAttributes.addFlashAttribute("mensaje", "Mascota registrada con éxito.");
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("error", "Mascota no pudo ser registrada con éxito.");
                    }
                }
                    


                }
                return "redirect:/mascotas"; 
        }            


    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String showFormEdicionMascota(@PathVariable Long id, Model model) {
        Mascota mascota = mascotaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<TipoMascota> listaMascotas = tipoMascotaRepository.findAll();
        model.addAttribute("listaMascotas", listaMascotas);
        model.addAttribute("mascota", mascota);
        return "registrarM";
    }

    // Actualizar información de una mascota
    @PostMapping("/actualizar/{id}")
    public String actualizarMascota(@PathVariable Long id, @ModelAttribute("mascota") Mascota mascotaActualizado, RedirectAttributes redirectAttributes) {
        Mascota mascota = mascotaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
    
        mascota.setNombre(mascotaActualizado.getNombre());
        mascota.setTipoMascota(mascotaActualizado.getTipoMascota());
        mascota.setEdad(mascotaActualizado.getEdad());
        mascota.setTiempo(mascotaActualizado.getTiempo());
        mascota.setDisponible(mascotaActualizado.getDisponible());
        mascota.setRaza(mascotaActualizado.getRaza());
        mascota.setVacunado(mascotaActualizado.getVacunado());
        mascota.setFoto(mascotaActualizado.getFoto());
        mascotaRepository.save(mascota);
    
        redirectAttributes.addFlashAttribute("mensaje", "Mascota actualizada con éxito.");
        return "redirect:/mascotas";
    }
    
    // Eliminar una mascota
    @GetMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Mascota mascota = mascotaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        mascotaRepository.delete(mascota);
    
        redirectAttributes.addFlashAttribute("mensaje", "Mascota eliminada con éxito.");
        return "redirect:/mascotas";
    }


}
