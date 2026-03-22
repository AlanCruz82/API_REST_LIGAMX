package com.ligamx.ligamx.service;

import com.ligamx.ligamx.dto.request.EquipoRequestDTO;
import com.ligamx.ligamx.dto.response.EquipoResponseDTO;

import java.util.List;

public interface EquipoService {
    EquipoResponseDTO crearEquipo(EquipoRequestDTO nuevoEquipo);
    EquipoResponseDTO actualizarEquipo(Long idEquipo, EquipoRequestDTO equipo);
    EquipoResponseDTO actualizarCiudadEquipo(Long idEquipo, String nuevaCiudad);
    EquipoResponseDTO actualizarEstadioEquipo(Long idEquipo, String nuevoEstadio);
    void eliminarEquipo(Long idEquipo);
    EquipoResponseDTO listarEquipoPorId(Long idEquipo);
    List<EquipoResponseDTO> listarEquipos();
    List<EquipoResponseDTO> listarPorCiudad(String ciudad);
    List<EquipoResponseDTO> listarPorEstadio(String estadio);
}
