package com.example.transactionaltestintegration.repository;

import com.example.transactionaltestintegration.entity.PostForLazy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostForLazyRepository extends JpaRepository<PostForLazy, Long> {
}
