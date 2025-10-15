package com.example.employee.web;

import com.example.employee.domain.Empleado;
import com.example.employee.service.EmpleadoService;
import com.example.employee.web.dto.EmpleadoRequest;
import com.example.employee.web.dto.EmpleadoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmpleadoResponse> listar() {
        return service.listar().stream()
                .map(e -> new EmpleadoResponse(e.getCodigo(), e.getNombre(), e.getEmail()))
                .toList();
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Validated @RequestBody EmpleadoRequest req) {
        Empleado creado = service.crear(new Empleado(req.codigo(), req.nombre(), req.email()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new EmpleadoResponse(creado.getCodigo(), creado.getNombre(), creado.getEmail()));
    }
}
