import { EmptyState } from './EmptyState';
import { Loader } from './Loader';
import type { ReactNode } from 'react';

export interface DataTableColumn<T> {
  header: string;
  accessor: keyof T | ((item: T) => ReactNode);
  className?: string;
}

interface DataTableProps<T> {
  columns: DataTableColumn<T>[];
  data: T[];
  rowKey: (item: T) => string | number;
  loading?: boolean;
  emptyMessage: string;
  renderActions?: (item: T) => ReactNode;
  className?: string;
}

export const DataTable = <T,>({ columns, data, rowKey, loading = false, emptyMessage, renderActions, className = '' }: DataTableProps<T>) => {
  if (loading) return <Loader />;
  if (!data.length) return <EmptyState message={emptyMessage} />;

  const renderCell = (item: T, column: DataTableColumn<T>): ReactNode =>
    typeof column.accessor === 'function' ? column.accessor(item) : (item[column.accessor] as React.ReactNode);

  return (
    <div className="table-wrapper">
      <table className={`table-professional ${className}`.trim()}>
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.header} className={column.className}>
                {column.header}
              </th>
            ))}
            {renderActions ? <th>Acciones</th> : null}
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr key={rowKey(item)}>
              {columns.map((column) => (
                <td key={column.header} className={column.className}>
                  {renderCell(item, column)}
                </td>
              ))}
              {renderActions ? <td>{renderActions(item)}</td> : null}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
