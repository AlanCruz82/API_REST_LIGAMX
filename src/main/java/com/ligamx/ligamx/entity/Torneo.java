package com.ligamx.ligamx.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private NombreTorneo nombre;

    @Column(updatable = false)
    private LocalDate fechaInicio;

    @Column(updatable = false)
    private LocalDate fechaFin;

    @Column(updatable = false, nullable = false)
    private Integer anio;

    //Incluimos el atributo de cascada para que cuando insertemos o eliminemos un torneo se elimine el detalle de ese torneo
    //oprhenRemoval se utiliza en caso de eliminar un equipo se elimine tambien de la bd
    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true)
    //Inicializamos la lista para evitar excepciones de NullPointer
    private List<DetalleTorneo> detallesTorneo = new ArrayList<>();
}
