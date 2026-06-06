import type { UsuarioRequest } from '../types/usuario.types';

const USERNAME_REGEX = /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,20}$/;
const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[^A-Za-z0-9])\S{8,}$/;
const REPEATED_DIGITS_REGEX = /.*(\d)\1\1\1.*/;

export type FormErrors<T> = Partial<Record<keyof T | 'general' | 'file', string>>;

export const required = (value?: string): string | undefined =>
  value?.trim() ? undefined : 'Este campo es obligatorio.';

export const validateLogin = (credential: string, password: string): FormErrors<{ credential: string; password: string }> => ({
  credential: required(credential),
  password: required(password)
});

export const validateRecover = (credential: string): FormErrors<{ credential: string }> => ({
  credential: required(credential)
});

export const validateIdentification = (identificacion: string): string | undefined => {
  if (!identificacion.trim()) return 'La identificacion es obligatoria.';
  if (!/^\d{10}$/.test(identificacion)) return 'La identificacion debe tener 10 digitos numericos.';
  if (REPEATED_DIGITS_REGEX.test(identificacion)) return 'La identificacion no puede contener 4 numeros iguales seguidos.';
  return undefined;
};

export const validateUsername = (username: string): string | undefined => {
  if (!username.trim()) return 'El username es obligatorio.';
  if (!USERNAME_REGEX.test(username)) return 'Debe tener 8 a 20 caracteres, una mayuscula y un numero.';
  return undefined;
};

export const validatePassword = (password?: string, requiredPassword = true): string | undefined => {
  if (!password?.trim()) return requiredPassword ? 'La contraseña es obligatoria.' : undefined;
  if (!PASSWORD_REGEX.test(password)) return 'Debe tener minimo 8 caracteres, una mayuscula, un signo y sin espacios.';
  return undefined;
};

export const validateUsuario = (values: UsuarioRequest, isAdmin: boolean, isEdit = false): FormErrors<UsuarioRequest> => ({
  nombres: required(values.nombres),
  apellidos: required(values.apellidos),
  identificacion: validateIdentification(values.identificacion),
  username: validateUsername(values.username),
  password: validatePassword(values.password, !isEdit),
  idRol: isAdmin && !values.idRol ? 'El rol es obligatorio.' : undefined
});

export const validateBulkFile = (file?: File | null): string | undefined => {
  if (!file) return 'Seleccione un archivo.';
  const allowedExtensions = ['xlsx', 'xls', 'csv'];
  const extension = file.name.split('.').pop()?.toLowerCase();
  return extension && allowedExtensions.includes(extension) ? undefined : 'Solo se permiten archivos .xlsx, .xls o .csv.';
};

export const hasErrors = <T>(errors: FormErrors<T>): boolean => Object.values(errors).some(Boolean);
