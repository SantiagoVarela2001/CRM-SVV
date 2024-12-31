import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import './ProveedorProductos.css'

const ProveedorProductos = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const { proveedorId } = useParams();
  const [nombreProveedor, setNombreProveedor] = useState('');
  const [productos, setProductos] = useState([]);

  useEffect(() => {
    fetchProveedorProductos();
  }, [proveedorId]);

  const fetchProveedorProductos = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${apiBaseURL}/api/proveedores/${proveedorId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      const data = await response.json();
      setNombreProveedor(data.nombre);
      setProductos(data.productos);
    } catch (error) {
      console.error('Error fetching Proveedor Productos:', error);
    }
  };

  return (
    <Container className="mt-5">
      <h2 className="mb-4">PRODUCTOS DEL PROVEEDOR: <span className="nombre_Proveedor">{nombreProveedor.toUpperCase()}</span></h2>

      <Row>
        {productos.map((producto, index) => (
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
    </Container>
  );
};

export default ProveedorProductos;
