package com.example.employee.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmpleadoRequest(
        @NotBlank String codigo,
        @NotBlank String nombre,
        @Email @NotBlank String email
) {}
