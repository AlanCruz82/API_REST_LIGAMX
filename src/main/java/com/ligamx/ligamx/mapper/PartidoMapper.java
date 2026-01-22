package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.PartidoRequestDTO;
import com.ligamx.ligamx.dto.response.PartidoResponseDTO;
import com.ligamx.ligamx.entity.Partido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        //Referencia al mapper del detallePartido que se usa para transformar la entidad detallePartido de cada partido
        //a su dto
        uses = DetallePartidoMapper.class
)
public interface PartidoMapper {

    //Ignoramos el mapeo del campo Torneo ya que como peticion esperamos recibir el id
    //del torneo, por lo que debemos gestionar la busqueda y asignacion del torneo en el servicio

    //Ignoramos el mapeo del campo detallesPartido ya que como argumento de entrada tenemos solo un resumen
    //de los detalles del partido por lo que debemos crearlos, almacenarlos en la base de datos y por ultimo asignarlos
    //al partido dentro del servicio del partido
    @Mapping(target = "torneo", ignore = true)
    @Mapping(target = "detallesPartido", ignore = true)
    Partido toEntity(PartidoRequestDTO dtoRequest);

    //Ya no debemos ignorar ningun atributo del DTO, Al estar obteniendo la informacion de la base de datos
    //previamente ya se garantizo que la informacion estuviera integra
    @Mapping(source = "torneo.id", target = "idTorneo")
    //El mapeo de los detallesPartido se relaciona con el mapeo del DTO de detallesPartido
    @Mapping(source = "detallesPartido", target = "detallesPartido")
    PartidoResponseDTO toResponseDTO(Partido entidadPartido);
}
