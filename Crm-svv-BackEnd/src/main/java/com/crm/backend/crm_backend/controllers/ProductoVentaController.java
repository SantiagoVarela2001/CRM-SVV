package com.crm.backend.crm_backend.controllers;

import com.crm.backend.crm_backend.dto.ProductoVentaDTO;
import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.entities.ProductoVenta;
import com.crm.backend.crm_backend.services.EmpresaService;
import com.crm.backend.crm_backend.services.ProductoService;
import com.crm.backend.crm_backend.services.ProductoVentaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/productos-ventas")
public class ProductoVentaController {

    @Autowired
    private ProductoVentaService productoVentaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoVentaDTO>> getAllProductoVentas(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        List<ProductoVentaDTO> productoVentas = productoVentaService.getAllProductoVentas(empresaId);
        return new ResponseEntity<>(productoVentas, HttpStatus.OK);
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<ProductoVentaDTO>> findByVentaId(@PathVariable Long ventaId, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        List<ProductoVentaDTO> productoVentas = productoVentaService.findByVentaId(ventaId, empresaId);
        return new ResponseEntity<>(productoVentas, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductoVenta> getProductoVentaById(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<ProductoVenta> productoVenta = productoVentaService.getProductoVentaById(id, empresaId);
        return productoVenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductoVenta createProductoVenta(@Valid @RequestBody ProductoVenta productoVenta, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Empresa> empresaDb = empresaService.getEmpresaById(empresaId);

        if (empresaDb.isPresent()) {
            productoVenta.setEmpresa(empresaDb.get());
        }
        return productoVentaService.saveProductoVenta(productoVenta, productoService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoVenta> updateProductoVenta(@Valid @PathVariable Long id,
            @RequestBody ProductoVenta productoVentaDetails, HttpServletRequest request) {
                Long empresaId = (Long) request.getAttribute("empresa id");
                return productoVentaService.updateProductoVenta(id, productoVentaDetails, empresaId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductoVenta(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        productoVentaService.deleteProductoVenta(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
