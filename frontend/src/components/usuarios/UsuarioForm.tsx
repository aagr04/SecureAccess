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
}

const toFormValues = (usuario?: Usuario | null): UsuarioRequest => ({
  username: usuario?.username ?? '',
  password: '',
  email: usuario?.email ?? '',
  status: usuario?.status ?? 'ACTIVO',
  idPersona: usuario?.persona?.idPersona,
  idRol: undefined,
  nombres: usuario?.persona?.nombres ?? '',
  apellidos: usuario?.persona?.apellidos ?? '',
  identificacion: usuario?.persona?.identificacion ?? '',
  fechaNacimiento: usuario?.persona?.fechaNacimiento ?? ''
});

export const UsuarioForm = ({ usuario, isAdmin, onSubmit }: UsuarioFormProps) => {
  const editing = Boolean(usuario);
  const [values, setValues] = useState<UsuarioRequest>(toFormValues(usuario));
  const [roles, setRoles] = useState<Rol[]>([]);
  const [errors, setErrors] = useState<FormErrors<UsuarioRequest>>({});
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => setValues(toFormValues(usuario)), [usuario]);

  useEffect(() => {
    if (!isAdmin) return;
    void rolService.listar().then(setRoles).catch(() => setRoles([]));
  }, [isAdmin]);

  const update = <K extends keyof UsuarioRequest>(field: K, value: UsuarioRequest[K]): void => {
    setValues((current) => ({ ...current, [field]: value }));
  };

  const submit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
    event.preventDefault();
    const validation = validateUsuario(values, isAdmin, editing);
    setErrors(validation);
    setMessage(null);
    if (hasErrors(validation)) return;

    setLoading(true);
    try {
      const { email: _email, ...payload } = { ...values, password: values.password || undefined };
      const saved = await onSubmit(payload, usuario?.idUsuario);
      const generatedEmail = saved?.email;
      setMessage(generatedEmail ? `Usuario guardado correctamente. Correo generado: ${generatedEmail}` : 'Usuario guardado correctamente.');
      if (!editing) setValues(toFormValues(null));
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className={editing ? 'panel' : 'panel user-form-panel-create'}>
      <h2 className={editing ? undefined : 'user-form-title'}>{editing ? 'Editar usuario' : 'Crear usuario'}</h2>
      {message ? <Alert type="success" message={message} /> : null}
      <form className="form-grid" onSubmit={submit} noValidate>
        <Input label="Nombres" value={values.nombres} onChange={(e) => update('nombres', e.target.value)} error={errors.nombres} />
        <Input label="Apellidos" value={values.apellidos} onChange={(e) => update('apellidos', e.target.value)} error={errors.apellidos} />
        <Input label="Identificacion" value={values.identificacion} onChange={(e) => update('identificacion', e.target.value)} error={errors.identificacion} />
        <Input label="Fecha nacimiento" type="date" value={values.fechaNacimiento ?? ''} onChange={(e) => update('fechaNacimiento', e.target.value)} />
        <Input label="Username" value={values.username} onChange={(e) => update('username', e.target.value)} error={errors.username} />
        <Input label="Contraseña" type="password" value={values.password ?? ''} onChange={(e) => update('password', e.target.value)} error={errors.password} />
        <Input
          label="Email"
          type="email"
          value={values.email ?? ''}
          className={editing ? undefined : 'email-generated-input'}
          readOnly
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
        <Button type="submit" loading={loading} tooltip={editing ? 'Guardar cambios del usuario' : 'Crear usuario'}>
          Guardar
        </Button>
      </form>
    </section>
  );
};
