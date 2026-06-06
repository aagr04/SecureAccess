import { useEffect, useState } from 'react';
import { dashboardService } from '../services/dashboardService';
import type { DashboardResumen } from '../types/dashboard.types';
import type { Sesion } from '../types/sesion.types';

export const useDashboard = () => {
  const [resumen, setResumen] = useState<DashboardResumen | null>(null);
  const [sesionesFallidas, setSesionesFallidas] = useState<Sesion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async (): Promise<void> => {
      setLoading(true);
      setError(null);
      try {
        const [resumenData, sesionesData] = await Promise.all([
          dashboardService.obtenerResumen(),
          dashboardService.obtenerSesionesFallidas()
        ]);
        setResumen(resumenData);
        setSesionesFallidas(sesionesData);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'No se pudo cargar el dashboard.');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, []);

  return { resumen, sesionesFallidas, loading, error };
};
