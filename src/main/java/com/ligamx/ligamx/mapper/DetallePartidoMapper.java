package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.DetalleTorneoDTO;
import com.ligamx.ligamx.entity.DetallePartido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetallePartidoMapper {

    //Ignoramos el mapeo del campo idEquipo ya que lo vamos a obtener del servicio
    //del partido cuando obtengamos el detalle del partido pedido
    @Mapping(target = "equipo", ignore = true)
    DetallePartido toEntity(DetalleTorneoDTO dto);
}
