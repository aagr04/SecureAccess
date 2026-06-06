import type { ReactNode } from 'react';
import { Button } from './Button';

interface ModalProps {
  open: boolean;
  title: string;
  children: ReactNode;
  onClose: () => void;
}

export const Modal = ({ open, title, children, onClose }: ModalProps) => {
  if (!open) return null;
  return (
    <div className="modal-backdrop" role="dialog" aria-modal="true" aria-labelledby="modal-title">
      <section className="modal">
        <header className="modal-header">
          <h2 id="modal-title">{title}</h2>
          <Button type="button" variant="ghost" onClick={onClose} aria-label="Cerrar modal" tooltip="Cerrar modal">
            X
          </Button>
        </header>
        {children}
      </section>
    </div>
  );
};
