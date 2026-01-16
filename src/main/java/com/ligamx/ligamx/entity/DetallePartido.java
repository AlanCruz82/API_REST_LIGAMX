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
        name = "detalle_partido",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"equipo_id", "partido_id"})
        }
)
public class DetallePartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id")
    private Partido partido;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private RolPartido rolEquipo;

    private Integer goles;
}
