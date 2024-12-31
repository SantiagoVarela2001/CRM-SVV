package com.crm.backend.crm_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.entities.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    List<Proveedor> findByEmpresaId(Long empresaid);

    @Transactional
    @Query("SELECT p FROM Proveedor p WHERE p.empresa.id = :empresaId AND p.id = :proveedorId")
    Optional<Proveedor> findById(@Param("proveedorId") Long proveedorId, @Param("empresaId") Long empresaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Proveedor p WHERE p.empresa.id = :empresaId AND p.id = :proveedorId")
    void deleteById(@Param("proveedorId") Long proveedorId, @Param("empresaId") Long empresaId);
}
