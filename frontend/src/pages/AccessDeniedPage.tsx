import { Link } from 'react-router-dom';
import { ROUTES } from '../utils/constants';

export const AccessDeniedPage = () => (
  <section className="page-section compact-message">
    <h1>Acceso denegado</h1>
    <p>No tiene permisos para acceder a esta sección.</p>
    <Link to={ROUTES.bienvenida}>Volver al inicio</Link>
  </section>
);
