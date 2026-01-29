package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.entity.security.UserEntity;
import com.ligamx.ligamx.repository.UserEntityRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsImpl implements UserDetailsService {

    //Bean del repositorio del usuario que nos va a ayudar a encontrar el usuario enviado como autenticacion
    private final UserEntityRepository userEntityRepository;

    public UserDetailsImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    //Metodo que va a buscar al usuario en la base de datos en base a su username y va a regresar el objeto userDetail que
    //entiende spring security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Buscamos al usuario enviado por username en el argumento en la base de datos
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("El usuario " + username + " no se encontro")
        );

        //Convertirmos los roles y permisos del usuario en entidades SimpleGrantedAuthority que entiende y registrar
        //spring security

        //Creamos una lista de tipo SimpleGrantedAuthoritu que va a almacenar los roles y permisos enlazados al usuario enviado
        List<SimpleGrantedAuthority> listaPermisosRoles = new ArrayList<>();

        //Recorremos cada rol de la lista y lo convertimos en un SimpleGrantedAuthority que son los que entiede Spring security
        user.getListaRoles().forEach(
                //Obtenemos el role de la lista y lo convertimos en una nueva instancia de SimpleGrnatedAuthority que vamos
                //a almacenar con el nombre del rol obtenido y con el prefijo por convencion ROLE_
                role -> listaPermisosRoles.add(new SimpleGrantedAuthority("ROLE_" .concat(role.getRoleEnum().name())))
        );

        //De la lista de roles obtenemos cada permiso asociado al role y lo almacenamos en la lista de rolesPermisos
        user.getListaRoles().stream().flatMap(role -> role.getListaPermisos().stream())
                //Usando flatMap obtenemos de cada rol del usuario sus lista de permisos asociados y los convertimos
                //en un stream para poder recorrer cada elemento y convertirlo en un SimpleGrantedAuthority que almacenamos
                //en la lista de rolesPermisos que tenemos, este sin prefij
                .forEach(permiso -> listaPermisosRoles.add(new SimpleGrantedAuthority(permiso.getName())));

        //Construimos el userDetails con los valores de nuestro usuario recuperado de la base de datos
        return new User(user.getUsername(),user.getPassword(),listaPermisosRoles);
    }
}
