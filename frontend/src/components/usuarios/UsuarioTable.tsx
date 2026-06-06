import type { Usuario } from '../../types/usuario.types';
import { Button } from '../shared/Button';
import { DataTable, type DataTableColumn } from '../shared/DataTable';

interface UsuarioTableProps {
  usuarios: Usuario[];
  isAdmin: boolean;
  onEdit: (usuario: Usuario) => void;
  onChangeStatus: (usuario: Usuario) => void;
  onDelete: (usuario: Usuario) => void;
  emptyMessage?: string;
}

export const UsuarioTable = ({ usuarios, isAdmin, onEdit, onChangeStatus, onDelete, emptyMessage = 'No existen usuarios para mostrar.' }: UsuarioTableProps) => {
  const columns: DataTableColumn<Usuario>[] = [
    { header: 'Usuario', accessor: 'username' },
    { header: 'Persona', accessor: (usuario) => `${usuario.persona?.nombres ?? ''} ${usuario.persona?.apellidos ?? ''}`.trim() || 'No disponible' },
    { header: 'Email', accessor: 'email' },
    { header: 'Estado', accessor: (usuario) => <span className={`status status-${usuario.status?.toLowerCase()}`}>{usuario.status}</span> },
    { header: 'Sesion', accessor: (usuario) => (usuario.sesionActiva ? 'Activa' : 'Inactiva') },
    { header: 'Fallidos', accessor: 'intentosFallidos' }
  ];

  return (
    <DataTable
      columns={columns}
      data={usuarios}
      rowKey={(usuario) => usuario.idUsuario}
      emptyMessage={emptyMessage}
      renderActions={(usuario) => (
        <div className="table-actions">
          <Button type="button" variant="secondary" onClick={() => onEdit(usuario)} tooltip={`Editar usuario ${usuario.username}`}>
            Editar
          </Button>
          {isAdmin ? (
            <>
              <Button type="button" variant="outline" onClick={() => onChangeStatus(usuario)} tooltip={`Cambiar estado de ${usuario.username}`}>
                Estado
              </Button>
              <Button type="button" variant="danger" onClick={() => onDelete(usuario)} tooltip={`Eliminar usuario ${usuario.username}`}>
                Eliminar
              </Button>
            </>
          ) : null}
        </div>
      )}
    />
  );
};
