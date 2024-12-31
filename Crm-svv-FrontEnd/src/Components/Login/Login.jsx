import React, { useState } from 'react';
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Form,
  Alert,
  Spinner,
  Collapse,
} from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = ({ setIsAuthenticated }) => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    passwordHash: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [id]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (!isLogin && formData.passwordHash !== formData.confirmPassword) {
      setError('Las contraseñas no coinciden');
      setLoading(false);
      return;
    }

    const endpoint = isLogin
      ? `${apiBaseURL}/api/empresas/login`
      : `${apiBaseURL}/api/empresas/registro`;

    const payload = isLogin
      ? { nombre: formData.nombre, passwordHash: formData.passwordHash }
      : {
          nombre: formData.nombre,
          email: formData.email,
          passwordHash: formData.passwordHash,
        };

        console.log(payload)

    try {
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error('Algo salió mal. Verifica tus datos.');
      }

      const data = await response.json();
      if (isLogin) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('isAuthenticated', 'true');
        setIsAuthenticated(true);
        navigate('/clientes');
      } else {
        setIsLogin(true);
      }
    } catch (error) {
      setError(error.message || 'Error al enviar los datos.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="p-4 body-login" fluid>
      <Row>
        <Col md={6} className="text-center text-md-start d-flex flex-column">
          <h1 className="my-5 display-3 fw-bold">
            {isLogin ? 'Bienvenido de nuevo' : 'Regístrate'} <br />
            <span className="text-primary-login">
              {isLogin
                ? 'Inicia sesión para continuar'
                : 'Únete a nuestra plataforma'}
            </span>
          </h1>
          <p className="text-alternativo">
            {isLogin
              ? 'Ingresa los datos de tu empresa para acceder.'
              : 'Crea una cuenta para disfrutar de nuestras funcionalidades.'}
          </p>
        </Col>

        <Col md={6}>
          <Card className="my-5">
            <Card.Body className="p-5 pt-4">

              <div className='logo-container'>
            <img src="/logo_CRM_SVV.svg" alt="" className='logo'/>
            </div>

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-4" controlId="nombre">
                  <Form.Label>Nombre de la empresa</Form.Label>
                  <Form.Control
                    type="text"
                    value={formData.nombre}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>

                {!isLogin && (
                  <Form.Group className="mb-4" controlId="email">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                      type="email"
                      value={formData.email}
                      onChange={handleChange}
                      required
                    />
                  </Form.Group>
                )}

                <Form.Group className="mb-4" controlId="passwordHash">
                  <Form.Label>Contraseña</Form.Label>
                  <Form.Control
                    type="password"
                    value={formData.passwordHash}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>

                {!isLogin && (
                  <Form.Group className="mb-4" controlId="confirmPassword">
                    <Form.Label>Confirmar Contraseña</Form.Label>
                    <Form.Control
                      type="passwordHash"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      required
                    />
                  </Form.Group>
                )}

                {error && <Alert variant="danger">{error}</Alert>}

                {loading && (
                  <Spinner
                    animation="border"
                    variant="primary"
                    className="mb-4"
                  />
                )}

                <Button
                  variant="primary"
                  className="w-100 btn-login"
                  type="submit"
                  disabled={loading}
                >
                  {isLogin ? 'Iniciar sesión' : 'Registrarse'}
                </Button>
                <Button
                  variant="outline-primary"
                  className="my-3 btn-switch-mode btn-login"
                  onClick={() => setIsLogin(!isLogin)}
                >
                  {isLogin
                    ? '¿No tienes una cuenta? Regístrate'
                    : '¿Ya tienes una cuenta? Inicia sesión'}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;
