import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { DashboardResumen } from '../types/dashboard.types';
import type { Sesion } from '../types/sesion.types';

export const dashboardService = {
  obtenerResumen: async (): Promise<DashboardResumen> => {
    const { data } = await axiosClient.get<DashboardResumen>(endpoints.dashboard.resumen);
    return data;
  },
  obtenerSesionesFallidas: async (): Promise<Sesion[]> => {
    const { data } = await axiosClient.get<Sesion[]>(endpoints.dashboard.sesionesFallidas);
    return data;
  }
};
