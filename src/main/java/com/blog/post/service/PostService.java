package com.blog.post.service;

import com.blog.post.domain.Post;
import com.blog.post.repository.PostRepository;
import com.blog.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post create(String title, String content, User author) {

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        Post post = new Post(title.trim(), content.trim(), author);

        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAllWithAuthor();
    }

    public Post findById(Long id) {
        return postRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public List<Post> findByAuthor(User author) {
        return postRepository.findAllWithAuthor()
                .stream()
                .filter(p -> p.getAuthor().equals(author))
                .toList();
    }
}