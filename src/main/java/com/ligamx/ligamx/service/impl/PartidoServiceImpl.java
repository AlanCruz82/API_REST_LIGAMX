package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.DetallePartidoDTO;
import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.*;
import com.ligamx.ligamx.mapper.DetallePartidoMapper;
import com.ligamx.ligamx.mapper.PartidoMapper;
import com.ligamx.ligamx.repository.DetallePartidoRepository;
import com.ligamx.ligamx.repository.EquipoRepository;
import com.ligamx.ligamx.repository.PartidoRepository;
import com.ligamx.ligamx.repository.TorneoRepository;
import com.ligamx.ligamx.service.PartidoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartidoServiceImpl implements PartidoService {

    //Bean del repositorio del partido para poder realizar las busquedas en la base de datos
    private final PartidoRepository partidoRepository;
    //Bean del detalle del partido para poder realizar las busquedas por equipo, rol o torneo en la base de datos
    private final DetallePartidoRepository dprepository;
    //Bean del repositorio del torneo para poder realizar la busqueda de los torneos relacionados a los partidos
    private final TorneoRepository torneoRepository;
    //Bean del repositorio del equipo para poder realizar la busqueda de los equipos enviados como detalle del partido
    private final EquipoRepository equipoRepository;
    //Bean del partidoMapper para poder convertir las peticiones y entidades a respuestas en formato DTO
    private final PartidoMapper partidoMapper;
    //Bean del detallePartidoMapper para poder convertir las entidades del detalle de partido en un resumen de respuesta DTO
    //y viceversa
    private final DetallePartidoMapper dpMapper;

    public PartidoServiceImpl(PartidoRepository partidoRepository, DetallePartidoRepository dprepository,
                              TorneoRepository torneoRepository, EquipoRepository equipoRepository,
                              PartidoMapper partidoMapper, DetallePartidoMapper dpMapper) {
        this.partidoRepository = partidoRepository;
        this.dprepository = dprepository;
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
        this.partidoMapper = partidoMapper;
        this.dpMapper = dpMapper;
    }

    @Override
    public PartidoResponseDTO crearPartido(PartidoRequestDTO partido) {
        //Buscamos el torneo del partido enviado como idTorneo para que, en caso de no existir manejar ese escenario
        Torneo torneo = torneoRepository.findById(partido.getIdTorneo()).orElseThrow(
                () -> new RuntimeException("EL torneo con id " + partido.getIdTorneo() + " no existe")
        );

        //Convertimos el requestDTO de entrada por la entidad del partido (aun sin asginar sus campos de torneo y detallesPartido)
        Partido entidadPartido = partidoMapper.toEntity(partido);

        //Asignamos el torneo enviado como argumento dentro del DTO del partido a la entidad del partido
        entidadPartido.setTorneo(torneo);

        //Creamos la lista de las entidades de detalles del partido que le vamos a asignar al partido una vez
        //los generemos
        List<DetallePartido> entidadesDetallePartido = new ArrayList<>();

        //Recorremos cada detalle de partido enviado como dto (equipo-rol-goles) y generamos su entidad detallePartido
        //con los datos de la entidad partido generada anteriormente
        for (DetallePartidoDTO detalleDTO : partido.getDetallesPartido()){

            //Buscamos el equipo enviado como detalle del partido por el idEquipo enviado dentro del detalle
            //para que en caso de no existir el equipo manejar ese escenario
            Equipo equipo = equipoRepository.findById(detalleDTO.getIdEquipo()).orElseThrow(
                    () -> new RuntimeException("El equipo con id " + detalleDTO.getIdEquipo() + " no existe")
            );

            //Convertimos el detalleDTO a su entidad para poder relacionarlo con su correspondiente partido
            DetallePartido entidadDp = dpMapper.toEntity(detalleDTO);

            //Le asignamos su equipo encontrado anteriormente a la entidad del detalle del partido
            entidadDp.setEquipo(equipo);

            //Le asignamos el partido generado a la entidad del detalle del partido
            entidadDp.setPartido(entidadPartido);

            //Agregamos la entidad del detalle de partido generada a la lista que vamos a asignarle al partido
            entidadesDetallePartido.add(entidadDp);
        }

        //Le asignamos su lista de entidades de detalle de partido generada a la entidad del partido
        entidadPartido.setDetallesPartido(entidadesDetallePartido);

        //Guardamos la entidad del partido ya con sus campos torneo y detallesPartido asignados y regresamos el responseDTO
        //con la informacion del partido que guardamos en la base de datos
        //(Al estar usando cascade en la entidad Partido permite que una vez guardado el partido de igual forma
        // se almacene los detallesPartido asignados al partido)
        return partidoMapper.toResponseDTO(partidoRepository.save(entidadPartido));
    }

    @Override
    public void eliminarPartido(Long idPartido) {
        Partido partido = partidoRepository.findById(idPartido).orElseThrow(
                () -> new RuntimeException("EL partido con id " + idPartido + " no existe")
        );

        partidoRepository.deleteById(idPartido);
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneo(Long idTorneo) {
        return partidoRepository.findByTorneoId(idTorneo).stream().map(partidoMapper::toResponseDTO).toList();
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
