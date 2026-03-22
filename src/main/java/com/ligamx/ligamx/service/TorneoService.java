package com.ligamx.ligamx.service;

import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.DetalleTorneoResponseDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.NombreTorneo;

import java.util.List;

public interface TorneoService {

    TorneoResponseDTO crearTorneo(TorneoRequestDTO torneo);
    List<TorneoResponseDTO> listarTorneos();
    TorneoResponseDTO listarTorneoPorNombreAnio(NombreTorneo nombre, Integer anio);
    //Regresamos el detalle ya que unicamente queremos mostrar como le fue a ese equipo en el torneo dado y no
    //toda la lista de equipos que participaron en el torneo mas el equipo dado (en este caso se regresa el Torneo completo)
    DetalleTorneoResponseDTO listarTorneoPorEquipo(Long idTorneo, Long idEquipo);
    List<DetalleTorneoResponseDTO> listarTorneoPorPuntos(Long idTorneo);
    void eliminarTorneo(Long idTorneo);
}
