import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/useAuth';
import { ROUTES } from '../../utils/constants';
import { hasErrors, validateLogin } from '../../utils/validators';
import { Alert } from '../shared/Alert';
import { Button } from '../shared/Button';
import { Input } from '../shared/Input';

export const LoginForm = () => {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const [credential, setCredential] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState(validateLogin('', ''));
  const [apiError, setApiError] = useState<string | null>(null);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
    event.preventDefault();
    const validation = validateLogin(credential, password);
    setErrors(validation);
    setApiError(null);
    if (hasErrors(validation)) return;
    try {
      await login({ credential, password });
      navigate(ROUTES.bienvenida, { replace: true });
    } catch (error) {
      setApiError(error instanceof Error ? error.message : 'No se pudo iniciar sesion.');
    }
  };

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      {apiError ? <Alert type="error" message={apiError} /> : null}
      <Input label="Usuario o correo" name="credential" value={credential} onChange={(e) => setCredential(e.target.value)} error={errors.credential} />
      <Input label="Contraseña" name="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} error={errors.password} />
      <Button type="submit" loading={loading} tooltip="Ingresar al sistema">
        Ingresar
      </Button>
      <Link to={ROUTES.recover} title="Recuperar contraseña">
        Recuperar contraseña
      </Link>
    </form>
  );
};
