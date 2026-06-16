import type { AuthState, AuthUser } from '../types/auth.types';
import type { MenuItem } from '../types/menu.types';

export type AuthAction =
  | { type: 'LOGIN_SUCCESS'; payload: { user: AuthUser; menu: MenuItem[] } }
  | { type: 'SET_MENU'; payload: MenuItem[] }
  | { type: 'LOGOUT' }
  | { type: 'SET_LOADING'; payload: boolean };

export type { AuthState, AuthUser };
