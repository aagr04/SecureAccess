import { createContext, useCallback, useEffect, useMemo, useReducer, useRef, type ReactNode } from 'react';
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
  user: storage.getUser(),
  menu: storage.getMenu(),
  isAuthenticated: false,
  loading: true
};

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const [state, dispatch] = useReducer(authReducer, initialState);
  const channelRef = useRef<BroadcastChannel | null>(null);

  const clearSession = useCallback((notifyTabs = false): void => {
    storage.clear();
    dispatch({ type: 'LOGOUT' });
    if (notifyTabs) {
      channelRef.current?.postMessage({ type: 'LOGOUT' });
    }
  }, []);

  useEffect(() => {
    channelRef.current = 'BroadcastChannel' in window ? new BroadcastChannel('auth') : null;

    const handleRemoteLogout = (event: MessageEvent): void => {
      if (event.data?.type === 'LOGOUT') {
        clearSession(false);
      }
    };
    const handleUnauthorized = (): void => clearSession(true);

    channelRef.current?.addEventListener('message', handleRemoteLogout);
    window.addEventListener('auth:unauthorized', handleUnauthorized);

    return () => {
      channelRef.current?.removeEventListener('message', handleRemoteLogout);
      channelRef.current?.close();
      window.removeEventListener('auth:unauthorized', handleUnauthorized);
    };
  }, [clearSession]);

  useEffect(() => {
    let mounted = true;
    authService
      .me()
      .then((response) => {
        if (!mounted) return;
        const user: AuthUser = {
          idUsuario: response.idUsuario,
          username: response.username,
          email: response.email,
          rol: response.rol
        };
        const menu = response.menu ?? [];
        storage.setUser(user);
        storage.setMenu(menu);
        dispatch({ type: 'LOGIN_SUCCESS', payload: { user, menu } });
      })
      .catch(() => {
        if (mounted) {
          clearSession(false);
        }
      });
    return () => {
      mounted = false;
    };
  }, [clearSession]);

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
      storage.setUser(user);
      storage.setMenu(menu);
      dispatch({ type: 'LOGIN_SUCCESS', payload: { user, menu } });
    } catch (error) {
      dispatch({ type: 'SET_LOADING', payload: false });
      throw error;
    }
  }, []);

  const logout = useCallback(async (): Promise<void> => {
    try {
      await authService.logout();
    } finally {
      clearSession(true);
    }
  }, [clearSession]);

  const setMenu = useCallback((menu: MenuItem[]): void => {
    storage.setMenu(menu);
    dispatch({ type: 'SET_MENU', payload: menu });
  }, []);

  const value = useMemo(() => ({ ...state, login, logout, setMenu }), [state, login, logout, setMenu]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
