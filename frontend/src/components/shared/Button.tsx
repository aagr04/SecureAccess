import type { ButtonHTMLAttributes, ReactNode } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'success' | 'outline' | 'ghost';
  loading?: boolean;
  tooltip?: string;
  children: ReactNode;
}

const variantClasses: Record<NonNullable<ButtonProps['variant']>, string> = {
  primary: 'bg-sky-500 text-white hover:bg-sky-600 focus:ring-sky-400',
  secondary: 'border border-sky-500 bg-white text-sky-600 hover:bg-sky-50 focus:ring-sky-400',
  danger: 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-400',
  success: 'bg-emerald-600 text-white hover:bg-emerald-700 focus:ring-emerald-400',
  outline: 'border border-sky-500 bg-white text-sky-600 hover:bg-sky-50 focus:ring-sky-400',
  ghost: 'bg-transparent text-sky-600 shadow-none hover:bg-sky-50 focus:ring-sky-400'
};

export const Button = ({ variant = 'primary', loading = false, disabled, children, className = '', tooltip, ...props }: ButtonProps) => {
  const classes = `btn btn-${variant} ${variantClasses[variant]} ${className}`.trim();

  return (
    <button className={classes} disabled={disabled || loading} title={tooltip} aria-label={tooltip} {...props}>
      {loading ? 'Procesando...' : children}
    </button>
  );
};
