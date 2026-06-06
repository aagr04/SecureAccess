import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { UsuarioForm } from '../components/usuarios/UsuarioForm';

describe('UsuarioForm', () => {
  it('no precarga username del admin en modo crear', () => {
    render(<UsuarioForm isAdmin={false} onSubmit={vi.fn()} />);

    expect(screen.getByLabelText(/username/i)).toHaveValue('');
    expect(screen.getByLabelText(/contraseña/i)).toHaveValue('');
  });

  it('valida username, password e identificacion antes de guardar', () => {
    const onSubmit = vi.fn();
    render(<UsuarioForm isAdmin={false} onSubmit={onSubmit} />);
    fireEvent.change(screen.getByLabelText(/nombres/i), { target: { value: 'Ana' } });
    fireEvent.change(screen.getByLabelText(/apellidos/i), { target: { value: 'Paz' } });
    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: '1111222233' } });
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'usuario' } });
    fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'password' } });
    fireEvent.click(screen.getByRole('button', { name: /crear usuario/i }));

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
    fireEvent.change(emailInput, { target: { value: 'manual@mail.com' } });

    fireEvent.change(screen.getByLabelText(/nombres/i), { target: { value: 'Juan Alberto' } });
    fireEvent.change(screen.getByLabelText(/apellidos/i), { target: { value: 'Piguave Loor' } });
    fireEvent.change(screen.getByLabelText(/identificacion/i), { target: { value: '1203574901' } });
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'Juan2024A' } });
    fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'Clave@123' } });
    fireEvent.click(screen.getByRole('button', { name: /crear usuario/i }));

    await waitFor(() => expect(onSubmit).toHaveBeenCalled());
    expect(onSubmit.mock.calls[0][0]).not.toHaveProperty('email');
    expect(await screen.findByText(/correo generado: jpiguavel@mail.com/i)).toBeInTheDocument();
  });
});
