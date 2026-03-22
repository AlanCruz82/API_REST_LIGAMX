package com.ligamx.ligamx.service;

import com.ligamx.ligamx.dto.request.JugadorRequestDTO;
import com.ligamx.ligamx.dto.response.JugadorResponseDTO;
import com.ligamx.ligamx.entity.PosicionJugador;

import java.util.List;

public interface JugadorService {

    JugadorResponseDTO crearJugador(JugadorRequestDTO nuevoJugador);
    JugadorResponseDTO actualizarJugador(Long idJugador, JugadorRequestDTO jugador);
    JugadorResponseDTO atualizarEquipoJugador(Long idJugador, Long idEquipo);
    JugadorResponseDTO actualizarPosicionJugador(Long idJugador, PosicionJugador nuevaPosicion);
    List<JugadorResponseDTO> listarJugadores();
    JugadorResponseDTO listarJugadorPorId(Long idJugador);
    List<JugadorResponseDTO> listarJugadoresPorEquipo(Long idEquipo);
    List<JugadorResponseDTO> listarJugadoresPorPosicion(PosicionJugador posicion);
    List<JugadorResponseDTO> listarJugadoresPorPais(String pais);
    List<JugadorResponseDTO> listarJugadoresPorNombre(String nombre);
    void eliminarJugador(Long idJugador);
}
