export const ROUTES = {
  login: '/login',
  recover: '/recover',
  bienvenida: '/bienvenida',
  usuarios: '/usuarios',
  dashboard: '/dashboard',
  personas: '/personas',
  roles: '/roles',
  opciones: '/opciones',
  sesiones: '/sesiones',
  perfil: '/perfil',
  accessDenied: '/access-denied',
  notFound: '/404'
} as const;

export const USER_STATUS = ['ACTIVO', 'INACTIVO', 'BLOQUEADO'] as const;
