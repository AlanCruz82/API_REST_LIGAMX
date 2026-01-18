package com.ligamx.ligamx.dto.request;

import com.ligamx.ligamx.dto.DetallePartidoDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class PartidoRequestDTO {

    @NotNull
    @Min(value = 1, message = "La jornada no puede ser menor a 1")
    @Max(value = 17, message = "La jornada no puede ser mayor a 17")
    private Integer jornada;

    @NotNull(message = "La fecha de inicio del partido no puede ser nula")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de final del partido no puede ser nula")
    private LocalDateTime fechaFin;

    @NotNull(message = "El id del torneo no puede ser nulo")
    private Long idTorneo;

    @NotEmpty(message = "No puede haber un partido sin equipos")
    List<DetallePartidoDTO> detallesPartido;
}
