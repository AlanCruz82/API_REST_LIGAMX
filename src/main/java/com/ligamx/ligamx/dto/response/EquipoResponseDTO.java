package com.ligamx.ligamx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipoResponseDTO {

    private Long id;
    private String nombre;
    private String estadio;
    private String ciudad;
}
