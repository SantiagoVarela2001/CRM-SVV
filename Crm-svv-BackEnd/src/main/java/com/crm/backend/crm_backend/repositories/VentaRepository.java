package com.crm.backend.crm_backend.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.entities.Cliente;
import com.crm.backend.crm_backend.entities.ProductoVenta;
import com.crm.backend.crm_backend.entities.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v.cliente FROM Venta v WHERE v.id = :id AND v.empresa.id = :empresaId")
    Optional<Cliente> findClienteByVentaId(@Param("id") Long id, @Param("empresaId") Long empresaId);


    @Query("SELECT pv FROM Venta v JOIN v.productoVentas pv WHERE v.id = :ventaId AND v.empresa.id = :empresaId")
    List<ProductoVenta> findProductoVentaByVentaId(@Param("ventaId") Long ventaId, @Param("empresaId") Long empresaId);
    
    List<Venta> findByEmpresaId(Long empresaid);

    @Transactional
    @Query("SELECT v FROM Venta v WHERE v.empresa.id = :empresaId AND v.id = :ventaId")
    Optional<Venta> findById(@Param("ventaId") Long ventaId, @Param("empresaId") Long empresaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Venta v WHERE v.empresa.id = :empresaId AND v.id = :ventaId")
    void deleteById(@Param("ventaId") Long ventaId, @Param("empresaId") Long empresaId);

}
