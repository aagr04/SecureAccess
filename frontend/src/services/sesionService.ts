import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { Sesion } from '../types/sesion.types';

export const sesionService = {
  obtenerSesiones: async (): Promise<Sesion[]> => {
    const { data } = await axiosClient.get<Sesion[]>(endpoints.sesiones.base);
    return data;
  },
  obtenerSesionesPorUsuario: async (idUsuario: number): Promise<Sesion[]> => {
    const { data } = await axiosClient.get<Sesion[]>(endpoints.sesiones.byUsuario(idUsuario));
    return data;
  },
  obtenerUltimaSesionPropia: async (): Promise<Sesion> => {
    const { data } = await axiosClient.get<Sesion>(endpoints.sesiones.lastMe);
    return data;
  }
};
