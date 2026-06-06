import { createContext, useCallback, useMemo, useReducer, type ReactNode } from 'react';
import { authService } from '../services/authService';
import type { LoginRequest, AuthState, AuthUser } from '../types/auth.types';
import type { MenuItem } from '../types/menu.types';
import { storage } from '../utils/storage';
import { authReducer } from './authReducer';

interface AuthContextValue extends AuthState {
  login: (request: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
  setMenu: (menu: MenuItem[]) => void;
}

const initialState: AuthState = {
  token: storage.getToken(),
  user: storage.getUser(),
  menu: storage.getMenu(),
  isAuthenticated: Boolean(storage.getToken()),
  loading: false
};

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  const login = useCallback(async (request: LoginRequest): Promise<void> => {
    dispatch({ type: 'SET_LOADING', payload: true });
    try {
      const response = await authService.login(request);
      const user: AuthUser = {
        idUsuario: response.idUsuario,
        username: response.username,
        email: response.email,
        rol: response.rol
      };
      const menu = response.menu ?? [];
      storage.setToken(response.token);
      storage.setUser(user);
      storage.setMenu(menu);
      dispatch({ type: 'LOGIN_SUCCESS', payload: { token: response.token, user, menu } });
    } catch (error) {
      dispatch({ type: 'SET_LOADING', payload: false });
      throw error;
    }
  }, []);

  const logout = useCallback(async (): Promise<void> => {
    try {
      await authService.logout();
    } finally {
      storage.clear();
      dispatch({ type: 'LOGOUT' });
    }
  }, []);

  const setMenu = useCallback((menu: MenuItem[]): void => {
    storage.setMenu(menu);
    dispatch({ type: 'SET_MENU', payload: menu });
  }, []);

  const value = useMemo(() => ({ ...state, login, logout, setMenu }), [state, login, logout, setMenu]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
