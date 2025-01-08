package com.crm.backend.crm_backend.controllers;

import com.crm.backend.crm_backend.dto.ProductoVentaDTO;
import com.crm.backend.crm_backend.dto.VentaDTO;
import com.crm.backend.crm_backend.entities.Cliente;
import com.crm.backend.crm_backend.entities.ProductoVenta;
import com.crm.backend.crm_backend.entities.Venta;
import com.crm.backend.crm_backend.services.ProductoVentaService;
import com.crm.backend.crm_backend.services.VentaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoVentaService productoVentaService;

    @GetMapping
    public List<VentaDTO> getAllVentas(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        return ventaService.getAllVentas(empresaId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<VentaDTO>> getVentaById(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<VentaDTO> VentaDTO = ventaService.getVentaDTOById(id, empresaId);
        return ResponseEntity.ok(VentaDTO);
    }
    

    @GetMapping("/{id}/cliente")
    public ResponseEntity<Cliente> findClienteByVentaId(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Cliente Cliente = ventaService.findClienteByVentaId(id, empresaId);
        return ResponseEntity.ok(Cliente);
    }

    @GetMapping("/{id}/productoVenta")
    public ResponseEntity<List<ProductoVentaDTO>> getProducto(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        List<ProductoVentaDTO> productoVentas = ventaService.getProductoVentaWithCliente(id, empresaId);
        return new ResponseEntity<>(productoVentas, HttpStatus.OK);
    }

    @PostMapping
    public Venta createVenta(@Valid @RequestBody Venta venta, BindingResult result, HttpServletRequest request) {

        Long empresaId = (Long) request.getAttribute("empresa id");
        return ventaService.saveVenta(venta, empresaId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenta(@Valid @RequestBody Venta ventaDetails, BindingResult result, @PathVariable Long id, HttpServletRequest request) {
       
        if (result.hasErrors()) {
            return validation(result);            
        }
       
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Venta> existingVenta = ventaService.getVentaById(id, empresaId);

        System.out.println();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Venta existente: " + existingVenta.get());
        System.out.println("Productos ventas a actualizar: " + ventaDetails.getProductoVentas());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println();

        if (existingVenta.isPresent()) {
            Venta ventadb = existingVenta.get();
            ventadb.setCliente(ventaDetails.getCliente());
            ventadb.setFecha(ventaDetails.getFecha());
            ventadb.setLinkFactura(ventaDetails.getLinkFactura());
            ventadb.setEstadoEnvio(ventaDetails.getEstadoEnvio());
            ventadb.getProductoVentas().clear();
            for(ProductoVenta productoVenta : ventaDetails.getProductoVentas()){
                productoVentaService.updateProductoVenta(productoVenta.getId(), productoVenta, empresaId);
            }
            Venta updatedVenta = ventaService.saveVenta(ventadb, empresaId);
            return ResponseEntity.ok(updatedVenta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        ventaService.deleteVenta(id, empresaId);
        return ResponseEntity.noContent().build();

    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
