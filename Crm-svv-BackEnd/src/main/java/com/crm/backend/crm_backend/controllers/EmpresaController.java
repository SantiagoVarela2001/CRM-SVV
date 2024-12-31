package com.crm.backend.crm_backend.controllers;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;

import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.services.EmpresaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "")
@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Empresa>> getEmpresaById(@PathVariable Long id) {
        Optional<Empresa> empresaDb = empresaService.getEmpresaById(id);

        if (empresaDb.isPresent()) {
            return ResponseEntity.ok(empresaDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping
    private ResponseEntity<?> create(@Valid @RequestBody Empresa empresa, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.saveEmpresa(empresa));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> register(@Valid @RequestBody Empresa empresa, BindingResult result) {
        empresa.setAdmin(false);
        return create(empresa, result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Empresa empresa) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(empresa.getNombre(), empresa.getPasswordHash()));
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/buscarPorEmail")
    public Empresa buscarPorEmail(@RequestParam String email) {
        return empresaService.obtenerEmpresaPorEmail(email);
    }

}
