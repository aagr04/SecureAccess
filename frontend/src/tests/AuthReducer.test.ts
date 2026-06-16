import { describe, expect, it } from 'vitest';
import { authReducer } from '../auth/authReducer';
import type { AuthState } from '../types/auth.types';
import { storage } from '../utils/storage';

describe('logout', () => {
  it('limpia estado y almacenamiento de sesión', () => {
    const state: AuthState = {
      user: { idUsuario: 1, username: 'Admin1234', email: 'admin@test.com', rol: 'ADMIN' },
      menu: [{ idOpcion: 1, nombre: 'Dashboard', ruta: '/dashboard' }],
      isAuthenticated: true,
      loading: false
    };

    storage.setUser(state.user!);
    storage.setMenu(state.menu);
    storage.clear();

    expect(authReducer(state, { type: 'LOGOUT' })).toEqual({
      user: null,
      menu: [],
      isAuthenticated: false,
      loading: false
    });
    expect(storage.getUser()).toBeNull();
    expect(storage.getMenu()).toEqual([]);
  });
});
