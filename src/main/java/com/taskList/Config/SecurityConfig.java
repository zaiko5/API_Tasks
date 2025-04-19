package com.taskList.Config;

import com.taskList.Auth.JWTAuthenticationFilter;
import com.taskList.Auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Se encarga de configurar la seguridad de toda la aplicación, Qué rutas están protegidas y cuáles no.
//Cómo se maneja la autenticación (por JWT, por sesión, etc.).
//Qué filtros de seguridad se usan (como tu JWTAuthenticationFilter).
//El codificador de contraseñas.
//El manejo de usuarios, roles, permisos.
@Configuration //Marcando que esta será una clase de configuracion.
@EnableWebSecurity //Marcando que se definirá una configuracion propia de seguridad.
public class SecurityConfig {

    @Autowired //Inyectando jwtFilter para
    private JWTAuthenticationFilter jwtFilter;

    //decirle a Spring Security qué debe proteger, cómo debe protegerlo, y con qué filtros debe trabajar.
    @Bean //Definiendo que esta función será un bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //Puede lanzar una excepcion.
        return http
                .csrf(csrf -> csrf.disable()) // 1. Desactiva CSRF (útil para APIs REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 2. No usamos sesiones, usamos JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // 3. Permitimos que todos accedan al login
                        .anyRequest().authenticated() // 4. Todo lo demás necesita estar autenticado
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // 5. Agregamos el filtro JWT antes del de login
                .build(); // 6. Construimos el filtro
    }

    //se utiliza para autenticar las solicitudes de los usuarios dentro de la aplicación, ya sea para login o para la validación de credenciales en cada solicitud.
    @Bean //Puede lanzar una excepcion.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Para encriptar la contraseña encriptada.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
