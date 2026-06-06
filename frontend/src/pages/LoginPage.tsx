import { LoginForm } from '../components/auth/LoginForm';

export const LoginPage = () => (
  <main className="auth-page">
    <section className="auth-panel">
      <h1>Iniciar sesión</h1>
      <p>Ingrese con su usuario o correo registrado.</p>
      <LoginForm />
    </section>
  </main>
);
