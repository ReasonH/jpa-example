package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FetchJoinTest {

    @Autowired
    private InitDBService initDBService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear() {
        commentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Fetch join으로 가져왔을 때 Eager 필드는 어떻게 참조되지?")
    @Order(1)
    public void foo1() {
        User user = new User("[User]");
        user = userRepository.save(user);

        Post post = new Post("[Post]");
        post.setUser(user);
        postRepository.save(post);

        Comment comment = new Comment("[Comment]", "lee");
        comment.setPost(post);
        Comment result = commentRepository.save(comment);

        user.setComment(result);
        userRepository.save(user);

        System.out.println("==================");
        Comment queriedComment = commentRepository.findById2(result.getId()).orElse(null);
        System.out.println("==================");
        Comment queriedComment2 = commentRepository.findById(result.getId()).orElse(null);
        System.out.println("==================");
        System.out.println(queriedComment.getPost().getUser().getName());
        System.out.println(queriedComment2.getPost().getUser().getName());

    }
}
