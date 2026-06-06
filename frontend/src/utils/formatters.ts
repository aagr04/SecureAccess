export const formatDateTime = (value?: string | null): string => {
  if (!value) return 'No disponible';
  return new Intl.DateTimeFormat('es-EC', {
    dateStyle: 'medium',
    timeStyle: 'short'
  }).format(new Date(value));
};

export const fullName = (nombres?: string, apellidos?: string): string =>
  [nombres, apellidos].filter(Boolean).join(' ') || 'Usuario';
