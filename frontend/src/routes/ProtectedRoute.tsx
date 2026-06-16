import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../auth/useAuth';
import { Loader } from '../components/shared/Loader';
import { ROUTES } from '../utils/constants';

export const ProtectedRoute = () => {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();
  if (loading) return <Loader label="Validando sesion..." />;
  return isAuthenticated ? <Outlet /> : <Navigate to={ROUTES.login} replace state={{ from: location }} />;
};
