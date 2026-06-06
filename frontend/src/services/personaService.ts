import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { Persona, PersonaRequest } from '../types/persona.types';

export const personaService = {
  listar: async (): Promise<Persona[]> => {
    const { data } = await axiosClient.get<Persona[]>(endpoints.personas.base);
    return data;
  },
  obtenerPorId: async (id: number): Promise<Persona> => {
    const { data } = await axiosClient.get<Persona>(endpoints.personas.byId(id));
    return data;
  },
  crear: async (request: PersonaRequest): Promise<Persona> => {
    const { data } = await axiosClient.post<Persona>(endpoints.personas.base, request);
    return data;
  },
  actualizar: async (id: number, request: PersonaRequest): Promise<Persona> => {
    const { data } = await axiosClient.put<Persona>(endpoints.personas.byId(id), request);
    return data;
  },
  eliminar: async (id: number): Promise<void> => {
    await axiosClient.delete(endpoints.personas.byId(id));
  }
};
