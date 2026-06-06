interface DashboardCardProps {
  title: string;
  value: number;
  tone?: 'default' | 'success' | 'warning' | 'danger';
}

export const DashboardCard = ({ title, value, tone = 'default' }: DashboardCardProps) => (
  <article className={`dashboard-card tone-${tone}`}>
    <span>{title}</span>
    <strong>{value}</strong>
  </article>
);
