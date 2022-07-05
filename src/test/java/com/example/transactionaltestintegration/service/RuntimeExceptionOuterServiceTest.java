package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.service.runtimeexeption.RuntimeExceptionOuterService;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class RuntimeExceptionOuterServiceTest {
    @Autowired
    private RuntimeExceptionOuterService runtimeExceptionOuterService;
    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void clear() {
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("런타임 예외를 던지는 @Transactional 메서드 호출")
    public void transactionalThrowingRuntimeEx() {

        Assertions.assertThatThrownBy(() -> runtimeExceptionOuterService.transactionalThrowingRuntimeEx())
                .isInstanceOf(UnexpectedRollbackException.class);
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 @Transactional 메서드 호출")
    public void transactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.transactionalCatchingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 던지는 일반 메서드 호출")
    public void nonTransactionalThrowingRuntimeEx() {
        runtimeExceptionOuterService.nonTransactionalThrowingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 일반 메서드 호출")
    public void nonTransactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.nonTransactionalCatchingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 던지는 @Transactional(REQUIRES_NEW) 메서드 호출")
    public void newTransactionalThrowingRuntimeEx() {
        runtimeExceptionOuterService.newTransactionalThrowingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo("[OUTER SERVICE]");
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 @Transactional(REQUIRES_NEW) 메서드 호출")
    public void newTransactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.newTransactionalCatchingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 던지는 @Transactional(NESTED) 메서드 호출")
    @Disabled
    public void nestedTransactionalThrowingRuntimeEx() {
        runtimeExceptionOuterService.nestedTransactionalThrowingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 @Transactional(NESTED) 메서드 호출")
    @Disabled
    public void nestedTransactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.nestedTransactionalCatchingRuntimeEx();
        assertThat(postRepository.count()).isEqualTo(1);
    }
}
