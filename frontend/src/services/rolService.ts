import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { Rol } from '../types/rol.types';

export const rolService = {
  listar: async (): Promise<Rol[]> => {
    const { data } = await axiosClient.get<Rol[]>(endpoints.roles.base);
    return data;
  }
};
