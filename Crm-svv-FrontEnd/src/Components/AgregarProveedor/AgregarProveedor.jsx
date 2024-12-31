import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';

const AgregarProveedor = ({ onClose }) => {

    const apiBaseURL = import.meta.env.VITE_API;

    const [proveedorData, setProveedorData] = useState({
        nombre: '',
        email: '',
        telefono: '',
        productos: []
    });

    const handleClose = () => onClose();
    const handleSave = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${apiBaseURL}/api/proveedores`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(proveedorData),
            });

            if (!response.ok) {
                throw new Error('Error al agregar el Proveedor');
            }

            handleClose();
        } catch (error) {
            console.error('Error al agregar el Proveedor:', error);
        }
        window.location.reload();
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProveedorData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    return (
        <Modal show={true} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Agrega al nuevo Proveedor</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-1" controlId="formBasicName">
                        <Form.Label>Nombre</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Escribe su nombre"
                            name="nombre"
                            value={proveedorData.nombre}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Ingresa su Email"
                            name="email"
                            value={proveedorData.email}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicPhone">
                        <Form.Label>Telefono</Form.Label>
                        <Form.Control
                            type="tel"
                            placeholder="Ingresa su Telefono"
                            name="telefono"
                            value={proveedorData.telefono}
                            onChange={handleChange}
                        />
                    </Form.Group>

                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="light" className="btn-modal" onClick={handleClose}>
                    Cerrar
                </Button>
                <Button className="btn-modal-send" onClick={handleSave}>
                    Enviar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default AgregarProveedor;