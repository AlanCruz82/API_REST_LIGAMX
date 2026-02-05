package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.request.LoginRequestDTO;
import com.ligamx.ligamx.dto.request.RegistrarRequestDTO;
import com.ligamx.ligamx.dto.response.AutentificacionResponse;
import com.ligamx.ligamx.entity.security.Role;
import com.ligamx.ligamx.entity.security.RoleEnum;
import com.ligamx.ligamx.entity.security.UserEntity;
import com.ligamx.ligamx.repository.RoleRepository;
import com.ligamx.ligamx.repository.UserEntityRepository;
import com.ligamx.ligamx.utils.JWTUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsImpl implements UserDetailsService {

    //Bean del repositorio del usuario que nos va a ayudar a encontrar el usuario enviado como autenticacion
    private final UserEntityRepository userEntityRepository;
    //Bean del repositorio del rol que vamos a utilizar para encontrar la lista de roles asignados a un nuevo usuario
    private final RoleRepository roleRepository;
    //Bean de las utilidades del JWT token que nos permite generar y validar los tokens generados y recibidos
    private final JWTUtils jwtUtils;
    //Bean del codificador de contranas para comprobar las contrasenas recibidas con las contrasenas de la base de datos
    private final PasswordEncoder passwordEncoder;

    public UserDetailsImpl(UserEntityRepository userEntityRepository, RoleRepository roleRepository, JWTUtils jwtUtils,
                           PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
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

    public AutentificacionResponse login(LoginRequestDTO loginRequestDTO){
        //Obtenemos las credenciales enviadas en la peticion de login
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        //Verificamos si el usuario con las credenciales enviadas existe en nuestra base de datos
        Authentication authentication = this.autenticacion(username,password);

        //Establecemos la autenticacion del usuario enviado en la peticion dentro del contexto del SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generemoas el tokenJWT del usuario recbido como peticion
        String tokenAcceso = jwtUtils.crearToken(authentication);

        //Regresamos el DTO de respuesta con el usuario que se logeo, el tokenJWT generado y un estatus booleano
        return new AutentificacionResponse(username, username + " logeado con exito", tokenAcceso, true);
    }

    public Authentication autenticacion(String username, String password){
        //Verificamos si el usuario enviado existe en la base de datos
        //EL usuario no puede llegar a ser null ya que en la definicion del metodo loadUser se lanza una exepcion en
        //el caso que suceda ese escenario
        UserDetails usuario = this.loadUserByUsername(username);

        //Verificamos si la contrasena enviada para el usuario no coincide con la que esta almacenada en la base de datos
        if (!passwordEncoder.matches(password, usuario.getPassword())){
            //Lanzamos una excepcion y cortamos el flujo del metodo
            throw new BadCredentialsException("Contrasena incorrecta");
        }

        //Regresamos una nueva instancia de Authenticacion con las credenciales del usuario obtenido de la bd
        return new UsernamePasswordAuthenticationToken(username,usuario.getPassword(),usuario.getAuthorities());
    }

    public AutentificacionResponse registrar(RegistrarRequestDTO nuevoRegistro){
        //Obtenemos las credenciales del nuevo usuario que vamos a guardar en la base de datos
        String username = nuevoRegistro.getUsername();
        String password = nuevoRegistro.getPassword();
        List<RoleEnum> listaPermisosRoles = nuevoRegistro.getListaRoles();

        //Obtenemos la lista de roles que se le asignaron al nuevo usuario y que existen en nuestra base de datos
        //Usamos un set para evitar asignarle roles duplicados
        Set<Role> conjuntoPermisosRoles = new HashSet<>(roleRepository.findByRoleEnumIn(listaPermisosRoles));

        //Creamos el nuevo usuario con las credenciales y roles encontrados en la base de datos
        UserEntity usuario = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .listaRoles(conjuntoPermisosRoles)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .isEnabled(true)
                .credentialNoExpired(true)
                .build();

        //Guardamos el nuevo usuario en la base de datos
        UserEntity usuarioRegistrado = userEntityRepository.save(usuario);

        //Creamos una nueva lista de tipo SimpleGrantedAuthority en la que vamos a almacenar los permisos
        //asignados al nuevo usuario registrado con el tipo que entiende spring security
        List<SimpleGrantedAuthority> permisosRoles = new ArrayList<>();

        //Obtenemos los roles asignados al usuario y los agregamos a la lista de permisosRoles con el prefijo ROLE_ para
        //que spring security los entienda
        usuarioRegistrado.getListaRoles()
                .forEach(rol -> permisosRoles.add(new SimpleGrantedAuthority("ROLE_".concat(rol.getRoleEnum().name()))));

        //Obtenemos la lista de permisos de cada rol y los vamos agreando en la lista de permisosRoles
        usuarioRegistrado.getListaRoles().stream().flatMap(rol -> rol.getListaPermisos().stream())
                .forEach(permiso -> permisosRoles.add(new SimpleGrantedAuthority(permiso.getName())));

        //Creamos un nuevo autentication con las credenciales del usuario registrado y la lista de permsisoRoles asignados
        Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioRegistrado.getUsername(),
                null, permisosRoles);

        //Creamos el token del nuevo usuario que fue registrado
        String jwtAcceso = jwtUtils.crearToken(authentication);

        return new AutentificacionResponse(usuarioRegistrado.getUsername(), usuarioRegistrado.getUsername() + " registrado con exito",
                jwtAcceso, true);
    }
}
