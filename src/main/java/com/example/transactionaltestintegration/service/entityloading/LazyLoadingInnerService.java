package com.example.transactionaltestintegration.service.entityloading;

import com.example.transactionaltestintegration.entity.PostForLazy;
import com.example.transactionaltestintegration.repository.PostForLazyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LazyLoadingInnerService {

    private final PostForLazyRepository postForLazyRepository;

    @Transactional
    public PostForLazy getPost(Long id) {
        return postForLazyRepository.findById(id).get();
    }
}
