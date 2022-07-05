package com.example.transactionaltestintegration.service.lazyloading;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.service.InitDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LazyLoadingOuterService {
    // TODO 비동기 메서드 레이지로딩
    // TODO 이벤트리스너 레이지로딩

    private final LazyLoadingInnerService lazyLoadingInnerService;
    private final InitDBService initDBService;

    public String osiv() {
        Post post = initDBService.initPost();
        Post findPost = lazyLoadingInnerService.getPost(post.getId());
        return findPost.getUser().getName();
    }

    public List<Post> lazyAndEager() {
        User user = initDBService.initUser();
        return lazyLoadingInnerService.getUserPostList(user.getId());
    }
}
