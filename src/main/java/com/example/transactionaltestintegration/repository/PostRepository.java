package com.example.transactionaltestintegration.repository;

import com.example.transactionaltestintegration.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);

    @Query("select p from Post p join fetch p.user u where p.id = ?1")
    Post findByIdUserFetch(long id);

    @Query("select p from Post p where p.id = ?1")
    Post findByIdNotUserFetch(long id);
}
