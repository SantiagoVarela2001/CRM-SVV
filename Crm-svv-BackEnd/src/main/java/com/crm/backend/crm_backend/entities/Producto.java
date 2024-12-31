package com.crm.backend.crm_backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres.")
    private String nombre;

    @Positive(message = "El precio de compra debe ser mayor a 0.")
    private double precioCompra;

    @Positive(message = "El precio de venta debe ser mayor a 0.")
    private double precioVenta;

    //@Positive(message = "El stock debe ser mayor a 0.")
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnore
    private Empresa empresa;

    @Lob
    private byte[] imagen;

    @ManyToOne
    @JsonBackReference
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductoVenta> productoVentas = new ArrayList<>();

    public Producto() {
    }

    public Producto(String nombre, double precioCompra, double precioVenta, int stock, Proveedor proveedor,
            byte[] imagen, Empresa empresa) {
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.proveedor = proveedor;
        this.imagen = imagen;
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precioCompra=" + precioCompra + ", precioVenta="
                + precioVenta + ", stock=" + stock + ", empresa=" + empresa + "]";
    }

    
}
