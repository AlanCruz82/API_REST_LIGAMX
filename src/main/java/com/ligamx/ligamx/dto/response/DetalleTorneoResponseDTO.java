package com.ligamx.ligamx.dto.response;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleTorneoResponseDTO {

    private EquipoResumenDTO equipo;
    private Integer victorias;
    private Integer empates;
    private Integer derrotas;
    private Integer puntos;
}
