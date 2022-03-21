package com.example.transactionaltestintegration.service.exceptionhandling;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OuterService {
    private final NonTransactionalInnerService nonTransactionalInnerService;
    private final TransactionalInnerService transactionalInnerService;
    private final NestedTransactionalInnerService nestedTransactionalInnerService;
    private final NewTransactionalInnerService newTransactionalInnerService;
    private final PostRepository postRepository;

    private static final Logger log = LoggerFactory.getLogger(OuterService.class);

    public OuterService(NonTransactionalInnerService nonTransactionalInnerService, TransactionalInnerService transactionalInnerService,
                        NestedTransactionalInnerService nestedTransactionalInnerService, NewTransactionalInnerService newTransactionalInnerService,
                        PostRepository postRepository) {
        this.nonTransactionalInnerService = nonTransactionalInnerService;
        this.transactionalInnerService = transactionalInnerService;
        this.nestedTransactionalInnerService = nestedTransactionalInnerService;
        this.newTransactionalInnerService = newTransactionalInnerService;
        this.postRepository = postRepository;
    }

    @Transactional
    public void nonTransactionalThrowingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            nonTransactionalInnerService.innerMethodThrowingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void nonTransactionalCatchingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            nonTransactionalInnerService.innerMethodCatchingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void transactionalThrowingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            transactionalInnerService.innerMethodThrowingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void transactionalCatchingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            transactionalInnerService.innerMethodCatchingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void nestedTransactionalThrowingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            nestedTransactionalInnerService.innerMethodThrowingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void nestedTransactionalCatchingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            nestedTransactionalInnerService.innerMethodCatchingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void newTransactionalThrowingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            newTransactionalInnerService.innerMethodThrowingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }

    @Transactional
    public void newTransactionalCatchingRuntimeEx() {
        try {
            postRepository.save(new Post("[OUTER SERVICE]"));
            newTransactionalInnerService.innerMethodCatchingRuntimeEx();
        } catch (RuntimeException ex) {
            log.warn("OuterService caught exception at outer. ex:{}", ex.getMessage());
        }
    }
}
