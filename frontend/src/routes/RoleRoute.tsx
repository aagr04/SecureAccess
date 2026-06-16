import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../auth/useAuth';
import { Loader } from '../components/shared/Loader';
import { ROUTES } from '../utils/constants';
import { hasRole } from '../utils/roles';

export const RoleRoute = ({ allowedRoles }: { allowedRoles: string[] }) => {
  const { user, loading } = useAuth();
  if (loading) return <Loader label="Validando permisos..." />;
  return hasRole(user?.rol, allowedRoles) ? <Outlet /> : <Navigate to={ROUTES.accessDenied} replace />;
};
