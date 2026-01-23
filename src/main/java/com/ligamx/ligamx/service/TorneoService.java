package com.ligamx.ligamx.service;

import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.NombreTorneo;

public interface TorneoService {

    TorneoResponseDTO crearTorneo(TorneoRequestDTO torneo);
    TorneoResponseDTO listarTorneoPorNombreAnio(NombreTorneo nombre, Integer anio);
    TorneoResponseDTO listarTorneoPorEquipo(Long idTorneo, Long idEquipo);
    void eliminarTorneo(Long idTorneo);
}
