export const ROLES = {
  admin: 'ADMIN',
  user: 'USER'
} as const;

export const normalizeRole = (role?: string): string => role?.replace('ROLE_', '').toUpperCase() ?? '';

export const hasRole = (userRole: string | undefined, allowedRoles: string[]): boolean =>
  allowedRoles.map(normalizeRole).includes(normalizeRole(userRole));

export const isAdmin = (role?: string): boolean => normalizeRole(role) === ROLES.admin;
