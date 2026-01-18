package com.ligamx.ligamx.dto.response;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import com.ligamx.ligamx.entity.PosicionJugador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JugadorResponseDTO {

    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private String pais;
    private PosicionJugador posicion;
    private EquipoResumenDTO equipo;
}
