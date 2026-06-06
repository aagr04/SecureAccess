import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import { AuthContext } from '../auth/AuthContext';
import { RoleRoute } from '../routes/RoleRoute';
import { ROLES } from '../utils/roles';

describe('RoleRoute', () => {
  it('bloquea USER en dashboard', () => {
    render(
      <AuthContext.Provider
        value={{
          token: 'token',
          user: { idUsuario: 1, username: 'User1234', email: 'user@test.com', rol: 'USER' },
          menu: [],
          isAuthenticated: true,
          loading: false,
          login: vi.fn(),
          logout: vi.fn(),
          setMenu: vi.fn()
        }}
      >
        <MemoryRouter initialEntries={['/dashboard']}>
          <Routes>
            <Route element={<RoleRoute allowedRoles={[ROLES.admin]} />}>
              <Route path="/dashboard" element={<p>Dashboard</p>} />
            </Route>
            <Route path="/access-denied" element={<p>Acceso denegado</p>} />
          </Routes>
        </MemoryRouter>
      </AuthContext.Provider>
    );

    expect(screen.getByText('Acceso denegado')).toBeInTheDocument();
  });
});
