import React, { useState, useEffect } from 'react';
import { Container, Table, Button } from 'react-bootstrap';
import { useParams, Link } from 'react-router-dom';

const ClienteCompras = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const { clienteId } = useParams();
  const [cliente, setCliente] = useState({});
  const [compras, setCompras] = useState([]);

  const opciones = { year: 'numeric', month: 'long', day: 'numeric' };

  useEffect(() => {
    fetchCompras();
  }, []);

  const fetchCompras = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`${apiBaseURL}/api/clientes/${clienteId}`,{
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      const data = await response.json();
      setCliente(data);
      const sortedCompras = data.ventas.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
      setCompras(sortedCompras);
    } catch (error) {
      console.error('Error fetching compras:', error);
    }
  };

  return (
    <Container className="mt-5">
      <h2 className="mb-4">Compras de {cliente.nombre}</h2>

      {compras.length === 0 ? (
        <p>No hay compras registradas para este cliente.</p>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Factura</th>
              <th>Estado de Envío</th>
              <th>Link</th>
            </tr>
          </thead>
          <tbody>
            {compras.map((compra, index) => (
              <tr key={index}>
                <td>
                  {new Date(compra.fecha).toLocaleDateString('es-ES', opciones)}
                </td>
                <td>{compra.linkFactura}</td>
                <td>{compra.estadoEnvio}</td>
                <td>
                  <Button variant="light" as={Link} to={`/compras/${compra.id}/productos`} className="btn-ladrillo">
                    Ver Productos
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </Container>
  );
};

export default ClienteCompras;


