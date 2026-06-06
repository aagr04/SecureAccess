import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../auth/useAuth';
import { ROUTES } from '../utils/constants';
import { hasRole } from '../utils/roles';

export const RoleRoute = ({ allowedRoles }: { allowedRoles: string[] }) => {
  const { user } = useAuth();
  return hasRole(user?.rol, allowedRoles) ? <Outlet /> : <Navigate to={ROUTES.accessDenied} replace />;
};
