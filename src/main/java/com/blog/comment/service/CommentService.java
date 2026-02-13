package com.blog.comment.service;

import com.blog.comment.domain.Comment;
import com.blog.comment.repository.CommentRepository;
import com.blog.post.domain.Post;
import com.blog.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(String content, User author, Post post) {

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }

        Comment comment = new Comment();
        comment.setContent(content.trim());
        comment.setAuthor(author);
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }
}