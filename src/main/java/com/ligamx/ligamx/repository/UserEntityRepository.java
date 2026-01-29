package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
    //Busquda simple para encontrar el usuario en base a su username que va a utilizar el servicio de detalles de usuario
    Optional<UserEntity> findByUsername(String username);
}
