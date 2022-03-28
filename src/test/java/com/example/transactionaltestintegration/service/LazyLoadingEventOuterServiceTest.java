package com.example.transactionaltestintegration.service;

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

    @Nested
    @DisplayName("@EventListener 지연로딩 테스트")
    class EventListenerLazyLoading {

        @Order(1)
        @DisplayName("리스너는 비동기 O, 트랜잭션 X, 내부에서 lazyLoading이 실패하지만 메인 스레드 함수에 영향을 주지 않는다.")
        @Test
        public void foo3() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.asyncLazyLoadingEvent(id);
        }
    }

    @Nested
    @DisplayName("@TxEventListener 지연로딩 테스트, caller는 Tx")
    class TxEventListenerLazyLoading {

        @Order(2)
        @DisplayName("리스너는 비동기 X, 트랜잭션 X")
        @Test
        public void foo() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.lazyLoadingTxEvent(id);
        }

        @Order(3)
        @DisplayName("리스너는 비동기 X, 트랜잭션 O")
        @Test
        public void foo2() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.txLazyLoadingTxEvent(id);
        }

        @Order(4)
        @DisplayName("리스너는 비동기 O, 트랜잭션 O, 내부에서 lazyLoading이 실패하지만 메인 스레드 함수에 영향을 주지 않는다.")
        @Test
        public void foo3() {
            long id = initDBService.initPost().getId();
            lazyLoadingEventOuterService.asyncTxLazyLoadingTxEvent(id);
        }
    }
}