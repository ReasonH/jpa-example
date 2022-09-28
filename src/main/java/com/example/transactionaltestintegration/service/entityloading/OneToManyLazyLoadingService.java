package com.example.transactionaltestintegration.service.entityloading;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OneToManyLazyLoadingService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void foo(long id) {
        User findUser = userRepository.findById(id).get();
        List<Post> postList = findUser.getPostList();
        for (Post post : postList) {
            System.out.println(post.getTitle());
        }
    }

    @Transactional
    public String foo2(long postId) {
        Post post = postRepository.findById(postId).get();
        return post.getUser().getName();
    }
}
