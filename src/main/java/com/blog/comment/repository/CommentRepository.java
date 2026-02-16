package com.blog.comment.repository;

import com.blog.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query("""
      SELECT c
      FROM Comment c
      JOIN FETCH c.author
      WHERE c.post.id = :postId
      ORDER BY c.createdAt ASC
      """)
  List<Comment> findByPostIdWithAuthor(@Param("postId") Long postId);
}