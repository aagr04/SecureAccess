import { useEffect, useState } from 'react';
import { Alert } from '../components/shared/Alert';
import { DataTable, type DataTableColumn } from '../components/shared/DataTable';
import { personaService } from '../services/personaService';
import type { Persona } from '../types/persona.types';

const columns: DataTableColumn<Persona>[] = [
  { header: 'Nombres', accessor: 'nombres' },
  { header: 'Apellidos', accessor: 'apellidos' },
  { header: 'Identificacion', accessor: 'identificacion' },
  { header: 'Fecha nacimiento', accessor: (persona) => persona.fechaNacimiento ?? 'No disponible' },
  { header: 'Activo', accessor: (persona) => (persona.activo ? 'Si' : 'No') }
];

export const PersonasPage = () => {
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async (): Promise<void> => {
      try {
        setPersonas(await personaService.listar());
      } catch (err) {
        setError(err instanceof Error ? err.message : 'No se pudieron cargar las personas.');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, []);

  return (
    <section className="page-section">
      <h1>Personas</h1>
      {error ? <Alert type="error" message={error} /> : null}
      <DataTable
        columns={columns}
        data={personas}
        loading={loading}
        rowKey={(persona) => persona.idPersona ?? persona.identificacion}
        emptyMessage="No hay personas registradas."
      />
    </section>
  );
};
