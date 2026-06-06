export interface DashboardResumen {
  totalUsuarios: number;
  usuariosActivos: number;
  usuariosInactivos: number;
  usuariosBloqueados: number;
  usuariosSesionActiva: number;
  usuariosSesionInactiva: number;
  totalSesionesFallidas: number;
}

export interface SesionFallidaResumen {
  idSesion: number;
  idUsuario: number;
  fechaIngreso: string;
  mensaje?: string;
  intentosFallidos?: number;
}
