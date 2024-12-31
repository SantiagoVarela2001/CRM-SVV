import React, { useState, useEffect } from 'react';
import './Body.css';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import NavVarLeft from '../NavVarLeft/NavVarLeft';
import Clientes from '../Clientes/Clientes';
import Productos from '../Productos/Productos';
import Ventas from '../Ventas/Ventas';
import Proveedores from '../Proveedores/Proveedores';
import ProveedorProductos from '../ProveedorProductos/ProveedorProductos';
import ClienteCompras from '../ClienteCompras/ClienteCompras';
import CompraProductos from '../CompraProductos/CompraProductos';
import MostrarProductosDeVenta from '../Ventas/MostrarProductosDeVenta';
import Login from '../Login/Login';

const Body = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() =>{
    const auth = localStorage.getItem('isAuthenticated');
    if(auth === 'true'){
      setIsAuthenticated(true);
    }
  }, [])

  if (isAuthenticated === null) {
    return <div>Cargando...</div>;
  }

  return (
    <Router>
      <Container fluid className=''>
        <Row className=''>
          
          {isAuthenticated && (
            <Col xs={2} className='nav-var-left'>
              <NavVarLeft />
            </Col>
          )}
          <Col xs={isAuthenticated ? 10 : 12} className='content p-0 m-0'>
            <Routes>

              

              {!isAuthenticated ? (
                <Route path="/login" element={<Login setIsAuthenticated={setIsAuthenticated} />} />
              ) : (
                <>
                  <Route path="/clientes" element={<Clientes />} />
                  <Route path="/clientes/:clienteId/compras" element={<ClienteCompras />} />

                  <Route path="/productos" element={<Productos />} />
                  <Route path="/compras/:compraId/productos" element={<CompraProductos />} />

                  <Route path="/ventas" element={<Ventas />} />

                  <Route path="/proveedores" element={<Proveedores />} />
                  <Route path="/proveedores/:proveedorId/productos" element={<ProveedorProductos />} />

                  <Route path="/productoVenta" element={<MostrarProductosDeVenta />} />
                </>
              )}
               <Route path="*" element={<Navigate to={isAuthenticated ? "/clientes" : "/login"} />} />
            </Routes>
          </Col>
        </Row>
      </Container>
    </Router>
  );
}

export default Body;
