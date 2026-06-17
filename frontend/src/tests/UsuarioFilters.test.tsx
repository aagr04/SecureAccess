import { fireEvent, render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { buildUsuarioFilters, UsuarioFilters } from '../components/usuarios/UsuarioFilters';

describe('UsuarioFilters', () => {
  it('construye filtros solo con identificacion', () => {
    expect(buildUsuarioFilters({ identificacion: ' 1203574901 ', username: 'Admin1234', email: 'x@mail.com' })).toEqual({
      identificacion: '1203574901'
    });
    expect(buildUsuarioFilters({ identificacion: ' ' })).toEqual({});
  });

  it('permite filtrar usando solo identificacion', () => {
    const onFilter = vi.fn();
    render(<UsuarioFilters onFilter={onFilter} onClear={vi.fn()} />);

    const filterButton = screen.getByRole('button', { name: /filtrar por identificación/i });
    expect(filterButton).toHaveClass('bg-sky-500');

    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: '1203574901' } });
    fireEvent.click(filterButton);

    expect(onFilter).toHaveBeenCalledWith({ identificacion: '1203574901' });
  });

  it('normaliza la identificacion a numeros y maximo 10 digitos', () => {
    const onFilter = vi.fn();
    render(<UsuarioFilters onFilter={onFilter} onClear={vi.fn()} />);

    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: 'abc120357490199' } });
    fireEvent.click(screen.getByRole('button', { name: /filtrar por identificación/i }));

    expect(screen.getByLabelText(/identificacion/i)).toHaveValue('1203574901');
    expect(onFilter).toHaveBeenCalledWith({ identificacion: '1203574901' });
  });

  it('renderiza solo identificacion como filtro visible', () => {
    render(<UsuarioFilters onFilter={vi.fn()} onClear={vi.fn()} />);

    expect(screen.getByLabelText(/identificacion/i)).toBeInTheDocument();
    expect(screen.queryByLabelText(/nombres/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/apellidos/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/username/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/email/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/estado/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/^rol$/i)).not.toBeInTheDocument();
  });

  it('limpiar vacia los campos y recarga la lista inicial', () => {
    const onClear = vi.fn();
    render(<UsuarioFilters onFilter={vi.fn()} onClear={onClear} />);

    const identificacion = screen.getByLabelText(/identificacion/i);
    const clearButton = screen.getByRole('button', { name: /limpiar filtro/i });
    expect(clearButton).toHaveClass('border-sky-500');
    expect(clearButton).toHaveClass('text-sky-600');
    expect(clearButton).toHaveAttribute('type', 'button');

    fireEvent.change(identificacion, { target: { value: '1203574901' } });
    fireEvent.click(clearButton);

    expect(identificacion).toHaveValue('');
    expect(onClear).toHaveBeenCalled();
  });

  it('renderiza filtrar y limpiar como botones celestes con tooltip', () => {
    render(<UsuarioFilters onFilter={vi.fn()} onClear={vi.fn()} />);

    const filterButton = screen.getByRole('button', { name: /filtrar por identificación/i });
    const clearButton = screen.getByRole('button', { name: /limpiar filtro/i });

    expect(filterButton).toHaveClass('bg-sky-500');
    expect(filterButton).toHaveClass('text-white');
    expect(filterButton).toHaveAttribute('title', 'Filtrar por identificación');
    expect(filterButton).toHaveAttribute('type', 'button');
    expect(clearButton).toHaveClass('border-sky-500');
    expect(clearButton).toHaveClass('text-sky-600');
    expect(clearButton).toHaveAttribute('title', 'Limpiar filtro');
    expect(clearButton).toHaveAttribute('type', 'button');
  });
});
