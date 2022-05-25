package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("Hello")
@RequiredArgsConstructor
@Slf4j
public class InitDBService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment initComment() {
        Post post = postRepository.save(new Post("[Init Post]"));

        Comment comment = new Comment("[Init Comment]", "Lee");
        comment.setPost(post);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Post initPost() {
        User user = userRepository.save(new User("[Init User]"));
        Post post = postRepository.save(new Post("[Init Post]"));
        post.setContent("[Init Content]");
        post.setUser(user);

        return post;
    }

    @Transactional
    public Comment init() {
        User user = userRepository.save(new User("[Init User]"));
        Post post = postRepository.save(new Post("[Init Post]"));
        post.setUser(user);

        Comment comment = new Comment("[Init Comment]", "Lee");
        comment.setPost(post);
        commentRepository.save(comment);

        log.info("hashcode COMMENT: {}, POST: {}", comment.hashCode(), post.hashCode());
        log.info("title: {}, comment: {}", post.getTitle(), comment.getContent());
        return comment;
    }
}
