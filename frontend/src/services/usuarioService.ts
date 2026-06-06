import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type {
  BulkUploadResponse,
  CambiarEstadoUsuarioRequest,
  Usuario,
  UsuarioFilter,
  UsuarioRequest
} from '../types/usuario.types';

export const cleanUsuarioFilters = (filters: UsuarioFilter): UsuarioFilter =>
  Object.fromEntries(
    Object.entries(filters)
      .map(([key, value]) => [key, value?.trim()] as const)
      .filter(([, value]) => value && value !== 'Seleccione')
  ) as UsuarioFilter;

const withoutEmail = (request: UsuarioRequest): UsuarioRequest => {
  const { email: _email, ...payload } = request;
  return payload;
};

export const usuarioService = {
  listar: async (): Promise<Usuario[]> => {
    const { data } = await axiosClient.get<Usuario[]>(endpoints.usuarios.base);
    return data;
  },
  obtenerPorId: async (id: number): Promise<Usuario> => {
    const { data } = await axiosClient.get<Usuario>(endpoints.usuarios.byId(id));
    return data;
  },
  obtenerMe: async (): Promise<Usuario> => {
    const { data } = await axiosClient.get<Usuario>(endpoints.usuarios.me);
    return data;
  },
  crear: async (request: UsuarioRequest): Promise<Usuario> => {
    const { data } = await axiosClient.post<Usuario>(endpoints.usuarios.base, withoutEmail(request));
    return data;
  },
  actualizar: async (id: number, request: UsuarioRequest): Promise<Usuario> => {
    const { data } = await axiosClient.put<Usuario>(endpoints.usuarios.byId(id), withoutEmail(request));
    return data;
  },
  actualizarMe: async (request: UsuarioRequest): Promise<Usuario> => {
    const { data } = await axiosClient.put<Usuario>(endpoints.usuarios.me, withoutEmail(request));
    return data;
  },
  eliminar: async (id: number): Promise<void> => {
    await axiosClient.delete(endpoints.usuarios.byId(id));
  },
  cambiarEstado: async (id: number, request: CambiarEstadoUsuarioRequest): Promise<Usuario> => {
    const { data } = await axiosClient.patch<Usuario>(endpoints.usuarios.estado(id), request);
    return data;
  },
  filtrar: async (filters: UsuarioFilter): Promise<Usuario[]> => {
    const { data } = await axiosClient.get<Usuario[]>(endpoints.usuarios.filter, { params: cleanUsuarioFilters(filters) });
    return data;
  },
  cargaMasiva: async (file: File): Promise<BulkUploadResponse> => {
    const formData = new FormData();
    formData.append('file', file);
    const { data } = await axiosClient.post<BulkUploadResponse>(endpoints.usuarios.bulk, formData);
    return data;
  }
};
