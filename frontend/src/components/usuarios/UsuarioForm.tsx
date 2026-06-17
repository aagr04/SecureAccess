import { useEffect, useState } from 'react';
import { rolService } from '../../services/rolService';
import type { Rol } from '../../types/rol.types';
import type { Usuario, UsuarioRequest } from '../../types/usuario.types';
import { USER_STATUS } from '../../utils/constants';
import { validateUsuario, hasErrors, type FormErrors } from '../../utils/validators';
import { Alert } from '../shared/Alert';
import { Button } from '../shared/Button';
import { Input } from '../shared/Input';
import { Select } from '../shared/Select';

interface UsuarioFormProps {
  usuario?: Usuario | null;
  isAdmin: boolean;
  onSubmit: (payload: UsuarioRequest, id?: number) => Promise<Usuario | void>;
  onSaved?: () => void;
  onCancelEdit?: () => void;
}

const initialCreateUserForm: UsuarioRequest = {
  username: '',
  password: '',
  email: '',
  status: 'ACTIVO',
  idRol: undefined,
  nombres: '',
  apellidos: '',
  identificacion: '',
  fechaNacimiento: ''
};

const toFormValues = (usuario?: Usuario | null): UsuarioRequest => {
  if (!usuario) return { ...initialCreateUserForm };

  return {
    username: usuario.username,
    password: '',
    email: usuario.email,
    status: usuario.status ?? 'ACTIVO',
    idPersona: usuario.persona?.idPersona,
    idRol: undefined,
    nombres: usuario.persona?.nombres ?? '',
    apellidos: usuario.persona?.apellidos ?? '',
    identificacion: usuario.persona?.identificacion ?? '',
    fechaNacimiento: usuario.persona?.fechaNacimiento ?? ''
  };
};

export const UsuarioForm = ({ usuario, isAdmin, onSubmit, onSaved, onCancelEdit }: UsuarioFormProps) => {
  const isEditMode = Boolean(usuario);
  const [values, setValues] = useState<UsuarioRequest>(toFormValues(usuario));
  const [roles, setRoles] = useState<Rol[]>([]);
  const [errors, setErrors] = useState<FormErrors<UsuarioRequest>>({});
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    setValues(toFormValues(usuario));
    setErrors({});
    setMessage(null);
  }, [usuario]);

  useEffect(() => {
    if (!isAdmin) return;
    void rolService.listar().then(setRoles).catch(() => setRoles([]));
  }, [isAdmin]);

  const update = <K extends keyof UsuarioRequest>(field: K, value: UsuarioRequest[K]): void => {
    setValues((current) => ({ ...current, [field]: value }));
  };

  const resetToCreateMode = (): void => {
    setValues(toFormValues(null));
    setErrors({});
    onSaved?.();
  };

  const cancelEdit = (): void => {
    setMessage(null);
    setValues(toFormValues(null));
    setErrors({});
    onCancelEdit?.();
  };

  const submit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
    event.preventDefault();
    const validation = validateUsuario(values, isAdmin, isEditMode);
    setErrors(validation);
    setMessage(null);
    if (hasErrors(validation)) return;

    setLoading(true);
    try {
      const { email: _email, ...payload } = { ...values, password: values.password || undefined };
      const saved = await onSubmit(payload, usuario?.idUsuario);
      const generatedEmail = saved?.email;
      setMessage(generatedEmail ? `Usuario guardado correctamente. Correo generado: ${generatedEmail}` : 'Usuario guardado correctamente.');
      resetToCreateMode();
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className={isEditMode ? 'panel' : 'panel user-form-panel-create'}>
      <h2 className={isEditMode ? undefined : 'user-form-title'}>{isEditMode ? 'Editar usuario' : 'Crear usuario'}</h2>
      {message ? <Alert type="success" message={message} /> : null}
      <form className="form-grid" onSubmit={submit} autoComplete="off" noValidate>
        <Input id="user-names" name="user-names" label="Nombres" value={values.nombres} onChange={(e) => update('nombres', e.target.value)} error={errors.nombres} />
        <Input id="user-lastnames" name="user-lastnames" label="Apellidos" value={values.apellidos} onChange={(e) => update('apellidos', e.target.value)} error={errors.apellidos} />
        <Input id="user-identification" name="user-identification" label="Identificacion" value={values.identificacion} onChange={(e) => update('identificacion', e.target.value)} error={errors.identificacion} />
        <Input id="user-birthdate" name="user-birthdate" label="Fecha nacimiento" type="date" value={values.fechaNacimiento ?? ''} onChange={(e) => update('fechaNacimiento', e.target.value)} />
        <Input
          id={isEditMode ? 'edit-user-username' : 'new-user-username'}
          name={isEditMode ? 'edit-user-username' : 'new-user-username'}
          label="Username"
          autoComplete="off"
          value={values.username}
          onChange={(e) => update('username', e.target.value)}
          error={errors.username}
        />
        <Input
          id={isEditMode ? 'edit-user-password' : 'new-user-password'}
          name={isEditMode ? 'edit-user-password' : 'new-user-password'}
          label="Contraseña"
          type="password"
          autoComplete="new-password"
          value={values.password ?? ''}
          onChange={(e) => update('password', e.target.value)}
          error={errors.password}
        />
        <Input
          id="generated-user-email"
          name="generated-user-email"
          label="Email"
          type="email"
          value={values.email ?? ''}
          className={isEditMode ? undefined : 'email-generated-input'}
          readOnly
          disabled={!isEditMode}
          placeholder="El correo se generará automáticamente"
        />
        <Select
          label="Estado"
          value={values.status ?? ''}
          onChange={(e) => update('status', e.target.value)}
          options={USER_STATUS.map((item) => ({ value: item, label: item }))}
        />
        {isAdmin ? (
          <Select
            label="Rol"
            value={values.idRol ?? ''}
            onChange={(e) => update('idRol', Number(e.target.value))}
            error={errors.idRol}
            options={roles.map((rol) => ({ value: rol.idRol, label: rol.nombre }))}
          />
        ) : null}
        <div className="actions-row">
          <Button type="submit" loading={loading} tooltip="Guardar usuario">
            Guardar
          </Button>
          {isEditMode ? (
            <Button type="button" variant="secondary" tooltip="Cancelar edición" onClick={cancelEdit} disabled={loading}>
              Cancelar
            </Button>
          ) : null}
        </div>
      </form>
    </section>
  );
};
