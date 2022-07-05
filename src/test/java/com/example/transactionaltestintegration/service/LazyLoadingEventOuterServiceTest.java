package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.lazyloading.LazyLoadingEventOuterService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LazyLoadingEventOuterServiceTest {

    @Autowired
    private LazyLoadingEventOuterService lazyLoadingEventOuterService;
    @Autowired
    private InitDBService initDBService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("@EventListener 지연로딩 테스트")
    class EventListenerLazyLoading {

        @Order(1)
        @DisplayName("리스너 비동기, 트랜잭션 X, 내부에서 lazy loading 이 실패하지만 메인 스레드 함수에 영향을 주지 않는다.")
        @Test
        public void foo() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.asyncLazyLoadingEvent(id);
        }
    }

    @Nested
    @DisplayName("@TxEventListener 지연로딩 테스트")
    class TxEventListenerLazyLoading {

        @Order(2)
        @DisplayName("리스너 동기, 트랜잭션 X, lazy loading 가능")
        @Test
        public void bar1() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.lazyLoadingTxEvent(id);
        }

        @Order(3)
        @DisplayName("리스너 동기, 트랜잭션 O, lazy loading 가능")
        @Test
        public void bar2() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.txLazyLoadingTxEvent(id);
        }

        @Order(4)
        @DisplayName("리스너 비동기, 트랜잭션 O, 내부에서 lazy loading 실패, 그러나 예외가 메인 스레드 함수에 영향을 주지 않는다.")
        @Test
        public void bar3() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.asyncTxLazyLoadingTxEvent(id);
        }
    }
}