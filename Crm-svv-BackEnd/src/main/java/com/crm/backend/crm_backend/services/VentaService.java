package com.crm.backend.crm_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.dto.ProductoVentaDTO;
import com.crm.backend.crm_backend.dto.VentaDTO;
import com.crm.backend.crm_backend.entities.Cliente;
import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.entities.Producto;
import com.crm.backend.crm_backend.entities.ProductoVenta;
import com.crm.backend.crm_backend.entities.Venta;
import com.crm.backend.crm_backend.repositories.VentaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoVentaService productoVentaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ConvertVentaToVentaDTO convertVentaToVentaDTO;


    @Transactional
    public Venta saveVenta(Venta venta, Long empresaId) {
        Optional<Empresa> empresadb = empresaService.getEmpresaById(empresaId);
        if (!empresadb.isPresent()) {
            throw new RuntimeException("Empresa no encontrada");
        }
        venta.setEmpresa(empresadb.get());
    
        // Validar y asignar el cliente
        Optional<Cliente> clienteOptional = clienteService.getClienteById(venta.getCliente().getId(), empresaId);
        if (!clienteOptional.isPresent()) {
            throw new RuntimeException("Cliente no encontrado");
        }
        venta.setCliente(clienteOptional.get());
    
        if (venta.getProductoVentas() != null) {
            for (ProductoVenta productoVenta : venta.getProductoVentas()) {
                validarProductoVenta(productoVenta, empresaId);
    
                Producto producto = productoVenta.getProducto();
    
                // Vincular la venta y el producto
                productoVenta.setVenta(venta);

                // Reducir stock
                reducirStockProducto(producto, productoVenta.getCantidadDelProducto());
    
                // Guardar ProductoVenta
                productoVentaService.saveProductoVenta(productoVenta, productoService);
            }
        }
    
        // Guardar y devolver la venta
        return ventaRepository.save(venta);
    }
    
    // Método auxiliar para validar ProductoVenta
    private void validarProductoVenta(ProductoVenta productoVenta, Long empresaId) {
        if (productoVenta.getProducto() == null || productoVenta.getProducto().getId() == null) {
            throw new RuntimeException("Producto no proporcionado");
        }
    
        Optional<Producto> productoOpt = productoService.getProductoById(productoVenta.getProducto().getId(), empresaId);
        if (!productoOpt.isPresent()) {
            throw new RuntimeException("Producto no encontrado");
        }
    
        productoVenta.setProducto(productoOpt.get());
    }
    
    // Método auxiliar para reducir stock del producto
    private void reducirStockProducto(Producto producto, int cantidad) {
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        productoService.saveProducto(producto);
    }
    

    public Optional<VentaDTO> getVentaDTOById(Long ventaId, Long empresaId) {
        Optional<Venta> ventaDB = ventaRepository.findById(ventaId, empresaId);
        if(!ventaDB.isPresent()){
            throw new IllegalArgumentException("La venta no puede ser nula");
        }else{
            return Optional.ofNullable(convertVentaToVentaDTO.convertirVentaDTO(ventaDB.get(), empresaId));
        }
        
    }

    public Optional<Venta> getVentaById(Long ventaId, Long empresaId) {
        return ventaRepository.findById(ventaId, empresaId);
    }

    public List<VentaDTO> getAllVentas(Long empresaId) {
        List<Venta> ventas = ventaRepository.findByEmpresaId(empresaId);
        return ventas.stream()
                .map(venta -> convertVentaToVentaDTO.convertirVentaDTO(venta, empresaId))
                 .collect(Collectors.toList());
    }

    public void deleteVenta(Long id, Long empresaId) {
        ventaRepository.deleteById(id, empresaId);
    }

    public Cliente findClienteByVentaId(Long id, Long empresaId) {
        return ventaRepository.findClienteByVentaId(id, empresaId)
                .orElseThrow();
    }

    public List<ProductoVentaDTO> getProductoVentaWithCliente(Long ventaId, Long empresaId) {
        List<ProductoVenta> productoVentas = ventaRepository.findProductoVentaByVentaId(ventaId, empresaId);
        
        // Convertimos los productos a DTOs
        List<ProductoVentaDTO> productoVentaDTOs = productoVentas.stream()
            .map(productoVenta -> new ProductoVentaDTO(
                productoVenta.getId(),
                productoVenta.getProducto().getId(),
                productoVenta.getProducto().getNombre(),
                productoVenta.getCantidadDelProducto(),
                productoVenta.getDescuento(),
                productoVenta.getPrecioFinal()
            ))
            .collect(Collectors.toList());
    
        return productoVentaDTOs;
    }
}
