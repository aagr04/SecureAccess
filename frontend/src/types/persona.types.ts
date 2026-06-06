export interface Persona {
  idPersona?: number;
  nombres: string;
  apellidos: string;
  identificacion: string;
  fechaNacimiento?: string;
  activo?: boolean;
}

export type PersonaRequest = Omit<Persona, 'idPersona' | 'activo'>;
