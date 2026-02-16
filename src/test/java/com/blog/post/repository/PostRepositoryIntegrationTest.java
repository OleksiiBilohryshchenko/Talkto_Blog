package com.blog.post.repository;

import com.blog.AbstractIntegrationTest;
import com.blog.post.domain.Post;
import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PostRepositoryIntegrationTest extends AbstractIntegrationTest {

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
}