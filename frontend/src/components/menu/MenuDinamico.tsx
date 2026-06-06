import { NavLink } from 'react-router-dom';
import type { MenuItem } from '../../types/menu.types';
import { EmptyState } from '../shared/EmptyState';

interface MenuDinamicoProps {
  menu: MenuItem[];
  compact?: boolean;
}

const visibleMenuName = (name: string): string => (name === 'Bienvenida' ? 'Bienvenido' : name);

export const MenuDinamico = ({ menu, compact = false }: MenuDinamicoProps) => {
  if (!menu.length) return <EmptyState message="No hay opciones de menu disponibles." />;

  return (
    <nav className="menu" aria-label="Menu principal">
      {menu.map((item) => {
        const label = visibleMenuName(item.nombre);
        return (
          <NavLink
            key={item.idOpcion}
            to={item.ruta}
            title={label}
            className={({ isActive }) => (isActive ? 'menu-link active' : 'menu-link')}
          >
            {compact ? <span aria-hidden="true">{label.charAt(0)}</span> : <span className="menu-label">{label}</span>}
          </NavLink>
        );
      })}
    </nav>
  );
};
