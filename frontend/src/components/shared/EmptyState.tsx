export const EmptyState = ({ message = 'No hay información disponible.' }: { message?: string }) => (
  <div className="empty-state">{message}</div>
);
