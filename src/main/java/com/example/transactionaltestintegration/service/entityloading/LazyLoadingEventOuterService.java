package com.example.transactionaltestintegration.service.entityloading;

import com.example.transactionaltestintegration.entity.PostForLazy;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyAsyncListener;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyAsyncTxListenerTx;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyTxListener;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyTxListenerTx;
import com.example.transactionaltestintegration.repository.PostForLazyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LazyLoadingEventOuterService {

    private final ApplicationEventPublisher eventPublisher;
    private final PostForLazyRepository postForLazyRepository;

    public void asyncLazyLoadingEvent(long id) {
        PostForLazy post = postForLazyRepository.findById(id).get();
        eventPublisher.publishEvent(new LazyAsyncListener(post));
    }

    @Transactional
    public void lazyLoadingTxEvent(long id) {
        PostForLazy post = postForLazyRepository.findById(id).get();
        eventPublisher.publishEvent(new LazyTxListener(post));
    }

    @Transactional
    public void txLazyLoadingTxEvent(long id) {
        PostForLazy post = postForLazyRepository.findById(id).get();
        eventPublisher.publishEvent(new LazyTxListenerTx(post));
    }

    @Transactional
    public void asyncTxLazyLoadingTxEvent(long id) {
        PostForLazy post = postForLazyRepository.findById(id).get();
        eventPublisher.publishEvent(new LazyAsyncTxListenerTx(post));
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
    }

}
