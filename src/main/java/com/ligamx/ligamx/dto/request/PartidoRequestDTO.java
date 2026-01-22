package com.ligamx.ligamx.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartidoRequestDTO {

    @NotNull
    @Min(value = 1, message = "La jornada no puede ser menor a 1")
    @Max(value = 17, message = "La jornada no puede ser mayor a 17")
    private Integer jornada;

    @NotNull(message = "La fecha de inicio del partido no puede ser nula")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha de final del partido no puede ser nula")
    private LocalDateTime fechaHoraFin;

    @NotNull(message = "El id del torneo no puede ser nulo")
    private Long idTorneo;

    @NotEmpty(message = "No puede haber un partido sin equipos")
    List<DetallePartidoRequestDTO> detallesPartido;
}
