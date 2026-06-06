import { useEffect, useState } from 'react';
import { useAuth } from '../auth/useAuth';
import { Alert } from '../components/shared/Alert';
import { Loader } from '../components/shared/Loader';
import { sesionService } from '../services/sesionService';
import { usuarioService } from '../services/usuarioService';
import type { Sesion } from '../types/sesion.types';
import type { Usuario } from '../types/usuario.types';
import { formatDateTime, fullName } from '../utils/formatters';

export const BienvenidaPage = () => {
  const { user } = useAuth();
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [ultimaSesion, setUltimaSesion] = useState<Sesion | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async (): Promise<void> => {
      if (!user?.idUsuario) return;
      setLoading(true);
      try {
        const data = await usuarioService.obtenerMe();
        setUsuario(data);
        const sesion = await sesionService.obtenerUltimaSesionPropia().catch(() => null);
        setUltimaSesion(sesion);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'No se pudo cargar la información del usuario.');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, [user]);

  if (loading) return <Loader />;

  return (
    <section className="page-section">
      {error ? <Alert type="error" message={error} /> : null}
      <h1>Bienvenido, {fullName(usuario?.persona?.nombres, usuario?.persona?.apellidos)}</h1>
      <div className="info-grid">
        <article><span>Username</span><strong>{usuario?.username ?? user?.username}</strong></article>
        <article><span>Email</span><strong>{usuario?.email ?? user?.email}</strong></article>
        <article><span>Rol</span><strong>{user?.rol}</strong></article>
        <article><span>Estado</span><strong>{usuario?.status ?? 'No disponible'}</strong></article>
        <article><span>Última sesión</span><strong>{formatDateTime(ultimaSesion?.fechaIngreso)}</strong></article>
        <article><span>Fecha fin</span><strong>{formatDateTime(ultimaSesion?.fechaCierre)}</strong></article>
        <article><span>Sesión actual</span><strong>{usuario?.sesionActiva ? 'Activa' : 'Inactiva'}</strong></article>
        <article><span>Intentos fallidos</span><strong>{usuario?.intentosFallidos ?? 0}</strong></article>
      </div>
    </section>
  );
};
