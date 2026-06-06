import { RecoverPasswordForm } from '../components/auth/RecoverPasswordForm';

export const RecoverPasswordPage = () => (
  <main className="auth-page">
    <section className="auth-panel">
      <h1>Recuperar contraseña</h1>
      <p>Solicite la recuperación usando su usuario o correo.</p>
      <RecoverPasswordForm />
    </section>
  </main>
);
