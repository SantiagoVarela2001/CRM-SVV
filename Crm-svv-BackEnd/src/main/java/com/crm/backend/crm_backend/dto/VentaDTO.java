package com.crm.backend.crm_backend.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDTO {

    private Long id;

    private Date fecha;
    private String linkFactura;
    private String estadoEnvio;
    private double precioFinal;

    private long clienteID;

    private List<ProductoVentaDTO> productoVentas = new ArrayList<>();


    public VentaDTO() {}

    public VentaDTO(Long id, Long clienteID, Date fecha, String linkFactura, String estadoEnvio, double precioFinal, ProductoVentaDTO productoVentaDTO) {
        this.id = id;
        this.clienteID = clienteID;
        this.fecha = fecha;
        this.linkFactura = linkFactura;
        this.precioFinal = precioFinal;
        this.estadoEnvio = estadoEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getLinkFactura() {
        return linkFactura;
    }

    public void setLinkFactura(String linkFactura) {
        this.linkFactura = linkFactura;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public long getClienteID() {
        return clienteID;
    }

    public void setClienteID(long clienteID) {
        this.clienteID = clienteID;
    }

    public List<ProductoVentaDTO> getProductoVentas() {
        return productoVentas;
    }

    public void setProductoVentas(List<ProductoVentaDTO> productoVentas) {
        this.productoVentas = productoVentas;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }


   
}
