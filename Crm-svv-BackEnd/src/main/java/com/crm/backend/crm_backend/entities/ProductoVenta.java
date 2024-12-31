package com.crm.backend.crm_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @JsonBackReference
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnore
    private Empresa empresa;

    @Positive(message = "La cantidad del producto debe ser mayor a 0.")
    private int cantidadDelProducto;

    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser menor a 0%.")
    @DecimalMax(value = "100.0", inclusive = true, message = "El descuento no puede ser mayor a 100%.")
    private double descuento;

    private double precioFinal;

    public ProductoVenta() {
    }

    public ProductoVenta(Producto producto, Venta venta, int cantidadDelProducto, double descuento, double precioFinal,
            Empresa empresa) {
        this.producto = producto;
        this.venta = venta;
        this.cantidadDelProducto = cantidadDelProducto;
        this.descuento = descuento;
        this.precioFinal = precioFinal;
        this.empresa = empresa;
    }
}
