package com.ligamx.ligamx.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int jornada;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHoraFin;

    @ManyToOne
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    //Un torneo puede tener multiples detalles de partido(2 por partido el del equipo local y visitante)
    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePartido> detallesPartido = new ArrayList<>();
}
