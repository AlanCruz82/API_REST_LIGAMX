package com.ligamx.ligamx.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleTorneoRequestDTO {

    private Long idEquipo;
    private Integer victorias;
    private Integer empates;
    private Integer derrotas;
    private Integer puntos;
}
