import type { DashboardResumen as DashboardResumenType } from '../../types/dashboard.types';
import { DashboardCard } from './DashboardCard';

export const DashboardResumen = ({ resumen }: { resumen: DashboardResumenType }) => (
  <section className="dashboard-grid">
    <DashboardCard title="Total usuarios" value={resumen.totalUsuarios} />
    <DashboardCard title="Usuarios activos" value={resumen.usuariosActivos} tone="success" />
    <DashboardCard title="Usuarios inactivos" value={resumen.usuariosInactivos} tone="warning" />
    <DashboardCard title="Usuarios bloqueados" value={resumen.usuariosBloqueados} tone="danger" />
    <DashboardCard title="Sesión activa" value={resumen.usuariosSesionActiva} tone="success" />
    <DashboardCard title="Sesión inactiva" value={resumen.usuariosSesionInactiva} />
    <DashboardCard title="Sesiones fallidas" value={resumen.totalSesionesFallidas} tone="danger" />
  </section>
);
