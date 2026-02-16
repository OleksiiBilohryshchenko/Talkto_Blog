package com.blog;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public abstract class AbstractFullIntegrationTest {

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
            PostgresTestContainer.INSTANCE::getJdbcUrl);
        registry.add("spring.datasource.username",
            PostgresTestContainer.INSTANCE::getUsername);
        registry.add("spring.datasource.password",
            PostgresTestContainer.INSTANCE::getPassword);
    }
}