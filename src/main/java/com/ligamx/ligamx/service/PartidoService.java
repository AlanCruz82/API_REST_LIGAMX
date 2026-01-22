package com.ligamx.ligamx.service;

import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.RolPartido;

import java.util.List;

public interface PartidoService {

    PartidoResponseDTO crearPartido(PartidoRequestDTO partido);
    void eliminarPartido(Long idPartido);
    List<PartidoResponseDTO> listarPartidosPorTorneo(Long idTorneo);
    List<PartidoResponseDTO> listarPartidosPorTorneoYEquipo(Long idTorneo, Long idEquipo);
    List<PartidoResponseDTO> listarPartidosPorTorneoEquipoRol(Long idTorneo, Long idEquipo, RolPartido rol);
    List<PartidoResponseDTO> listarPartidosPorTorneoYJornada(Long idTorneo, int jornada);
}
