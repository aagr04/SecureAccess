import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { Button } from '../components/shared/Button';

describe('Button', () => {
  it('muestra tooltip y variante danger para Salir', () => {
    render(
      <Button type="button" variant="danger" className="logout-button" tooltip="Salir del sistema">
        Salir
      </Button>
    );

    const button = screen.getByRole('button', { name: /salir del sistema/i });
    expect(button).toHaveAttribute('title', 'Salir del sistema');
    expect(button).toHaveClass('btn-danger');
    expect(button).toHaveClass('logout-button');
  });

  it('aplica variante primaria celeste', () => {
    render(
      <Button type="button" tooltip="Filtrar usuarios">
        Filtrar
      </Button>
    );

    const button = screen.getByRole('button', { name: /filtrar usuarios/i });
    expect(button).toHaveClass('btn-primary');
    expect(button).toHaveClass('bg-sky-500');
    expect(button).toHaveClass('hover:bg-sky-600');
  });
});
