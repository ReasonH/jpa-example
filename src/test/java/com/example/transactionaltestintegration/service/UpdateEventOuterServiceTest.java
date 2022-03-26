package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.service.updateevent.UpdateEventOuterService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateEventOuterServiceTest {

    @Autowired
    private InitDBService initDBService;
    @Autowired
    private UpdateEventOuterService updateEventOuterService;
    @Autowired
    private CommentRepository commentRepository;

    @Nested
    @DisplayName("동기식 @EventListenr")
    class syncEventListener {

        @Test
        @DisplayName("caller 함수 non Tx, 이벤트는 non Tx인 경우 수정 내역이 DB에 반영되지 않음")
        @Order(1)
        public void updateByEventNonTx() {
            long id = initDBService.initComment().getId();
            updateEventOuterService.updateByEventNonTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }

        @Test
        @DisplayName("caller 함수 non Tx, 이벤트는 Tx인 경우 수정 내역이 DB에 반영되지 않음")
        @Order(2)
        public void updateByEventTx() {
            long id = initDBService.initComment().getId();
            updateEventOuterService.updateByEventTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }

        @Test
        @DisplayName("caller 함수 non Tx, 이벤트는 Tx 이며 내부에서 값을 조회 후 수정하는 경우 내역이 DB에 반영")
        @Order(3)
        public void updateByEventWithIdTx() {
            long id = initDBService.initComment().getId();
            updateEventOuterService.updateByEventWithIdTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Event Service Comment]");
        }
    }

    @Nested
    @DisplayName("동기식 @TxEventListener, caller는 무조건적으로 Tx")
    class syncTransactionalEventListener {

        @Test
        @DisplayName("TxListener 이벤트에서 엔티티를 바로 수정하는 경우, Tx 유무, 전파레벨 관걔없이 수정 값이 DB에 반영되지 않는다.")
        @Order(4)
        public void updateByTxListenerNonTx() {
            long id = initDBService.initComment().getId();
            updateEventOuterService.updateByTxListenerNonTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }

        @Test
        @DisplayName("new Tx를 사용하면서 내부에서 값을 조회 후 수정하는 경우만 DB에 반영됨")
        @Order(5)
        public void updateByTxListenerTxWithId() {
            long id = initDBService.initComment().getId();
            updateEventOuterService.updateByTxListenerTxWithId(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Event Service Comment]");
        }
    }
}