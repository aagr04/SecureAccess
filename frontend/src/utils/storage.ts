import type { AuthUser } from '../types/auth.types';
import type { MenuItem } from '../types/menu.types';

const USER_KEY = 'loginapp.user';
const MENU_KEY = 'loginapp.menu';

const normalizeMenu = (menu: MenuItem[]): MenuItem[] =>
  menu.map((item) => {
    if (item.nombre === 'Sesiones' || item.ruta === '/sessions') {
      return { ...item, ruta: '/sesiones', icono: item.icono === '/sesiones' ? 'clock' : (item.icono ?? 'clock') };
    }
    return item;
  });

export const storage = {
  getUser: (): AuthUser | null => {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? (JSON.parse(raw) as AuthUser) : null;
  },
  setUser: (user: AuthUser): void => localStorage.setItem(USER_KEY, JSON.stringify(user)),
  getMenu: (): MenuItem[] => {
    const raw = localStorage.getItem(MENU_KEY);
    if (!raw) return [];
    const menu = normalizeMenu(JSON.parse(raw) as MenuItem[]);
    localStorage.setItem(MENU_KEY, JSON.stringify(menu));
    return menu;
  },
  setMenu: (menu: MenuItem[]): void => localStorage.setItem(MENU_KEY, JSON.stringify(normalizeMenu(menu))),
  clear: (): void => {
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(MENU_KEY);
  }
};
