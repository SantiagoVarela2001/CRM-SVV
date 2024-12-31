package com.crm.backend.crm_backend.controllers;

import com.crm.backend.crm_backend.dto.ProductoDTO;
import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.entities.Producto;
import com.crm.backend.crm_backend.entities.Proveedor;
import com.crm.backend.crm_backend.repositories.EmpresaRepository;
import com.crm.backend.crm_backend.repositories.ProveedorRepository;
import com.crm.backend.crm_backend.services.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public List<ProductoDTO> getAllProductos(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        return productoService.getAllProductos(empresaId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> createProducto(@Valid
            @RequestPart("producto") String productoJson,
            @RequestPart("imagen") MultipartFile imagen,
            HttpServletRequest request) throws IOException {

        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Empresa> empresa = empresaRepository.findById(empresaId);

        ObjectMapper objectMapper = new ObjectMapper();
        Producto producto = objectMapper.readValue(productoJson, Producto.class);

        if (!imagen.isEmpty()) {
            byte[] imagenComprimida = productoService.compressImage(imagen.getBytes());
            producto.setImagen(imagenComprimida);
        }

        if (producto.getProveedor() != null && producto.getProveedor().getId() != null) {
            Optional<Proveedor> proveedorOpt = proveedorRepository.findById(producto.getProveedor().getId());
            if (proveedorOpt.isPresent() && empresa.isPresent()){
                producto.setEmpresa(empresa.get());
                producto.setProveedor(proveedorOpt.get());
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }

        Producto savedProducto = productoService.saveProducto(producto);

        return ResponseEntity.ok(savedProducto);
    }

    @GetMapping("/{productoId}/imagen")
    public ResponseEntity<byte[]> getImagenProducto(@PathVariable Long productoId) {
        Optional<Producto> productoOpt = productoService.getProductoByIdImagen(productoId);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            byte[] imagen = producto.getImagen();
            if (imagen != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long productoId, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Producto> producto = productoService.getProductoById(productoId, empresaId);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<Producto> updateProducto(@Valid @PathVariable Long productoId, @RequestBody Producto productoDetails, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Producto> existingProducto = productoService.getProductoById(productoId, empresaId);

        if (existingProducto.isPresent()) {
            Producto productoDb = existingProducto.get();
            productoDb.setNombre(productoDetails.getNombre());
            productoDb.setPrecioCompra(productoDetails.getPrecioCompra());
            productoDb.setPrecioVenta(productoDetails.getPrecioVenta());
            productoDb.setStock(productoDetails.getStock());
            productoDb.setProveedor(productoDetails.getProveedor());
            Producto updatedProducto = productoService.saveProducto(productoDb);
            return ResponseEntity.ok(updatedProducto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long productoId, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        productoService.deleteProducto(productoId, empresaId);
        return ResponseEntity.noContent().build();

    }
}
