package com.example.employee.repo;

import com.example.employee.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    boolean existsByCodigo(String codigo);
}
