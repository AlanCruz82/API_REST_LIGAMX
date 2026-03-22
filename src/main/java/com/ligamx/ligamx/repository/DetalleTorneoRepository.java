package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.DetalleTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleTorneoRepository extends JpaRepository<DetalleTorneo,Long> {

    //Busqueda compuesta para encontrar un equipo en un torneo
    Optional<DetalleTorneo> findByTorneoIdAndEquipoId(Long idTorneo, Long idEquipo);

    //Busquda simple para encontrar el detalle de torneo por el orden ascedente de los puntos(Tabla general)
    @Query("""
            select dt
            from DetalleTorneo dt
            join dt.equipo e
            where dt.torneo.id= :idTorneo
            order by dt.puntos desc
            """)
    List<DetalleTorneo> findByOrderPuntos(Long idTorneo);

    //Busqueda para validar relacion de jugador con un equipo (en la eliminacion de un equipo)
    boolean existsByEquipoId(Long idEquipo);
}
