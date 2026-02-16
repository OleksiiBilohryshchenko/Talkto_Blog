package com.blog.user.repository;

import com.blog.AbstractIntegrationTest;
import com.blog.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_and_findByEmail() {
        User user = new User("John", "john@mail.com", "password123");

        userRepository.save(user);

        var found = userRepository.findByEmail("john@mail.com");

        assertThat(found).isPresent();
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        User user = new User("John", "exists@mail.com", "password123");
        userRepository.save(user);

        assertThat(userRepository.existsByEmail("exists@mail.com")).isTrue();
    }

    @Test
    void email_shouldBeUnique() {
        User u1 = new User("John", "unique@mail.com", "password123");
        userRepository.save(u1);

        User u2 = new User("Another", "unique@mail.com", "anotherPass");

        assertThatThrownBy(() -> userRepository.saveAndFlush(u2))
            .isInstanceOf(Exception.class);
    }
}