import { useCallback, useEffect, useRef, useState } from 'react';
import { menuService } from '../services/menuService';
import type { MenuItem } from '../types/menu.types';
import { useAuth } from '../auth/useAuth';

export const useMenu = () => {
  const { menu, setMenu, isAuthenticated } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const hasFetchedRef = useRef(false);

  const loadMenu = useCallback(async (): Promise<void> => {
    if (!isAuthenticated) return;
    if (menu.length > 0) return;
    if (hasFetchedRef.current) return;

    hasFetchedRef.current = true;
    setLoading(true);
    setError(null);
    try {
      const data = await menuService.obtenerMenu();
      setMenu(data.sort((a, b) => (a.orden ?? 0) - (b.orden ?? 0)));
    } catch (err) {
      hasFetchedRef.current = false;
      setError(err instanceof Error ? err.message : 'No se pudo cargar el menu.');
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated, menu.length, setMenu]);

  useEffect(() => {
    void loadMenu();
  }, [loadMenu]);

  return { menu, loading, error, reload: loadMenu };
};
