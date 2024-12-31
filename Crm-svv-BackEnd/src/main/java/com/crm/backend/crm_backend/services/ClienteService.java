package com.crm.backend.crm_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.backend.crm_backend.entities.Cliente;
import com.crm.backend.crm_backend.repositories.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> getClienteById(Long clienteId, Long empresaId) {
        return clienteRepository.findById(clienteId, empresaId);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public void deleteCliente(Long clienteId, Long empresaId) {
        clienteRepository.deleteById(clienteId, empresaId);
    }

    public List<Cliente> findByEmpresaId(Long empresaId) {
        return clienteRepository.findByEmpresaIdAndEliminadoFalse(empresaId);
    }
}