package com.blog.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
class UserRepositoryIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("talkto_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void save_and_findByEmail() {
        var user = new com.blog.user.domain.User("John", "john@mail.com", "password123");

        userRepository.save(user);

        var found = userRepository.findByEmail("john@mail.com");

        assertThat(found).isPresent();
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        var user = new com.blog.user.domain.User("John", "exists@mail.com", "password123");
        userRepository.save(user);

        assertThat(userRepository.existsByEmail("exists@mail.com")).isTrue();
    }

    @Test
    void email_shouldBeUnique() {
        var u1 = new com.blog.user.domain.User("John", "unique@mail.com", "password123");
        userRepository.save(u1);

        var u2 = new com.blog.user.domain.User("Another", "unique@mail.com", "anotherPass");

        assertThatThrownBy(() -> userRepository.saveAndFlush(u2))
                .isInstanceOf(Exception.class);
    }
}