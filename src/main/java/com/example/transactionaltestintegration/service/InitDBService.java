package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Category;
import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.CategoryRepository;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitDBService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

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
    public User initUser() {
        User user = userRepository.save(new User("[Init User]"));
        Post post1 = postRepository.save(new Post("[Init Post1]"));
        Post post2 = postRepository.save(new Post("[Init Post2]"));
        Post post3 = postRepository.save(new Post("[Init Post3]"));

        Category category = categoryRepository.save(new Category("[CATEGORY]"));

        post1.setCategory(category);
        post2.setCategory(category);
        post3.setCategory(category);
        post1.setUser(user);
        post2.setUser(user);
        post3.setUser(user);

        return user;
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
