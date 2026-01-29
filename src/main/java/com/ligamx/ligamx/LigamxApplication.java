package com.ligamx.ligamx;

import com.ligamx.ligamx.entity.security.Permission;
import com.ligamx.ligamx.entity.security.Role;
import com.ligamx.ligamx.entity.security.RoleEnum;
import com.ligamx.ligamx.entity.security.UserEntity;
import com.ligamx.ligamx.repository.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class LigamxApplication {

	public static void main(String[] args) {
		SpringApplication.run(LigamxApplication.class, args);
	}

	//Metodo para crear y almacenar los usuarios en la base de datos con sus atributos definidos
//	@Bean
//	public CommandLineRunner init(UserEntityRepository userEntityRepository){
//		return args -> {
//			//Permisos que vamos a definir pueden tener los diferentes roles
//			Permission crear = Permission.builder()
//					.name("CREATE")
//					.build();
//
//			Permission leer = Permission.builder()
//					.name("READ")
//					.build();
//
//			Permission editar = Permission.builder()
//					.name("UPDATE")
//					.build();
//
//			Permission eliminar = Permission.builder()
//					.name("DELETE")
//					.build();
//
//			//Roles que van a poder tener los usuarios
//			Role admin = Role.builder()
//					.roleEnum(RoleEnum.ADMIN)
//					.listaPermisos(Set.of(crear,leer,editar,eliminar))
//					.build();
//
//			Role user = Role.builder()
//					.roleEnum(RoleEnum.USER)
//					.listaPermisos(Set.of(crear,leer,editar))
//					.build();
//
//			//Uusarios que vamos a tener en la base de datos disponibles para usar
//			UserEntity german = UserEntity.builder()
//					.username("German")
//					.password("$2a$10$dkj5YUDroYTZC1O7Zbt4dufFrqTxXW6s6vERknavRNqd4uIpgvEey")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.listaRoles(Set.of(admin))
//					.build();
//
//			UserEntity juan = UserEntity.builder()
//					.username("Juan")
//					.password("$2a$10$CO.RN4pAXFlsQGvUeaF6hO6/IZ79EJVyZo1WQFZPAhq4HQhC2KNni")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.listaRoles(Set.of(user))
//					.build();
//
//			//Almacenamos los usuarios construidos con sus roles y permisos definidos
//			userEntityRepository.saveAll(List.of(german,juan));
//		};
//	}
}
