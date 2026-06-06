import { useEffect, useState } from 'react';
import type { Usuario } from '../../types/usuario.types';
import { USER_STATUS } from '../../utils/constants';
import { Button } from '../shared/Button';
import { Modal } from '../shared/Modal';
import { Select } from '../shared/Select';

interface Props {
  usuario: Usuario | null;
  open: boolean;
  onClose: () => void;
  onConfirm: (status: string, activo: boolean) => Promise<void>;
}

export const CambiarEstadoUsuarioModal = ({ usuario, open, onClose, onConfirm }: Props) => {
  const [status, setStatus] = useState(usuario?.status ?? 'ACTIVO');
  const [activo, setActivo] = useState(usuario?.activo ?? true);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setStatus(usuario?.status ?? 'ACTIVO');
    setActivo(usuario?.activo ?? true);
  }, [usuario]);

  const submit = async (): Promise<void> => {
    setLoading(true);
    try {
      await onConfirm(status, activo);
      onClose();
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal open={open} title="Cambiar estado" onClose={onClose}>
      <div className="modal-body">
        <p>{usuario?.username}</p>
        <Select
          label="Estado"
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          options={USER_STATUS.map((item) => ({ value: item, label: item }))}
        />
        <label className="check-field">
          <input type="checkbox" checked={activo} onChange={(e) => setActivo(e.target.checked)} />
          Activo
        </label>
        <div className="actions-row">
          <Button type="button" loading={loading} onClick={submit} tooltip="Confirmar cambio de estado">
            Confirmar
          </Button>
          <Button type="button" variant="secondary" onClick={onClose} tooltip="Cancelar cambio de estado">
            Cancelar
          </Button>
        </div>
      </div>
    </Modal>
  );
};
