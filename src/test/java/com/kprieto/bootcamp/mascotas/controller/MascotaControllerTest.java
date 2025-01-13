package com.kprieto.bootcamp.mascotas.controller;

import java.net.URI;

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
import com.kprieto.bootcamp.mascotas.model.Mascota;
import com.kprieto.bootcamp.mascotas.model.TipoMascota;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MascotaControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllMascotasWhenListIsRequested(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/mascotas", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int mascotasCount = documentContext.read("$.length()");
        assertThat(mascotasCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1,2,3);

        JSONArray mascotas = documentContext.read("$..nombre");
        assertThat(mascotas).containsExactlyInAnyOrder("Jasy", "Coco", "Nemo");
    

    }

    @Test
    void shouldReturnAnMascotaById(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/mascotas/3", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(3);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Jasy");

        String tipoMascotaNombre = documentContext.read("$.tipoMascota"); 
        TipoMascota tipoMascota = new TipoMascota(); 
        tipoMascota.setNombre(tipoMascotaNombre); 
        assertThat(tipoMascota.getNombre()).isEqualTo("Perro");

        Number edad = documentContext.read("$.edad");
        assertThat(edad).isEqualTo(2);

        Boolean disponible = documentContext.read("$.disponible");
        assertThat(disponible).isEqualTo(true);

        String foto = documentContext.read("$.foto");
        assertThat(foto).isEqualTo("Jasy.jpg");

        String tiempo = documentContext.read("$.tiempo");
        assertThat(tiempo).isEqualTo("Años");

        String raza = documentContext.read("$.raza");
        assertThat(raza).isEqualTo("Meztizo");

        Boolean vacunado = documentContext.read("$.vacunado");
        assertThat(vacunado).isEqualTo(true);

    }

    @Test
    void shouldNotReturnMascotaWithAnUnknowId(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/mascotas/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateNewMascota(){
        TipoMascota tipoPerro = new TipoMascota("Perro");

        Mascota mascota = new Mascota("Moyo", tipoPerro, 2, true,"Jasy.jpg","Años","Mestizo", true);
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/mascotas", mascota, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location= response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");


        assertThat(id).isNotNull();
        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Moyo");

        String tipoMascotaNombre = documentContext.read("$.tipoMascota"); 
        TipoMascota tipoMascota = new TipoMascota(); 
        tipoMascota.setNombre(tipoMascotaNombre); 
        assertThat(tipoMascota.getNombre()).isEqualTo("Perro");

        Number edad = documentContext.read("$.edad");
        assertThat(edad).isEqualTo(2);

        Boolean disponible = documentContext.read("$.disponible");
        assertThat(disponible).isEqualTo(true);

        String foto = documentContext.read("$.foto");
        assertThat(foto).isEqualTo("Moyo.jpg");

        String tiempo = documentContext.read("$.tiempo");
        assertThat(tiempo).isEqualTo("Años");

        String raza = documentContext.read("$.raza");
        assertThat(raza).isEqualTo("Meztizo");

        Boolean vacunado = documentContext.read("$.vacunado");
        assertThat(vacunado).isEqualTo(true);


    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingMascota(){
        TipoMascota tipoPerro = new TipoMascota("Perro");

        Mascota mascota = new Mascota("Fiona", tipoPerro, 2, true,"Jasy.jpg","Años","Mestizo", true);
        HttpEntity<Mascota> request = new HttpEntity<>(mascota);

        ResponseEntity<Void> response = restTemplate.exchange("/api/mascotas/1",HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/mascotas/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Fiona");

        String tipoMascotaNombre = documentContext.read("$.tipoMascota"); 
        TipoMascota tipoMascota = new TipoMascota(); 
        tipoMascota.setNombre(tipoMascotaNombre); 
        assertThat(tipoMascota.getNombre()).isEqualTo("Perro");

        Number edad = documentContext.read("$.edad");
        assertThat(edad).isEqualTo(2);

        Boolean disponible = documentContext.read("$.disponible");
        assertThat(disponible).isEqualTo(true);

        String foto = documentContext.read("$.foto");
        assertThat(foto).isEqualTo("Fiona.jpg");

        String tiempo = documentContext.read("$.tiempo");
        assertThat(tiempo).isEqualTo("Años");

        String raza = documentContext.read("$.raza");
        assertThat(raza).isEqualTo("Meztizo");

        Boolean vacunado = documentContext.read("$.vacunado");
        assertThat(vacunado).isEqualTo(true);
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnMascotaById(){
        ResponseEntity<Void> response = restTemplate.exchange("/api/mascotas/3", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/mascotas/3", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);



    }
}
