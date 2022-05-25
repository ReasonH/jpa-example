package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Temp {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Temp(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

//    @Transactional(readOnly = false)
    public void hello() {
        System.out.println("=======1");
        System.out.println(commentRepository.findById(1L).get().getContent());
        System.out.println("=======2");
        System.out.println(commentRepository.findById(1L).get().getPost().getTitle());
        System.out.println("=======3");
        System.out.println(commentRepository.findById(1L).get().getPost().getUser().getName());
    }

    @Transactional(readOnly = true)
    public void hello2() {
        System.out.println(postRepository.findById(1L).get().getTitle());
    }
}
