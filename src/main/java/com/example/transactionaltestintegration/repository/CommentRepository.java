package com.example.transactionaltestintegration.repository;

import com.example.transactionaltestintegration.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.post p " +
            " where c.id = ?1 ")
    Optional<Comment> findById2(Long id);

    Optional<Comment> findById(Long id);
}
