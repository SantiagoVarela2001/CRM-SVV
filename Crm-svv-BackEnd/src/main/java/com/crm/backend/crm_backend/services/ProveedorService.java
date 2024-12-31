package com.crm.backend.crm_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.backend.crm_backend.entities.Proveedor;
import com.crm.backend.crm_backend.repositories.ProveedorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public Proveedor saveProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Optional<Proveedor> getProveedorById(Long proveedorId, Long empresaId) {
        return proveedorRepository.findById(proveedorId, empresaId);
    }

    public List<Proveedor> findByEmpresaId(Long empresaId){
        return proveedorRepository.findByEmpresaId(empresaId);
    }

    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    public void deleteProveedor(Long ProveedorId, Long empresaId) {
        proveedorRepository.deleteById(ProveedorId, empresaId);
    }
}
