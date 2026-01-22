package com.ligamx.ligamx.dto.response;

import com.ligamx.ligamx.dto.request.DetallePartidoRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartidoResponseDTO {

    private Long id;
    private int jornada;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Long idTorneo;
    private List<DetallePartidoResponseDTO> detallesPartido;
}
