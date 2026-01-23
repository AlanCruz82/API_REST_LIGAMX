package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.NombreTorneo;
import com.ligamx.ligamx.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo,Long> {

    //Busqueda compuesta para encontrar el torneo en base a su nombre [APERTURA/CLAUSURA] y su año
    Optional<Torneo> findByNombreAndAnio(NombreTorneo nombre, Integer anio);

    //Misma busqueda compuesta para validar si ya existe un torneo del mismo tipo creado
    boolean existsByNombreAndAnio(NombreTorneo nombre, Integer anio);
}
