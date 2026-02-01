package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.request.DetalleTorneoRequestDTO;
import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.DetalleTorneoResponseDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.DetalleTorneo;
import com.ligamx.ligamx.entity.Equipo;
import com.ligamx.ligamx.entity.NombreTorneo;
import com.ligamx.ligamx.entity.Torneo;
import com.ligamx.ligamx.exception.ResourceConflictException;
import com.ligamx.ligamx.exception.ResourceNotFoundException;
import com.ligamx.ligamx.mapper.DetalleTorneoMapper;
import com.ligamx.ligamx.mapper.TorneoMapper;
import com.ligamx.ligamx.repository.DetalleTorneoRepository;
import com.ligamx.ligamx.repository.EquipoRepository;
import com.ligamx.ligamx.repository.TorneoRepository;
import com.ligamx.ligamx.service.TorneoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TorneoServiceImpl implements TorneoService {

    //Bean del repositorio del torneo para poder encontrar los torneos por su id y crearlos
    private final TorneoRepository torneoRepository;
    //Bean del repositorio del detalle de torneo para poder encontrar el detalle del torneo encontrado
    private final DetalleTorneoRepository dtRepository;
    //Bean del repositorio del equipo que usamos para validar la existencia de los equipos en los detalles de torneo recibidos
    private final EquipoRepository equipoRepository;
    //Bean del mapper del torneo para poder convertir la entidad a su respuesta DTO
    private final TorneoMapper torneoMapper;
    //Bean del mapper del detalle del torneo para poder convertir la entidad a su respuesta DTO
    private final DetalleTorneoMapper dtMapper;


    public TorneoServiceImpl(TorneoRepository torneoRepository, DetalleTorneoRepository dtRepository,
                             EquipoRepository equipoRepository, TorneoMapper torneoMapper, DetalleTorneoMapper dtMapper) {
        this.torneoRepository = torneoRepository;
        this.dtRepository = dtRepository;
        this.equipoRepository = equipoRepository;
        this.torneoMapper = torneoMapper;
        this.dtMapper = dtMapper;
    }

    @Override
    public TorneoResponseDTO crearTorneo(TorneoRequestDTO torneo) {
        //Verificamos si no existe un torneo previamente guardado con el mismo nombre y año del torneo que se quiere
        //crear
        if (torneoRepository.existsByNombreAndAnio(torneo.getNombre(),torneo.getAnio())){
            //En caso de existir un regitro del torneo que se quiere crear le avisamos al controlador
            throw new ResourceConflictException("El torneo ya ha sido registrado previamente");
        }

        //Convertimos el torneo enviado como requestDTO a una entidad de torneo(aun sin detallesTorneo)
        Torneo entidadTorneo = torneoMapper.toEntity(torneo);

        //Creamos una lista para almacenar los equipos enviados y despues poder recorrerla
        List<DetalleTorneoRequestDTO> equiposTorneo = torneo.getDetallesTorneo();

        //Creamos una lista de tipo DetalleTorneo en la que vamos ir almacenando los detallesTorneo
        //construidos en el recorrido de los detalles enviados como dto
        List<DetalleTorneo> detallesTorneo = new ArrayList<>();

        //Empezamos a recorrer cada equipo para verificar si ya existe en la base de datos y en caso de que no
        //manejar ese escenario
        for (DetalleTorneoRequestDTO dtDTO : equiposTorneo){
            //Validamos si el equipo enviado en base a su id existe en la base de datos
            Equipo equipo = equipoRepository.findById(dtDTO.getIdEquipo()).orElseThrow(
                    () -> new ResourceNotFoundException("El equipo con id " + dtDTO.getIdEquipo() + " no existe")
            );

            //Convertimos el detalleTorneoDTO a su entidad (aun sin equipo ni torneo asignado)
            DetalleTorneo dt = dtMapper.toEntity(dtDTO);

            //Asignamos el euipo encontrado a la entidad detalle torneo
            dt.setEquipo(equipo);

            //Asignamos el torneo previamente creado como torneo de la entidad detalle torneo
            dt.setTorneo(entidadTorneo);

            //Agregamos el detalle de torneo generado a la lista de detalles de torneo
            detallesTorneo.add(dt);
        }

        //Asignamos sus entidades de detallesTorneo al torneo creado
        entidadTorneo.setDetallesTorneo(detallesTorneo);

        //Guardamos el torneo creado en la base de datos a la par que regresamos el torneo guardado como responseDTO
        return torneoMapper.toResponseDTO(torneoRepository.save(entidadTorneo));
    }

    @Override
    public TorneoResponseDTO listarTorneoPorNombreAnio(NombreTorneo nombre, Integer anio) {
        Torneo torneo = torneoRepository.findByNombreAndAnio(nombre,anio).orElseThrow(
                () -> new ResourceNotFoundException("El torneo no fue encontrado por el nombre y año dado")
        );

        return torneoMapper.toResponseDTO(torneo);
    }

    @Override
    public DetalleTorneoResponseDTO listarTorneoPorEquipo(Long idTorneo, Long idEquipo) {
        //Validamos la existencia del equipo y del torneo
        DetalleTorneo dt = dtRepository.findByTorneoIdAndEquipoId(idTorneo,idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("EL torneo no fue encontrado por el id del torneo y equipo dado")
        );

        return dtMapper.toResponseDTO(dt);
    }

    @Override
    public void eliminarTorneo(Long idTorneo) {
        //Validamos la existencia del torneo en base al id dado
        Torneo torneo = torneoRepository.findById(idTorneo).orElseThrow(
                () -> new ResourceNotFoundException("El torneo con id " + idTorneo + " no se encontro")
        );

        //Eliminamos el equipo de la base de datos
        torneoRepository.deleteById(idTorneo);
    }
}
