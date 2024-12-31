package com.crm.backend.crm_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.backend.crm_backend.dto.ProductoDTO;
import com.crm.backend.crm_backend.entities.Producto;
import com.crm.backend.crm_backend.repositories.ProductoRepository;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoVentaService productoVentaService;

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> getProductoById(Long productoId, Long empresaId) {
        return productoRepository.findById(productoId, empresaId);
    }

    public List<ProductoDTO> getAllProductos(Long empresaId) {
        List<ProductoDTO> productosDto = new ArrayList<>();

        List<Producto> productos = productoRepository.findByEmpresaId(empresaId);

        for (Producto p : productos) {
            ProductoDTO productoDTO = new ProductoDTO(p.getId(), p.getNombre(), p.getPrecioCompra(), p.getPrecioVenta(),
                    p.getStock(), p.getProveedor(), p.getEmpresa(),
                    productoVentaService.getAllProductoVentasXId(empresaId, p.getId()));
            productosDto.add(productoDTO);
        }
        return productosDto;
    }

    public void deleteProducto(Long productoId, Long empresaId) {
        productoRepository.deleteById(productoId, empresaId);
    }

    public Optional<Producto> getProductoByIdImagen(Long productoId) {
        return productoRepository.findById(productoId);
    }

    public void descontarStock(Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto con ID: " + productoId);
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    public byte[] compressImage(byte[] originalImage) throws IOException {
        // Leer la imagen desde un array de bytes
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(originalImage));
        
        // Crear un ByteArrayOutputStream para almacenar la imagen comprimida
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        
        // Escribir la imagen en formato JPG en el ByteArrayOutputStream
        ImageIO.write(image, "jpg", compressed);
        
        // Retornar los bytes de la imagen comprimida
        return compressed.toByteArray();
    }
}
