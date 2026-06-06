import { fireEvent, render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { UsuarioTable } from '../components/usuarios/UsuarioTable';
import type { Usuario } from '../types/usuario.types';

const usuario: Usuario = {
  idUsuario: 1,
  username: 'Admin1234',
  email: 'admin@test.com',
  status: 'ACTIVO',
  activo: true,
  intentosFallidos: 0,
  sesionActiva: false,
  persona: {
    idPersona: 1,
    nombres: 'Admin',
    apellidos: 'Principal',
    identificacion: '1234567890'
  }
};

describe('UsuarioTable', () => {
  it('muestra acciones administrativas solo para ADMIN', () => {
    const onDelete = vi.fn();
    render(<UsuarioTable usuarios={[usuario]} isAdmin onEdit={vi.fn()} onChangeStatus={vi.fn()} onDelete={onDelete} />);

    fireEvent.click(screen.getByRole('button', { name: /eliminar/i }));
    expect(onDelete).toHaveBeenCalledWith(usuario);
    expect(screen.getByRole('button', { name: /estado/i })).toBeInTheDocument();
  });

  it('oculta acciones administrativas para USER', () => {
    render(<UsuarioTable usuarios={[usuario]} isAdmin={false} onEdit={vi.fn()} onChangeStatus={vi.fn()} onDelete={vi.fn()} />);

    expect(screen.queryByRole('button', { name: /eliminar/i })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /estado/i })).not.toBeInTheDocument();
  });
});
