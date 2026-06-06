export const endpoints = {
  auth: {
    login: '/auth/login',
    logout: '/auth/logout',
    recover: '/auth/recover'
  },
  usuarios: {
    base: '/usuarios',
    me: '/usuarios/me',
    byId: (id: number) => `/usuarios/${id}`,
    estado: (id: number) => `/usuarios/${id}/estado`,
    bulk: '/usuarios/bulk',
    filter: '/usuarios/filter'
  },
  personas: {
    base: '/personas',
    byId: (id: number) => `/personas/${id}`
  },
  roles: {
    base: '/roles',
    byId: (id: number) => `/roles/${id}`
  },
  opciones: {
    base: '/opciones'
  },
  rolOpciones: {
    base: '/rol-opciones'
  },
  menu: {
    base: '/menu'
  },
  sesiones: {
    base: '/sessions',
    lastMe: '/sessions/me/last',
    byUsuario: (idUsuario: number) => `/sessions/${idUsuario}`
  },
  dashboard: {
    resumen: '/dashboard/resumen',
    sesionesFallidas: '/dashboard/sesiones-fallidas'
  }
} as const;
