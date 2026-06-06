import { useState } from 'react';
import type { UsuarioFilter } from '../../types/usuario.types';
import { USER_STATUS } from '../../utils/constants';
import { Button } from '../shared/Button';
import { Input } from '../shared/Input';
import { Select } from '../shared/Select';

interface UsuarioFiltersProps {
  onFilter: (filters: UsuarioFilter) => void;
  onClear: () => void;
}

const initialFilters: UsuarioFilter = {};

export const buildUsuarioFilters = (filters: UsuarioFilter): UsuarioFilter =>
  Object.fromEntries(
    Object.entries(filters)
      .map(([key, value]) => [key, value?.trim()] as const)
      .filter(([, value]) => value && value !== 'Seleccione')
  ) as UsuarioFilter;

export const UsuarioFilters = ({ onFilter, onClear }: UsuarioFiltersProps) => {
  const [filters, setFilters] = useState<UsuarioFilter>(initialFilters);
  const update = (field: keyof UsuarioFilter, value: string): void => setFilters((current) => ({ ...current, [field]: value }));

  return (
    <form
      className="filters-grid panel"
      onSubmit={(event) => {
        event.preventDefault();
        onFilter(buildUsuarioFilters(filters));
      }}
    >
      <Input label="Nombres" value={filters.nombres ?? ''} onChange={(e) => update('nombres', e.target.value)} />
      <Input label="Apellidos" value={filters.apellidos ?? ''} onChange={(e) => update('apellidos', e.target.value)} />
      <Input label="Identificacion" value={filters.identificacion ?? ''} onChange={(e) => update('identificacion', e.target.value)} />
      <Input label="Username" value={filters.username ?? ''} onChange={(e) => update('username', e.target.value)} />
      <Input label="Email" value={filters.email ?? ''} onChange={(e) => update('email', e.target.value)} />
      <Select
        label="Estado"
        value={filters.status ?? ''}
        onChange={(e) => update('status', e.target.value)}
        options={USER_STATUS.map((status) => ({ value: status, label: status }))}
      />
      <Input label="Rol" value={filters.rol ?? ''} onChange={(e) => update('rol', e.target.value)} />
      <div className="actions-row">
        <Button type="submit" tooltip="Filtrar usuarios">
          Filtrar
        </Button>
        <Button
          type="button"
          variant="secondary"
          tooltip="Limpiar filtros"
          onClick={() => {
            setFilters(initialFilters);
            onClear();
          }}
        >
          Limpiar
        </Button>
      </div>
    </form>
  );
};
