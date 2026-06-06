package com.empresa.loginapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "opciones")
public class OpcionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Long idOpcion;
    private String nombre;
    private String ruta;
    private String icono;
    private Integer orden;
    private Boolean activo;
    @PrePersist void prePersist() { if (activo == null) activo = true; }
}
