package com.empresa.loginapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rol_opciones")
public class RolOpcionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol_opcion")
    private Long idRolOpcion;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_rol")
    private RolEntity rol;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_opcion")
    private OpcionEntity opcion;
    private Boolean activo;
    @PrePersist void prePersist() { if (activo == null) activo = true; }
}
