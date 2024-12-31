import React, { useState, useEffect } from 'react';
import { Container, Table } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import './CompraProductos.css'

const CompraProductos = () => {

    const apiBaseURL = import.meta.env.VITE_API;

    const { compraId } = useParams();
    const [productos, setProductos] = useState([]);
    const [productosVentas, setProductosVentas] = useState([]);
    const [precioTotal, setPrecioTotal] = useState(0);

    useEffect(() => {
        fetchProductos();
    }, []);

    const fetchProductos = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${apiBaseURL}/api/ventas/${compraId}/productoVenta`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            });
            const data = await response.json();
            setProductosVentas(data);

            const total = data.reduce((acc, productoVenta) => {
                return acc + productoVenta.precioFinal;
            }, 0);

            setPrecioTotal(total);

            const productosDetails = await Promise.all(
                data.map(async (pv) => {
                    const response2 = await fetch(`${apiBaseURL}/api/productos/${pv.productoId}`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`,
                        }
                    });
                    const productoDetail = await response2.json();
                    return {
                        ...pv,
                        producto: productoDetail
                    };
                })
            );


            setProductos(productosDetails);

        } catch (error) {
            console.error('Error fetching productos:', error);
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="mb-4">Productos de la Compra #{compraId}</h2>

            {productos.length === 0 ? (
                <p>No hay productos registrados para esta compra.</p>
            ) : (
                <Container className="mt-5">
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Cantidad</th>
                                <th>Descuento</th>
                                <th>Precio</th>
                            </tr>
                        </thead>
                        <tbody>
                            {productosVentas.map((productoVenta, index) => (
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
            )}
        </Container>
    );
};

export default CompraProductos;
