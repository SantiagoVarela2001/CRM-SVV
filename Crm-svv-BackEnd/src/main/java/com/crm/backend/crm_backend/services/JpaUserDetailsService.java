package com.crm.backend.crm_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.repositories.EmpresaRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private EmpresaRepository repository;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<Empresa> empresaOptional = repository.findByNombre(username);

        if (empresaOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }
        Empresa empresa = empresaOptional.orElseThrow();

        List<GrantedAuthority> authorities = empresa.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(empresa.getNombre(),
                empresa.getPasswordHash(),
                empresa.isEnabled(),
                true,
                true,
                true,
                authorities);
    }

}
