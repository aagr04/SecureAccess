import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { AuthContext } from '../auth/AuthContext';
import { LoginForm } from '../components/auth/LoginForm';

const loginMock = vi.fn();

const renderLogin = () =>
  render(
    <AuthContext.Provider
      value={{
        token: null,
        user: null,
        menu: [],
        isAuthenticated: false,
        loading: false,
        login: loginMock,
        logout: vi.fn(),
        setMenu: vi.fn()
      }}
    >
      <MemoryRouter>
        <LoginForm />
      </MemoryRouter>
    </AuthContext.Provider>
  );

describe('LoginPage', () => {
  afterEach(() => vi.clearAllMocks());

  it('renderiza formulario de login', () => {
    renderLogin();
    expect(screen.getByLabelText(/usuario o correo/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/contraseña/i)).toBeInTheDocument();
  });

  it('no llama API si los campos estan vacios', () => {
    renderLogin();
    fireEvent.click(screen.getByRole('button', { name: /ingresar/i }));
    expect(loginMock).not.toHaveBeenCalled();
    expect(screen.getAllByText(/obligatorio/i).length).toBeGreaterThan(0);
  });

  it('muestra error del backend', async () => {
    loginMock.mockRejectedValueOnce(new Error('Usuario bloqueado.'));
    renderLogin();
    fireEvent.change(screen.getByLabelText(/usuario o correo/i), { target: { value: 'Admin1234' } });
    fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'Admin@1234' } });
    fireEvent.click(screen.getByRole('button', { name: /ingresar/i }));
    await waitFor(() => expect(screen.getByText('Usuario bloqueado.')).toBeInTheDocument());
  });

  it('redirige a bienvenida cuando el login es exitoso', async () => {
    loginMock.mockResolvedValueOnce(undefined);
    render(
      <AuthContext.Provider
        value={{
          token: null,
          user: null,
          menu: [],
          isAuthenticated: false,
          loading: false,
          login: loginMock,
          logout: vi.fn(),
          setMenu: vi.fn()
        }}
      >
        <MemoryRouter initialEntries={['/login']}>
          <Routes>
            <Route path="/login" element={<LoginForm />} />
            <Route path="/bienvenida" element={<p>Bienvenida destino</p>} />
          </Routes>
        </MemoryRouter>
      </AuthContext.Provider>
    );

    fireEvent.change(screen.getByLabelText(/usuario o correo/i), { target: { value: 'Admin1234' } });
    fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'Admin@1234' } });
    fireEvent.click(screen.getByRole('button', { name: /ingresar/i }));

    await waitFor(() => expect(screen.getByText('Bienvenida destino')).toBeInTheDocument());
  });
});
