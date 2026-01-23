package com.ligamx.ligamx.controller;

import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.NombreTorneo;
import com.ligamx.ligamx.service.TorneoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/torneos")
public class TorneoController {

    //Bean del servicio del torneo para poder realizar las operaciones con el repositorio
    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping("/nombre_anio/{nombreTorneo}/{anio}")
    public ResponseEntity<TorneoResponseDTO> listarTorneoPorNombreyAnio(@PathVariable NombreTorneo nombreTorneo,
                                                                        @PathVariable Integer anio){
        return ResponseEntity.ok(torneoService.listarTorneoPorNombreAnio(nombreTorneo,anio));
    }

    @GetMapping("/torneo_equipo/{idTorneo}/{idEquipo}")
    public ResponseEntity<TorneoResponseDTO> listarTorneoPorEquipo(@PathVariable Long idTorneo, @PathVariable Long idEquipo){
        return ResponseEntity.ok(torneoService.listarTorneoPorEquipo(idTorneo,idEquipo));
    }

    @PostMapping("/registrar")
    public ResponseEntity<TorneoResponseDTO> crearTorneo(@RequestBody TorneoRequestDTO torneo){
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.crearTorneo(torneo));
    }

    @DeleteMapping("/eliminar/{idTorneo}")
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Long idTorneo){
        torneoService.eliminarTorneo(idTorneo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
