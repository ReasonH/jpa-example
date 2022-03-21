package com.example.transactionaltestintegration.repository;

import com.example.transactionaltestintegration.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
