package com.crm.backend.crm_backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@ToString(exclude = "productoVentas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha no puede estar vacía.")
    private Date fecha;

    @NotBlank(message = "La factura no puede estar vacía.")
    private String linkFactura;

    @NotBlank(message = "El estado de envio no puede estar vací0.")
    @Size(max = 100, message = "El estado de envio no puede exceder 100 caracteres.")
    private String estadoEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnore
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<ProductoVenta> productoVentas = new ArrayList<>();

    public Venta() {
    }

    //La venta esta mas pensada como "ticket" ya que no contiene la venta en si sino los datos de la misma sin el precio

    public Venta(Cliente cliente, Date fecha, String linkFactura, String estadoEnvio,
            Empresa empresa) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.linkFactura = linkFactura;
        this.estadoEnvio = estadoEnvio;
        this.empresa = empresa;
    }
}
