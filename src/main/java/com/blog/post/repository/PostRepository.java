package com.blog.post.repository;

import com.blog.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("""
      SELECT p
      FROM Post p
      JOIN FETCH p.author
      WHERE p.id = :id
      """)
  Optional<Post> findByIdWithAuthor(@Param("id") Long id);

  @Query("""
      SELECT p
      FROM Post p
      JOIN FETCH p.author
      ORDER BY p.createdAt DESC
      """)
  List<Post> findAllWithAuthor();

  @Query("""
      SELECT p
      FROM Post p
      JOIN FETCH p.author
      WHERE p.author.id = :authorId
      ORDER BY p.createdAt DESC
      """)
  List<Post> findByAuthorId(@Param("authorId") Long authorId);

}