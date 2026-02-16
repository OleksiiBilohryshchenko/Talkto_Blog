package com.blog.post.repository;

import com.blog.post.domain.Post;
import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
class PostRepositoryIntegrationTest {

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
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void findByIdWithAuthor_shouldReturnPostWithAuthor() {
        User user = userRepository.save(
                new User("John", "author@mail.com", "password123")
        );

        Post post = postRepository.save(
                new Post("Title", "Content", user)
        );

        var found = postRepository.findByIdWithAuthor(post.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getAuthor()).isNotNull();
        assertThat(found.get().getAuthor().getEmail())
                .isEqualTo("author@mail.com");
    }

    @Test
    void findAllWithAuthor_shouldReturnSorted() {
        User user = userRepository.save(
                new User("John", "sort@mail.com", "password123")
        );

        postRepository.save(new Post("First", "Content1", user));
        postRepository.save(new Post("Second", "Content2", user));

        List<Post> posts = postRepository.findAllWithAuthor();

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getCreatedAt())
                .isAfterOrEqualTo(posts.get(1).getCreatedAt());
    }

    @Test
    void findByAuthorId_shouldReturnOnlyAuthorPosts() {
        User user1 = userRepository.save(
                new User("John", "john@mail.com", "password123")
        );

        User user2 = userRepository.save(
                new User("Other", "other@mail.com", "password123")
        );

        postRepository.save(new Post("Post1", "C1", user1));
        postRepository.save(new Post("Post2", "C2", user2));

        List<Post> posts = postRepository.findByAuthorId(user1.getId());

        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getAuthor().getId())
                .isEqualTo(user1.getId());
    }


}