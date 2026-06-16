import axios, { AxiosError } from 'axios';

interface BackendError {
  message?: string;
  error?: string;
}

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status?: number
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

export const axiosClient = axios.create({
  baseURL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<BackendError>) => {
    const status = error.response?.status;

    // 401 means the session cookie is absent, expired, or invalidated.
    // 403 means the user is authenticated but not authorized; keep the session active.
    if (status === 401) {
      window.dispatchEvent(new Event('auth:unauthorized'));
      if (window.location.pathname !== '/login') {
        window.location.assign('/login');
      }
    }

    if (!error.response) {
      return Promise.reject(new ApiError('No hay conexion con el backend. Verifique que el servidor este disponible.'));
    }

    const backendMessage = error.response.data?.message || error.response.data?.error;
    const message = mapBackendError(backendMessage, status);
    return Promise.reject(new ApiError(message, status));
  }
);

const mapBackendError = (message?: string, status?: number): string => {
  const normalized = normalizeMessage(message);
  if (normalized.includes('not found') || normalized.includes('no encontrado')) return 'Usuario no encontrado.';
  if (normalized.includes('password') || normalized.includes('contrasena')) return 'Contraseña incorrecta.';
  if (normalized.includes('bloque')) return 'Usuario bloqueado.';
  if (normalized.includes('sesion activa')) return 'El usuario ya tiene una sesion activa.';
  if (status === 403) return message || 'Acceso denegado.';
  if (status === 401) return 'Sesion expirada o invalida. Inicie sesion nuevamente.';
  if (status === 400) return message || 'Error de validacion.';
  if (status && status >= 500) return 'Error de servidor. Intente nuevamente mas tarde.';
  return message || 'Ocurrio un error inesperado.';
};

const normalizeMessage = (message?: string): string =>
  (message ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase();
