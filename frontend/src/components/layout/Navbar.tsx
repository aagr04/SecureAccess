import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../../auth/useAuth';
import { Button } from '../shared/Button';

export const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [closing, setClosing] = useState(false);

  const handleLogout = async (): Promise<void> => {
    setClosing(true);
    try {
      await logout();
      navigate('/login', { replace: true });
    } finally {
      setClosing(false);
    }
  };

  return (
    <header className="navbar">
      <div>
        <strong>{user?.username}</strong>
        <span>{user?.rol}</span>
      </div>
      <Button type="button" variant="danger" className="logout-button" onClick={handleLogout} loading={closing} tooltip="Salir del sistema">
        Salir
      </Button>
    </header>
  );
};
