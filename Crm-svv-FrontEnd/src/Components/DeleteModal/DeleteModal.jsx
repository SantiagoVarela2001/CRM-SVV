import { useEffect, useState } from 'react'
import { Modal, Button } from 'react-bootstrap';

const DeleteModal = ({ show, onHide, item, onDelete }) => {

  const [deleteData, setDeleteData] = useState(item);

  useEffect(() => {
    setDeleteData(item)
  }, [item])

  const handleDelete = (deleteData) => {
    onDelete(deleteData);
    onHide();
  }

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>Eliminar</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>¿Estás seguro que deseas eliminarlo?</p>
        <p>¡No podrás revertir esta situación!</p>
      </Modal.Body>
      <Modal.Footer>
        <Button type="submit" variant="danger" onClick={() => handleDelete(deleteData)} className='btn-ladrillo'
        >Eliminar</Button>
        <Button variant="secondary" onClick={onHide} className='btn-modal-send'>Cancelar</Button>
      </Modal.Footer>
    </Modal>
  )
}

export default DeleteModal