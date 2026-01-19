package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    Optional<Equipo> findByNombre(String nombre);
    List<Equipo> findByCiudad(String ciudad);
    List<Equipo> findByEstadio(String estadio);
    boolean existsByNombre(String nombre);
}
