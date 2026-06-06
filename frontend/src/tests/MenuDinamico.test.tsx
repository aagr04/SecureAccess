import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it } from 'vitest';
import { MenuDinamico } from '../components/menu/MenuDinamico';

describe('MenuDinamico', () => {
  it('renderiza rutas dinamicas del backend', () => {
    render(
      <MemoryRouter>
        <MenuDinamico
          menu={[
            { idOpcion: 1, nombre: 'Dashboard', ruta: '/dashboard', orden: 1 },
            { idOpcion: 2, nombre: 'Roles', ruta: '/roles', orden: 2 },
            { idOpcion: 3, nombre: 'Opciones', ruta: '/opciones', orden: 3 },
            { idOpcion: 4, nombre: 'Sesiones', ruta: '/sesiones', orden: 4 }
          ]}
        />
      </MemoryRouter>
    );

    expect(screen.getByRole('link', { name: /dashboard/i })).toHaveAttribute('href', '/dashboard');
    expect(screen.getByRole('link', { name: /roles/i })).toHaveAttribute('href', '/roles');
    expect(screen.getByRole('link', { name: /opciones/i })).toHaveAttribute('href', '/opciones');
    expect(screen.getByRole('link', { name: /sesiones/i })).toHaveAttribute('href', '/sesiones');
  });
});
