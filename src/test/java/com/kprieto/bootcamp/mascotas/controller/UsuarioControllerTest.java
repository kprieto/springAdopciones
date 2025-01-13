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
import com.kprieto.bootcamp.mascotas.model.Usuario;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllUsuariosWhenListIsRequested(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/usuarios", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int usuariosCount = documentContext.read("$.length()");
        assertThat(usuariosCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1,2,3);

        JSONArray usuarios = documentContext.read("$..nombre");
        assertThat(usuarios).containsExactlyInAnyOrder("Julio", "Romina", "Daniel");
    

    }

    @Test
    void shouldReturnAnUsuarioById(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/usuarios/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(2);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Julio");

        String apellidoPaterno = documentContext.read("$.apellidoPaterno");
        assertThat(apellidoPaterno).isEqualTo("Roblez");

        String apellidoMaterno = documentContext.read("$.apellidoMaterno");
        assertThat(apellidoMaterno).isEqualTo("Fuentes");

        String correoElectronico = documentContext.read("$.correoElectronico");
        assertThat(correoElectronico).isEqualTo("julio@gmail.com");

        Number edad = documentContext.read("$.edad");
        assertThat(edad).isEqualTo(35);

        String direccion = documentContext.read("$.direccion");
        assertThat(direccion).isEqualTo("Calle Juarez Zona Centro");

        String telefono = documentContext.read("$.telefono");
        assertThat(telefono).isEqualTo("646-163-58-25");

    }

    @Test
    void shouldNotReturnUsuarioWithAnUnknowId(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/usuarios/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateNewUsuario(){

        Usuario usuario = new Usuario("Maria", "Parra", "Torres", "maria@gmail.com", "Calle Sotero Villas", 45, "646-126-69-98");
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/usuarios", usuario, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location= response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");


        assertThat(id).isNotNull();
        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Maria");

        String apellidoPaterno = documentContext.read("$.apellidoPaterno");
        assertThat(apellidoPaterno).isEqualTo("Parra");

        String apellidoMaterno = documentContext.read("$.apellidoMaterno");
        assertThat(apellidoMaterno).isEqualTo("Torres");

        String correoElectronico = documentContext.read("$.correoElectronico");
        assertThat(correoElectronico).isEqualTo("maria@gmail.com");

        Number edad = documentContext.read("$.edad");
        assertThat(edad).isEqualTo(45);

        String direccion = documentContext.read("$.direccion");
        assertThat(direccion).isEqualTo("Calle Sotero Villas");

        String telefono = documentContext.read("$.telefono");
        assertThat(telefono).isEqualTo("646-126-69-98");


    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingUsuario(){

        Usuario usuario = new Usuario("Maria", "Parra", "Torres", "maria@gmail.com", "Calle Sotero Villas", 45, "646-126-69-98");
        HttpEntity<Usuario> request = new HttpEntity<>(usuario);

        ResponseEntity<Void> response = restTemplate.exchange("/api/usuarios/1",HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/usuarios/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(3);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Juan");

        String apellidoPaterno = documentContext.read("$.apellidoPaterno");
        assertThat(apellidoPaterno).isEqualTo("Ramblaz");

        String apellidoMaterno = documentContext.read("$.apellidoMaterno");
        assertThat(apellidoMaterno).isEqualTo("Prieto");

        String correoElectronico = documentContext.read("$.correoElectronico");
        assertThat(correoElectronico).isEqualTo("juan@gmail.com");

        Number edad = documentContext.read("$.edad");
        assertThat(edad).isEqualTo(49);

        String direccion = documentContext.read("$.direccion");
        assertThat(direccion).isEqualTo("Calle Juarez Zona Centro");

        String telefono = documentContext.read("$.telefono");
        assertThat(telefono).isEqualTo("646-115-98-22");
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnUsuarioById(){
        ResponseEntity<Void> response = restTemplate.exchange("/api/usuarios/3", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/usuarios/3", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);



    }
    
}
