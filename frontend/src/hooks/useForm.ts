import { useState } from 'react';

export const useForm = <T extends Record<string, unknown>>(initialValues: T) => {
  const [values, setValues] = useState<T>(initialValues);

  const setField = <K extends keyof T>(field: K, value: T[K]): void => {
    setValues((current) => ({ ...current, [field]: value }));
  };

  const reset = (nextValues = initialValues): void => setValues(nextValues);

  return { values, setField, setValues, reset };
};
