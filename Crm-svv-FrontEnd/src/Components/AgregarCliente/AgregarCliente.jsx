import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';

const AgregarCliente = ({ onClose }) => {

    const apiBaseURL = import.meta.env.VITE_API;

    const [clienteData, setClienteData] = useState({
        nombre: '',
        email: '',
        telefono: '',
        pais: '',
        direccion: ''
    });

    const handleClose = () => onClose();
    
    const handleSave = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${apiBaseURL}/api/clientes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(clienteData),
            });

            if (!response.ok) {
                throw new Error('Error al agregar el Cliente');
            }

            handleClose();
        } catch (error) {
            console.error('Error al agregar el cliente:', error);
        }
        window.location.reload();
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setClienteData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    return (
        <Modal show={true} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Agrega al nuevo Cliente</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-1" controlId="formBasicName">
                        <Form.Label>Nombre</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Escribe su nombre"
                            name="nombre"
                            value={clienteData.nombre}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Ingresa su Email"
                            name="email"
                            value={clienteData.email}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicPhone">
                        <Form.Label>Telefono</Form.Label>
                        <Form.Control
                            type="tel"
                            placeholder="Ingresa su Telefono"
                            name="telefono"
                            value={clienteData.telefono}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicCountry">
                        <Form.Label>Pais</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Escribe su Pais"
                            name="pais"
                            value={clienteData.pais}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicAdress">
                        <Form.Label>Direccion</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Escribe su Direccion"
                            name="direccion"
                            value={clienteData.direccion}
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

export default AgregarCliente;