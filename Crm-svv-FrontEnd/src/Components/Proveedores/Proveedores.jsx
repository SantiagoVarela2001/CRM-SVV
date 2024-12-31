import React, { useState, useEffect } from 'react';
import { Container, Form, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import './Proveedores.css'
import AgregarProveedor from '../AgregarProveedor/AgregarProveedor';

const Proveedores = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [searchTerm, setSearchTerm] = useState('');
  const [proveedores, setProveedores] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  useEffect(() => {
    fetchProveedores();
  }, []);

  const fetchProveedores = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${apiBaseURL}/api/proveedores`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      if (!response.ok) {
        throw new Error('Algo salió mal al obtener los proveedores.');
      }
      const data = await response.json();
      setProveedores(data);
    } catch (error) {
      console.error('Error fetching Proveedores:', error);
    }
  };

  const filteredProveedores = proveedores.filter(proveedor =>
    proveedor.nombre.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container className="mt-5">
      <h2 className="mb-4">PROVEEDORES</h2>

      <Form.Control
        type="text"
        placeholder="Buscar proveedor por nombre..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="mb-4"
      />

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>Productos</th>
          </tr>
        </thead>
        <tbody>
          {filteredProveedores.map((proveedor, index) => (
            <tr key={index}>
              <td>{proveedor.nombre}</td>
              <td>{proveedor.email}</td>
              <td>{proveedor.telefono}</td>
              <td>
                <Link to={`/proveedores/${proveedor.id}/productos`} className="link-ladrillo">Ver Productos</Link>
              </td>

            </tr>
          ))}
        </tbody>
      </Table>

      {/* Botón flotante */}
      <button className="floating-button" onClick={handleShowModal}>
        +
      </button>

      {/* Modal de Agregar Cliente */}
      {showModal && <AgregarProveedor onClose={handleCloseModal} />}
    </Container>
  );
};

export default Proveedores;
