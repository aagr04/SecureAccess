import { render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { AuthContext } from '../auth/AuthContext';
import { BienvenidaPage } from '../pages/BienvenidaPage';

const obtenerMeMock = vi.hoisted(() => vi.fn());
const obtenerUltimaSesionPropiaMock = vi.hoisted(() => vi.fn());

vi.mock('../services/usuarioService', () => ({
  usuarioService: {
    obtenerMe: obtenerMeMock
  }
}));

vi.mock('../services/sesionService', () => ({
  sesionService: {
    obtenerUltimaSesionPropia: obtenerUltimaSesionPropiaMock
  }
}));

describe('BienvenidaPage', () => {
  it('muestra datos del usuario y ultima sesion', async () => {
    obtenerMeMock.mockResolvedValueOnce({
      idUsuario: 1,
      username: 'Juan2024A',
      email: 'jpiguavel@mail.com',
      status: 'ACTIVO',
      activo: true,
      intentosFallidos: 0,
      sesionActiva: true,
      persona: { nombres: 'Juan Alberto', apellidos: 'Piguave Loor', identificacion: '1203574901' }
    });
    obtenerUltimaSesionPropiaMock.mockResolvedValueOnce({
      idSesion: 1,
      idUsuario: 1,
      fechaIngreso: '2026-06-05T20:00:00',
      fechaCierre: '2026-06-05T21:00:00',
      activa: false,
      exitoso: true,
      intentosFallidos: 0
    });

    render(
      <AuthContext.Provider
        value={{
          user: { idUsuario: 1, username: 'Juan2024A', email: 'jpiguavel@mail.com', rol: 'USER' },
          menu: [],
          isAuthenticated: true,
          loading: false,
          login: vi.fn(),
          logout: vi.fn(),
          setMenu: vi.fn()
        }}
      >
        <BienvenidaPage />
      </AuthContext.Provider>
    );

    await waitFor(() => expect(screen.getByText(/bienvenido, juan alberto piguave loor/i)).toBeInTheDocument());
    expect(screen.getByText('jpiguavel@mail.com')).toBeInTheDocument();
    expect(screen.getByText('USER')).toBeInTheDocument();
    expect(screen.getByText('ACTIVO')).toBeInTheDocument();
    expect(screen.getByText('0')).toBeInTheDocument();
  });
});
