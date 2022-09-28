package com.example.transactionaltestintegration.service.entityloading;

import com.example.transactionaltestintegration.entity.PostForLazy;
import com.example.transactionaltestintegration.service.InitDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LazyLoadingOuterService {

    private final LazyLoadingInnerService lazyLoadingInnerService;
    private final InitDBService initDBService;

    public String osiv() {
        PostForLazy post = initDBService.initPostForLazyAndUser();
        PostForLazy findPost = lazyLoadingInnerService.getPost(post.getId());
        return findPost.getUser().getName();
    }
}
