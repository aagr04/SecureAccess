import { useRef, useState } from 'react';
import { useAuth } from '../auth/useAuth';
import { CambiarEstadoUsuarioModal } from '../components/usuarios/CambiarEstadoUsuarioModal';
import { UsuarioBulkUpload } from '../components/usuarios/UsuarioBulkUpload';
import { UsuarioFilters } from '../components/usuarios/UsuarioFilters';
import { UsuarioForm } from '../components/usuarios/UsuarioForm';
import { UsuarioTable } from '../components/usuarios/UsuarioTable';
import { Alert } from '../components/shared/Alert';
import { Loader } from '../components/shared/Loader';
import { useUsuarios } from '../hooks/useUsuarios';
import type { Usuario } from '../types/usuario.types';
import { isAdmin as checkIsAdmin, normalizeRole, ROLES } from '../utils/roles';

export const UsuariosPage = () => {
  const { user } = useAuth();
  const admin = checkIsAdmin(user?.rol);
  const { usuarios, loading, error, listar, filtrar, guardar, cambiarEstado, eliminar } = useUsuarios(admin);
  const [selected, setSelected] = useState<Usuario | null>(null);
  const [statusUser, setStatusUser] = useState<Usuario | null>(null);
  const [filtered, setFiltered] = useState(false);
  const [actionMessage, setActionMessage] = useState<string | null>(null);
  const editFormRef = useRef<HTMLDivElement | null>(null);

  const handleFilter = (filters: Parameters<typeof filtrar>[0]): void => {
    setFiltered(true);
    setActionMessage(null);
    void filtrar(filters);
  };

  const handleClear = (): void => {
    setFiltered(false);
    setActionMessage(null);
    void listar();
  };

  const handleEdit = (usuario: Usuario): void => {
    setSelected(usuario);
    setActionMessage(admin && normalizeRole(usuario.rol) === ROLES.admin ? 'No se puede actualizar otro administrador.' : null);
    setTimeout(() => {
      editFormRef.current?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }, 0);
  };

  const handleDelete = (usuario: Usuario): void => {
    if (!admin) return;
    const confirmed = window.confirm(`Desea eliminar el usuario ${usuario.username}?`);
    if (confirmed) {
      void eliminar(usuario.idUsuario)
        .then(() => setActionMessage('Usuario eliminado correctamente.'))
        .catch(() => undefined);
    }
  };

  return (
    <section className="page-section">
      <h1>Mantenimiento de usuarios</h1>
      {error ? <Alert type="error" message={error} /> : null}
      {actionMessage ? <Alert type={actionMessage.includes('correctamente') ? 'success' : 'error'} message={actionMessage} /> : null}
      {admin ? (
        <>
          <UsuarioFilters onFilter={handleFilter} onClear={handleClear} />
          <UsuarioBulkUpload />
        </>
      ) : null}
      <div ref={editFormRef}>
        <UsuarioForm usuario={selected} isAdmin={admin} onSubmit={guardar} onSaved={() => setSelected(null)} onCancelEdit={() => setSelected(null)} />
      </div>
      {loading ? (
        <Loader />
      ) : (
        <UsuarioTable
          usuarios={usuarios}
          isAdmin={admin}
          onEdit={handleEdit}
          onChangeStatus={setStatusUser}
          onDelete={handleDelete}
          emptyMessage={filtered ? 'No se encontraron usuarios con los filtros ingresados.' : undefined}
        />
      )}
      <CambiarEstadoUsuarioModal
        usuario={statusUser}
        open={Boolean(statusUser)}
        onClose={() => setStatusUser(null)}
        onConfirm={(status, activo) => (statusUser ? cambiarEstado(statusUser.idUsuario, { status, activo }) : Promise.resolve())}
      />
    </section>
  );
};
