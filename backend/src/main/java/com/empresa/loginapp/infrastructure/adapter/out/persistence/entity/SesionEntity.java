package com.empresa.loginapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "sesiones")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SesionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Long idSesion;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;
    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
    private Boolean activa;
    private Boolean exitoso;
    private String mensaje;
    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos;
}
