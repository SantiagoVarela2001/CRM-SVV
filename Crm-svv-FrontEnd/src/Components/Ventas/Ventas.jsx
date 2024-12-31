import React, { useState, useEffect } from 'react';
import { Container, Form, Table } from 'react-bootstrap';
import AgregarVenta from '../AgregarVenta/AgregarVenta';
import './Ventas.css'
import TablaVentas from './TablaVentas';

const Ventas = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [searchTerm, setSearchTerm] = useState('');
  const [ventas, setVentas] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  useEffect(() => {
    fetchVentas();
  }, []);

  const fetchVentas = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${apiBaseURL}/api/ventas`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      if (!response.ok) {
        throw new Error('Algo salió mal al obtener las ventas.');
      }
      const data = await response.json();
      setVentas(data)
      console.log(data);
    } catch (error) {
      console.error('Error fetching Ventas:', error);
    }
  };

  const filteredVentas = ventas.filter(venta =>
    venta.estadoEnvio.toLowerCase().includes(searchTerm.toLowerCase()) ||
    venta.estadoEnvio.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container className="mt-5">
      <h2 className="mb-4">VENTAS</h2>

      <Form.Control
        type="text"
        placeholder="Buscar por producto o cliente..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="mb-4"
      />

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Producto</th>
            <th>Cliente</th>
            <th>Fecha</th>
            <th>Factura</th>
            <th>Estado de Envío</th>
            <th>Editar</th>
            <th>Eliminar</th>
          </tr>
        </thead>
        <tbody>
          {filteredVentas.map((venta, index) => (
            <TablaVentas key={venta.id} venta={venta} index={index}/>
          ))}
        </tbody>
      </Table>

      {/* Botón flotante */}
      <button className="floating-button" onClick={handleShowModal}>
        +
      </button>

      {/* Modal de Agregar Cliente */}
      {showModal && <AgregarVenta onClose={handleCloseModal} />}
    </Container>
  );
};

export default Ventas;