package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.DetalleTorneoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface DetalleTorneoMapper {

    //Ignoramos el mapeo del idEquipo ya que lo vamos a obtener en el
    //servicio del torneo
    @Mapping(target = "idEquipo", ignore = true)
    DetalleTorneoMapper toEntity(DetalleTorneoDTO dto);
}
