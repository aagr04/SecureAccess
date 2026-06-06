import type { ButtonHTMLAttributes, ReactNode } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'success' | 'outline' | 'ghost';
  loading?: boolean;
  tooltip?: string;
  children: ReactNode;
}

export const Button = ({ variant = 'primary', loading = false, disabled, children, className = '', tooltip, ...props }: ButtonProps) => (
  <button className={`btn btn-${variant} ${className}`} disabled={disabled || loading} title={tooltip} aria-label={tooltip} {...props}>
    {loading ? 'Procesando...' : children}
  </button>
);
