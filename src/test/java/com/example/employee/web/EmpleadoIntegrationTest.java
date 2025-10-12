package com.example.employee.web;

import com.example.employee.EmployeeServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = EmployeeServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "app.db.resolveFromSsm=false" }  // Desactiva SSM en tests
)
public class EmpleadoIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.3")
            .withDatabaseName("appdb")
            .withUsername("root")
            .withPassword("secret");

    @DynamicPropertySource
    static void configureProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Test
    void post_y_get_empleados() {
        var payload = Map.of(
                "codigo","EMP001",
                "nombre","Alice",
                "email","alice@example.com"
        );
        ResponseEntity<String> postResp = rest.postForEntity("http://localhost:" + port + "/api/empleados", payload, String.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> getResp = rest.getForEntity("http://localhost:" + port + "/api/empleados", String.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody()).contains("EMP001").contains("Alice").contains("alice@example.com");
    }
}
