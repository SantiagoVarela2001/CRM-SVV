package com.crm.backend.crm_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Transactional(readOnly = true)
    List<Cliente> findByEmpresaIdAndEliminadoFalse(Long empresaId);

    Optional<Cliente> findByIdAndEmpresaId(Long clienteId, Long empresaId);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM Cliente c WHERE c.empresa.id = :empresaId AND c.id = :clienteId")
    Optional<Cliente> findById(@Param("clienteId") Long clienteId, @Param("empresaId") Long empresaId);

    @Modifying
    @Transactional
    @Query("UPDATE Cliente c SET eliminado = true WHERE c.empresa.id = :empresaId AND c.id = :clienteId")
    void deleteById(@Param("clienteId") Long clienteId, @Param("empresaId") Long empresaId);

}
