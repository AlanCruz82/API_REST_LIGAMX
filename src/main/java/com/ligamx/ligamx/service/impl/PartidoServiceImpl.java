package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.RolPartido;
import com.ligamx.ligamx.mapper.DetallePartidoMapper;
import com.ligamx.ligamx.mapper.PartidoMapper;
import com.ligamx.ligamx.repository.DetallePartidoRepository;
import com.ligamx.ligamx.repository.PartidoRepository;
import com.ligamx.ligamx.service.PartidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidoServiceImpl implements PartidoService {

    private final PartidoRepository partidoRepository;
    private final DetallePartidoRepository dprepository;
    private final PartidoMapper partidoMapper;
    private final DetallePartidoMapper dpMapper;

    public PartidoServiceImpl(PartidoRepository partidoRepository, DetallePartidoRepository dprepository, PartidoMapper partidoMapper, DetallePartidoMapper dpMapper) {
        this.partidoRepository = partidoRepository;
        this.dprepository = dprepository;
        this.partidoMapper = partidoMapper;
        this.dpMapper = dpMapper;
    }

    @Override
    public PartidoResponseDTO crearPartido(PartidoRequestDTO partido) {
        return null;
    }

    @Override
    public void eliminarPartido(Long idPartido) {

    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneo(Long idTorneo) {
        return List.of();
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneoYEquipo(Long idTorneo, Long idEquipo) {
        return List.of();
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneoEquipoRol(Long idTorneo, Long idEquipo, RolPartido rol) {
        return List.of();
    }

    @Override
    public PartidoResponseDTO listarPartidoPorTorneoYJornada(Long idTorneo, int jornada) {
        return null;
    }
}
