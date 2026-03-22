package com.ligamx.ligamx.dto.request;

import com.ligamx.ligamx.entity.NombreTorneo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TorneoRequestDTO {

    @NotNull(message = "El nombre del torneo no puede ser nulo o fuera de las opciones definidas")
    private NombreTorneo nombre;
    
    @NotNull(message = "La fecha de inicio del torneo no puede ser nula")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin del torneo no puede ser nula")
    private LocalDate fechaFin;

    @NotNull(message = "El año del torneo no puede ser nulo")
    private Integer anio;

    @NotEmpty(message = "El torneo no puede no tener equipos participando")
    @Size(max = 18, min = 18, message = "El torneo no puede contener menos de 18 equipos ni mas de 18 equipos para ser creado")
    private List<DetalleTorneoRequestDTO> detallesTorneo;
}
