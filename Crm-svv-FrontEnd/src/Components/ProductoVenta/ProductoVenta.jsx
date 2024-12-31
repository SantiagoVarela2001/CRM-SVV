import React, { useState } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import './ProductoVenta.css'; // Asegúrate de tener este archivo CSS

const ProductoVenta = ({ producto, setProductosVentas, productosVentas }) => {

  const [cantidad, setCantidad] = useState(0);
  const [descuento, setDescuento] = useState(0.0);
  const [productoVenta, setProductoVenta] = useState({
    "producto": { "id": producto.id },
    "venta": '',
    "cantidadDelProducto": cantidad,
    "descuento": descuento
  });

  const handleIncrement = () => {
    if (cantidad < producto.stock) {
      const nuevaCantidad = cantidad + 1;
      setCantidad(nuevaCantidad);
      const nuevoProductoVenta = {
        ...productoVenta,
        cantidadDelProducto: nuevaCantidad,
        descuento
      };

      setProductosVentas(prev => {
        const existe = prev.find(p => p.producto.id === producto.id);
        if (existe) {
          return prev.map(p => p.producto.id === producto.id ? nuevoProductoVenta : p);
        } else {
          return [...prev, nuevoProductoVenta];
        }
      });
    }
  };

  const handleDecrement = () => {
    if (cantidad > 0) {
      const nuevaCantidad = cantidad - 1;
      setCantidad(nuevaCantidad);

      if (nuevaCantidad === 0) {
        setProductoVenta({
          ...productoVenta,
          cantidadDelProducto: 0,
          descuento
        });
        setProductosVentas(prev => prev.filter(p => p.producto.id !== producto.id));
      } else {
        const nuevoProductoVenta = {
          ...productoVenta,
          cantidadDelProducto: nuevaCantidad,
          descuento
        };
        setProductosVentas(prev => prev.map(p => 
          p.producto.id === producto.id ? nuevoProductoVenta : p
        ));
      }
    }
  };

  const handleDescuentoChange = (e) => {
    const nuevoDescuento = parseFloat(e.target.value);
    setDescuento(nuevoDescuento);
    setProductoVenta({
      ...productoVenta,
      descuento: nuevoDescuento
    });

    setProductosVentas(prev => prev.map(p => 
      p.producto.id === producto.id ? { ...p, descuento: nuevoDescuento } : p
    ));
  };

  return (
    <div className="producto-venta border p-3 mb-3 shadow">
      <div className="producto-info mb-3">
        <h3 className="text-center">{producto.nombre}</h3>
        <p>Stock disponible: <span className="stock-rojo">{producto.stock}</span></p>
      </div>
      <InputGroup className="mb-3">
        <Button variant="outline-danger" onClick={handleDecrement}>
          -
        </Button>
        <Form.Control className="text-center" value={cantidad} readOnly aria-label="Cantidad" />
        <Button variant="outline-danger" onClick={handleIncrement} disabled={cantidad >= producto.stock}>
          +
        </Button>
      </InputGroup>
      <Form.Group className="mb-3">
        <Form.Label>Porcentaje de Descuento:</Form.Label>
        <Form.Control 
          type="number" 
          value={descuento} 
          onChange={handleDescuentoChange} 
          step="0.1" 
          min="0" 
          max="100" 
          aria-label="Descuento" 
        />
      </Form.Group>
    </div>
  );
};

export default ProductoVenta;
