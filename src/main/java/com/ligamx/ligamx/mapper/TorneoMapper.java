package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.Torneo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TorneoMapper {

    //Ignoramos el mapeo del campo detallesTorneo ya que es un DTO de otra entidad y por ello
    //debemos gestionarlo en el servicio del torneo
    @Mapping(target = "detallesTorneo", ignore = true)
    Torneo toEntity(TorneoRequestDTO dtoRequest);

    //Ignoramos el mapeo del campo detallesTorneo ya que es un DTO de otra entidad y por ello
    //debemos gestionarlo en el servicio del torneo
    @Mapping(target = "detallesTorneo", ignore = true)
    TorneoResponseDTO toResponseDTO(Torneo entidadTorneo);
}
