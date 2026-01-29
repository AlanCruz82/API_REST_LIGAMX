package com.ligamx.ligamx.config;

import com.ligamx.ligamx.service.impl.UserDetailsImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//Clase que define los compomentes basicos necesarios para implementar la autenticacion de usuarios a traves del provedor DAO
public class SecurityConfig {

    @Bean
    //Filtros de seguridad que vamos a personalizar para las request entrantes
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                //Desactivamos la deteccion de la vulnerabilidad CROSS-SITE-REQUEST-FORESTY ya que en nuestra rest no tenemos un formulario
                //en caso de que exista la activamos
                .csrf(csrf -> csrf.disable())
                //Habilitamos la autenticacion basico con sus parametros por defecto
                .httpBasic(Customizer.withDefaults())
                //Evitamos generar un objeto de la sesion
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Definicion de las peticiones http que vamos a autorizar
                .authorizeHttpRequests(http -> {
                    //Autizacion de los endopoints usados por swagger para permitir recibir peticiones desde swagger
                    http.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();

                    //Endpoints publicos que todos van a poder usar
                    http.requestMatchers(HttpMethod.GET, "/api/v1/equipos/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/jugadores/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/partidos/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/torneos/**").permitAll();

                    //Endpoints publicos que solo usuarios y el administrador van a poder usar
                    http.requestMatchers(HttpMethod.POST, "/api/v1/equipos/**").hasAnyRole( "USER", "ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/jugadores/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/partidos/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/torneos/**").hasAnyRole("USER", "ADMIN");

                    //Endpoints publicos que solo los usuarios y el administrador van a poder usar
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/equipos/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/jugadores/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/partidos/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/torneos/**").hasAnyRole("USER", "ADMIN");

                    //Endpoints privados que solo el administrador va a poder usar
                    http.requestMatchers(HttpMethod.DELETE,"/api/v1/equipos/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE,"/api/v1/jugadores/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE,"/api/v1/partidos/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE,"/api/v1/torneos/**").hasRole("ADMIN");

                    //Cualquier otro endpoint solicitado se va a denegar el acceso
                    http.anyRequest().denyAll();
                })
                .build();
    }

    @Bean
    //Bean de configuracion para el autenticador basico de swagger
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }

    @Bean
    //Manejador de autenticacion que va a comunicarse con el proovedor para comprobar la autenticacion del usuario
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        //Obtenemos el manejador de autenticacion que entiende spring security a partir del authenticationConfiguration
        return authenticationConfiguration.getAuthenticationManager();
    }


    //Provedor que vamos a utilizar DAO con sus componentes necesarios: PassEncoder para validar la contrasena del usuario
    //y userDetails (nuestra implementacion hecha) para poder recuperar el usuario de la base de datos y validar sus datos
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsImpl userDetails){
        //Creamos una nueva instancia del provedor de DAO que va a autenticar nuestro usuario en base a su existencia
        //o no en la base de datos con sus credenciales correctas
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider(userDetails);
        daoProvider.setPasswordEncoder(passwordEncoder());

        return daoProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //Regresamos una nueva instancia de BCrypt que va a servir para reconocer si la contrasena enviada en texto
        //plano encaja con el valor hash almacenado en la base de datos
        return new BCryptPasswordEncoder();
    }

}
