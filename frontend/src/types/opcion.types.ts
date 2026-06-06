export interface Opcion {
  idOpcion: number;
  nombre: string;
  ruta: string;
  icono?: string;
  orden?: number;
  activo?: boolean;
}

export interface OpcionRequest {
  nombre: string;
  ruta: string;
  icono?: string;
  orden?: number;
}
