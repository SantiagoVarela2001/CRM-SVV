package com.crm.backend.crm_backend.services;

import org.springframework.stereotype.Service;

import com.crm.backend.crm_backend.dto.ProductoVentaDTO;
import com.crm.backend.crm_backend.entities.Producto;
import com.crm.backend.crm_backend.entities.ProductoVenta;
import com.crm.backend.crm_backend.repositories.ProductoVentaRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoVentaService {

    private final ProductoVentaRepository productoVentaRepository;

    public ProductoVentaService(ProductoVentaRepository productoVentaRepository) {
        this.productoVentaRepository = productoVentaRepository;
    }

    @Transactional
    public ProductoVenta saveProductoVenta(ProductoVenta productoVenta, ProductoService productoService) {

        if (productoVenta.getProducto() == null || productoVenta.getProducto().getId() == null) {
            throw new IllegalArgumentException("El producto en ProductoVenta no es válido.");
        }

        // Descontar stock usando el servicio como parámetro
        productoService.descontarStock(productoVenta.getProducto().getId(), productoVenta.getCantidadDelProducto());
        
        //setear el precio final del producto venta
        Optional<Producto> producto = productoService.getProductoById(productoVenta.getProducto().getId(), productoVenta.getEmpresa().getId());

        if(producto.isPresent()){
            productoVenta.setPrecioFinal(
                (producto.get().getPrecioCompra() * productoVenta.getCantidadDelProducto()) * 
                (1 - productoVenta.getDescuento() / 100));
        }else{
            throw new IllegalArgumentException("El producto No existe !!."); 
        }
        return productoVentaRepository.save(productoVenta);
    }

    public List<ProductoVentaDTO> findByVentaId(Long ventaId, Long empresaId) {
        List<ProductoVenta> productoVentas = productoVentaRepository.findByVentaId(ventaId, empresaId);
        
        // Convertir cada ProductoVenta a ProductoVentaDTO
        return productoVentas.stream().map(productoVenta -> new ProductoVentaDTO(
            productoVenta.getId(),
            productoVenta.getProducto().getId(),
            productoVenta.getProducto().getNombre(),
            productoVenta.getCantidadDelProducto(),
            productoVenta.getDescuento(),
            productoVenta.getPrecioFinal()
        )).collect(Collectors.toList());
    }

    public Optional<ProductoVenta> getProductoVentaById(Long id, Long empresaId) {
        return productoVentaRepository.findById(id);
    }

     public List<ProductoVentaDTO> getAllProductoVentas(Long empresaId) {
        List<ProductoVenta> productoVentas = productoVentaRepository.findByEmpresaId(empresaId);
        
        // Convertir cada ProductoVenta a ProductoVentaDTO
        return productoVentas.stream().map(productoVenta -> new ProductoVentaDTO(
            productoVenta.getId(),
            productoVenta.getProducto().getId(),
            productoVenta.getProducto().getNombre(),
            productoVenta.getCantidadDelProducto(),
            productoVenta.getDescuento(),
            productoVenta.getPrecioFinal()
        )).collect(Collectors.toList());
    }

    public List<ProductoVentaDTO> getAllProductoVentasXId(Long empresaId, Long productoId) {
        List<ProductoVenta> productoVentas = productoVentaRepository.findByProductoIdAndEmpresaId(productoId, empresaId);
        
        // Convertir cada ProductoVenta a ProductoVentaDTO
        return productoVentas.stream().map(productoVenta -> new ProductoVentaDTO(
            productoVenta.getId(),
            productoVenta.getProducto().getId(),
            productoVenta.getProducto().getNombre(),
            productoVenta.getCantidadDelProducto(),
            productoVenta.getDescuento(),
            productoVenta.getPrecioFinal()
        )).collect(Collectors.toList());
    }

    public void deleteProductoVenta(Long id, Long empresaId) {
        productoVentaRepository.deleteById(id, empresaId);
    }
}

