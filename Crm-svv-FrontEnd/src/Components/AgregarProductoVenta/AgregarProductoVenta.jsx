import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import { Form } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import ProductoVenta from '../ProductoVenta/ProductoVenta';
import './AgregarProductoVenta.css'

const AgregarProductoVenta = ({ onClose, setProductosVentas, productosVentas }) => {

    const apiBaseURL = import.meta.env.VITE_API;

    const [searchTerm, setSearchTerm] = useState('');
    const [productos, setProductos] = useState([]);

    useEffect(() => {
        fetchProductos();
    }, []);

    const fetchProductos = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`${apiBaseURL}/api/productos`,{
                method: "GET",
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            });
            const data = await response.json();
            setProductos(data);
        } catch (error) {
            console.error('Error fetching Productos:', error);
        }
    };

    const filteredProductos = productos.filter(producto =>
        producto.nombre.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <Modal show={true} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Agregar Productos a la Venta</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Control
                    type="text"
                    placeholder="Buscar producto por nombre..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="mb-4"
                />
                <div className="grid-container">
                    {filteredProductos.map((producto) => (
                        <ProductoVenta key={producto.id} producto={producto}
                            setProductosVentas={setProductosVentas}
                            productosVentas={productosVentas} />
                    ))}
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="light" className="btn-modal" onClick={onClose}>
                    Cerrar
                </Button>
                <Button className="btn-modal-send" onClick={onClose}>
                    Enviar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default AgregarProductoVenta;
