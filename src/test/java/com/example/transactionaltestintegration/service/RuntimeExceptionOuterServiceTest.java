package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.service.runtimeexeption.RuntimeExceptionOuterService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class RuntimeExceptionOuterServiceTest {
    @Autowired
    private RuntimeExceptionOuterService runtimeExceptionOuterService;
    @Autowired
    private PostRepository repository;

    @BeforeEach
    public void clear() {
        repository.deleteAllInBatch();
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
        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 던지는 일반 메서드 호출")
    public void nonTransactionalThrowingRuntimeEx() {
        runtimeExceptionOuterService.nonTransactionalThrowingRuntimeEx();
        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 일반 메서드 호출")
    public void nonTransactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.nonTransactionalCatchingRuntimeEx();
        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 던지는 @Transactional(REQUIRED_NEW) 메서드 호출")
    public void newTransactionalThrowingRuntimeEx() {
        runtimeExceptionOuterService.newTransactionalThrowingRuntimeEx();
        assertThat(repository.findAll().get(0).getTitle()).isEqualTo("[OUTER SERVICE]");
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 @Transactional(REQUIRED_NEW) 메서드 호출")
    public void newTransactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.newTransactionalCatchingRuntimeEx();
        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("런타임 예외를 던지는 @Transactional(NESTED) 메서드 호출")
    @Ignore
    public void nestedTransactionalThrowingRuntimeEx() {
        runtimeExceptionOuterService.nestedTransactionalThrowingRuntimeEx();
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("런타임 예외를 내부에서 처리하는 @Transactional(NESTED) 메서드 호출")
    @Ignore
    public void nestedTransactionalCatchingRuntimeEx() {
        runtimeExceptionOuterService.nestedTransactionalCatchingRuntimeEx();
        assertThat(repository.count()).isEqualTo(1);
    }
}
