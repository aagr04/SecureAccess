import type { AuthAction, AuthState } from './authTypes';

export const authReducer = (state: AuthState, action: AuthAction): AuthState => {
  switch (action.type) {
    case 'LOGIN_SUCCESS':
      return {
        token: action.payload.token,
        user: action.payload.user,
        menu: action.payload.menu,
        isAuthenticated: true,
        loading: false
      };
    case 'SET_MENU':
      return { ...state, menu: action.payload };
    case 'SET_LOADING':
      return { ...state, loading: action.payload };
    case 'LOGOUT':
      return { token: null, user: null, menu: [], isAuthenticated: false, loading: false };
    default:
      return state;
  }
};
