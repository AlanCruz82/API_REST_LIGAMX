package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.NombreTorneo;
import com.ligamx.ligamx.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo,Long> {

    //Busqueda compuesta para encontrar el torneo en base a su nombre [APERTURA/CLAUSURA] y su año
    Torneo findByNombreAndAnio(NombreTorneo nombre, Integer anio);
}
