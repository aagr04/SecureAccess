import { Navigate, Route, Routes } from 'react-router-dom';
import { AppLayout } from '../components/layout/AppLayout';
import { AccessDeniedPage } from '../pages/AccessDeniedPage';
import { BienvenidaPage } from '../pages/BienvenidaPage';
import { DashboardPage } from '../pages/DashboardPage';
import { LoginPage } from '../pages/LoginPage';
import { NotFoundPage } from '../pages/NotFoundPage';
import { OpcionesPage } from '../pages/OpcionesPage';
import { PerfilPage } from '../pages/PerfilPage';
import { PersonasPage } from '../pages/PersonasPage';
import { RecoverPasswordPage } from '../pages/RecoverPasswordPage';
import { RolesPage } from '../pages/RolesPage';
import { SesionesPage } from '../pages/SesionesPage';
import { UsuariosPage } from '../pages/UsuariosPage';
import { ROUTES } from '../utils/constants';
import { ROLES } from '../utils/roles';
import { ProtectedRoute } from './ProtectedRoute';
import { RoleRoute } from './RoleRoute';

export const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<Navigate to={ROUTES.bienvenida} replace />} />
    <Route path={ROUTES.login} element={<LoginPage />} />
    <Route path={ROUTES.recover} element={<RecoverPasswordPage />} />
    <Route path="/recover-password" element={<RecoverPasswordPage />} />
    <Route element={<ProtectedRoute />}>
      <Route element={<AppLayout />}>
        <Route path={ROUTES.bienvenida} element={<BienvenidaPage />} />
        <Route path="/welcome" element={<Navigate to={ROUTES.bienvenida} replace />} />
        <Route path={ROUTES.perfil} element={<PerfilPage />} />
        <Route path="/profile" element={<Navigate to={ROUTES.perfil} replace />} />
        <Route path={ROUTES.accessDenied} element={<AccessDeniedPage />} />
        <Route element={<RoleRoute allowedRoles={[ROLES.admin]} />}>
          <Route path={ROUTES.dashboard} element={<DashboardPage />} />
          <Route path={ROUTES.usuarios} element={<UsuariosPage />} />
          <Route path="/users" element={<Navigate to={ROUTES.usuarios} replace />} />
          <Route path="/bulk-upload" element={<UsuariosPage />} />
          <Route path={ROUTES.personas} element={<PersonasPage />} />
          <Route path={ROUTES.roles} element={<RolesPage />} />
          <Route path={ROUTES.opciones} element={<OpcionesPage />} />
          <Route path={ROUTES.sesiones} element={<SesionesPage />} />
          <Route path="/sessions" element={<Navigate to={ROUTES.sesiones} replace />} />
        </Route>
      </Route>
    </Route>
    <Route path={ROUTES.notFound} element={<NotFoundPage />} />
    <Route path="*" element={<NotFoundPage />} />
  </Routes>
);
