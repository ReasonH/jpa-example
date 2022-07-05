package com.example.transactionaltestintegration.service.lazyloading;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.service.InitDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LazyLoadingController {

    private final LazyLoadingInnerService lazyLoadingInnerService;
    private final InitDBService initDBService;

    @GetMapping("/osiv")
    public String lazyLoadingController() {
        Post post = initDBService.initPost();
        Post findPost = lazyLoadingInnerService.getPost(post.getId());
        return findPost.getUser().getName();
    }
}
