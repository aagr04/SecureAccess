import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { MenuItem } from '../types/menu.types';

export const menuService = {
  obtenerMenu: async (): Promise<MenuItem[]> => {
    const { data } = await axiosClient.get<MenuItem[]>(endpoints.menu.base);
    return data;
  }
};
