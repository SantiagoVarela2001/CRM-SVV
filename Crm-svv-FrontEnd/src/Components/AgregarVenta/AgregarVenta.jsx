import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import AgregarProductoVenta from '../AgregarProductoVenta/AgregarProductoVenta';

const AgregarVenta = ({ onClose, ventaPreEdit, saveChanges }) => {

    const apiBaseURL = import.meta.env.VITE_API;

    const [clientes, setClientes] = useState([]);
    const [esEdit, setEsEdit] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [productosVentas, setProductosVentas] = useState([])

    const [ventaData, setVentaData] = useState({
        cliente: {},
        productoVentas: [],
        fecha: '',
        linkFactura: '',
        estadoEnvio: ''
    });

    const handleShowModal = () => setShowModal(true);
    const handleCloseModal = () => setShowModal(false);

    const obtenerClienteDelCache = (id) => {
        const clientes = JSON.parse(localStorage.getItem('clientes') || '[]');
        return clientes.find((cliente) => cliente.id === id);
    };

    useEffect(() => {
        if (ventaPreEdit != undefined) {
            setVentaData({
                cliente: obtenerClienteDelCache(ventaPreEdit.clienteID),
                productoVentas: ventaPreEdit.productoVentas,
                fecha: ventaPreEdit.fecha,
                linkFactura: ventaPreEdit.linkFactura,
                estadoEnvio: ventaPreEdit.estadoEnvio
            });
            setEsEdit(true);
        }
        fetchClientes();
    }, []);

    const fetchClientes = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${apiBaseURL}/api/clientes`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            });
            const data = await response.json();
            setClientes(data);
        } catch (error) {
            console.error('Error fetching Clientes:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setVentaData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleSave = async () => {
        const token = localStorage.getItem('token');

        try {
            // Paso 1: Crear la venta
            const ventaResponse = await fetch(`${apiBaseURL}/api/ventas`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    cliente: ventaData.cliente,
                    estadoEnvio: ventaData.estadoEnvio,
                    fecha: ventaData.fecha,
                    linkFactura: ventaData.linkFactura,
                }),
            });

            if (!ventaResponse.ok) {
                throw new Error('Error al crear la venta');
            }

            const ventaCreada = await ventaResponse.json();
            const ventaId = ventaCreada.id; // Obtener el ID de la venta creada

            // Paso 2: Crear los productos relacionados a la venta
            const productosRequests = productosVentas.map((pv) => {
                const productoConVenta = {
                    producto: pv.producto,
                    venta: { id: ventaId },
                    cantidadDelProducto: pv.cantidadDelProducto,
                    descuento: pv.descuento,
                };


                console.log("productoConVenta", productoConVenta);

                return fetch(`${apiBaseURL}/api/productos-ventas`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                    body: JSON.stringify(productoConVenta),
                });
            });

            const productosResponses = await Promise.all(productosRequests);

            // Verificar que todos los productos se crearon correctamente
            const errores = productosResponses.filter((response) => !response.ok);
            if (errores.length > 0) {
                throw new Error('Error al crear algunos productos de la venta');
            }


            console.log("Venta creada: ", ventaCreada)

            onClose(); // Cerrar modal o notificar éxito
        } catch (error) {
            console.error('Error al procesar la venta:', error);
        }
        window.location.reload();
    };

    const handleClienteChange = (e) => {
        setVentaData({
            ...ventaData,
            cliente: { id: e.target.value }
        });
    };

    return (
        <Modal show={true} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Agregar nueva Venta</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-1" controlId="formBasicCliente">
                        <Form.Label>Cliente</Form.Label>
                        <Form.Control
                            as="select"
                            name="cliente"
                            value={ventaData.cliente?.id || ''}
                            onChange={handleClienteChange}
                        >
                            <option value="">Seleccione un Cliente</option>
                            {clientes.map((cliente) => (
                                <option key={cliente.id} value={cliente.id}>
                                    {cliente.nombre}
                                </option>
                            ))}
                        </Form.Control>
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicProductos">
                        <Form.Label>Productos</Form.Label>
                        <Form.Control
                            type="button"
                            onClick={handleShowModal}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicFecha">
                        <Form.Label>Fecha</Form.Label>
                        <Form.Control
                            type="date"
                            name="fecha"
                            value={ventaData.fecha}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicLinkFactura">
                        <Form.Label>Link Factura</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Escribe el link de la factura"
                            name="linkFactura"
                            value={ventaData.linkFactura}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicEstadoEnvio">
                        <Form.Label>Estado de Envio</Form.Label>
                        <Form.Control
                            as="select"
                            name="estadoEnvio"
                            value={ventaData.estadoEnvio}
                            onChange={handleChange}
                        >
                            <option value="">Seleccione el Estado del Envio</option>
                            <option value="Entregado">Entregado</option>
                            <option value="Despachado">Despachado</option>
                            <option value="Preparando">Preparando</option>
                        </Form.Control>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="light" className="btn-modal" onClick={onClose}>
                    Cerrar
                </Button>
                <Button
                    className="btn-modal-send"
                    onClick={esEdit ? () => saveChanges(ventaData) : handleSave}
                >
                    {esEdit ? "Actualizar" : "Guardar"}
                </Button>
            </Modal.Footer>

            {showModal && <AgregarProductoVenta onClose={handleCloseModal}
                setProductosVentas={setProductosVentas}
                productosVentas={productosVentas} />
            }
        </Modal>
    );

};

export default AgregarVenta;