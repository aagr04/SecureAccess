import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { UsuarioForm } from '../components/usuarios/UsuarioForm';

describe('UsuarioForm', () => {
  it('no precarga username ni contrasena en modo crear', () => {
    render(<UsuarioForm isAdmin={false} onSubmit={vi.fn()} />);

    const username = screen.getByLabelText(/username/i);
    const password = screen.getByLabelText(/contraseña/i);

    expect(username).toHaveValue('');
    expect(password).toHaveValue('');
    expect(username).toHaveAttribute('name', 'new-user-username');
    expect(username).toHaveAttribute('autocomplete', 'off');
    expect(password).toHaveAttribute('name', 'new-user-password');
    expect(password).toHaveAttribute('autocomplete', 'new-password');
    expect(screen.getByLabelText(/email/i)).toBeDisabled();
  });

  it('valida username, password e identificacion antes de guardar', () => {
    const onSubmit = vi.fn();
    render(<UsuarioForm isAdmin={false} onSubmit={onSubmit} />);

    fireEvent.change(screen.getByLabelText(/nombres/i), { target: { value: 'Ana' } });
    fireEvent.change(screen.getByLabelText(/apellidos/i), { target: { value: 'Paz' } });
    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: '1111222233' } });
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'usuario' } });
    fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'password' } });
    fireEvent.click(screen.getByRole('button', { name: /guardar usuario/i }));

    expect(onSubmit).not.toHaveBeenCalled();
    expect(screen.getByText(/una mayuscula y un numero/i)).toBeInTheDocument();
    expect(screen.getByText(/un signo/i)).toBeInTheDocument();
    expect(screen.getByText(/4 numeros iguales/i)).toBeInTheDocument();
  });

  it('no permite escribir email ni lo envia en el payload', async () => {
    const onSubmit = vi.fn().mockResolvedValue({ email: 'jpiguavel@mail.com' });
    render(<UsuarioForm isAdmin={false} onSubmit={onSubmit} />);

    const emailInput = screen.getByLabelText(/email/i);
    expect(emailInput).toHaveAttribute('readonly');
    expect(emailInput).toBeDisabled();

    fireEvent.change(screen.getByLabelText(/nombres/i), { target: { value: 'Juan Alberto' } });
    fireEvent.change(screen.getByLabelText(/apellidos/i), { target: { value: 'Piguave Loor' } });
    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: '1203574901' } });
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'Juan2024A' } });
    fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'Clave@123' } });
    fireEvent.click(screen.getByRole('button', { name: /guardar usuario/i }));

    await waitFor(() => expect(onSubmit).toHaveBeenCalled());
    expect(onSubmit.mock.calls[0][0]).not.toHaveProperty('email');
    expect(await screen.findByText(/correo generado: jpiguavel@mail.com/i)).toBeInTheDocument();
  });

  it('carga username al editar pero mantiene password vacio', () => {
    render(
      <UsuarioForm
        isAdmin={false}
        onSubmit={vi.fn()}
        usuario={{
          idUsuario: 1,
          username: 'EditUser123',
          email: 'edit@mail.com',
          status: 'ACTIVO',
          activo: true,
          intentosFallidos: 0,
          sesionActiva: false,
          persona: {
            idPersona: 1,
            nombres: 'Edit',
            apellidos: 'User',
            identificacion: '1203574901',
            fechaNacimiento: '1990-01-01',
            activo: true
          }
        }}
      />
    );

    expect(screen.getByLabelText(/username/i)).toHaveValue('EditUser123');
    expect(screen.getByLabelText(/contraseña/i)).toHaveValue('');
  });

  it('notifica cancelacion de edicion para volver a modo crear', () => {
    const onCancelEdit = vi.fn();
    render(
      <UsuarioForm
        isAdmin={false}
        onSubmit={vi.fn()}
        onCancelEdit={onCancelEdit}
        usuario={{
          idUsuario: 1,
          username: 'EditUser123',
          email: 'edit@mail.com',
          status: 'ACTIVO',
          activo: true,
          intentosFallidos: 0,
          sesionActiva: false
        }}
      />
    );

    fireEvent.click(screen.getByRole('button', { name: /cancelar edición/i }));

    expect(onCancelEdit).toHaveBeenCalled();
  });
});
