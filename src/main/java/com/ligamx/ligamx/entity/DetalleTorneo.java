package com.ligamx.ligamx.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "detalle_torneo",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"equipo_id", "torneo_id"})
        }
)
public class DetalleTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Utilizamos una carga perezosa para que no traer todos los equipos a menos que los vayamos a usar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    //Utilizamos una carga perezosa para que no traer todos los torneos a menos que los vayamos a usar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    private Integer victorias;

    private Integer empates;

    private Integer derrotas;

    private Integer puntos;

}
