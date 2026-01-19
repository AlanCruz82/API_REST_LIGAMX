package com.ligamx.ligamx.dto.request;

import com.ligamx.ligamx.entity.PosicionJugador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class JugadorRequestDTO {

    @NotBlank(message = "El nombre del jugador no puede estar vacio")
    @Size(min = 2, max = 40, message = "El nombre del jugador debe tener minimo 2 carcteres y maximo 40")
    private String nombre;

    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno del jugador no puede estar vacio")
    @Size(min = 4, max = 40, message = "El apellido materno del jugador debe tener minimo 4 carcteres y maximo 40")
    private String apellidoMaterno;

    @NotNull(message = "La fecha de nacimiento del jugador no puede ser nula")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La nacionalidad del jugador no puede estar vacia")
    private String pais;

    @NotNull(message = "La posicion del jugador no puede ser nula")
    private PosicionJugador posicion;

    @NotNull(message = "EL equipo del jugador no puede ser nulo")
    private Long idEquipo;
}
