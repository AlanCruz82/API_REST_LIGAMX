package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.DetallePartidoDTO;
import com.ligamx.ligamx.entity.DetallePartido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DetallePartidoMapper {

    //Ignoramos el mapeo del campo idEquipo ya que lo vamos a obtener del servicio
    //del partido cuando obtengamos el detalle del partido pedido
    @Mapping(target = "equipo", ignore = true)
    DetallePartido toEntity(DetallePartidoDTO dto);

    //Como DTO de respuesta ya no debemos hacer validaciones o ignorar campos poeque estamos obteniendo la
    //informacion directamente de la base de datos por lo que ya se garantizo que hubiera integridad en ella
    @Mapping(source = "equipo.id", target = "idEquipo")
    DetallePartidoDTO toDTO(DetallePartido entidadDp);
}
