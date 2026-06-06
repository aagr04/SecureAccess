import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import { AuthContext } from '../auth/AuthContext';
import { ProtectedRoute } from '../routes/ProtectedRoute';

describe('ProtectedRoute', () => {
  it('redirige a login si no existe token', () => {
    render(
      <AuthContext.Provider
        value={{
          token: null,
          user: null,
          menu: [],
          isAuthenticated: false,
          loading: false,
          login: vi.fn(),
          logout: vi.fn(),
          setMenu: vi.fn()
        }}
      >
        <MemoryRouter initialEntries={['/bienvenida']}>
          <Routes>
            <Route element={<ProtectedRoute />}>
              <Route path="/bienvenida" element={<p>Privado</p>} />
            </Route>
            <Route path="/login" element={<p>Login destino</p>} />
          </Routes>
        </MemoryRouter>
      </AuthContext.Provider>
    );

    expect(screen.getByText('Login destino')).toBeInTheDocument();
  });
});
