import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { UsuarioBulkUpload } from '../components/usuarios/UsuarioBulkUpload';
import { validateBulkFile } from '../utils/validators';

const cargaMasivaMock = vi.hoisted(() => vi.fn());

vi.mock('../services/usuarioService', () => ({
  usuarioService: {
    cargaMasiva: cargaMasivaMock
  }
}));

describe('UsuarioBulkUpload', () => {
  beforeEach(() => cargaMasivaMock.mockReset());

  it('valida extension de archivo', () => {
    const file = new File(['x'], 'usuarios.txt', { type: 'text/plain' });
    expect(validateBulkFile(file)).toMatch(/xlsx, .xls o .csv/i);
    expect(validateBulkFile(new File(['x'], 'usuarios.xlsx'))).toBeUndefined();
    expect(validateBulkFile(new File(['x'], 'usuarios.csv'))).toBeUndefined();
  });

  it('deshabilita carga sin archivo y permite eliminar archivo seleccionado', () => {
    render(<UsuarioBulkUpload />);

    expect(screen.getByRole('button', { name: /cargar archivo seleccionado/i })).toBeDisabled();

    const input = screen.getByLabelText(/archivo de usuarios/i);
    const file = new File(['nombres,apellidos'], 'usuarios.csv', { type: 'text/csv' });
    fireEvent.change(input, { target: { files: [file] } });

    expect(screen.getByText('usuarios.csv')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /cargar archivo seleccionado/i })).toBeEnabled();

    fireEvent.click(screen.getByRole('button', { name: /eliminar archivo seleccionado/i }));

    expect(screen.queryByText('usuarios.csv')).not.toBeInTheDocument();
    expect(screen.getByRole('button', { name: /cargar archivo seleccionado/i })).toBeDisabled();
  });

  it('envia archivo y muestra resumen de carga', async () => {
    cargaMasivaMock.mockResolvedValueOnce({
      totalProcesados: 2,
      exitosos: 1,
      fallidos: 1,
      erroresPorFila: ['Fila 2: identificacion invalida']
    });
    render(<UsuarioBulkUpload />);

    const file = new File(['nombres,apellidos'], 'usuarios.csv', { type: 'text/csv' });
    fireEvent.change(screen.getByLabelText(/archivo de usuarios/i), { target: { files: [file] } });
    fireEvent.click(screen.getByRole('button', { name: /cargar archivo seleccionado/i }));

    await waitFor(() => expect(cargaMasivaMock).toHaveBeenCalledWith(file));
    expect(screen.getByText(/total procesados: 2/i)).toBeInTheDocument();
    expect(screen.getByText(/exitosos: 1/i)).toBeInTheDocument();
    expect(screen.getByText(/fila 2: identificacion invalida/i)).toBeInTheDocument();
  });
});
