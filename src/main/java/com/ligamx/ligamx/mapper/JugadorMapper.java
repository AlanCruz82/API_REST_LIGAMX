package com.ligamx.ligamx.mapper;

import com.ligamx.ligamx.dto.request.JugadorRequestDTO;
import com.ligamx.ligamx.dto.response.JugadorResponseDTO;
import com.ligamx.ligamx.entity.Jugador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//ComponentModel nos permite indicarle al contenedor de spring que debe generar un bean de esta clase
@Mapper(componentModel = "spring",
        //Referencia al mapper del equipo para poder obtener el resumen del equipo enviado en el responseDTO del jugador
        uses = EquipoMapper.class
)
public interface JugadorMapper {

    //Ignoramos el mapeo del campo equipo ya que en la peticion vamos a recibir solamente el id del equipo
    //por lo que gestionamos el equipo del jugador en el servicio del jugador
    @Mapping(target = "equipo", ignore = true)
    Jugador toEntity(JugadorRequestDTO dtoRequest);

    @Mapping(source = "equipo", target = "equipo")
    JugadorResponseDTO toResponseDTO(Jugador entidadJugador);
}
