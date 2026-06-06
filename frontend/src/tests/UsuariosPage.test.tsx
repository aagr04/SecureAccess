import { fireEvent, render, screen } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { UsuariosPage } from '../pages/UsuariosPage';

const mocks = vi.hoisted(() => ({
  useAuth: vi.fn(),
  useUsuarios: vi.fn()
}));

vi.mock('../auth/useAuth', () => ({
  useAuth: mocks.useAuth
}));

vi.mock('../hooks/useUsuarios', () => ({
  useUsuarios: mocks.useUsuarios
}));

vi.mock('../components/usuarios/UsuarioBulkUpload', () => ({
  UsuarioBulkUpload: () => <div>Carga masiva</div>
}));

vi.mock('../components/usuarios/UsuarioForm', () => ({
  UsuarioForm: () => <div>Formulario usuario</div>
}));

describe('UsuariosPage', () => {
  const listar = vi.fn();
  const filtrar = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    mocks.useUsuarios.mockReturnValue({
      usuarios: [],
      loading: false,
      error: null,
      listar,
      filtrar,
      guardar: vi.fn(),
      cambiarEstado: vi.fn(),
      eliminar: vi.fn()
    });
  });

  it('muestra filtros globales para ADMIN', () => {
    mocks.useAuth.mockReturnValue({ user: { rol: 'ADMIN' } });

    render(<UsuariosPage />);

    expect(screen.getByRole('button', { name: /filtrar usuarios/i })).toBeInTheDocument();
    expect(screen.getByText('Carga masiva')).toBeInTheDocument();
  });

  it('oculta filtros globales para USER', () => {
    mocks.useAuth.mockReturnValue({ user: { rol: 'USER' } });

    render(<UsuariosPage />);

    expect(screen.queryByRole('button', { name: /filtrar usuarios/i })).not.toBeInTheDocument();
    expect(screen.queryByText('Carga masiva')).not.toBeInTheDocument();
  });

  it('muestra mensaje de busqueda sin resultados despues de filtrar', () => {
    mocks.useAuth.mockReturnValue({ user: { rol: 'ADMIN' } });

    render(<UsuariosPage />);
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'NoExiste1999' } });
    fireEvent.click(screen.getByRole('button', { name: /filtrar usuarios/i }));

    expect(filtrar).toHaveBeenCalledWith({ username: 'NoExiste1999' });
    expect(screen.getByText('No se encontraron usuarios con los filtros ingresados.')).toBeInTheDocument();
  });

  it('limpiar filtros recarga la lista inicial', () => {
    mocks.useAuth.mockReturnValue({ user: { rol: 'ADMIN' } });

    render(<UsuariosPage />);
    fireEvent.click(screen.getByRole('button', { name: /limpiar filtros/i }));

    expect(listar).toHaveBeenCalled();
  });
});
