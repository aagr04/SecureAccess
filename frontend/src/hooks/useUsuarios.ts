import { useCallback, useEffect, useState } from 'react';
import { usuarioService } from '../services/usuarioService';
import type { CambiarEstadoUsuarioRequest, Usuario, UsuarioFilter, UsuarioRequest } from '../types/usuario.types';

export const useUsuarios = (autoLoad = true) => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleError = (err: unknown, fallback: string): void => {
    setError(err instanceof Error ? err.message : fallback);
  };

  const listar = useCallback(async (): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      setUsuarios(await usuarioService.listar());
    } catch (err) {
      handleError(err, 'No se pudieron cargar los usuarios.');
    } finally {
      setLoading(false);
    }
  }, []);

  const filtrar = async (filters: UsuarioFilter): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      setUsuarios(await usuarioService.filtrar(filters));
    } catch (err) {
      handleError(err, 'No se pudo realizar la búsqueda de usuarios.');
    } finally {
      setLoading(false);
    }
  };

  const guardar = async (payload: UsuarioRequest, id?: number): Promise<Usuario | void> => {
    setLoading(true);
    setError(null);
    try {
      const saved = id ? await usuarioService.actualizar(id, payload) : await usuarioService.crear(payload);
      await listar();
      return saved;
    } catch (err) {
      handleError(err, 'No se pudo guardar el usuario.');
    } finally {
      setLoading(false);
    }
  };

  const cambiarEstado = async (id: number, payload: CambiarEstadoUsuarioRequest): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      await usuarioService.cambiarEstado(id, payload);
      await listar();
    } catch (err) {
      handleError(err, 'No se pudo cambiar el estado.');
    } finally {
      setLoading(false);
    }
  };

  const eliminar = async (id: number): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      await usuarioService.eliminar(id);
      await listar();
    } catch (err) {
      handleError(err, 'No se pudo eliminar el usuario.');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (autoLoad) void listar();
  }, [autoLoad, listar]);

  return { usuarios, loading, error, listar, filtrar, guardar, cambiarEstado, eliminar, setError };
};
