package com.ligamx.ligamx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//Anotacion para indicar que esta clase va a manejar de forma global los errores de los controladores rest
@RestControllerAdvice
public class GlobalHandlerException {

    //Metodo para manejar las excepciones que no encuentren un recurso
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RespuestaError> handlerResourceNotFoundException(ResourceNotFoundException exception){
        //Nueva instancia de la clase respuesta error que personaliza la excepcion mostrada a traves de la construccion
        //de el nuevo objeto
        RespuestaError error = new RespuestaError(
                "Recurso no encontrado",
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    //Metodo para manejar las excepciones por una incorrecta solicitud
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<RespuestaError> handlerBadRequestException(BadRequestException exception){
        //Nueva instancia del error personalizado que vamos a mostrar (no incluimos la fecha en el contructor
        // ya que se establece a la fecha y hora en la cual se crea la instancia)
        RespuestaError error = new RespuestaError(
                "Solicitud incorrecta",
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    //Metodo para manejar las excepciones por el conflicto al querer guardar un recurso ya existente
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<RespuestaError> handlerResourceConflictException(ResourceConflictException exception){
        //Nueva instancia del error personalizado que vamos a mostrar (no incluimos la fecha en el contructor
        // ya que se establece a la fecha y hora en la cual se crea la instancia)
        RespuestaError error = new RespuestaError(
                "El recurso ya existe",
                HttpStatus.CONFLICT.value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> errores = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(
                error -> errores.put(error.getField(), error.getDefaultMessage()));

        String detalles = "Errores de validacion en los campos " + String.join(", ", errores.keySet());

        RespuestaError error = new RespuestaError(
                "Validacion de campo fallida",
                HttpStatus.BAD_REQUEST.value(),
                detalles
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
