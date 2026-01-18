package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.Partido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface PartidoMapper {

    //Ignoramos el mapeo del campo Torneo ya que como peticion esperamos recibir el id
    //del torneo, por lo que debemos gestionar la busqueda y asignacion del torneo en el servicio

    //Ignoramos el mapeo del campo detallesPartido ya que es un DTO de otra entidad
    //por lo que debemos gestionarlo en el servicio del partido
    @Mapping(target = "torneo", ignore = true)
    @Mapping(target = "detallesPartido", ignore = true)
    Partido toEntity(PartidoRequestDTO dtoRequest);

    //Ignoramos el mapeo del campo idTorneo ya que, como solo vamos a mostrar el id del torneo
    //vamos a gestionar esta parte en el servicio

    //Ignoramos el mapeo del campo detallesPartido ya que es un DTO de otra entidad
    //por lo que debemos gesionarlo en el servicio
    @Mapping(target = "idTorneo", ignore = true)
    @Mapping(target = "detallesPartido", ignore = true)
    PartidoResponseDTO toResponseDTO(Partido entidadPartido);
}
