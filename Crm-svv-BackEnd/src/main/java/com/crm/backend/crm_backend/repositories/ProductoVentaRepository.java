package com.crm.backend.crm_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.entities.ProductoVenta;

@Repository
public interface ProductoVentaRepository extends JpaRepository<ProductoVenta, Long> {

    List<ProductoVenta> findByProductoId(Long productoId);

    List<ProductoVenta> findByEmpresaId(Long empresaid);

    List<ProductoVenta> findByProductoIdAndEmpresaId(Long productoId, Long empresaId);

    @Query("SELECT pv FROM ProductoVenta pv WHERE pv.venta.id = :ventaId AND pv.empresa.id = :empresaId")
    List<ProductoVenta> findByVentaId(@Param("ventaId") Long ventaId, @Param("empresaId") Long empresaId);

    @Transactional
    @Query("SELECT pv FROM ProductoVenta pv WHERE pv.empresa.id = :empresaId AND pv.id = :productoventaId")
    Optional<ProductoVenta> findById(@Param("productoventaId") Long productoventaId, @Param("empresaId") Long empresaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductoVenta pv WHERE pv.empresa.id = :empresaId AND pv.id = :productoventaId")
    void deleteById(@Param("productoventaId") Long productoventaId, @Param("empresaId") Long empresaId);
}
