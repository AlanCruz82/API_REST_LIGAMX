package com.ligamx.ligamx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipoRequestDTO {

    @NotBlank(message = "El nombre del equipo no puede estar vacio")
    @Size(min = 4, max = 80, message = "El nombre del equipo debe tener minimo 4 caracteres y maximo 80")
    private String nombre;

    @NotBlank(message = "La ciudad del equipo no puede estar vacia")
    @Size(min = 4, max = 80, message = "La ciudad del equipo debe tener minimo 4 caracteres y maximo 80")
    private String ciudad;

    @NotBlank(message = "El estadio del equipo no puede estar vacio")
    @Size(min = 4, max = 80, message = "El estadio del equipo debe tener minimo 4 caracteres y maximo 80")
    private String estadio;

}
