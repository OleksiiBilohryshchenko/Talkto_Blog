package com.blog.post.repository;

import com.blog.post.domain.Post;
import com.blog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    @Query("""
           SELECT p
           FROM Post p
           JOIN FETCH p.author
           ORDER BY p.createdAt DESC
           """)
    List<Post> findAllWithAuthor();
}