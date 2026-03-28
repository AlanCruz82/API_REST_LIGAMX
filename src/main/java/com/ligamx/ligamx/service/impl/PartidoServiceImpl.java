package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.request.DetallePartidoRequestDTO;
import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.*;
import com.ligamx.ligamx.exception.ResourceNotFoundException;
import com.ligamx.ligamx.mapper.DetallePartidoMapper;
import com.ligamx.ligamx.mapper.PartidoMapper;
import com.ligamx.ligamx.repository.*;
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
    //Bean del repositorio del detalle de torneo utilizado para actualizar el puntaje de los equipos al registrar un nuevo
    //partido
    private final DetalleTorneoRepository dtRepository;
    //Bean del partidoMapper para poder convertir las peticiones y entidades a respuestas en formato DTO
    private final PartidoMapper partidoMapper;
    //Bean del detallePartidoMapper para poder convertir las entidades del detalle de partido en un resumen de respuesta DTO
    //y viceversa
    private final DetallePartidoMapper dpMapper;

    public PartidoServiceImpl(PartidoRepository partidoRepository, DetallePartidoRepository dprepository,
                              TorneoRepository torneoRepository, EquipoRepository equipoRepository,
                              DetalleTorneoRepository dtRepository, PartidoMapper partidoMapper, DetallePartidoMapper dpMapper) {
        this.partidoRepository = partidoRepository;
        this.dprepository = dprepository;
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
        this.dtRepository = dtRepository;
        this.partidoMapper = partidoMapper;
        this.dpMapper = dpMapper;
    }

    @Override
    public PartidoResponseDTO crearPartido(PartidoRequestDTO partido) {
        //Buscamos el torneo del partido enviado como idTorneo para que, en caso de no existir manejar ese escenario
        Torneo torneo = torneoRepository.findById(partido.getIdTorneo()).orElseThrow(
                () -> new ResourceNotFoundException("EL torneo con id " + partido.getIdTorneo() + " no existe")
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
        for (DetallePartidoRequestDTO detalleDTO : partido.getDetallesPartido()){

            //Buscamos el equipo enviado como detalle del partido por el idEquipo enviado dentro del detalle
            //para que en caso de no existir el equipo manejar ese escenario
            Equipo equipo = equipoRepository.findById(detalleDTO.getIdEquipo()).orElseThrow(
                    () -> new ResourceNotFoundException("El equipo con id " + detalleDTO.getIdEquipo() + " no existe")
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

        //Asignamos cada detalle de partido a un rol determinado[no importa este rol ya que solo queremos validar
        // quien metio mas goles que el otro o si fue empate]
        DetallePartido dpLocal = entidadesDetallePartido.get(0);
        DetallePartido dpVisitante = entidadesDetallePartido.get(1);

        //Obtenemos la entidad DetalleTorneo de cada equipo que forma parte del detalle del partido para poder
        //actualizar sus estadisticas
        DetalleTorneo dtLocal= dtRepository.findByTorneoIdAndEquipoId(entidadPartido.getTorneo().getId(),
                dpLocal.getEquipo().getId()).orElseThrow(
                () -> new ResourceNotFoundException("EL equipo con no fue encontrado")
        );

        DetalleTorneo dtVisitante= dtRepository.findByTorneoIdAndEquipoId(entidadPartido.getTorneo().getId(),
                dpVisitante.getEquipo().getId()).orElseThrow(
                () -> new ResourceNotFoundException("EL equipo con no fue encontrado")
        );

        //Validamos el escenario en el que el equipo del primer detalle de partido le gano al segundo
        if(dpLocal.getGoles() > dpVisitante.getGoles()){
            dtLocal.setVictorias(dtLocal.getVictorias()+1);
            dtLocal.setPuntos(dtLocal.getPuntos()+3);
            dtVisitante.setDerrotas(dtVisitante.getDerrotas()+1);
            //Validamos el escenario en el que el equipo del segundo detalle de partido le gano al primero
        }else if(dpVisitante.getGoles() > dpLocal.getGoles()){
            dtVisitante.setVictorias(dtVisitante.getVictorias()+1);
            dtVisitante.setPuntos(dtVisitante.getPuntos()+3);
            dtLocal.setDerrotas(dtLocal.getDerrotas()+1);
            //Escenario de empate y reparticion de puntos
        }else{
            dtLocal.setEmpates(dtLocal.getEmpates()+1);
            dtLocal.setPuntos(dtLocal.getPuntos()+1);
            dtVisitante.setEmpates(dtVisitante.getEmpates()+1);
            dtVisitante.setPuntos(dtVisitante.getPuntos()+1);
        }

        //Guardamos los detalles de torneo de cada equipo con sus nuevas estadisticas
        dtRepository.save(dtLocal);
        dtRepository.save(dtVisitante);

        //Guardamos la entidad del partido ya con sus campos torneo y detallesPartido asignados y regresamos el responseDTO
        //con la informacion del partido que guardamos en la base de datos
        //(Al estar usando cascade en la entidad Partido permite que una vez guardado el partido de igual forma
        // se almacene los detallesPartido asignados al partido)
        return partidoMapper.toResponseDTO(partidoRepository.save(entidadPartido));
    }

    @Override
    public void eliminarPartido(Long idPartido) {
        //Validamos si el equipo con el id recibido existe
        Partido partido = partidoRepository.findById(idPartido).orElseThrow(
                () -> new ResourceNotFoundException("EL partido con id " + idPartido + " no existe")
        );

        //Obtenemos el torneo relacionado con el partido que se quiere eliminar para poder eliminar las estadisticas
        //del partido a cada equipo involucrado
        Torneo torneo = torneoRepository.findById(partido.getTorneo().getId()).orElseThrow(
                () -> new ResourceNotFoundException("El torneo del partido " + partido.getId() + " no se encontro")
        );

        //Obtenemos los detalles del partido que se quiere eliminar para conocer el marcador y las estadisticas que vamos a eliminar
        List<DetallePartido> detallesPartido = partido.getDetallesPartido();

        //Validamos el caso en el que el primer equipo del detalle le gano al quipo del segundo detalle
        if(detallesPartido.get(0).getGoles() > detallesPartido.get(1).getGoles()){
            //Eliminamos las estadisticas de ambos equipos

            //Obtenmos las estadisticas del primer equipo
            DetalleTorneo dtLocal = dtRepository.findByTorneoIdAndEquipoId(torneo.getId(),detallesPartido.get(0).getEquipo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("El detalle del equipo no se encontro"));
            //Eliminamos los puntos que se le sumaron al primer equipo por su victoria en el partido
            dtLocal.setPuntos(dtLocal.getPuntos() - 3);
            //Eliminamos la victoria del equipo de su estadistica de victorias
            dtLocal.setVictorias(dtLocal.getVictorias() - 1);


            //Obtenmos las estadisticas del segundo equipo
            DetalleTorneo dtVisitante = dtRepository.findByTorneoIdAndEquipoId(torneo.getId(),detallesPartido.get(1).getEquipo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("El detalle del equipo no se encontro"));
            //Eliminamos la derrota del equipo de su estadistica de derrotas
            dtVisitante.setDerrotas(dtVisitante.getDerrotas() - 1);

            //Validamos el caso en el que el segundo equipo del detalle le gano al primero
        } else if (detallesPartido.get(1).getGoles() > detallesPartido.get(0).getGoles()) {
            //Eliminamos las estadisticas de ambos equipos

            //Obtenemos las estadisticas del primer equipo
            DetalleTorneo dtLocal = dtRepository.findByTorneoIdAndEquipoId(torneo.getId(), detallesPartido.get(0).getEquipo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("El detalle del equipo no se encontro"));
            //Eliminamos la derrota del equipo de su estadistica de derrotas
            dtLocal.setVictorias(dtLocal.getDerrotas() - 1);

            //Obtenmos las estadisticas del segundo equipo
            DetalleTorneo dtVisitante = dtRepository.findByTorneoIdAndEquipoId(torneo.getId(), detallesPartido.get(1).getEquipo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("El detalle del equipo no se encontro"));
            //Eliminamos los puntos que se le sumaron al segundo equipo por su victoria en el partido
            dtVisitante.setPuntos(dtVisitante.getPuntos() - 3);
            //Eliminamos la victoria del equipo de su estadistica de victorias
            dtVisitante.setDerrotas(dtVisitante.getVictorias() - 1);

            //En caso contrario, el escenario del partido es un empate
        }else{
            //Eliminamos las estadisticas de ambos equipos

            //Obtenemos las estadisticas del primer equipo
            DetalleTorneo dtLocal = dtRepository.findByTorneoIdAndEquipoId(torneo.getId(), detallesPartido.get(0).getEquipo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("El detalle del equipo no se encontro"));
            //Eliminamos el punto obtenido por el equipo en el partido
            dtLocal.setPuntos(dtLocal.getPuntos() - 1);
            //Eliminamos el empate del equipo de su estadistica de empates
            dtLocal.setEmpates(dtLocal.getEmpates() - 1);

            //Obtenmos las estadisticas del segundo equipo
            DetalleTorneo dtVisitante = dtRepository.findByTorneoIdAndEquipoId(torneo.getId(), detallesPartido.get(1).getEquipo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("El detalle del equipo no se encontro"));
            //Eliminamos el punto obtenido por el equipo en el partido
            dtVisitante.setPuntos(dtVisitante.getPuntos() - 1);
            //Eliminamos el empate del equipo de su estadistica de empates
           dtVisitante.setEmpates(dtVisitante.getEmpates() - 1);
        }

        //Eliminamos el partido de la bd
        partidoRepository.deleteById(idPartido);
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneo(Long idTorneo) {
        return partidoRepository.findByTorneoId(idTorneo).stream().map(partidoMapper::toResponseDTO).toList();
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneoYEquipo(Long idTorneo, Long idEquipo) {
        List<DetallePartido> detallePartido = dprepository.findByPartidoTorneoIdAndEquipoId(idTorneo,idEquipo);

        //Creamos una lista de tipos partidos donde vamos a almacenar los partidos relacionados con el equipo dado
        List<Partido> partidosEquipo = new ArrayList<>();

        //Recorremos cada detalle de partido obtenido en base al equipo dado
        for (DetallePartido dp : detallePartido){
            //Obtenemos el partido jugado por el equipo
            Partido partido = partidoRepository.findById(dp.getPartido().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Partido con id " + dp.getPartido().getId() + " no encontrado")
            );

            //Agregamos el partido encontrado a la lista de partidos jugados por el equipo
            partidosEquipo.add(partido);
        }

        //Convertimos cada partido en un responseDTO y los regresamos como respuesta
        return partidosEquipo.stream().map(partidoMapper::toResponseDTO).toList();
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneoEquipoRol(Long idTorneo, Long idEquipo, RolPartido rol) {
        //Obtenemos los detalles de los partidos donde el equipo fue el rol en el torneo dado
        List<DetallePartido> detallePartido = dprepository.findByPartidoTorneoIdAndEquipoIdAndRolEquipo(idTorneo,idEquipo,rol);

        //Creamos una lista de tipo partido donde vamos a almacenar los partidos relacionados con el detalle de partido
        List<Partido> partidosEquipo = new ArrayList<>();

        //Recorremos cada detalle de partido para poder obtener el partido con el que esta relacionado
        for (DetallePartido dp : detallePartido){

            //Buscamos el partido con el que esta relacionado el detalle partido del equipo con rol dado
            Partido partido = partidoRepository.findById(dp.getPartido().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("EL partido con id " + dp.getPartido().getId() + " no se encontro")
            );

            //Agregamos el partido encontrado a los partidos del equipo y rol dado
            partidosEquipo.add(partido);
        }

        //Convertimos cada partido en su responseDTO y los regresamos como respuesta
        return partidosEquipo.stream().map(partidoMapper::toResponseDTO).toList();
    }

    @Override
    public List<PartidoResponseDTO> listarPartidosPorTorneoYJornada(Long idTorneo, int jornada) {
        //Obtenemos el partido por el torneo y jornada dada del repositorio del partido
        List<Partido> partidos = partidoRepository.findByTorneoIdAndJornada(idTorneo,jornada);

        return partidos.stream().map(partidoMapper::toResponseDTO).toList();
    }
}
