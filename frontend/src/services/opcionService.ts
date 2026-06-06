import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { Opcion } from '../types/opcion.types';

export const opcionService = {
  listar: async (): Promise<Opcion[]> => {
    const { data } = await axiosClient.get<Opcion[]>(endpoints.opciones.base);
    return data;
  }
};
