package com.crm.backend.crm_backend.dto;
public class ProductoVentaDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private int cantidadDelProducto;
    private double descuento;
    private double precioFinal;

    public ProductoVentaDTO() {}

    public ProductoVentaDTO(Long id, Long productoId, String productoNombre, int cantidadDelProducto, double descuento, double precioFinal) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidadDelProducto = cantidadDelProducto;
        this.descuento = descuento;
        this.precioFinal = precioFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public int getCantidadDelProducto() {
        return cantidadDelProducto;
    }

    public void setCantidadDelProducto(int cantidadDelProducto) {
        this.cantidadDelProducto = cantidadDelProducto;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
    
}