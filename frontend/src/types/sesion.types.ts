export interface Sesion {
  idSesion: number;
  idUsuario: number;
  fechaIngreso: string;
  fechaCierre?: string | null;
  activa: boolean;
  exitoso: boolean;
  mensaje?: string;
  intentosFallidos?: number;
}

export type UltimaSesion = Sesion;
