package com.ligamx.ligamx.service.impl;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import com.ligamx.ligamx.dto.request.JugadorRequestDTO;
import com.ligamx.ligamx.dto.response.JugadorResponseDTO;
import com.ligamx.ligamx.entity.Equipo;
import com.ligamx.ligamx.entity.Jugador;
import com.ligamx.ligamx.entity.PosicionJugador;
import com.ligamx.ligamx.exception.ResourceConflictException;
import com.ligamx.ligamx.exception.ResourceNotFoundException;
import com.ligamx.ligamx.mapper.EquipoMapper;
import com.ligamx.ligamx.mapper.JugadorMapper;
import com.ligamx.ligamx.repository.EquipoRepository;
import com.ligamx.ligamx.repository.JugadorRepository;
import com.ligamx.ligamx.service.JugadorService;
import org.springframework.security.authentication.BadCredentialsException;
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

    public JugadorServiceImpl(JugadorRepository jugadorRepository,EquipoRepository equipoRepository,
                              JugadorMapper jugadorMapper) {
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.jugadorMapper = jugadorMapper;
    }

    @Override
    public JugadorResponseDTO crearJugador(JugadorRequestDTO nuevoJugador) {
        //Buscamos el equipo del idEquipo enviado en el requestDTO para comprobar si existe o si no avisarle al usuario
        Equipo equipo = equipoRepository.findById(nuevoJugador.getIdEquipo()).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + nuevoJugador.getIdEquipo() + " no existe")
        );
        //Validamos el pais de origen del jugador para en caso de ser extranjero validar el escenario del numero maximo de extranjeros que se pueden inscribir
        if (!(nuevoJugador.getPais()).equalsIgnoreCase("mexico")){

            //Obtenemos el numero de extranjeros registrados actualmente para el equipo del jugador enviado
            int totalExtranjerosEquipo = jugadorRepository.numeroExtranjerosEquipo(equipo.getId());

            //Si el equipo del jugador dado ya inscribio el numero maximo de extranjeros, entonces negamos la inscripcion del nuevo jugador
            if (totalExtranjerosEquipo >= 9){
                throw new ResourceConflictException("El equipo " + equipo.getNombre() + " ya inscribio 9 extranjeros");
            }

        }
        //Convertimos el requestDTO a una entidad de jugador para poder establecer el equipo del jugador
        Jugador jugadorGuardar = jugadorMapper.toEntity(nuevoJugador);

        //Establecemos el equipo del idEquipo enviado en el requestDTO en la entidad del jugador
        jugadorGuardar.setEquipo(equipo);

        //Gudardamos la entidad jugador y a la par lo convertimos en el responseDTO que vamos a regresarle al controlador
        return jugadorMapper.toResponseDTO(jugadorRepository.save(jugadorGuardar));
    }

    @Override
    public JugadorResponseDTO actualizarJugador(Long idJugador, JugadorRequestDTO jugador) {
        //Buscamos el jugador por el id enviado en el argumento para en caso de que no exista avisarle al usuario
        Jugador jugadorPorId= jugadorRepository.findById(idJugador).orElseThrow(
                () -> new ResourceNotFoundException("El jugador con id " + idJugador + " no existe")
        );

        //Buscamos el id del equipo enviado en el resquestDTO del jugador para en caso de que no exista avisarle al usuario
        Equipo equipo = equipoRepository.findById(jugador.getIdEquipo()).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + jugador.getIdEquipo() + " no existe")
        );

        //Le establecemos su campo de equipo a la entidad del jugador
        jugadorPorId.setEquipo(equipo);

        //Establecemos las propiedades del jugador guardado por las del nuevo jugador enviado como argumento
        /*La fecha de nacimiento no la establecemos ya que en las configuraciones de la entidad colocamos
        que no debe ser actualizable*/
        jugadorPorId.setNombre(jugador.getNombre());
        jugadorPorId.setApellidoPaterno(jugador.getApellidoPaterno());
        jugadorPorId.setApellidoMaterno(jugador.getApellidoMaterno());
        jugadorPorId.setPosicion(jugador.getPosicion());
        jugadorPorId.setPais(jugador.getPais());

        //Almacenamos el jugador con sus propiedades actualizadas a la par que lo convertimos en el responseDTO
        return jugadorMapper.toResponseDTO(jugadorRepository.save(jugadorPorId));
    }

    @Override
    public JugadorResponseDTO atualizarEquipoJugador(Long idJugador, Long idEquipo) {
        //Buscamos el jugador por el idJugador enviado para que en caso de que no exista avisarle al usuario
        Jugador jugador = jugadorRepository.findById(idJugador).orElseThrow(
                () -> new ResourceNotFoundException("El jugador con id " + idJugador + " no existe")
        );

        //Buscamos el equipo por el id enviado para que en caso de que no exista avisarle al usuario
        Equipo equipo = equipoRepository.findById(idEquipo).orElseThrow(
                () -> new ResourceNotFoundException("El equipo con id " + idEquipo + " no existe")
        );
        //Le colocamos el nuevo equipo al jugador
        jugador.setEquipo(equipo);

        //Actualizamos el jugador enviado con el nuevo equipo y a la par convertimos la entidad al resposneDTO
        //que vamos a devolver
        return jugadorMapper.toResponseDTO(jugadorRepository.save(jugador));
    }

    @Override
    public JugadorResponseDTO actualizarPosicionJugador(Long idJugador, PosicionJugador nuevaPosicion) {
        //Buscamos el jugador con el idJugador pasado como argumento para que en caso de no encontrarlo
        //manejar ese escenario
        Jugador jugador = jugadorRepository.findById(idJugador).orElseThrow(
                () -> new ResourceNotFoundException("EL jugador con id " + idJugador + " no existe")
        );

        //Actualizamos la posicion del jugador almamcenado en la base de datos por la nuevaPosicion enviada como argumento
        jugador.setPosicion(nuevaPosicion);

        //Gudardamos el jugador con su nueva posicion colcoada a la par que convertimos la entidad almacenada en el
        //responseDTO y regresamos el dto obtenido
        return jugadorMapper.toResponseDTO(jugadorRepository.save(jugador));
    }

    @Override
    public List<JugadorResponseDTO> listarJugadores() {
        //Obtenemos la lista de los jugadores encontrados y vamos uno por uno convirtiendo su entidad equipo a un resumen
        //del equipo a la par que lo convertimos en el formato responseDTO
        return jugadorRepository.findAll().stream().map(jugadorMapper::toResponseDTO).toList();
    }

    @Override
    public JugadorResponseDTO listarJugadorPorId(Long idJugador) {
        //Validamos si el jugador con el id enviado como parametro existe en la base de datos
        Jugador jugador = jugadorRepository.findById(idJugador).orElseThrow(
                () -> new ResourceNotFoundException("El jugador con el id " + idJugador + " no existe")
        );

        //Regresamos el jugador solicitado por id
        return jugadorMapper.toResponseDTO(jugador);
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorEquipo(Long idEquipo) {
        //Obtenemos la lista de los jugadores encontrados y vamos uno por uno convirtiendo su entidad equipo a un resumen
        //del equipo a la par que lo convertimos en el formato responseDTO
        return jugadorRepository.findByEquipoId(idEquipo).stream().map(jugadorMapper::toResponseDTO).toList();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorPosicion(PosicionJugador posicion) {
        //Obtenemos la lista de los jugadores encontrados y vamos uno por uno convirtiendo su entidad equipo a un resumen
        //del equipo a la par que lo convertimos en el formato responseDTO
        return jugadorRepository.findByPosicion(posicion).stream().map(jugadorMapper::toResponseDTO).toList();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorPais(String pais) {
        //Obtenemos la lista de los jugadores encontrados y vamos uno por uno convirtiendo su entidad equipo a un resumen
        //del equipo a la par que lo convertimos en el formato responseDTO
        return jugadorRepository.findByPais(pais).stream().map(jugadorMapper::toResponseDTO).toList();
    }

    @Override
    public List<JugadorResponseDTO> listarJugadoresPorNombre(String nombre) {
        //Usamos una lista en este metodo ya que en la consulta/metodo que se usa en el repositorio no buscamos con una clausula
        //WHERE sino con un patron de caracteres LIKE
        return jugadorRepository.findAllByNombreContainingIgnoreCase(nombre).stream().map(jugadorMapper::toResponseDTO).toList();
    }

    @Override
    public void eliminarJugador(Long idJugador) {
        //Buscamos el jugador enviado por idJugador para reconocer si existe o no en la base de datos
        //y manejar dicho escenario
        Jugador jugador = jugadorRepository.findById(idJugador).orElseThrow(
                () -> new ResourceNotFoundException("EL juador con id " + idJugador + " no existe")
        );

        //Eliminamos el jugador enviado por idJugador de la base de datos
        jugadorRepository.deleteById(idJugador);
    }
}
