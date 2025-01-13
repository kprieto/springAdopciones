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
import com.kprieto.bootcamp.mascotas.model.TipoMascota;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TipoMascotaControllerTest {
        @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllTipoMascotasWhenListIsRequested(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/tipoMascotas", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int tipoMascotasCount = documentContext.read("$.length()");
        assertThat(tipoMascotasCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1,2,3);

        JSONArray tipoMascotas = documentContext.read("$..nombre");
        assertThat(tipoMascotas).containsExactlyInAnyOrder("Perro", "Gato", "Pez");
    

    }

    @Test
    void shouldReturnAnTipoMascotaById(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/tipoMascotas/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(2);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Perro");

        
    }

    @Test
    void shouldNotReturnTipoMascotaWithAnUnknowId(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/tipoMascotas/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateNewTipoMascota(){
        TipoMascota tipoMascota = new TipoMascota("Pez");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/tipoMascotas", tipoMascota, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location= response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");


        assertThat(id).isNotNull();
        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Pez");

        
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingTipoMascota(){
        TipoMascota tipoMascota = new TipoMascota("Perro");

        HttpEntity<TipoMascota> request = new HttpEntity<>(tipoMascota);

        ResponseEntity<Void> response = restTemplate.exchange("/api/mascotas/1",HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/mascotas/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Perro");

        
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnTipoMascotaById(){
        ResponseEntity<Void> response = restTemplate.exchange("/api/tipoMascotas/3", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/tipoMascotas/3", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);



    }
}
