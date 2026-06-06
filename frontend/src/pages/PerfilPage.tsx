import { useEffect, useState } from 'react';
import { UsuarioForm } from '../components/usuarios/UsuarioForm';
import { Alert } from '../components/shared/Alert';
import { Loader } from '../components/shared/Loader';
import { usuarioService } from '../services/usuarioService';
import type { Usuario, UsuarioRequest } from '../types/usuario.types';

export const PerfilPage = () => {
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    void usuarioService.obtenerMe()
      .then(setUsuario)
      .catch((err) => setError(err instanceof Error ? err.message : 'No se pudo cargar el perfil.'))
      .finally(() => setLoading(false));
  }, []);

  const guardarPerfil = async (payload: UsuarioRequest): Promise<Usuario> => {
    const actualizado = await usuarioService.actualizarMe(payload);
    setUsuario(actualizado);
    return actualizado;
  };

  if (loading) return <Loader />;

  return (
    <section className="page-section">
      <h1>Mi perfil</h1>
      {error ? <Alert type="error" message={error} /> : null}
      <UsuarioForm usuario={usuario} isAdmin={false} onSubmit={guardarPerfil} />
    </section>
  );
};
