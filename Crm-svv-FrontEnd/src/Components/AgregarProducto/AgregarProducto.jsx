import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';

const AgregarProducto = ({ onClose }) => {

    const apiBaseURL = import.meta.env.VITE_API;

    const [proveedores, setProveedores] = useState([]);
    const [productoData, setProductoData] = useState({
        nombre: '',
        precioCompra: '',
        precioVenta: '',
        stock: '',
        proveedor: {}
    });

    useEffect(() => {
        fetchProveedores();
    }, []);

    const fetchProveedores = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${apiBaseURL}/api/proveedores`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            });
            
            const data = await response.json();
            setProveedores(data);
        } catch (error) {
            console.error('Error fetching Proveedores:', error);
        }
    };

    const handleClose = () => onClose();

    const handleSave = async () => {
        const token = localStorage.getItem('token');
        const formData = new FormData();
        formData.append("producto", JSON.stringify({
            nombre: productoData.nombre,
            precioCompra: parseFloat(productoData.precioCompra),
            precioVenta: parseFloat(productoData.precioVenta),
            stock: parseInt(productoData.stock),
            proveedor: { id: productoData.proveedor.id }
        }));
        formData.append("imagen", productoData.imagen);
        try {
            const response = await fetch(`${apiBaseURL}/api/productos`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                body: formData,
            });
            
            if (!response.ok) {
                throw new Error('Error al agregar el producto');
            }

            handleClose();
        } catch (error) {
            console.error('Error al agregar el producto:', error);
        }
        window.location.reload();
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProductoData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleProveedorChange = (e) => {
        setProductoData({
            ...productoData,
            proveedor: { id: e.target.value }
        });
    };

    const handleImageChange = (e) => {
        setProductoData({
            ...productoData,
            imagen: e.target.files[0]
        });
    };

    return (
        <Modal show={true} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Agregar Nuevo Producto</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>

                    <Form.Group className="mb-1" controlId="formBasicImage">
                        <Form.Label>Imagen del Producto</Form.Label>
                        <Form.Control
                            type="file"
                            name="imagen"
                            accept="image/*"
                            onChange={handleImageChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicName">
                        <Form.Label>Nombre</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Escribe el nombre del producto"
                            name="nombre"
                            value={productoData.nombre}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicPriceBuy">
                        <Form.Label>Precio Compra</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Ingresa el precio de compra"
                            name="precioCompra"
                            value={productoData.precioCompra}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicPriceSell">
                        <Form.Label>Precio Venta</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Ingresa el precio de venta"
                            name="precioVenta"
                            value={productoData.precioVenta}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicStock">
                        <Form.Label>Stock</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Ingresa el Stock"
                            name="stock"
                            value={productoData.stock}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-1" controlId="formBasicProveedor">
                        <Form.Label>Proveedor</Form.Label>
                        <Form.Control
                            as="select"
                            name="proveedor"
                            value={productoData.proveedor?.id || ''}
                            onChange={handleProveedorChange}
                        >
                            <option value="">Seleccione un proveedor</option>
                            {proveedores.map((proveedor) => (
                                <option key={proveedor.id} value={proveedor.id}>
                                    {proveedor.nombre}
                                </option>
                            ))}
                        </Form.Control>
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

export default AgregarProducto;
