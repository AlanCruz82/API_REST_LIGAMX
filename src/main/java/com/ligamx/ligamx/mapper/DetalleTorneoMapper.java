package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.DetalleTorneoRequestDTO;
import com.ligamx.ligamx.entity.DetalleTorneo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        //Referencia al mapper de equipo que se usa para convertir el campo equipo de la entidad detalleTorneo
        //a un resumen del equipo (id + nombreEquipo)
        uses = EquipoMapper.class
)
public interface DetalleTorneoMapper {

    //Ignoramos el mapeo del idEquipo ya que lo vamos a obtener en el
    //servicio del torneo
    @Mapping(target = "equipo", ignore = true)
    DetalleTorneo toEntity(DetalleTorneoRequestDTO dto);

    //Referencia del campo equipo que tiene la entidad detalleTorneo que debe ser mapeado y convertido a un resumen del equipo
    //para dar como respuesta en el dto
    @Mapping(source = "equipo", target = "equipo")
    DetalleTorneoRequestDTO toDTO(DetalleTorneo entidadDt);
}
