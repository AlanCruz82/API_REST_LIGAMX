package com.ligamx.ligamx.controller;

import com.ligamx.ligamx.dto.request.JugadorRequestDTO;
import com.ligamx.ligamx.dto.response.JugadorResponseDTO;
import com.ligamx.ligamx.entity.PosicionJugador;
import com.ligamx.ligamx.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jugadores")
public class JugadorController {

    //Bean del servicio del jugador que nos permite realizar las operaciones con el repositorio y manejar la logica de negocios
    private final JugadorService jugadorService;

    //Inyeccion de la depedencia (bean) por constructor
    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ResponseEntity<List<JugadorResponseDTO>> listarJugadores(){
        return ResponseEntity.ok(jugadorService.listarJugadores());
    }

    @GetMapping("/{idJugador}")
    public ResponseEntity<JugadorResponseDTO> listarJugadorPorId(@PathVariable Long idJugador){
        return ResponseEntity.ok(jugadorService.listarJugadorPorId(idJugador));
    }

    @GetMapping("/nombre/{nombreJugador}")
    public ResponseEntity<List<JugadorResponseDTO>> listarJugadoresPorNombre(@PathVariable String nombreJugador){
        return ResponseEntity.ok(jugadorService.listarJugadoresPorNombre(nombreJugador));
    }

    @GetMapping("/posicion/{posicionJugador}")
    public ResponseEntity<List<JugadorResponseDTO>> listarJugadoresPorPosicion(@PathVariable PosicionJugador posicionJugador){
        return ResponseEntity.ok(jugadorService.listarJugadoresPorPosicion(posicionJugador));
    }

    @GetMapping("/pais/{paisJugador}")
    public ResponseEntity<List<JugadorResponseDTO>> listarJugadoresPorPais(@PathVariable(name = "paisJugador") String pais){
        return ResponseEntity.ok(jugadorService.listarJugadoresPorPais(pais));
    }

    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<List<JugadorResponseDTO>> listarJugadoresPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(jugadorService.listarJugadoresPorEquipo(idEquipo));
    }

    @PostMapping("/registrar")
    public ResponseEntity<JugadorResponseDTO> registrarJugador(@Valid @RequestBody JugadorRequestDTO jugador){
        return ResponseEntity.ok(jugadorService.crearJugador(jugador));
    }

    @PutMapping("/actualizar/{idJugador}")
    public ResponseEntity<JugadorResponseDTO> actualizarJugador(@PathVariable Long idJugador, @Valid @RequestBody JugadorRequestDTO jugador){
        return ResponseEntity.ok(jugadorService.actualizarJugador(idJugador,jugador));
    }

    @PutMapping("/posicion/{idJugador}/{nuevaPosicion}")
    public ResponseEntity<JugadorResponseDTO> actualizarPosicionJugador(@PathVariable Long idJugador, @PathVariable PosicionJugador nuevaPosicion){
        return ResponseEntity.ok(jugadorService.actualizarPosicionJugador(idJugador,nuevaPosicion));
    }

    @PutMapping("/equipo/{idJugador}/{idEquipo}")
    public ResponseEntity<JugadorResponseDTO> actualizarEquipoJugador(@PathVariable Long idJugador, @PathVariable Long idEquipo){
        return ResponseEntity.ok(jugadorService.atualizarEquipoJugador(idJugador,idEquipo));
    }

    @DeleteMapping("/eliminar/{idJugador}")
    public ResponseEntity<Void> eliminarJugador(@PathVariable Long idJugador){
        jugadorService.eliminarJugador(idJugador);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
