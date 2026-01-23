package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.DetalleTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleTorneoRepository extends JpaRepository<DetalleTorneo,Long> {

    //Busqueda compuesta para encontrar un equipo en un torneo
    Optional<DetalleTorneo> findByTorneoIdAndEquipoId(Long idTorneo, Long idEquipo);
}
