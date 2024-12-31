package com.crm.backend.crm_backend.controllers;

import com.crm.backend.crm_backend.entities.Cliente;
import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.services.ClienteService;
import com.crm.backend.crm_backend.services.EmpresaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Cliente> getAllClientes(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        List<Cliente> clientes = clienteService.findByEmpresaId(empresaId);
        return clientes;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Optional<Cliente>> getClienteById(@PathVariable Long clienteId, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Cliente> cliente = clienteService.getClienteById(clienteId, empresaId);

        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PostMapping
    public Cliente createCliente(@Valid @RequestBody Cliente cliente, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Empresa> empresa = empresaService.getEmpresaById(empresaId);

        if (empresa.isPresent()) {
            cliente.setEmpresa(empresa.get());
        }

        return clienteService.saveCliente(cliente);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> updateCliente(@Valid @PathVariable Long clienteId, @RequestBody Cliente clienteDetails, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        Optional<Cliente> existingCliente = clienteService.getClienteById(clienteId, empresaId);

        if (existingCliente.isPresent()) {
            Cliente clientedb = existingCliente.get();

            clientedb.setNombre(clienteDetails.getNombre());
            clientedb.setDireccion(clienteDetails.getDireccion());
            clientedb.setEmail(clienteDetails.getEmail());
            clientedb.setPais(clienteDetails.getPais());
            clientedb.setTelefono(clienteDetails.getTelefono());
            clientedb.setVentas(clienteDetails.getVentas());

            Cliente updatedCliente = clienteService.saveCliente(clientedb);
            return ResponseEntity.ok(updatedCliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long clienteId, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresa id");
        clienteService.deleteCliente(clienteId, empresaId);
        return ResponseEntity.noContent().build();
    }
}
