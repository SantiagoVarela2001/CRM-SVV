package com.crm.backend.crm_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.entities.Role;
import com.crm.backend.crm_backend.repositories.EmpresaRepository;
import com.crm.backend.crm_backend.repositories.RoleRepository;

import jakarta.transaction.Transactional;

@Service
public class EmpresaService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Empresa saveEmpresa(Empresa empresa) {
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        optionalRole.ifPresent(roles::add);

        if(empresa.isAdmin()){
            Optional<Role> optinalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
	        optinalRoleAdmin.ifPresent(roles::add);
        }

        empresa.setRoles(roles);
        empresa.setPasswordHash(passwordEncoder.encode(empresa.getPasswordHash()));
        return empresaRepository.save(empresa);
    }

    public boolean verificarUsuario(String email, String passwordIngresada) {
        Empresa empresa = empresaRepository.findByEmail(email);
        if (empresa != null) {
            return passwordEncoder.matches(passwordIngresada, empresa.getPasswordHash());
        }
        return false;
    }

    public Optional<Empresa> getEmpresaById(Long id) {
        return empresaRepository.findById(id);
    }

    public void deleteEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }

    public Empresa obtenerEmpresaPorEmail(String email) {
        return empresaRepository.findByEmail(email);
    }
}
