package com.kprieto.bootcamp.mascotas.controller;

import java.net.URI;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.kprieto.bootcamp.mascotas.model.Adopcion;
import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.model.TipoMascota;
import com.kprieto.bootcamp.mascotas.model.Usuario;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdopcionControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllAdopcionWhenListIsRequested(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/adopciones", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int adopcionCount = documentContext.read("$.length()");
        assertThat(adopcionCount).isEqualTo(2);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1,2);

        JSONArray adopcionFechas = documentContext.read("$..fechaAdopcion"); 
        Date fecha = new Date();
        assertThat(adopcionFechas).containsExactlyInAnyOrder(fecha, fecha);
    

    }

    @Test
    void shouldReturnAnAdopcionById(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/adopciones/3", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(3);

        String mascotaNombre = documentContext.read("$.mascota"); 
        Mascota mascota = new Mascota(); 
        mascota.setNombre(mascotaNombre); 
        assertThat(mascota.getNombre()).isEqualTo("Jasy");

        
        String usuarioNombre = documentContext.read("$.nombre"); 
        Usuario usuario = new Usuario(); 
        usuario.setNombre(usuarioNombre); 
        assertThat(usuario.getNombre()).isEqualTo("Daniel");

        Date fechaAdopcion = documentContext.read("$.fechaAdopcion");
        Date fechaActual = new Date();
        assertThat(fechaAdopcion).isEqualTo(fechaActual);


    }

    @Test
    void shouldNotReturnAdopcionWithAnUnknowId(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/adopciones/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateNewAdopcion(){
        TipoMascota tipoPerro = new TipoMascota("Perro");
        Usuario user = new Usuario("Daniel", "Lopez", "Flores", "daniel@gmail.com", 
        "Calle Juarez Zona Centro", 25, "646-162-36-25");
        Mascota mascota = new Mascota("Moyo", tipoPerro, 2, true,"Jasy.jpg","Años","Mestizo", true);
        Date fechaActual = new Date();
        Adopcion adopcion = new Adopcion(fechaActual, mascota, user);
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/adopciones", adopcion, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location= response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");


        assertThat(id).isNotNull();
        String mascotaNombre = documentContext.read("$.mascota"); 
        Mascota mascotaA = new Mascota(); 
        mascotaA.setNombre(mascotaNombre); 
        assertThat(mascotaA.getNombre()).isEqualTo("Moyo");

        
        String usuarioNombre = documentContext.read("$.nombre"); 
        Usuario usuario = new Usuario(); 
        usuario.setNombre(usuarioNombre); 
        assertThat(usuario.getNombre()).isEqualTo("Daniel");

        Date fechaAdopcion = documentContext.read("$.fechaAdopcion");
        assertThat(fechaAdopcion).isEqualTo(fechaActual);


    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingAdopcion(){
        TipoMascota tipoPerro = new TipoMascota("Perro");
        Usuario user = new Usuario("Daniel", "Lopez", "Flores", "daniel@gmail.com", 
        "Calle Juarez Zona Centro", 25, "646-162-36-25");
        Mascota mascota = new Mascota("Jasy", tipoPerro, 2, true,"Jasy.jpg","Años","Mestizo", true);
        Date fechaActual = new Date();
        Adopcion adopcion = new Adopcion(fechaActual, mascota, user);
        HttpEntity<Adopcion> request = new HttpEntity<>(adopcion);

        ResponseEntity<Void> response = restTemplate.exchange("/api/mascotas/1",HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/mascotas/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String mascotaNombre = documentContext.read("$.mascota"); 
        Mascota mascotaA = new Mascota(); 
        mascotaA.setNombre(mascotaNombre); 
        assertThat(mascotaA.getNombre()).isEqualTo("Moyo");

        
        String usuarioNombre = documentContext.read("$.nombre"); 
        Usuario usuario = new Usuario(); 
        usuario.setNombre(usuarioNombre); 
        assertThat(usuario.getNombre()).isEqualTo("Daniel");

        Date fechaAdopcion = documentContext.read("$.fechaAdopcion");
        assertThat(fechaAdopcion).isEqualTo(fechaActual);
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnAdopcionById(){
        ResponseEntity<Void> response = restTemplate.exchange("/api/adopciones/3", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/adopciones/3", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);



    }
    
}
