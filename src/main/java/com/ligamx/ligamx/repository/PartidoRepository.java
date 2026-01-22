package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartidoRepository extends JpaRepository<Partido,Long> {

    //Busqueda simple de un partido por su torneo id
    List<Partido> findByTorneoId(Long idTorneo);

    //Busqueda compuesta de un partido en base a su torneo id y la jornada especificada en el parametro
    Optional<Partido> findByTorneoIdAndJornada(Long idTorneo, int jornada);
}
