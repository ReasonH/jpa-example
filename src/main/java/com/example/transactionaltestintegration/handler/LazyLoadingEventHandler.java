package com.example.transactionaltestintegration.handler;

import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyAsyncListener;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyAsyncTxListenerTx;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyTxListener;
import com.example.transactionaltestintegration.handler.event.lazyloading.LazyTxListenerTx;
import com.example.transactionaltestintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LazyLoadingEventHandler {
    private static final Logger log = LoggerFactory.getLogger(LazyLoadingEventHandler.class);

    private final UserRepository userRepository;
    @EventListener
    @Async
    public void onMyEvent(LazyAsyncListener event) {
        event.getPost().getUser().getName();
    }

    @TransactionalEventListener
    public void onMyEvent(LazyTxListener event) {
        event.getPost().getUser().getName();
    }

    @TransactionalEventListener
    @Transactional
    public void onMyEvent(LazyTxListenerTx event) {
        User user = event.getPost().getUser();
        user.setName("[New User]");
        userRepository.save(user);
    }

    @TransactionalEventListener
    @Async
    @Transactional
    public void onMyEvent(LazyAsyncTxListenerTx event) {
        event.getPost().getUser().getName();
    }
}
