import { useEffect, useState } from 'react';
import { Alert } from '../components/shared/Alert';
import { DataTable, type DataTableColumn } from '../components/shared/DataTable';
import { opcionService } from '../services/opcionService';
import type { Opcion } from '../types/opcion.types';

const columns: DataTableColumn<Opcion>[] = [
  { header: 'Nombre', accessor: 'nombre' },
  { header: 'Ruta', accessor: 'ruta' },
  { header: 'Icono', accessor: (opcion) => opcion.icono ?? 'No disponible' },
  { header: 'Orden', accessor: (opcion) => opcion.orden ?? 'No disponible' },
  { header: 'Activo', accessor: (opcion) => (opcion.activo ? 'Si' : 'No') }
];

export const OpcionesPage = () => {
  const [opciones, setOpciones] = useState<Opcion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async (): Promise<void> => {
      try {
        setOpciones(await opcionService.listar());
      } catch (err) {
        setError(err instanceof Error ? err.message : 'No se pudieron cargar las opciones.');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, []);

  return (
    <section className="page-section">
      <h1>Opciones de menu</h1>
      {error ? <Alert type="error" message={error} /> : null}
      <DataTable columns={columns} data={opciones} loading={loading} rowKey={(opcion) => opcion.idOpcion} emptyMessage="No hay opciones registradas." />
    </section>
  );
};
