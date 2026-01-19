package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import com.ligamx.ligamx.dto.request.JugadorRequestDTO;
import com.ligamx.ligamx.dto.response.JugadorResponseDTO;
import com.ligamx.ligamx.entity.Equipo;
import com.ligamx.ligamx.entity.Jugador;
import com.ligamx.ligamx.entity.PosicionJugador;
import com.ligamx.ligamx.mapper.EquipoMapper;
import com.ligamx.ligamx.mapper.JugadorMapper;
import com.ligamx.ligamx.repository.EquipoRepository;
import com.ligamx.ligamx.repository.JugadorRepository;
import com.ligamx.ligamx.service.JugadorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JugadorServiceImpl implements JugadorService {

    //Definimos un bean del repositorio del jugador para poder hacer las operaciones CRUD en la base de datos
    private final JugadorRepository jugadorRepository;
    //Bean del repositorio del equipo para poder gestionar la busqueda de los id de equipos que envian en el requestDTO
    private final EquipoRepository equipoRepository;
    //Bean del mapper para poder transformar las entidades del repositorio a DTO y viceversa
    private final JugadorMapper jugadorMapper;
    //Bean del mapper del equipo para poder transformar el campo del equipo en un resumen del equipo
    private final EquipoMapper equipoMapper;

    public JugadorServiceImpl(JugadorRepository jugadorRepository, EquipoRepository equipoRepository, JugadorMapper jugadorMapper, EquipoMapper equipoMapper) {
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.jugadorMapper = jugadorMapper;
        this.equipoMapper = equipoMapper;
    }

    @Override
    public JugadorResponseDTO crearJugador(JugadorRequestDTO nuevoJugador) {
        //Buscamos el equipo del idEquipo enviado en el requestDTO para comprobar si existe o si no avisarle al usuario
        Equipo equipo = equipoRepository.findById(nuevoJugador.getIdEquipo()).orElseThrow(
                () -> new RuntimeException("El equipo con id " + nuevoJugador.getIdEquipo() + " no existe")
        );

        //Convertimos el requestDTO a una entidad de jugador para poder establecer el equipo del jugador
        Jugador jugadorGuardar = jugadorMapper.toEntity(nuevoJugador);

        //Establecemos el equipo del idEquipo enviado en el requestDTO en la entidad del jugador
        jugadorGuardar.setEquipo(equipo);

        //Creamos el resumen del equipo asignado al jugador (id + nombre del equipo)
        EquipoResumenDTO equipoResumido = equipoMapper.toResumenDTO(equipo);

        //Gudardamos la entidad jugador y a la par lo convertimos en el responseDTO que vamos a regresarle al controlador
        JugadorResponseDTO jugadorGuardado = jugadorMapper.toResponseDTO(jugadorRepository.save(jugadorGuardar));

        //Establecemos el resumen del equipo colocado en la entidad al responseDTO para que solo se vea el id y nombre
        //del equipo
        jugadorGuardado.setEquipo(equipoResumido);

        //Regresamos el resposneDTO con su resumen de equipo ya asignado
        return jugadorGuardado;
    }

    @Override
    public JugadorResponseDTO actualizarJugador(Long idJugador, JugadorRequestDTO jugador) {
        //Buscamos el jugador por el id enviado en el argumento para en caso de que no exista avisarle al usuario
        Jugador jugadorPorId= jugadorRepository.findById(idJugador).orElseThrow(
                () -> new RuntimeException("El jugador con id " + idJugador + " no existe")
        );

        //Buscamos el id del equipo enviado en el resquestDTO del jugador para en caso de que no exista avisarle al usuario
        Equipo equipo = equipoRepository.findById(jugador.getIdEquipo()).orElseThrow(
                () -> new RuntimeException("El equipo con id " + jugador.getIdEquipo() + " no existe")
        );

        //Convertimos el jugador requestDTO a una entidad para poder asignarle su campo de equipo
        Jugador jugadorGuardar = jugadorMapper.toEntity(jugador);

        //Le establecemos su campo de equipo a la entidad del jugador
        jugadorGuardar.setEquipo(equipo);

        //Establecemos las propiedades del jugador guardado por las del nuevo jugador enviado como argumento
        /*La fecha de nacimiento no la establecemos ya que en las configuraciones de la entidad colocamos
        que no debe ser actualizable*/
        jugadorGuardar.setNombre(jugador.getNombre());
        jugadorGuardar.setApellidoPaterno(jugador.getApellidoPaterno());
        jugadorGuardar.setApellidoMaterno(jugador.getApellidoMaterno());
        jugadorGuardar.setPosicion(jugador.getPosicion());
        jugadorGuardar.setPais(jugador.getPais());

        //Convertimos el equipo asignado al jugador en un resumen del equipo (id + nombre equipo)
        EquipoResumenDTO resumenEquipo = equipoMapper.toResumenDTO(equipo);

        //Almacenamos el jugador con sus propiedades actualizadas a la par que lo convertimos en el responseDTO
        JugadorResponseDTO jugadorActualizado = jugadorMapper.toResponseDTO(jugadorRepository.save(jugadorGuardar));

        //Le establecemos el resumen del equipo asignado en la entidad al responseDTO
        jugadorActualizado.setEquipo(resumenEquipo);

        //Regresamos el responseDTO generado al guardar la entidad del jugador y despues de establecer el resumen de su
        //equipo
        return jugadorActualizado;
    }

    @Override
    public JugadorResponseDTO atualizarEquipoJugador(Long idJugador, Long idEquipo) {
        //Buscamos el jugador por el idJugador enviado para que en caso de que no exista avisarle al usuario
        Jugador jugador = jugadorRepository.findById(idJugador).orElseThrow(
                () -> new RuntimeException("El jugador con id " + idJugador + " no existe")
        );

        //Buscamos el equipo por el id enviado para que en caso de que no exista avisarle al usuario
        Equipo equipo = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new RuntimeException("El equipo con id " + idEquipo + " no existe")
        );

        //Creamos el resumen del equipo enviado y el cual vamos a asignarle al jugador
        EquipoResumenDTO resumenEquipo = equipoMapper.toResumenDTO(equipo);

        //Le colocamos el nuevo equipo al jugador
        jugador.setEquipo(equipo);

        //Actualizamos el jugador enviado con el nuevo equipo y a la par convertimos la entidad al resposneDTO
        //que vamos a devolver
        JugadorResponseDTO jugadorActualizado = jugadorMapper.toResponseDTO(jugadorRepository.save(jugador));

        //Establecemos el resumen del nuevo equipo asignado al responseDTO que vamos a devolver del jugador
        jugadorActualizado.setEquipo(resumenEquipo);

        //Regresamos el responseDTO del jugador con su nuevo equipo asignado
        return jugadorActualizado;
    }

    @Override
    public JugadorResponseDTO actualizarPosicionJugador(Long idJugador, PosicionJugador nuevaPosicion) {
        return null;
    }

    @Override
    public List<JugadorResponseDTO> listarJugadores() {
        return List.of();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorEquipo(Long idEquipo) {
        return List.of();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorPosicion(PosicionJugador posicion) {
        return List.of();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorPais(String pais) {
        return List.of();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorNombre(String nombre) {
        return List.of();
    }

    @Override
    public void eliminarJugador(Long idJugador) {

    }
}
