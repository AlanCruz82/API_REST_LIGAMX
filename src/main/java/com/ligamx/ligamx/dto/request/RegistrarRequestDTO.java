package com.ligamx.ligamx.dto.request;

import com.ligamx.ligamx.entity.security.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarRequestDTO {

    @NotBlank(message = "El usuario no puede ser vacio")
    private String username;
    @NotBlank(message = "La contrasena no puede ser vacia")
    private String password;
    @Size(max = 2, message = "El usuario no puede tener mas de dos roles")
    private List<RoleEnum> listaRoles;
}
