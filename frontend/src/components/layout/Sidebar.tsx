import type { MenuItem } from '../../types/menu.types';
import { MenuDinamico } from '../menu/MenuDinamico';
import { Button } from '../shared/Button';

interface SidebarProps {
  menu: MenuItem[];
  open: boolean;
  onToggle: () => void;
}

export const Sidebar = ({ menu, open, onToggle }: SidebarProps) => (
  <aside className={open ? 'sidebar sidebar-open' : 'sidebar sidebar-closed'}>
    <div className="sidebar-header">
      <h1 className="brand-title">Login App</h1>
      <Button
        type="button"
        variant="ghost"
        className="sidebar-toggle"
        onClick={onToggle}
        tooltip={open ? 'Ocultar menu' : 'Mostrar menu'}
      >
        ☰
      </Button>
    </div>
    <MenuDinamico menu={menu} compact={!open} />
  </aside>
);
