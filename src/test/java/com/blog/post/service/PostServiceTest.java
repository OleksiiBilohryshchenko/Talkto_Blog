package com.blog.post.service;

import com.blog.post.domain.Post;
import com.blog.post.repository.PostRepository;
import com.blog.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Test
    void create_success() {
        User author = new User("John", "mail", "pass");

        when(postRepository.save(any(Post.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Post post = postService.create("Title", "Content", author);

        assertThat(post.getTitle()).isEqualTo("Title");
        assertThat(post.getAuthor()).isEqualTo(author);

        verify(postRepository).save(any(Post.class));
    }

    @Test
    void create_authorNull() {
        assertThatThrownBy(() ->
                postService.create("Title", "Content", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_emptyTitle() {
        User author = new User("John", "mail", "pass");

        assertThatThrownBy(() ->
                postService.create("  ", "Content", author))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_emptyContent() {
        User author = new User("John", "mail", "pass");

        assertThatThrownBy(() ->
                postService.create("Title", " ", author))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findById_success() {
        User author = new User("John", "mail", "pass");
        Post post = new Post("Title", "Content", author);

        when(postRepository.findByIdWithAuthor(1L))
                .thenReturn(Optional.of(post));

        Post result = postService.findById(1L);

        assertThat(result).isEqualTo(post);
    }

    @Test
    void findById_notFound() {
        when(postRepository.findByIdWithAuthor(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByAuthorId_null() {
        assertThatThrownBy(() ->
                postService.findByAuthorId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByAuthorId_success() {
        when(postRepository.findByAuthorId(1L))
                .thenReturn(List.of());

        List<Post> posts = postService.findByAuthorId(1L);

        assertThat(posts).isEmpty();
    }
}