package com.example.transactionaltestintegration.handler;

import com.example.transactionaltestintegration.handler.event.AsyncEvent;
import com.example.transactionaltestintegration.handler.event.SyncEvent;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventHandler {
    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public EventHandler(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @TransactionalEventListener
    @Async
    @Transactional
    public void onMyEvent(SyncEvent syncEvent) {
        log.info("InnerService Test {}", syncEvent.getPost().getUser().getName());
        syncEvent.getPost().setTitle("update title");
        syncEvent.getPost().getUser().setName("update name");
        log.info("InnerService Test {}", syncEvent.getPost().getUser().getName());
    }

    @TransactionalEventListener
    public void onMyEvent(AsyncEvent event) {
        log.info("InnerService Test {}", event.getPost().getUser().getName());
        event.getPost().setTitle("update title");
        event.getPost().getUser().setName("update name");
        log.info("InnerService Test {}", event.getPost().getUser().getName());
    }
}
