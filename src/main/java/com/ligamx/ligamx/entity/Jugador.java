package com.ligamx.ligamx.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String nombre;

    @Column(length = 40)
    private String apellidoPaterno;

    @Column(nullable = false, length = 40)
    private String apellidoMaterno;

    @Column(updatable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 45)
    private String pais;

    //Anotacion para indicar que el ENUM se debe guardar como String
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PosicionJugador posicion;

    //Anotacion para evitar generar joins con Equipo cuando llamemos a un jugador
    //En caso de querer acceder al equipo solo lo indicamos en el servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;
}
