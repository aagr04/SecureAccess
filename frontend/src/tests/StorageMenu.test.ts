import { describe, expect, it, beforeEach } from 'vitest';
import { storage } from '../utils/storage';

describe('storage menu migration', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('normaliza la ruta visual legacy /sessions a /sesiones', () => {
    localStorage.setItem(
      'loginapp.menu',
      JSON.stringify([{ idOpcion: 6, nombre: 'Sesiones', ruta: '/sessions', icono: 'clock', orden: 6 }])
    );

    expect(storage.getMenu()).toEqual([{ idOpcion: 6, nombre: 'Sesiones', ruta: '/sesiones', icono: 'clock', orden: 6 }]);
  });
});
