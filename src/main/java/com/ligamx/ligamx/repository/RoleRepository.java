package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.security.Role;
import com.ligamx.ligamx.entity.security.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    //Consulta para obtener la lista de roles que coincidan con los que tenemos almacenados en la base de datos
    List<Role> findByRoleEnumIn(List<RoleEnum> listaRoles);
}
