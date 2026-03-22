package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.Jugador;
import com.ligamx.ligamx.entity.PosicionJugador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador,Long> {

    //Usamos una lista ya que la consulta va a ser un like por lo que nos puede devolver multiples
    //jugadores que encajen con ese patron de cadena de texto
    List<Jugador> findAllByNombreContainingIgnoreCase(String nombre);
    List<Jugador> findByEquipoId(Long idEquipo);
    List<Jugador> findByPosicion(PosicionJugador posicion);
    List<Jugador> findByPais(String pais);
    //Consulta para validar el maximo numero de extranjeros que puede tener un equipo
    @Query("select count(*) from Jugador where pais not like 'Mexico' and equipo.id = :idEquipo")
    int numeroExtranjerosEquipo(Long idEquipo);

    //Busqueda para validar relacion de jugador con un equipo (en la eliminacion de un equipo)
    boolean existsByEquipoId(Long idEquipo);
}
