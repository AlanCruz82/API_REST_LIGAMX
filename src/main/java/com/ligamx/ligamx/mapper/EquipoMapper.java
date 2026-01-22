package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.EquipoResumenDTO;
import com.ligamx.ligamx.dto.request.EquipoRequestDTO;
import com.ligamx.ligamx.dto.response.EquipoResponseDTO;
import com.ligamx.ligamx.entity.Equipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

    EquipoResponseDTO toResponseDTO(Equipo equipo);

    //El equipo fuente del dto es el equipo del jugador pasado por el metodo toResponseDTO del jugadorMapper
    @Mapping(source = "equipo.id", target = "id")
    @Mapping(source = "equipo.nombre", target = "nombre")
    EquipoResumenDTO toResumenDTO(Equipo equipo);

    Equipo toEntity(EquipoRequestDTO dtoRequest);
}
