package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.DetallePartido;
import com.ligamx.ligamx.entity.RolPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePartidoRepository extends JpaRepository<DetallePartido,Long> {

    //Busqueda compuesta para los partidos de un equipo en un torneo y rol dado (LOCAL/VISITANTE)
    List<DetallePartido> findByPartidoTorneoIdAndEquipoIdAndRolEquipo(Long idTorneo, Long idEquipo, RolPartido rolEquipo);

    //Busqueda compuesta de los detalles del partido disputado por un equipo en un torneo dado
    List<DetallePartido> findByPartidoTorneoIdAndEquipoId(Long torneoId, Long equipoId);

    //Busqueda simple que vamos a usar para poder relacionar el detalle partido con el partido que pida el usuario
    List<DetallePartido> findByPartidoId(Long idPartido);

    //Busqueda para validar relacion de jugador con un equipo (en la eliminacion de un equipo)
    boolean existsByEquipoId(Long idEquipo);
}
