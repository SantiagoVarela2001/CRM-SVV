package com.crm.backend.crm_backend.service;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crm.backend.crm_backend.entities.Empresa;
import com.crm.backend.crm_backend.repositories.EmpresaRepository;
import com.crm.backend.crm_backend.repositories.RoleRepository;
import com.crm.backend.crm_backend.services.EmpresaService;

@SpringBootTest
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmpresaService empresaService;

    @BeforeEach
    void setUp() {
        // Inicializar los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmpresa() {
        // Preparar datos de prueba
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Nueva Empresa");
        empresa.setPasswordHash("password123");

        Empresa empresaGuardada = new Empresa();
        empresaGuardada.setId(1L);
        empresaGuardada.setNombre("Nueva Empresa");
        empresaGuardada.setPasswordHash("hashed_password123");

        // Simular encriptación de contraseña
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password123");

        // Simular guardar en el repositorio
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaGuardada);

        // Llamar al método que queremos probar
        Empresa resultado = empresaService.saveEmpresa(empresa);

        // Verificar resultados
        assertThat(resultado).isNotNull();
        assertThat(resultado.getPasswordHash()).isEqualTo("hashed_password123");

        // Verificar interacciones con los mocks
        verify(passwordEncoder, times(1)).encode("password123");
        verify(empresaRepository, times(1)).save(any(Empresa.class));
    }

    @Test
    void testVerificarUsuario_CredencialesCorrectas() {
        // Preparar datos de prueba
        Empresa empresa = new Empresa();
        empresa.setEmail("test@example.com");
        empresa.setPasswordHash("hashed_password");

        // Configurar mocks
        when(empresaRepository.findByEmail("test@example.com")).thenReturn(empresa);
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);

        // Llamar al método que queremos probar
        boolean resultado = empresaService.verificarUsuario("test@example.com", "password123");

        // Verificar resultados
        assertThat(resultado).isTrue();

        // Verificar interacciones con los mocks
        verify(empresaRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("password123", "hashed_password");
    }
}
