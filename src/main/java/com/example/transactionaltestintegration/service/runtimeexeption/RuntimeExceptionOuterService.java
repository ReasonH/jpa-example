package com.example.transactionaltestintegration.service.runtimeexeption;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RuntimeExceptionOuterService {
    private final NonTransactionalInnerService nonTransactionalInnerService;
    private final TransactionalInnerService transactionalInnerService;
    private final NestedTransactionalInnerService nestedTransactionalInnerService;
    private final NewTransactionalInnerService newTransactionalInnerService;
    private final PostRepository postRepository;

    private static final Logger log = LoggerFactory.getLogger(RuntimeExceptionOuterService.class);

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
}
