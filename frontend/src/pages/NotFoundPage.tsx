import { Link } from 'react-router-dom';
import { ROUTES } from '../utils/constants';

export const NotFoundPage = () => (
  <section className="page-section compact-message">
    <h1>Página no encontrada</h1>
    <p>La ruta solicitada no existe.</p>
    <Link to={ROUTES.bienvenida}>Ir al inicio</Link>
  </section>
);
