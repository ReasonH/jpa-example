package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.*;
import com.example.transactionaltestintegration.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("Hello")
@RequiredArgsConstructor
@Slf4j
public class InitDBService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostForLazyRepository postForLazyRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final SectorRepository sectorRepository;

    @Transactional
    public Comment initCommentAndPost() {
        Post post = postRepository.save(new Post("[Init Post]"));

        Comment comment = new Comment("[Init Comment]", "Lee");
        comment.setPost(post);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Post initPostAndUser() {
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
    public PostForLazy initPostForLazyAndUser() {
        User user = userRepository.save(new User("[Init User]"));
        PostForLazy post = postForLazyRepository.save(new PostForLazy("[Init Post For Lazy Loading]"));
        post.setContent("[Init Content]");
        post.setUser(user);

        return post;
    }

    @Transactional
    public Comment initCommentAndPostAndUser() {
        User user = userRepository.save(new User("[Init User]"));
        Post post = postRepository.save(new Post("[Init Post]"));
        post.setUser(user);

        Comment comment = new Comment("[Init Comment]", "Lee");
        comment.setPost(post);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public long initPostAndSectorAndUser() {
        User user = userRepository.save(new User("[Init User]"));
        Sector sector1 = sectorRepository.save(new Sector("[Sector 1]"));
        Sector sector2 = sectorRepository.save(new Sector("[Sector 1]"));

        Post post1 = new Post("[Init Post 1]");
        post1.setUser(user);
        post1.setSector(sector1);
        postRepository.save(post1);

        Post post2 = new Post("[Init Post 2]");
        post2.setUser(user);
        post2.setSector(sector2);
        postRepository.save(post2);

        return user.getId();
    }
}
