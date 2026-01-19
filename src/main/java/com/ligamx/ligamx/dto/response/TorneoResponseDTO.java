package com.ligamx.ligamx.dto.response;

import com.ligamx.ligamx.dto.DetalleTorneoDTO;
import com.ligamx.ligamx.entity.NombreTorneo;
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
public class TorneoResponseDTO {

    private Long id;
    private NombreTorneo nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer anio;
    private List<DetalleTorneoDTO> detallesTorneo;
}
