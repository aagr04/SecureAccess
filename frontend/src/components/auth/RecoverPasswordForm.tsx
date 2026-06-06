import { useState } from 'react';
import { Link } from 'react-router-dom';
import { authService } from '../../services/authService';
import { ROUTES } from '../../utils/constants';
import { hasErrors, validateRecover } from '../../utils/validators';
import { Alert } from '../shared/Alert';
import { Button } from '../shared/Button';
import { Input } from '../shared/Input';

export const RecoverPasswordForm = () => {
  const [credential, setCredential] = useState('');
  const [errors, setErrors] = useState(validateRecover(''));
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);
  const [apiError, setApiError] = useState<string | null>(null);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
    event.preventDefault();
    const validation = validateRecover(credential);
    setErrors(validation);
    setMessage(null);
    setApiError(null);
    if (hasErrors(validation)) return;
    setLoading(true);
    try {
      const response = await authService.recoverPassword({ credential });
      setMessage(response.message);
    } catch (error) {
      setApiError(error instanceof Error ? error.message : 'No se pudo recuperar la contraseña.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      {message ? <Alert type="success" message={message} /> : null}
      {apiError ? <Alert type="error" message={apiError} /> : null}
      <Input label="Usuario o correo" name="credential" value={credential} onChange={(e) => setCredential(e.target.value)} error={errors.credential} />
      <Button type="submit" loading={loading} tooltip="Recuperar contraseña">
        Recuperar
      </Button>
      <Link to={ROUTES.login} title="Volver al login">
        Volver al login
      </Link>
    </form>
  );
};
