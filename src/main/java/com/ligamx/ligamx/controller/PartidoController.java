package com.ligamx.ligamx.controller;

import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.RolPartido;
import com.ligamx.ligamx.service.PartidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partidos")
public class PartidoController {

    //Bean del servicio del partido para poder realizar las operaciones con el repositorio
    private final PartidoService partidoService;

    //Inyeccion de dependencia por contructor
    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping("/torneo/{idTorneo}")
    public ResponseEntity<List<PartidoResponseDTO>> listarPartidosPorTorneo(@PathVariable Long idTorneo){
        return ResponseEntity.ok(partidoService.listarPartidosPorTorneo(idTorneo));
    }

    @GetMapping("/torneo_y_equipo/{idTorneo}/{idEquipo}")
    public ResponseEntity<List<PartidoResponseDTO>> listarPartidosPorTorneoYEquipo(@PathVariable Long idTorneo,
                                                                                   @PathVariable Long idEquipo){
        return ResponseEntity.ok(partidoService.listarPartidosPorTorneoYEquipo(idTorneo,idEquipo));
    }

    @GetMapping("/torneo_equipo_rol/{idTorneo}/{idEquipo}/{rolEquipo}")
    public ResponseEntity<List<PartidoResponseDTO>> listarPartidosPorTorneoEquipoRol(@PathVariable Long idTorneo,
                                                                                     @PathVariable Long idEquipo,
                                                                                     @PathVariable RolPartido rolEquipo){
        return ResponseEntity.ok(partidoService.listarPartidosPorTorneoEquipoRol(idTorneo,idEquipo,rolEquipo));
    }

    @GetMapping("/torneo_y_jornada/{idTorneo}/{jornada}")
    public ResponseEntity<List<PartidoResponseDTO>> listarPartidosPorTorneoYJornada(@PathVariable Long idTorneo,
                                                                                    @PathVariable int jornada){
        return ResponseEntity.ok(partidoService.listarPartidosPorTorneoYJornada(idTorneo,jornada));
    }

    @PostMapping("/registrar")
    public ResponseEntity<PartidoResponseDTO> crearPartido(@RequestBody PartidoRequestDTO partido){
        return ResponseEntity.status(HttpStatus.CREATED).body(partidoService.crearPartido(partido));
    }

    @DeleteMapping("/eliminar/{idPartido}")
    public ResponseEntity<Void> eliminarPartido(@PathVariable Long idPartido){
        partidoService.eliminarPartido(idPartido);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
