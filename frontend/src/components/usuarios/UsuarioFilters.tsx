import { useState } from 'react';
import type { UsuarioFilter } from '../../types/usuario.types';
import { Button } from '../shared/Button';
import { Input } from '../shared/Input';

interface UsuarioFiltersProps {
  onFilter: (filters: UsuarioFilter) => void;
  onClear: () => void;
}

const initialFilters: UsuarioFilter = {};

export const buildUsuarioFilters = (filters: UsuarioFilter): UsuarioFilter =>
  filters.identificacion?.trim() ? { identificacion: filters.identificacion.trim() } : {};

export const UsuarioFilters = ({ onFilter, onClear }: UsuarioFiltersProps) => {
  const [filters, setFilters] = useState<UsuarioFilter>(initialFilters);
  const filterUsers = (): void => onFilter(buildUsuarioFilters(filters));
  const clearFilters = (): void => {
    setFilters(initialFilters);
    onClear();
  };
  const updateIdentification = (value: string): void => {
    setFilters({ identificacion: value.replace(/\D/g, '').slice(0, 10) });
  };

  return (
    <form className="filters-grid panel" autoComplete="off" onSubmit={(event) => event.preventDefault()}>
      <Input
        label="Identificacion"
        inputMode="numeric"
        maxLength={10}
        placeholder="Ingrese identificacion"
        value={filters.identificacion ?? ''}
        onChange={(e) => updateIdentification(e.target.value)}
      />
      <div className="actions-row">
        <Button type="button" variant="primary" tooltip="Filtrar por identificación" onClick={filterUsers}>
          Filtrar
        </Button>
        <Button type="button" variant="secondary" tooltip="Limpiar filtro" onClick={clearFilters}>
          Limpiar
        </Button>
      </div>
    </form>
  );
};
