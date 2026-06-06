import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { DataTable } from '../components/shared/DataTable';

describe('DataTable', () => {
  it('aplica clase profesional para encabezado verde reutilizable', () => {
    render(
      <DataTable
        columns={[{ header: 'Nombre', accessor: 'nombre' }]}
        data={[{ id: 1, nombre: 'Admin' }]}
        rowKey={(item) => item.id}
        emptyMessage="Sin datos"
      />
    );

    expect(screen.getByRole('table')).toHaveClass('table-professional');
    expect(screen.getByRole('columnheader', { name: /nombre/i })).toBeInTheDocument();
  });
});
