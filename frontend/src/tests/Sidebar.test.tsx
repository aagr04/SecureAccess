import { fireEvent, render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import { Sidebar } from '../components/layout/Sidebar';

const menu = [
  { idOpcion: 1, nombre: 'Dashboard', ruta: '/dashboard', icono: 'layout-dashboard' },
  { idOpcion: 2, nombre: 'Usuarios', ruta: '/usuarios', icono: 'users' },
  { idOpcion: 3, nombre: 'Sesiones', ruta: '/sesiones', icono: 'clock' }
];

describe('Sidebar', () => {
  it('puede ocultarse y muestra nombres limpios sin texto tecnico de iconos', () => {
    const onToggle = vi.fn();
    render(
      <MemoryRouter>
        <Sidebar menu={menu} open onToggle={onToggle} />
      </MemoryRouter>
    );

    expect(screen.getByRole('link', { name: /dashboard/i })).toHaveAttribute('href', '/dashboard');
    expect(screen.getByRole('link', { name: /usuarios/i })).toHaveAttribute('href', '/usuarios');
    expect(screen.getByRole('link', { name: /sesiones/i })).toHaveAttribute('href', '/sesiones');
    expect(screen.queryByText('layout-dashboard')).not.toBeInTheDocument();
    expect(screen.queryByText('users')).not.toBeInTheDocument();
    expect(screen.queryByText('clock')).not.toBeInTheDocument();

    fireEvent.click(screen.getByRole('button', { name: /ocultar menu/i }));
    expect(onToggle).toHaveBeenCalled();
  });
});
