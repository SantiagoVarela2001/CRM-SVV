package com.crm.backend.crm_backend.dto;

import java.util.ArrayList;
import java.util.List;

import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.entities.Proveedor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


public class ProductoDTO {

    

    private Long id;
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnore
    private Empresa empresa;

    @ManyToOne
    @JsonBackReference
    private Proveedor proveedor;

    private List<ProductoVentaDTO> productoVentas = new ArrayList<>();

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String nombre, double precioCompra, double precioVenta, int stock, Proveedor proveedor, Empresa empresa, List<ProductoVentaDTO> productoVentas) {
        this.id = id;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.proveedor = proveedor;
        this.empresa = empresa;
        this.productoVentas = productoVentas;
    }

   

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<ProductoVentaDTO> getProductoVentas() {
        return productoVentas;
    }

    public void setProductoVentas(List<ProductoVentaDTO> productoVentas) {
        this.productoVentas = productoVentas;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", stock=" + stock +
                '}';
    }
}
