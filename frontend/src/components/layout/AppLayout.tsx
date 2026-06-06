import { Outlet } from 'react-router-dom';
import { useState } from 'react';
import { useMenu } from '../../hooks/useMenu';
import { Alert } from '../shared/Alert';
import { LayoutContent } from './LayoutContent';
import { Navbar } from './Navbar';
import { Sidebar } from './Sidebar';

export const AppLayout = () => {
  const { menu, error } = useMenu();
  const [sidebarOpen, setSidebarOpen] = useState(true);

  return (
    <div className={sidebarOpen ? 'app-shell' : 'app-shell sidebar-collapsed'}>
      <Sidebar menu={menu} open={sidebarOpen} onToggle={() => setSidebarOpen((current) => !current)} />
      <div className="main-shell">
        <Navbar />
        <LayoutContent>
          {error ? <Alert type="error" message={error} /> : null}
          <Outlet />
        </LayoutContent>
      </div>
    </div>
  );
};
