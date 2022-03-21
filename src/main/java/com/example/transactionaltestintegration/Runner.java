package com.example.transactionaltestintegration;

import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.exceptionhandling.OuterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {

    private final OuterService outerService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public Runner(OuterService outerService, PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {

        this.outerService = outerService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {

    }
}
