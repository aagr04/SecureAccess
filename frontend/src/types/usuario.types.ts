import type { Persona } from './persona.types';

export interface Usuario {
  idUsuario: number;
  username: string;
  email: string;
  status: string;
  activo: boolean;
  intentosFallidos: number;
  sesionActiva: boolean;
  rol?: string;
  persona?: Persona;
}

export interface UsuarioRequest {
  username: string;
  password?: string;
  email?: string;
  status?: string;
  idPersona?: number;
  idRol?: number;
  rol?: string;
  nombres: string;
  apellidos: string;
  identificacion: string;
  fechaNacimiento?: string;
}

export type UsuarioResponse = Usuario;

export interface UsuarioFilter {
  nombres?: string;
  apellidos?: string;
  identificacion?: string;
  username?: string;
  email?: string;
  status?: string;
  rol?: string;
}

export interface CambiarEstadoUsuarioRequest {
  status: string;
  activo?: boolean;
}

export interface ErrorFilaBulk {
  fila?: number;
  mensaje: string;
}

export interface BulkUploadResponse {
  totalProcesados: number;
  exitosos: number;
  fallidos: number;
  erroresPorFila: string[];
}
