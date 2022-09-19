package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.Sector;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.SectorRepository;
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
