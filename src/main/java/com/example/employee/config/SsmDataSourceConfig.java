package com.example.employee.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "app.db.resolveFromSsm", havingValue = "true", matchIfMissing = true)
public class SsmDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String env = System.getenv().getOrDefault("APP_ENV", "dev");
        String prefix = System.getenv().getOrDefault("APP_SSM_PREFIX", "myapp");

        String awsRegion = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
        SsmClient ssm = SsmClient.builder()
                .region(Region.of(awsRegion))
                .build();

        String base = "/" + prefix + "/" + env + "/db/";
        String url = getString(ssm, base + "url");
        String user = getString(ssm, base + "username");
        String pass = getString(ssm, base + "password");

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // Small pool defaults for POC
        cfg.setMaximumPoolSize(5);
        cfg.setMinimumIdle(1);
        return new HikariDataSource(cfg);
    }

    private String getString(SsmClient ssm, String name) {
        try {
            return ssm.getParameter(GetParameterRequest.builder()
                            .name(name)
                            .withDecryption(true)
                            .build())
                    .parameter()
                    .value();
        } catch (ParameterNotFoundException ex) {
            throw new IllegalStateException("SSM parameter not found: " + name, ex);
        }
    }
}
