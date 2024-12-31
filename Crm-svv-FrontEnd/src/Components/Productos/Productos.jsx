import React, { useState, useEffect } from 'react';
import { Container, Form, Card, Row, Col } from 'react-bootstrap';
import './Productos.css'
import AgregarProducto from '../AgregarProducto/AgregarProducto';

const Productos = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [searchTerm, setSearchTerm] = useState('');
  const [productos, setProductos] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  useEffect(() => {
    fetchProductos();
  }, []);

  const fetchProductos = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${apiBaseURL}/api/productos`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      if (!response.ok) {
        throw new Error('Algo salió mal al obtener los productos.');
      }
      const data = await response.json();
      setProductos(data);
      localStorage.setItem('productos', JSON.stringify(data));
    } catch (error) {
      console.error('Error fetching productos:', error);
    }
  };

  const filteredProductos = productos.filter(producto =>
    producto.nombre.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container className="mt-5">
      <h2 className="mb-4">PRODUCTOS</h2>

      <Form.Control
        type="text"
        placeholder="Buscar producto por nombre..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="mb-4"
      />

      <Row>
        {filteredProductos.map((producto, index) => (
          <Col md={3} key={index} className="mb-4">
            <Card>
              <Card.Img variant="top" src={`${apiBaseURL}/api/productos/${producto.id}/imagen`} alt={producto.nombre} />
              <Card.Body>
                <Card.Title>{producto.nombre}</Card.Title>
                <Card.Text>
                  <strong>Precio de Compra:</strong> ${producto.precioCompra}<br />
                  <strong>Precio de Venta:</strong> ${producto.precioVenta}<br />
                  <strong>Stock:</strong> {producto.stock}<br />
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
      <button className="floating-button" onClick={handleShowModal}>
        +
      </button>

      {showModal && <AgregarProducto onClose={handleCloseModal} />}
    </Container>
  );
};

export default Productos;
