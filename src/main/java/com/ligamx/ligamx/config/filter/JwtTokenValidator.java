package com.ligamx.ligamx.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ligamx.ligamx.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

//Heredando OncePerRequestFilter nos aseugramos que a cada peticion hecha se le aplique la validacion del tokenJWT
public class JwtTokenValidator extends OncePerRequestFilter {

    //Bean de las utilidades del token JWT que nos va a permitir validar y obtener las propiedades del token
    private final JWTUtils jwtUtils;

    public JwtTokenValidator(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        //Obtenemos el token del usuario por el header de la peticion (Aun con el bearer)
        String tokenJWT = request.getHeader(HttpHeaders.AUTHORIZATION);

        //Si el token no es vacio o no fue enviado
        if (tokenJWT != null){
            //Obtenemos el token de acceso del usuario desplazando el inicio de la cadena de caracteres hasta el 7 indice
            //ignorando el "bearer "
            tokenJWT = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);

            //Obtenemos el JWT decodificado a la vez que validamos su autenticidad para poder leer sus propiedades
            DecodedJWT jwtDecodificado = jwtUtils.validarToken(tokenJWT);

            //Obtenemos el usuario al que le pertenece el token validado
            String usuario = jwtUtils.obtenerUsuario(jwtDecodificado);

            //Obtenemos el claim de los permisosRoles que tiene el usuario en el tokenJWT enviado
            String permisosRoles = jwtUtils.obtenerClaim(jwtDecodificado, "permisosRoles").asString();

            //Creamos una coleccion de cualquier tipo que herede de GrantedAuthority donde vamos a almacenar los permisosRoles
            //del usuario del token separados por coma
            Collection<? extends GrantedAuthority> listaPermisosRoles = AuthorityUtils.commaSeparatedStringToAuthorityList(permisosRoles);

            //Obtenemos el contexto del compomente SecurityContextHolder y lo almacenamos para poder establecer el usuario
            //autenticado con sus permisosRoles
            SecurityContext contexto = SecurityContextHolder.getContext();

            //Generemoas un nuevo objeto del tipo Authentication con los valores del usuario y permisosRoles obtenidos del
            //token enviado
            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario,null, listaPermisosRoles);
            //Establecemos la autenticacion del usuario enviado dentro del token con sus permisos y roles dentro del componente
            //SecurityContextHolder para que pueda realizar las operaciones permitidas en sus roles
            contexto.setAuthentication(authentication);
            SecurityContextHolder.setContext(contexto);
        }

        //En caso de que el header de la peticion sea vacia regresamos la peticion a la cadena de filtros para que sea
        //rechazada
        filterChain.doFilter(request,response);
    }
}
