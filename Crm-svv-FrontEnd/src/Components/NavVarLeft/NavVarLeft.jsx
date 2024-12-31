import React from 'react';
import { Nav, Navbar, Container } from 'react-bootstrap';
import { Link, useLocation } from 'react-router-dom';
import './NavVarLeft.css';

const NavVarLeft = () => {
  const location = useLocation();

  return (
    <Navbar bg="dark" variant="dark" className="flex-column vh-100 p-3 contenedor-nav">
      <h4 className='crm-titulo'>CRM SVV</h4>
      <Container>
        <Nav className="flex-column w-100">
          <Nav.Link 
            as={Link} 
            to='/clientes' 
            className={location.pathname === '/clientes' ? 'active' : ''}
          >
            Clientes
          </Nav.Link>
          <Nav.Link 
            as={Link} 
            to='/productos' 
            className={location.pathname === '/productos' ? 'active' : ''}
          >
            Productos
          </Nav.Link>
          <Nav.Link 
            as={Link} 
            to='/ventas' 
            className={location.pathname === '/ventas' ? 'active' : ''}
          >
            Ventas
          </Nav.Link>
          <Nav.Link 
            as={Link} 
            to='/proveedores' 
            className={location.pathname === '/proveedores' ? 'active' : ''}
          >
            Proveedores
          </Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  );
};

export default NavVarLeft;
