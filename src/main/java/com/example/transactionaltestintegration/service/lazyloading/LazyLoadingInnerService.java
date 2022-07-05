package com.example.transactionaltestintegration.service.lazyloading;

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
public class LazyLoadingInnerService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post getPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Transactional
    public List<Post> getUserPostList(Long id) {
        User user = userRepository.findById(id).get();
        user.getPostList().size();
        return user.getPostList();
    }
}
