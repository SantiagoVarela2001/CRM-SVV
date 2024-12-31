package com.crm.backend.crm_backend.services;

import org.springframework.stereotype.Service;

import com.crm.backend.crm_backend.dto.VentaDTO;
import com.crm.backend.crm_backend.entities.Venta;

@Service
public class ConvertVentaToVentaDTO {

    private final ProductoVentaService productoVentaService;

    public ConvertVentaToVentaDTO(ProductoVentaService productoVentaService) {
        this.productoVentaService = productoVentaService;
    }

    public VentaDTO convertirVentaDTO(Venta venta, Long empresaId) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }

        VentaDTO ventaDTO = new VentaDTO();
        if (venta.getCliente() != null) {
            ventaDTO.setClienteID(venta.getCliente().getId());
        }
        ventaDTO.setEstadoEnvio(venta.getEstadoEnvio());
        ventaDTO.setFecha(venta.getFecha());
        ventaDTO.setId(venta.getId());
        ventaDTO.setLinkFactura(venta.getLinkFactura());
        ventaDTO.setProductoVentas(productoVentaService.findByVentaId(venta.getId(), empresaId));

        return ventaDTO;
    }
}
