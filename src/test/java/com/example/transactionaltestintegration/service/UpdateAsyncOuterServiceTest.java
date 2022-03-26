package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.service.updateasync.UpdateAsyncOuterService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateAsyncOuterServiceTest {

    @Autowired
    private UpdateAsyncOuterService updateAsyncOuterService;
    @Autowired
    private InitDBService initDBService;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Order(1)
    @DisplayName("함수 내에서 스레드를 만들고 객체 수정하면 dirty-checking이 동작한다.")
    public void updateWithThreadAndWait() {
        long id = initDBService.initComment().getId();
        updateAsyncOuterService.updateWithThreadAndWait(id);

        assertThat(commentRepository.findById(id).get().getContent()).isEqualTo("[Thread Comment]");
    }

    @Test
    @Order(2)
    @DisplayName("스레드가 영속성 객체를 수정하는 함수를 호출, 영속성 컨텍스트가 전파되지 않으며, 내부에선 dirty-checking이 이루어지지 않는다.")
    public void updateWithFunctionInThread() {
        long id = initDBService.initComment().getId();
        updateAsyncOuterService.updateWithFunctionInThread(id);
        assertThat(commentRepository.findById(id).get().getContent()).isEqualTo("[Init Comment]");
    }

    @Test
    @Order(3)
    @DisplayName("스레드가 영속성 객체를 수정하는 함수를 호출, caller가 스레드를 대기한다면 caller 종료 시점에 dirty-checking이 동작한다.")
    public void updateWithFunctionInThreadAndWait() {
        long id = initDBService.initComment().getId();
        updateAsyncOuterService.updateWithFunctionInThreadAndWait(id);
        assertThat(commentRepository.findById(id).get().getContent()).isEqualTo("[Async Inner Service Comment]");
    }

    @Test
    @Order(4)
    @DisplayName("스레드에서 호출한 함수가 @Transactional인 경우 새로운 트랜잭션을 시작한다.")
    public void updateWithTransactionalFunctionInThreadAndWait() {
        long id = initDBService.initComment().getId();
        updateAsyncOuterService.updateWithTransactionalFunctionInThreadAndWait(id);
        assertThat(commentRepository.findById(id).get().getContent()).isEqualTo("[Async Inner Service Comment]");
    }

    @Test
    @Order(5)
    @DisplayName("Async 함수에서 업데이트 수행, caller가 비동기 호출을 대기하는 경우 종료되면서 dirty-checking이 동작한다.")
    public void updateWithAsyncFunction() {
        long id = initDBService.initComment().getId();
        updateAsyncOuterService.updateWithAsyncFunction(id);
        assertThat(commentRepository.findById(id).get().getContent()).isEqualTo("[Async Inner Service Comment]");
    }
}