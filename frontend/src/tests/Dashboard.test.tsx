import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { DashboardResumen } from '../components/dashboard/DashboardResumen';

describe('Dashboard', () => {
  it('muestra indicadores', () => {
    render(
      <DashboardResumen
        resumen={{
          totalUsuarios: 10,
          usuariosActivos: 7,
          usuariosInactivos: 2,
          usuariosBloqueados: 1,
          usuariosSesionActiva: 3,
          usuariosSesionInactiva: 7,
          totalSesionesFallidas: 4
        }}
      />
    );

    expect(screen.getByText('Total usuarios')).toBeInTheDocument();
    expect(screen.getByText('10')).toBeInTheDocument();
    expect(screen.getByText('Sesiones fallidas')).toBeInTheDocument();
  });
});
