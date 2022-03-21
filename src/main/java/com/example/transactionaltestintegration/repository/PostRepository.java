package com.example.transactionaltestintegration.repository;

import com.example.transactionaltestintegration.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
