package com.kprieto.bootcamp.mascotas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller()
public class LoginController {
    
    @GetMapping("/login")
    public String login() {
        return "login"; // Nombre del archivo HTML del formulario de login
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Página después del login
    }
}
