package com.ligamx.ligamx.controller;

import com.ligamx.ligamx.dto.request.LoginRequestDTO;
import com.ligamx.ligamx.dto.request.RegistrarRequestDTO;
import com.ligamx.ligamx.dto.response.AutentificacionResponse;
import com.ligamx.ligamx.service.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutentificacionController {

    //Bean de la implementacion hecha del servicio de usuarios para poder logear y registrar los usuarios
    private final UserDetailsImpl userDetails;

    public AutentificacionController(UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
    }

    @PostMapping("/log-in")
    public ResponseEntity<AutentificacionResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(userDetails.login(loginRequestDTO));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AutentificacionResponse> registrar(@RequestBody @Valid RegistrarRequestDTO nuevoUsuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetails.registrar(nuevoUsuario));
    }
}
