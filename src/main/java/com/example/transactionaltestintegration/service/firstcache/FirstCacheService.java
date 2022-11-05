package com.example.transactionaltestintegration.service.firstcache;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirstCacheService {

    private final PostRepository postRepository;

    @Transactional
    public void updateUniqueColumnAndSaveNewEntity(long id) {
        Post post = postRepository.findById(id).get();
        String content = post.getContent();
        post.setTitle("[Post]");
        post.setContent("[Content]");

        Post newPost = new Post("[New Post]");
        newPost.setContent(content);

        postRepository.save(newPost);
    }

    @Transactional
    public void updateUniqueColumnAndEarlySaveOldAndSaveNew(long id) {
        Post post = postRepository.findById(id).get();
        String content = post.getContent();
        post.setTitle("[Post]");
        post.setContent("[Content]");
        postRepository.save(post);

        Post newPost = new Post("[New Post]");
        newPost.setContent(content);

        postRepository.save(newPost);
    }

    @Transactional
    public void updateUniqueColumnAndEarlySaveFlushOldAndSaveNew(long id) {
        Post post = postRepository.findById(id).get();
        String content = post.getContent();
        post.setTitle("[Post]");
        post.setContent("[Content]");
        postRepository.saveAndFlush(post);

        Post newPost = new Post("[New Post]");
        newPost.setContent(content);

        postRepository.save(newPost);
    }
}
