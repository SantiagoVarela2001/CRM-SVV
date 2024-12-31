import React, { useState, useEffect } from 'react';
import { Container, Form, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import AgregarCliente from '../AgregarCliente/AgregarCliente';
import EditModal from '../EditModal/EditModal';
import './clientes.css';
import DeleteModal from '../DeleteModal/DeleteModal';

const Clientes = () => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [searchTerm, setSearchTerm] = useState('');
  const [clientes, setClientes] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedCliente, setSelectedCliente] = useState(null);
  const [selectedClienteDelete, setSelectedClienteDelete] = useState(null);
  const [showEdit, setShowEdit] = useState(false);
  const [showDelete, setShowDelete] = useState(false);

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const fields = [
    { name: 'nombre', label: 'Nombre' },
    { name: 'email', label: 'Email', type: 'email' },
    { name: 'telefono', label: 'Telefono', type: 'tel' },
    { name: 'pais', label: 'Pais' },
    { name: 'direccion', label: 'Direccion' }
  ];

  useEffect(() => {
    fetchClientes();
  }, []);

  const fetchClientes = async () => {
    const token = localStorage.getItem('token');

    try {
      const response = await fetch(`${apiBaseURL}/api/clientes`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      if (!response.ok) {
        throw new Error('Algo salió mal al obtener los clientes.');
      }
      const data = await response.json();
      setClientes(data);
      localStorage.setItem('clientes', JSON.stringify(data));
    } catch (error) {
      console.error('Error fetching Clientes:', error);
    }
  };

  const filteredClientes = clientes.filter(cliente =>
    cliente.nombre.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const openEditModal = (cliente) => {
    setSelectedCliente(cliente);
    setShowEdit(true);
  };

  const openDeleteModal = (cliente) => {
    setSelectedClienteDelete(cliente);
    setShowDelete(true);
  };

  const saveChanges = async (updatedCliente) => {
    const token = localStorage.getItem('token');

    const clienteActualizado = {
      nombre: updatedCliente.nombre,
      email: updatedCliente.email,
      telefono: updatedCliente.telefono,
      pais: updatedCliente.pais,
      direccion: updatedCliente.direccion
    }

    try {
      const response = await fetch(`${apiBaseURL}/api/clientes/${selectedCliente.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(clienteActualizado),
      });

      if (!response.ok) {
        const contentType = response.headers.get('Content-Type');
        let errorMessage = 'Error al actualizar el cliente';

        if (contentType && contentType.includes('application/json')) {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        }

        throw new Error(errorMessage);
      }

      let updatedData = null;
      const contentType = response.headers.get('Content-Type');

      if (contentType && contentType.includes('application/json')) {
        updatedData = await response.json();
      }
      setShowEdit(false);
      alert('Cliente actualizado correctamente');
      window.location.reload();
    } catch (error) {
      console.error('Error al actualizar el cliente:', error);
      alert(error.message);
    }
  };

  const DeleteCliente = async () => {
    const token = localStorage.getItem('token');

    try {
      const response = await fetch(`${apiBaseURL}/api/clientes/${selectedClienteDelete.id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });

      if (!response.ok) {
        const contentType = response.headers.get('Content-Type');
        let errorMessage = 'Error al eliminar el cliente';

        if (contentType && contentType.includes('application/json')) {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        }

        throw new Error(errorMessage);
      }

      let updatedData = null;
      const contentType = response.headers.get('Content-Type');

      if (contentType && contentType.includes('application/json')) {
        updatedData = await response.json();
      }
      setShowDelete(false);
      alert('Cliente eliminado correctamente');
      window.location.reload();
    } catch (error) {
      console.error('Error al eliminar el cliente:', error);
      alert(error.message);
    }
  }

  return (
    <Container className="mt-5">
      <h2 className="mb-4">CLIENTES</h2>

      <Form.Control
        type="text"
        placeholder="Buscar cliente por nombre..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="mb-4"
      />

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>País</th>
            <th>Dirección</th>
            <th>Compras</th>
            <th>Editar</th>
            <th>Eliminar</th>
          </tr>
        </thead>
        <tbody>
          {filteredClientes.map((cliente, index) => (
            <tr key={index}>
              <td>{cliente.nombre}</td>
              <td>{cliente.email}</td>
              <td>{cliente.telefono}</td>
              <td>{cliente.pais}</td>
              <td>{cliente.direccion}</td>
              <td>
                <Link to={`/clientes/${cliente.id}/compras`} className="link-ladrillo">Ver Compras</Link>
              </td>
              <td><button onClick={() => openEditModal(cliente)} className='btn-ladrillo'>Editar</button></td>
              <td><button onClick={() => openDeleteModal(cliente)} className='btn-ladrillo'>Eliminar</button></td>
            </tr>
          ))}
        </tbody>
      </Table>
      {selectedCliente && (
        <EditModal
          show={showEdit}
          onHide={() => setShowEdit(false)}
          item={selectedCliente}
          onSave={saveChanges}
          fields={fields}
        />
      )}
      {selectedClienteDelete && (
        <DeleteModal
        show={showDelete}
        onHide={() => setShowDelete(false)}
        item={selectedClienteDelete}
        onDelete={DeleteCliente}/>
      )}

      {/* Botón flotante */}
      <button className="floating-button" onClick={handleShowModal}>
        +
      </button>

      {/* Modal de Agregar Cliente */}
      {showModal && <AgregarCliente onClose={handleCloseModal} />}
    </Container>
  );
};

export default Clientes;
