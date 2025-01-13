package com.kprieto.bootcamp.mascotas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
            .requestMatchers("/login","/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/api/**").permitAll()
            // .requestMatchers("/admin/**").hasRole("ADMIN") // Rutas que solo los ADMIN pueden acceder
            // .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Rutas que los USER o ADMIN pueden acceder
            // .requestMatchers("/login/**").permitAll() // Rutas públicas accesibles por todos
            .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
        )
        .formLogin((form) -> form
        .loginPage("/login") // Página de login personalizada
        .defaultSuccessUrl("/home", true) // Redirigir a /home después de login exitoso
        .permitAll()
        )
        // Configuración para API: Deshabilitar redirecciones al login
        .exceptionHandling((exceptions) -> exceptions
            .authenticationEntryPoint((request, response, authException) -> {
                if (request.getRequestURI().startsWith("/api/")) {
                    // Responder con 401 para las solicitudes de API
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                } else {
                    // Redirigir al login para solicitudes normales
                    response.sendRedirect("/login");
                }
            })
        )
        // Deshabilitar CSRF para APIs REST
        .csrf((csrf) -> csrf
            .ignoringRequestMatchers("/api/**") // Ignorar CSRF solo para rutas de API
        );
        // .logout((logout) -> logout
        //     .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL para manejar logout
        //     .logoutSuccessUrl("/login?logout") // Redirigir después del logout exitoso
        //     .invalidateHttpSession(true) // Invalidar la sesión
        //     .deleteCookies("JSESSIONID") // Borrar la cookie de sesión
        //     .permitAll()
        // );
    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para codificar las contraseñas
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user1")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
