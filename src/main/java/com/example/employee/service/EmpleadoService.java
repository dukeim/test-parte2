package com.example.employee.service;

import com.example.employee.domain.Empleado;
import com.example.employee.repo.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService {
    private final EmpleadoRepository repo;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listar() {
        return repo.findAll();
    }

    @Transactional
    public Empleado crear(Empleado e) {
        if (repo.existsByCodigo(e.getCodigo())) {
            throw new IllegalArgumentException("El código ya existe: " + e.getCodigo());
        }
        return repo.save(e);
    }
}
