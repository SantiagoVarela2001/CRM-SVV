package com.crm.backend.crm_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.entities.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByEmpresaId(Long empresaid);

    @Transactional
    @Query("SELECT p FROM Producto p WHERE p.empresa.id = :empresaId AND p.id = :productoId")
    Optional<Producto> findById(@Param("productoId") Long productoId, @Param("empresaId") Long empresaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Producto p WHERE p.empresa.id = :empresaId AND p.id = :productoId")
    void deleteById(@Param("productoId") Long productoId, @Param("empresaId") Long empresaId);
}
