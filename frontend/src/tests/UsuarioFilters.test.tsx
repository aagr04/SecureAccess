import { fireEvent, render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { buildUsuarioFilters, UsuarioFilters } from '../components/usuarios/UsuarioFilters';
import { cleanUsuarioFilters } from '../services/usuarioService';

describe('UsuarioFilters', () => {
  it('construye filtros sin valores vacios', () => {
    expect(buildUsuarioFilters({ username: ' Admin1234 ', email: '', status: 'ACTIVO' })).toEqual({
      username: 'Admin1234',
      status: 'ACTIVO'
    });
  });

  it('permite filtrar usando solo identificacion', () => {
    const onFilter = vi.fn();
    render(<UsuarioFilters onFilter={onFilter} onClear={vi.fn()} />);

    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: '1203574901' } });
    fireEvent.click(screen.getByRole('button', { name: /filtrar usuarios/i }));

    expect(onFilter).toHaveBeenCalledWith({ identificacion: '1203574901' });
  });

  it('permite filtrar usando solo username', () => {
    const onFilter = vi.fn();
    render(<UsuarioFilters onFilter={onFilter} onClear={vi.fn()} />);

    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'Agustin1999' } });
    fireEvent.click(screen.getByRole('button', { name: /filtrar usuarios/i }));

    expect(onFilter).toHaveBeenCalledWith({ username: 'Agustin1999' });
  });

  it('permite filtrar usando solo email', () => {
    const onFilter = vi.fn();
    render(<UsuarioFilters onFilter={onFilter} onClear={vi.fn()} />);

    fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'aguevarar@mail.com' } });
    fireEvent.click(screen.getByRole('button', { name: /filtrar usuarios/i }));

    expect(onFilter).toHaveBeenCalledWith({ email: 'aguevarar@mail.com' });
  });

  it('permite filtrar por nombres y apellidos juntos sin exigir otros campos', () => {
    const onFilter = vi.fn();
    render(<UsuarioFilters onFilter={onFilter} onClear={vi.fn()} />);

    fireEvent.change(screen.getByLabelText(/nombres/i), { target: { value: 'Agustin' } });
    fireEvent.change(screen.getByLabelText(/apellidos/i), { target: { value: 'Guevara' } });
    fireEvent.click(screen.getByRole('button', { name: /filtrar usuarios/i }));

    expect(onFilter).toHaveBeenCalledWith({ nombres: 'Agustin', apellidos: 'Guevara' });
  });

  it('ignora estado seleccione y construye parametros solo con campos llenos', () => {
    expect(cleanUsuarioFilters({
      nombres: '',
      apellidos: '  Reyes ',
      identificacion: '',
      username: undefined,
      email: ' ',
      status: 'Seleccione',
      rol: ' USER '
    })).toEqual({
      apellidos: 'Reyes',
      rol: 'USER'
    });
  });

  it('limpiar vacia los campos y recarga la lista inicial', () => {
    const onClear = vi.fn();
    render(<UsuarioFilters onFilter={vi.fn()} onClear={onClear} />);

    const username = screen.getByLabelText(/username/i);
    fireEvent.change(username, { target: { value: 'Agustin1999' } });
    fireEvent.click(screen.getByRole('button', { name: /limpiar filtros/i }));

    expect(username).toHaveValue('');
    expect(onClear).toHaveBeenCalled();
  });
});
