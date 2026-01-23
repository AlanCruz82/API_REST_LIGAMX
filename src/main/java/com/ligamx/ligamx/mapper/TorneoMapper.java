package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.TorneoRequestDTO;
import com.ligamx.ligamx.dto.response.TorneoResponseDTO;
import com.ligamx.ligamx.entity.Torneo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        //Referencia al mapper de detalleTorneo que se usa para poder construir el campo detallesTorneo
        //del dto que damos como respuesta del torneo
        uses = DetalleTorneoMapper.class
)
public interface TorneoMapper {

    //Ignoramos el mapeo del campo detallesTorneo ya que es un DTO de otra entidad y por ello
    //debemos gestionarlo en el servicio del torneo
    @Mapping(target = "detallesTorneo", ignore = true)
    Torneo toEntity(TorneoRequestDTO dtoRequest);

    //Referencia del campo detallesTorneo de la entidad torneo que debe ser convertida a detallesTorneoResponseDTO
    //en el responseDTO (para esto mapper llama al mapper de detallesTorneo que se encarga de construir el dto de esa
    //entidad)
    @Mapping(source = "detallesTorneo", target = "detallesTorneo")
    TorneoResponseDTO toResponseDTO(Torneo entidadTorneo);
}
