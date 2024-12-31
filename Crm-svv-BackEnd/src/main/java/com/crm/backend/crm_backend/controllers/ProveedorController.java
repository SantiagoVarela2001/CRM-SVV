package com.crm.backend.crm_backend.controllers;

import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.entities.Proveedor;
import com.crm.backend.crm_backend.services.EmpresaService;
import com.crm.backend.crm_backend.services.ProveedorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Proveedor> getAllProveedores(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        List<Proveedor> proveedores = proveedorService.findByEmpresaId(empresaId);
        return proveedores;
    }

    @GetMapping("/{proveedorId}")
    public ResponseEntity<Optional<Proveedor>> getProveedorById(@PathVariable Long proveedorId,
            HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Proveedor> proveedor = proveedorService.getProveedorById(proveedorId, empresaId);

        if (proveedor != null) {
            return ResponseEntity.ok(proveedor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PostMapping
    public Proveedor createProveedor(@Valid @RequestBody Proveedor proveedor, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Empresa> empresa = empresaService.getEmpresaById(empresaId);
        if (empresa.isPresent()) {
            proveedor.setEmpresa(empresa.get());
        }
        return proveedorService.saveProveedor(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@Valid @PathVariable Long id, @RequestBody Proveedor proveedorDetails,
            HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Proveedor> existingProveedor = proveedorService.getProveedorById(id, empresaId);

        if (existingProveedor.isPresent()) {
            Proveedor proveedordb = existingProveedor.get();
            proveedordb.setNombre(proveedorDetails.getNombre());
            proveedordb.setTelefono(proveedorDetails.getTelefono());
            proveedordb.setEmail(proveedorDetails.getEmail());
            Proveedor updatedProveedor = proveedorService.saveProveedor(proveedordb);
            return ResponseEntity.ok(updatedProveedor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        proveedorService.deleteProveedor(id, empresaId);
        return ResponseEntity.noContent().build();

    }
}