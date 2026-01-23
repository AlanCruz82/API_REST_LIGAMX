package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.DetallePartidoRequestDTO;
import com.ligamx.ligamx.dto.response.DetallePartidoResponseDTO;
import com.ligamx.ligamx.entity.DetallePartido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        //Referencia al mapper de equipo para poder crear el resumen del equipo al crear el dto de respuesta
        uses = EquipoMapper.class
)
public interface DetallePartidoMapper {

    //Ignoramos el mapeo del campo idEquipo ya que lo vamos a obtener del servicio
    //del partido cuando obtengamos el detalle del partido pedido
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "partido", ignore = true)
    DetallePartido toEntity(DetallePartidoRequestDTO dto);

    //Asignamos el campo equipo de la entidad del detalle al campo equipo (resumen de equipo) del dto de respuesta
    @Mapping(source = "equipo", target = "equipo")
    DetallePartidoResponseDTO toResponseDTO(DetallePartido entidadDp);
}
