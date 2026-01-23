package com.ligamx.ligamx.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class RespuestaError {

    private String mensaje;
    private int codigoEstado;
    private LocalDateTime fechaHora;
    private String detalles;

    public RespuestaError(String mensaje, int codigoEstado, String detalles) {
        this.mensaje = mensaje;
        this.codigoEstado = codigoEstado;
        //Establecemos el la fecha y hora del error a la fechaHora en la que se genere una nueva instancia de la respuesta error
        this.fechaHora = LocalDateTime.now();
        this.detalles = detalles;
    }
}
