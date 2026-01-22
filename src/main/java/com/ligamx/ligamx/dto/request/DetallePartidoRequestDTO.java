package com.ligamx.ligamx.dto.request;

import com.ligamx.ligamx.entity.RolPartido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetallePartidoRequestDTO {

    private Long idEquipo;

    private RolPartido rolEquipo;

    private Integer goles;
}
