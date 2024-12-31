import React, { useState, useEffect } from 'react';
import { Container, Table } from 'react-bootstrap';
import { useLocation } from 'react-router-dom';
import './Ventas.css';

const MostrarProductosDeVenta = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [productosVenta, setProductosVenta] = useState([]);
  const [precioTotal, setPrecioTotal] = useState(0);

  const location = useLocation();
  const { venta } = location.state || {};

  useEffect(() => {
    if (venta && venta.id) {
      fetchProductoVentaxVenta();
    }
  }, [venta]);

  const fetchProductoVentaxVenta = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${apiBaseURL}/api/productos-ventas/venta/${venta?.id}`,{
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      const data = await response.json();
      setProductosVenta(data);

      console.log(data)

      const total = data.reduce((acc, productoVenta) => {
        return acc + productoVenta.precioFinal;
      }, 0);

      setPrecioTotal(total);
    } catch (error) {
      console.error('Error fetching Venta por ID:', error);
    }
  };

  return (
    <Container className="mt-5">
      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Cantidad</th>
            <th>Descuento</th>
            <th>Precio Final</th>
          </tr>
        </thead>
        <tbody>
          {productosVenta.map((productoVenta, index) => (
            <tr key={index}>
              <td>{productoVenta.productoNombre}</td>
              <td>{productoVenta.cantidadDelProducto}</td>
              <td>{productoVenta.descuento}%</td>
              <td>${productoVenta.precioFinal}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <div className="precio-total-container minimal text-center mt-4">
        <h4 className="precio-total-titulo">Precio Total</h4>
        <span className="precio-total-valor">${precioTotal.toFixed(2)}</span>
      </div>
    </Container>
  );
};

export default MostrarProductosDeVenta;

