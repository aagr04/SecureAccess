import { useEffect, useState } from 'react';
import { Alert } from '../components/shared/Alert';
import { DataTable, type DataTableColumn } from '../components/shared/DataTable';
import { rolService } from '../services/rolService';
import type { Rol } from '../types/rol.types';

const columns: DataTableColumn<Rol>[] = [
  { header: 'ID', accessor: 'idRol' },
  { header: 'Nombre', accessor: 'nombre' },
  { header: 'Activo', accessor: (rol) => (rol.activo ? 'Si' : 'No') }
];

export const RolesPage = () => {
  const [roles, setRoles] = useState<Rol[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async (): Promise<void> => {
      try {
        setRoles(await rolService.listar());
      } catch (err) {
        setError(err instanceof Error ? err.message : 'No se pudieron cargar los roles.');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, []);

  return (
    <section className="page-section">
      <h1>Roles</h1>
      {error ? <Alert type="error" message={error} /> : null}
      <DataTable columns={columns} data={roles} loading={loading} rowKey={(rol) => rol.idRol} emptyMessage="No hay roles registrados." />
    </section>
  );
};
