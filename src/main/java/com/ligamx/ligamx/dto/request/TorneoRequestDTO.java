package com.ligamx.ligamx.dto.request;

import com.ligamx.ligamx.dto.DetalleTorneoDTO;
import com.ligamx.ligamx.entity.NombreTorneo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TorneoRequestDTO {

    @NotNull(message = "El nombre del torneo no puede ser nulo o fuera de las opciones definidas")
    private NombreTorneo nombre;

    @NotNull(message = "La fecha de inicio del torneo no puede ser nula")
    private LocalDate fechaHoraInicio;

    @NotNull(message = "La fecha de fin del torneo no puede ser nula")
    private LocalDate fechaHoraFin;

    @NotNull(message = "El año del torneo no puede ser nulo")
    private Integer anio;

    @NotEmpty(message = "El torneo no puede no tener equipos participando")
    private List<DetalleTorneoDTO> equipos;
}
