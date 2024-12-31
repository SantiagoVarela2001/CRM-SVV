import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import EditModal from '../EditModal/EditModal';
import AgregarVenta from '../AgregarVenta/AgregarVenta';

const TablaVentas = ({ venta, index }) => {

  const apiBaseURL = import.meta.env.VITE_API;

  const [ventaCliente, setVentaCliente] = useState(null);
  const [selectedVenta, setSelectedVenta] = useState(null);
  const [selectedVentaDelete, setSelectedVentaDelete] = useState(null);
  const [showEdit, setShowEdit] = useState(false);
  const [showDelete, setShowDelete] = useState(false);
  
  const handleCloseModal = () => setShowEdit(false);


  const navigate = useNavigate();
  const opciones = { year: 'numeric', month: 'long', day: 'numeric' };
  

  useEffect(() => {
    fetchVenta();
  }, [venta]);

  const openEditModal = (venta) => {
    setSelectedVenta(venta);
    setShowEdit(true);
  };

  const fetchVenta = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${apiBaseURL}/api/ventas/${venta.id}/cliente`,{
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
      const data = await response.json();
      setVentaCliente(data);
    } catch (error) {
      console.error('Error fetching Ventas:', error);
    }
  };

  const handleVerProductos = () => {
    navigate('/ProductoVenta', { state: { venta } });
  };

  const fechaVenta = new Date(venta.fecha);

  const saveChanges = async (updateVenta) => {
    const token = localStorage.getItem('token');
    
    const ventaActualizada = {
      producto: updateVenta.producto,
      cliente: updatedCliente.cliente,
      fecha: updatedCliente.fecha,
      estadoEnvio: updatedCliente.pais,
    }

    try {
      const response = await fetch(`${apiBaseURL}/api/ventas/${selectedVenta.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(ventaActualizada),
      });

      if (!response.ok) {
        const contentType = response.headers.get('Content-Type');
        let errorMessage = 'Error al actualizar la Venta';

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
      alert('Venta actualizada correctamente');
      window.location.reload();
    } catch (error) {
      console.error('Error al actualizar la Venta:', error);
      alert(error.message);
    }
  };

  return (
      <>
        {selectedVenta && (
          <AgregarVenta onClose={handleCloseModal} ventaPreEdit={selectedVenta} saveChanges={saveChanges}
          />
        )}
        {selectedVentaDelete && (
          <DeleteModal
          show={showDelete}
          onHide={() => setShowDelete(false)}
          item={selectedVentaDelete}
          onDelete={DeleteVenta}/>
        )}
        <tr key={index}>
          <td>
            <button type="button" onClick={handleVerProductos} className="btn-ladrillo">
              Ver Productos
            </button>
          </td>
          <td>{ventaCliente ? ventaCliente.nombre : 'Cargando...'}</td>
          <td>{fechaVenta.toLocaleDateString('es-ES', opciones)}</td>
          <td>
            <a href={venta.linkFactura} target="_blank" rel="noopener noreferrer" className="link-ladrillo">
              Ver Factura
            </a>
          </td>
          <td>{venta.estadoEnvio}</td>
          <td><button onClick={() => openEditModal(venta)} className='btn-ladrillo'>Editar</button></td>
          <td><button onClick={() => openDeleteModal(cliente)} className='btn-ladrillo'>Eliminar</button></td>
        </tr>
      </>
    );
};

export default TablaVentas;
