package com.example.employee.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank
    private String codigo;

    @Column(nullable = false, length = 120)
    @NotBlank
    private String nombre;

    @Column(nullable = false, length = 200)
    @Email
    @NotBlank
    private String email;

    public Empleado() {}

    public Empleado(String codigo, String nombre, String email) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public void setId(Long id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
}
