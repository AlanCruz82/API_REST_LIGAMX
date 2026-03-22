package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.request.EquipoRequestDTO;
import com.ligamx.ligamx.dto.response.EquipoResponseDTO;
import com.ligamx.ligamx.entity.Equipo;
import com.ligamx.ligamx.exception.ResourceConflictException;
import com.ligamx.ligamx.exception.ResourceNotFoundException;
import com.ligamx.ligamx.mapper.EquipoMapper;
import com.ligamx.ligamx.repository.DetallePartidoRepository;
import com.ligamx.ligamx.repository.DetalleTorneoRepository;
import com.ligamx.ligamx.repository.EquipoRepository;
import com.ligamx.ligamx.repository.JugadorRepository;
import com.ligamx.ligamx.service.EquipoService;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final EquipoMapper equipoMapper;
    private final JugadorRepository jugadorRepository;
    private final DetallePartidoRepository dpRepository;
    private final DetalleTorneoRepository dtRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository, EquipoMapper equipoMapper, JugadorRepository jugadorRepository,
                             DetallePartidoRepository dpRepository, DetalleTorneoRepository dtRepository) {
        this.equipoRepository = equipoRepository;
        this.equipoMapper = equipoMapper;
        this.jugadorRepository = jugadorRepository;
        this.dpRepository = dpRepository;
        this.dtRepository = dtRepository;
    }

    @Override
    public EquipoResponseDTO crearEquipo(EquipoRequestDTO nuevoEquipo) {
        //Si el nombre del equipo enviado ya esta reigstrado, lanzamos una excepcion de aviso
        if(equipoRepository.existsByNombre(nuevoEquipo.getNombre())){
            throw new ResourceNotFoundException("El equipo " + nuevoEquipo.getNombre() + " ya existe");
        }

        //Creamos una nueva entidad del equipo que se va a guardar en la base de datos
        //convirtiendo el dto mandado en la peticion
        Equipo equipoGuardado = equipoRepository.save(equipoMapper.toEntity(nuevoEquipo));

        //Regresamoe el equipo guardado en el formato convertido del dto de respuesta
        return equipoMapper.toResponseDTO(equipoGuardado);

    }

    @Override
    public EquipoResponseDTO actualizarEquipo(Long idEquipo, EquipoRequestDTO equipo) {
        //Buscamos en en el repositorio si el equipo por su id exite en la base de datos
        Equipo equipoPorId = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + idEquipo + " no se encontro")
        );

        //Actualizamos las propiedades del equipo por las del equipo enviado como argumento
        equipoPorId.setNombre(equipo.getNombre());
        equipoPorId.setEstadio(equipo.getEstadio());
        equipoPorId.setCiudad(equipo.getCiudad());

        //Guardamos el equipo que habia con sus nuevas propiedades
        Equipo equipoActualizado = equipoRepository.save(equipoPorId);

        //Regresamos el equipo guardado convertido al formato de DTO
        return equipoMapper.toResponseDTO(equipoActualizado);
    }

    @Override
    public EquipoResponseDTO actualizarCiudadEquipo(Long idEquipo, String nuevaCiudad) {
        //Buscamos en el repositorio si el equipo por su id existe en la base de datos
        Equipo equipoPorId = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + idEquipo + " no se encontro")
        );

        //Actualizamos la ciudad del equipo encontrado por la nueva ciudad enviada como argumento
        equipoPorId.setCiudad(nuevaCiudad);

        //Guardamos el equipo con su nueva ciudad
        Equipo equipoActualizado = equipoRepository.save(equipoPorId);

        //Regresamos el equipo actualizado en formato DTO
        return equipoMapper.toResponseDTO(equipoActualizado);
    }

    @Override
    public EquipoResponseDTO actualizarEstadioEquipo(Long idEquipo, String nuevoEstadio) {
        //Buscamos en el repositorio si el equipo por su id existe en la base de datos
        Equipo equipoPorId = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + idEquipo + " no se encontro")
        );

        //Actualizamos el estadio del equiop encontrado por id por el estadio enviado como argumento
        equipoPorId.setEstadio(nuevoEstadio);

        //Guardamos el equipo con su nuevo estadio
        Equipo equipoActualizado = equipoRepository.save(equipoPorId);

        //Regresamoe el equipo actualizado en formato DTO
        return equipoMapper.toResponseDTO(equipoActualizado);
    }

    @Override
    public void eliminarEquipo(Long idEquipo) {
        //Buscamos si el equipo por el id enviado existe en la base de datos
        Equipo equipo = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + idEquipo + " no se encontro")
        );

        //Validamos si el equipo que se quiere eliminar no esta presente en registros de otras tablas
        if(jugadorRepository.existsByEquipoId(idEquipo) || dpRepository.existsByEquipoId(idEquipo) || dtRepository.existsByEquipoId(idEquipo)){
            throw new ResourceConflictException("No se puede eliminar el equipo con id " + idEquipo + " porque tiene presencia en otros registros");
        }

        //Eliminamos el equipo encontrado por el id enviado
        equipoRepository.deleteById(idEquipo);
    }

    @Override
    public EquipoResponseDTO listarEquipoPorId(Long idEquipo) {
        //Buscamos si el equipo por el id enviado existe en la base de datos
        Equipo equipo = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + idEquipo + " no existe"));

        //Regresamos el equipo solicitado
        return equipoMapper.toResponseDTO(equipo);
    }

    @Override
    public List<EquipoResponseDTO> listarEquipos() {
        //Regresamos la lista de equipo guardados en la base de datos convirtiendo cada entidad de equipo a el formato
        //de respuesta DTO usando un stream de la lista de equipos encontrados para convertilos al DTO del equipo
        return equipoRepository.findAll().stream().map(equipoMapper::toResponseDTO).toList();
    }

    @Override
    public List<EquipoResponseDTO> listarPorCiudad(String ciudad) {
        //Obtenemos la lista de equipo encontraoos y los convertimos a su DTO de respuesta convertiendo en un stream
        //la lista para poder convertir a cada elemento en su formato DTO de respuesta
        return equipoRepository.findByCiudad(ciudad).stream().map(equipoMapper::toResponseDTO).toList();
    }

    @Override
    public List<EquipoResponseDTO> listarPorEstadio(String estadio) {
        //Obtenemos la lista de equipo encontraoos y los convertimos a su DTO de respuesta convertiendo en un stream
        //la lista para poder convertir a cada elemento en su formato DTO de respuesta
        return equipoRepository.findByEstadio(estadio).stream().map(equipoMapper::toResponseDTO).toList();
    }
}
