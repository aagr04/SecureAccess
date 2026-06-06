package com.empresa.loginapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "rol_usuario")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RolUsuarioEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol_usuario")
    private Long idRolUsuario;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_rol")
    private RolEntity rol;
    private Boolean activo;
    @PrePersist void prePersist() { if (activo == null) activo = true; }
}
