import { useEffect, useState } from 'react';
import { Alert } from '../components/shared/Alert';
import { DataTable, type DataTableColumn } from '../components/shared/DataTable';
import { sesionService } from '../services/sesionService';
import type { Sesion } from '../types/sesion.types';
import { formatDateTime } from '../utils/formatters';

const columns: DataTableColumn<Sesion>[] = [
  { header: 'Usuario', accessor: 'idUsuario' },
  { header: 'Ingreso', accessor: (sesion) => formatDateTime(sesion.fechaIngreso) },
  { header: 'Cierre', accessor: (sesion) => formatDateTime(sesion.fechaCierre) },
  { header: 'Activa', accessor: (sesion) => (sesion.activa ? 'Si' : 'No') },
  { header: 'Exitoso', accessor: (sesion) => (sesion.exitoso ? 'Si' : 'No') },
  { header: 'Mensaje', accessor: (sesion) => sesion.mensaje ?? 'No disponible' },
  { header: 'Fallidos', accessor: (sesion) => sesion.intentosFallidos ?? 0 }
];

export const SesionesPage = () => {
  const [sesiones, setSesiones] = useState<Sesion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async (): Promise<void> => {
      try {
        setSesiones(await sesionService.obtenerSesiones());
      } catch (err) {
        setError(err instanceof Error ? err.message : 'No se pudieron cargar las sesiones.');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, []);

  return (
    <section className="page-section">
      <h1>Sesiones</h1>
      {error ? <Alert type="error" message={error} /> : null}
      <DataTable columns={columns} data={sesiones} loading={loading} rowKey={(sesion) => sesion.idSesion} emptyMessage="No hay sesiones registradas." />
    </section>
  );
};
