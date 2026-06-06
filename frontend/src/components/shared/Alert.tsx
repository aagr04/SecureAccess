interface AlertProps {
  type?: 'success' | 'error' | 'info';
  message: string;
}

export const Alert = ({ type = 'info', message }: AlertProps) => <div className={`alert alert-${type}`}>{message}</div>;
