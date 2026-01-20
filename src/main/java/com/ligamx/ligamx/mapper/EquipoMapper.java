package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import com.ligamx.ligamx.dto.request.EquipoRequestDTO;
import com.ligamx.ligamx.dto.response.EquipoResponseDTO;
import com.ligamx.ligamx.entity.Equipo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

    EquipoResponseDTO toResponseDTO(Equipo equipo);

    EquipoResumenDTO toResumenDTO(Equipo equipo);

    Equipo toEntity(EquipoRequestDTO dtoRequest);
}
