package com.example.employee.service;

import com.example.employee.domain.Empleado;
import com.example.employee.repo.EmpleadoRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmpleadoServiceTest {

    @Test
    void listar_delega_en_repo() {
        EmpleadoRepository repo = mock(EmpleadoRepository.class);
        when(repo.findAll()).thenReturn(List.of(new Empleado("C1","Alice","a@x.com")));
        EmpleadoService svc = new EmpleadoService(repo);

        var all = svc.listar();
        assertEquals(1, all.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void crear_valida_codigo_unico() {
        EmpleadoRepository repo = mock(EmpleadoRepository.class);
        when(repo.existsByCodigo("C1")).thenReturn(true);
        EmpleadoService svc = new EmpleadoService(repo);
        assertThrows(IllegalArgumentException.class, () -> svc.crear(new Empleado("C1","A","a@x.com")));
    }

    @Test
    void crear_guarda_si_no_existe_codigo() {
        EmpleadoRepository repo = mock(EmpleadoRepository.class);
        when(repo.existsByCodigo("C2")).thenReturn(false);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        EmpleadoService svc = new EmpleadoService(repo);

        var e = svc.crear(new Empleado("C2","B","b@x.com"));
        assertEquals("C2", e.getCodigo());
        verify(repo).save(any());
    }
}
