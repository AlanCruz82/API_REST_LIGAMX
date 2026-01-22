package com.ligamx.ligamx.dto.response;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import com.ligamx.ligamx.entity.RolPartido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetallePartidoResponseDTO {

    private EquipoResumenDTO equipo;

    private RolPartido rolEquipo;

    private Integer goles;
}
