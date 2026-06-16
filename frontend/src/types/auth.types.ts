import type { MenuItem } from './menu.types';

export interface LoginRequest {
  credential: string;
  password: string;
}

export interface LoginResponse {
  idUsuario: number;
  username: string;
  email: string;
  rol: string;
  menu?: MenuItem[];
}

export interface RecoverPasswordRequest {
  credential: string;
}

export interface RecoverPasswordResponse {
  message: string;
}

export interface AuthUser {
  idUsuario: number;
  username: string;
  email: string;
  rol: string;
  status?: string;
  nombres?: string;
  apellidos?: string;
  sesionActiva?: boolean;
  intentosFallidos?: number;
}

export interface AuthState {
  user: AuthUser | null;
  menu: MenuItem[];
  isAuthenticated: boolean;
  loading: boolean;
}
