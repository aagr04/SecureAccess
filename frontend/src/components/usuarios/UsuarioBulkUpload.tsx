import { useRef, useState } from 'react';
import { usuarioService } from '../../services/usuarioService';
import type { BulkUploadResponse } from '../../types/usuario.types';
import { validateBulkFile } from '../../utils/validators';
import { Alert } from '../shared/Alert';
import { Button } from '../shared/Button';

const EXPECTED_FORMAT = 'nombres, apellidos, identificacion, username, password, rol';

const formatFileSize = (size: number): string => {
  if (size < 1024) return `${size} B`;
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
  return `${(size / (1024 * 1024)).toFixed(1)} MB`;
};

const downloadTemplate = (): void => {
  const rows = [
    EXPECTED_FORMAT.replace(/, /g, ','),
    'Juan Alberto,Piguave Loor,1203574901,Juan1234,Clave@123,USER'
  ];
  const blob = new Blob([rows.join('\n')], { type: 'text/csv;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = 'plantilla_usuarios.csv';
  link.click();
  URL.revokeObjectURL(url);
};

export const UsuarioBulkUpload = () => {
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<BulkUploadResponse | null>(null);

  const handleFileChange = (selectedFile: File | null): void => {
    setFile(selectedFile);
    setResult(null);
    setError(selectedFile ? (validateBulkFile(selectedFile) ?? null) : null);
  };

  const handleRemoveFile = (): void => {
    setFile(null);
    setResult(null);
    setError(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleUpload = async (): Promise<void> => {
    const validation = validateBulkFile(file);
    setError(validation ?? null);
    setResult(null);
    if (validation || !file) return;

    setLoading(true);
    try {
      setResult(await usuarioService.cargaMasiva(file));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error de carga masiva.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="panel bulk-upload">
      <div>
        <h2>Carga masiva</h2>
        <p className="bulk-description">Sube usuarios desde Excel o CSV. Formato esperado: {EXPECTED_FORMAT}.</p>
      </div>

      {error ? <Alert type="error" message={error} /> : null}

      <input
        ref={fileInputRef}
        aria-label="Archivo de usuarios"
        type="file"
        accept=".xlsx,.xls,.csv"
        onChange={(event) => handleFileChange(event.target.files?.[0] ?? null)}
      />

      {file ? (
        <div className="file-details">
          <strong>{file.name}</strong>
          <span>Tamano: {formatFileSize(file.size)}</span>
          <span>Tipo: {file.type || 'No disponible'}</span>
        </div>
      ) : null}

      <div className="file-actions">
        <Button type="button" loading={loading} disabled={!file || Boolean(error)} onClick={handleUpload} tooltip="Cargar archivo seleccionado">
          Cargar archivo
        </Button>
        {file ? (
          <Button type="button" variant="danger" onClick={handleRemoveFile} tooltip="Eliminar archivo seleccionado">
            Eliminar archivo
          </Button>
        ) : null}
        <Button type="button" variant="outline" onClick={downloadTemplate} tooltip="Descargar plantilla de carga masiva">
          Descargar plantilla
        </Button>
      </div>

      {result ? (
        <div className="bulk-result">
          <p>Total procesados: {result.totalProcesados}</p>
          <p>Exitosos: {result.exitosos}</p>
          <p>Fallidos: {result.fallidos}</p>
          {result.erroresPorFila.length ? (
            <ul>
              {result.erroresPorFila.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          ) : null}
        </div>
      ) : null}
    </section>
  );
};
