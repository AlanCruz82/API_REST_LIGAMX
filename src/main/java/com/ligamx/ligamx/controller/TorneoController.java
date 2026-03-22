package com.ligamx.ligamx.controller;

import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.DetalleTorneoResponseDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.NombreTorneo;
import com.ligamx.ligamx.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/torneos")
public class TorneoController {

    //Bean del servicio del torneo para poder realizar las operaciones con el repositorio
    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping
    public ResponseEntity<List<TorneoResponseDTO>> listarTorneos(){
        return ResponseEntity.ok(torneoService.listarTorneos());
    }

    @GetMapping("/nombre_anio/{nombreTorneo}/{anio}")
    public ResponseEntity<TorneoResponseDTO> listarTorneoPorNombreyAnio(@PathVariable String nombreTorneo,
                                                                        @PathVariable Integer anio){
        NombreTorneo nombre = NombreTorneo.valueOf(nombreTorneo);

        return ResponseEntity.ok(torneoService.listarTorneoPorNombreAnio(nombre,anio));
    }

    @GetMapping("/torneo_equipo/{idTorneo}/{idEquipo}")
    public ResponseEntity<DetalleTorneoResponseDTO> listarTorneoPorEquipo(@PathVariable Long idTorneo, @PathVariable Long idEquipo){
        return ResponseEntity.ok(torneoService.listarTorneoPorEquipo(idTorneo,idEquipo));
    }

    @GetMapping("/puntos/{idTorneo}")
    public ResponseEntity<List<DetalleTorneoResponseDTO>> listarTorneoPorPuntos(@PathVariable Long idTorneo){
        return ResponseEntity.ok(torneoService.listarTorneoPorPuntos(idTorneo));
    }

    @PostMapping("/registrar")
    public ResponseEntity<TorneoResponseDTO> crearTorneo(@Valid @RequestBody TorneoRequestDTO torneo){
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.crearTorneo(torneo));
    }

    @DeleteMapping("/eliminar/{idTorneo}")
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Long idTorneo){
        torneoService.eliminarTorneo(idTorneo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
