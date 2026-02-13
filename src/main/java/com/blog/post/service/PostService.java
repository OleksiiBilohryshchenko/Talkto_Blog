package com.blog.post.service;

import com.blog.post.domain.Post;
import com.blog.post.repository.PostRepository;
import com.blog.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post create(String title, String content, User author) {
        Post post = new Post(title, content, author);
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Post> findByAuthor(User author) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
}