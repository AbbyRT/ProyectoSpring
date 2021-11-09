package com.generation.SpringSecurityJWT.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.generation.SpringSecurityJWT.security.Constants.HEADER_AUTHORIZACION_KEY;
import static com.generation.SpringSecurityJWT.security.Constants.LOGIN_URL;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    //configuracion de spring security WebSecurityConfigurerAdapter

    private UserDetailsService userDetailsService; //clssdr pra hacer config de usuarios para login

    public WebSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    //inyeccion de dependencia
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //devuelve new bcrypt que es para encriptar contraseñas,

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /*
         * 1. Se desactiva el uso de cookies
         * 2. Se activa la configuración CORS con los valores por defecto
         * 3. Se desactiva el filtro CSRF
         * 4. Se indica que el login no requiere autenticación
         * 5. Se indica que el resto de URLs esten securizadas
         */
        httpSecurity //peticion sin estado, se esta desactivando cookies, se va a trabajar con jwt
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and() //cross over resource sharing, bloquea accesos de ciertas paginas, si recibe petiiones de otras pags lo va a bloquear
                .csrf().disable() // se desactiva filtro CRF cross side request forbide
                .authorizeRequests().antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                //se indica que peticiones seran permitidas (login por post), en las que no se necesitara el token
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .anyRequest().authenticated().and()
                //solo se dejan 2 rutas libres, las demas estaran protegidas
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                //middle where, filtro que hara el login, si se completa devolvera el token
                .addFilter(new JWTAuthorizationFilter(authenticationManager()));
                //verificara el token de cada peticion y dara acceso o no
    }

    //mandara autenticacion
    //ya tendra los datos del usuario userDetailsService
    //password encoder es un cifrado en una sola via, una vez cifrada no se puede descifrar
    //compara coontraseñas, las encripta  y da acceso
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Se define la clase que recupera los usuarios y el algoritmo para procesar las passwords
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    //corse cross over sharing - bloquea acceso desde otros lados
    //"/**" indica que puede recibir peticiones de todos lados
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.applyPermitDefaultValues().addExposedHeader(HEADER_AUTHORIZACION_KEY);
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
