package com.kprieto.bootcamp.mascotas.init;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kprieto.bootcamp.mascotas.model.Adopcion;
import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.model.TipoMascota;
import com.kprieto.bootcamp.mascotas.model.Usuario;
import com.kprieto.bootcamp.mascotas.repository.AdopcionRepository;
import com.kprieto.bootcamp.mascotas.repository.MascotaRepository;
import com.kprieto.bootcamp.mascotas.repository.TipoMascotaRepository;
import com.kprieto.bootcamp.mascotas.repository.UsuarioRepository;


@Configuration 
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(MascotaRepository mascotaRepository, TipoMascotaRepository tipoMascotaRepository, 
                                UsuarioRepository usuarioRepository, AdopcionRepository adopcionRepository) {
        return args -> {
            if (mascotaRepository.count() == 0 && tipoMascotaRepository.count() == 0 && usuarioRepository.count() == 0 && adopcionRepository.count() == 0){
                Usuario user = new Usuario("Daniel", "Lopez", "Flores", "daniel@gmail.com", 
                        "Calle Juarez Zona Centro", 25, "646-162-36-25");
                Usuario user2 = new Usuario("Lorena", "Martinez", "Suarez", "lorena@gmail.com", 
                        "Calle Mateos Zona Centro", 35, "646-152-85-14");

                log.info("Guardado Usuario: {}", usuarioRepository.save(user));
                log.info("Guardado Usuario: {}", usuarioRepository.save(user2));

                TipoMascota tipoPerro = new TipoMascota("Perro");
                TipoMascota tipoGato = new TipoMascota("Gato");

                log.info("Guardado TM: {}", tipoMascotaRepository.save(tipoPerro));
                log.info("Guardado TM: {}", tipoMascotaRepository.save(tipoGato));

                Mascota perro = new Mascota("Jasy", tipoPerro, 2, false, "Jasy.jpg","meses","mestizo",true);
                Mascota gato = new Mascota("Coco", tipoGato, 3, false, "Coco.jpg", "meses","común europeo",true);
                
                log.info("Guardado Mascota: {}", mascotaRepository.save(perro));
                log.info("Guardado Mascota: {}", mascotaRepository.save(gato));

                Date fechaActual = new Date();
                Adopcion adopcionPerro = new Adopcion(fechaActual, perro, user);
                Adopcion adopcionGato = new Adopcion(fechaActual, gato, user2);

                log.info("Guardado Adopcion: {}", adopcionRepository.save(adopcionPerro));
                log.info("Guardado Adopcion: {}", adopcionRepository.save(adopcionGato));



            }
            


        };
    }
}
