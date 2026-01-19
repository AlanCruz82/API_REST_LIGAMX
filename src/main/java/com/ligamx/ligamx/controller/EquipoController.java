package com.ligamx.ligamx.controller;

import com.ligamx.ligamx.dto.request.EquipoRequestDTO;
import com.ligamx.ligamx.dto.response.EquipoResponseDTO;
import com.ligamx.ligamx.service.EquipoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipos")
public class EquipoController {

    //Bean del service que nos va a ayudar para hacer las operaciones de la base de datos (repository)
    private final EquipoService equipoService;

    //Inyeccion de dependencia del bean por constructor
    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> listarEquipos(){
        return ResponseEntity.ok(equipoService.listarEquipos());
    }

    @GetMapping("/ciudad/{ciudadEquipo}")
    public ResponseEntity<List<EquipoResponseDTO>> listarEquiposPorCiudad(@PathVariable String ciudadEquipo){
        return ResponseEntity.ok(equipoService.listarPorCiudad(ciudadEquipo));
    }

    @GetMapping("/estadio/{estadioEquipo}")
    public ResponseEntity<List<EquipoResponseDTO>> listarEquiposPorEstadio(@PathVariable String estadioEquipo){
        return ResponseEntity.ok(equipoService.listarPorEstadio(estadioEquipo));
    }

    @PostMapping("/registrar")
    public ResponseEntity<EquipoResponseDTO> registrarEquipo(@RequestBody EquipoRequestDTO nuevoEquipo){
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.crearEquipo(nuevoEquipo));
    }

    @PutMapping("/actualizar/{idEquipo}")
    public ResponseEntity<EquipoResponseDTO> actualizarEquipo(@PathVariable Long idEquipo, @RequestBody EquipoRequestDTO equipoActualizado){
        return ResponseEntity.ok(equipoService.actualizarEquipo(idEquipo,equipoActualizado));
    }

    @PutMapping("/ciudad/{idEquipo}/{nuevaCiudad}")
    public ResponseEntity<EquipoResponseDTO> actualizarCiudadEquipo(@PathVariable Long idEquipo, @PathVariable String nuevaCiudad){
        return ResponseEntity.ok(equipoService.actualizarCiudadEquipo(idEquipo,nuevaCiudad));
    }

    @PutMapping("/estadio/{idEquipo}/{nuevoEstadio}")
    public ResponseEntity<EquipoResponseDTO> actualizarEstadioEquipo(@PathVariable Long idEquipo, @PathVariable String nuevoEstadio){
        return ResponseEntity.ok(equipoService.actualizarEstadioEquipo(idEquipo,nuevoEstadio));
    }

    @DeleteMapping("/eliminar/{idEquipo}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable Long idEquipo){
        equipoService.eliminarEquipo(idEquipo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
