import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it } from 'vitest';
import { RecoverPasswordPage } from '../pages/RecoverPasswordPage';

describe('RecoverPasswordPage', () => {
  it('renderiza formulario de recuperación', () => {
    render(
      <MemoryRouter>
        <RecoverPasswordPage />
      </MemoryRouter>
    );

    expect(screen.getByRole('heading', { name: /recuperar contraseña/i })).toBeInTheDocument();
    expect(screen.getByLabelText(/usuario o correo/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /recuperar/i })).toBeInTheDocument();
  });
});
