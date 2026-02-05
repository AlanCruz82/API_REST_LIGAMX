package com.ligamx.ligamx.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    @Value("${GENERATE_USER}")
    String userGenerator;
    @Value("${PRIVATE_KEY}")
    String privateKey;

    public String crearToken(Authentication authentication){
        //Establecemos el algoritmo con el que va a ser encriptada la llave privada
        Algorithm algoritmo = Algorithm.HMAC256(this.privateKey);

        //Obtenemos el usuario y sus roles-permisos del security context holder
        String usuario = authentication.getPrincipal().toString();

        //Los permisos-roles los unimos por una separacion de coma a la vez que los obtenemos del GrantedAuthority con
        //su getter definido
        String permisosRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        //Creamos el JWT con los claims necesarios para describir el header, payload y signature del JWT
        return JWT.create()
                .withIssuer(this.userGenerator) //Usuario que genera el token (backend)
                .withSubject(usuario) //Uusario al que le pertenece el token (persona del request)
                .withClaim("permisosRoles", permisosRoles) //Claim personalizado para mostrar los permisosRoles que tiene
                .withIssuedAt(new Date()) //Fecha de creacion del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 18000000)) //Fecha de expiracion del token (30min)
                .withJWTId(UUID.randomUUID().toString()) //Id del JWT generado de forma random con UUID para que sea unico
                .withNotBefore(new Date(System.currentTimeMillis())) //Fecha a partir de la que debe ser valido el token (Ahora)
                .sign(algoritmo); //Firma del JWT (la clave privada que definimos previamente)
    }

    public DecodedJWT validarToken(String tokenJWT){
        try {
            //Firma con la que debe estar firmado el token
            Algorithm algoritmo = Algorithm.HMAC256(this.privateKey);

            //Verificador de JWT que valida en base a la firma con la que creamos los tokens y el usuario que los genera (backend)
            JWTVerifier verificador = JWT.require(algoritmo)
                    .withIssuer(this.userGenerator).build();

            //Validamos el token enviado como argumento
            return verificador.verify(tokenJWT);

        }catch(JWTVerificationException exception){
            //Validamos la excepcion en caso de una firma no valida o un claim no valido
            throw new JWTVerificationException("Token no valido");
        }
    }

    public String obtenerUsuario(DecodedJWT tokenJWT){
        return tokenJWT.getSubject();
    }

    public Claim obtenerClaim(DecodedJWT tokenJWT, String claim){
        return tokenJWT.getClaim(claim);
    }

    public Map<String,Claim> obtenerTodosClaims(DecodedJWT tokenJWT){
        return tokenJWT.getClaims();
    }
}
