import { DashboardResumen } from '../components/dashboard/DashboardResumen';
import { Alert } from '../components/shared/Alert';
import { EmptyState } from '../components/shared/EmptyState';
import { Loader } from '../components/shared/Loader';
import { useDashboard } from '../hooks/useDashboard';
import { formatDateTime } from '../utils/formatters';

export const DashboardPage = () => {
  const { resumen, sesionesFallidas, loading, error } = useDashboard();
  if (loading) return <Loader />;

  return (
    <section className="page-section">
      <h1>Dashboard administrativo</h1>
      {error ? <Alert type="error" message={error} /> : null}
      {resumen ? <DashboardResumen resumen={resumen} /> : <EmptyState message="No hay resumen disponible." />}
      <section className="panel">
        <h2>Inicios de sesión fallidos</h2>
        {sesionesFallidas.length ? (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Usuario</th>
                  <th>Fecha</th>
                  <th>Intentos</th>
                  <th>Mensaje</th>
                </tr>
              </thead>
              <tbody>
                {sesionesFallidas.map((sesion) => (
                  <tr key={sesion.idSesion}>
                    <td>{sesion.idUsuario}</td>
                    <td>{formatDateTime(sesion.fechaIngreso)}</td>
                    <td>{sesion.intentosFallidos ?? 0}</td>
                    <td>{sesion.mensaje ?? 'No disponible'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <EmptyState message="No hay sesiones fallidas registradas." />
        )}
      </section>
    </section>
  );
};
