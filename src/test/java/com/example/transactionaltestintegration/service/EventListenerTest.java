package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostForLazyRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.entityloading.LazyLoadingEventOuterService;
import com.example.transactionaltestintegration.service.entityloading.OneToManyLazyLoadingService;
import com.example.transactionaltestintegration.service.updateevent.UpdateEventOuterService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventListenerTest {

    @Autowired
    private UpdateEventOuterService updateEventOuterService;
    @Autowired
    private LazyLoadingEventOuterService lazyLoadingEventOuterService;
    @Autowired
    private InitDBService initDBService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostForLazyRepository postForLazyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void clear() {
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        postForLazyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("@EventListener 지연로딩 테스트")
    class EventListenerLazyLoading {

        @Order(1)
        @DisplayName("리스너 비동기, 트랜잭션 X, 내부에서 lazy loading 이 실패하지만 메인 스레드 함수에 영향을 주지 않는다.")
        @Test
        public void test() {
            long id = initDBService.initPostForLazyAndUser().getId();
            lazyLoadingEventOuterService.asyncLazyLoadingEvent(id);
        }
    }

    @Nested
    @DisplayName("@TransactionalEventListener 지연로딩 테스트, caller는 Tx")
    class TransactionalEventListenerLazyLoading {

        @Order(2)
        @DisplayName("리스너 동기, 트랜잭션 X, lazy loading 가능")
        @Test
        public void test1() {
            long id = initDBService.initPostForLazyAndUser().getId();
            lazyLoadingEventOuterService.lazyLoadingTxEvent(id);
        }

        @Order(3)
        @DisplayName("리스너 동기, 트랜잭션 O, lazy loading 가능, 쓰기작업 불가능")
        @Test
        public void test2() {
            long id = initDBService.initPostForLazyAndUser().getId();
            lazyLoadingEventOuterService.txLazyLoadingTxEvent(id);
            Assertions.assertThat(userRepository.findByName("[New User]")).isEmpty();
        }

        @Order(4)
        @DisplayName("리스너 비동기, 트랜잭션 O, 내부에서 lazy loading 실패, 그러나 예외가 메인 스레드 함수에 영향을 주지 않는다.")
        @Test
        public void test3() {
            long id = initDBService.initPostForLazyAndUser().getId();
            lazyLoadingEventOuterService.asyncTxLazyLoadingTxEvent(id);
        }
    }

    @Nested
    @DisplayName("동기식 @EventListenr")
    class syncEventListener {

        @Test
        @DisplayName("caller 함수 non Tx, 이벤트는 non Tx인 경우 수정 내역이 DB에 반영되지 않음")
        @Order(1)
        public void updateByEventNonTx() {
            long id = initDBService.initCommentAndPost().getId();
            updateEventOuterService.updateByEventNonTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }

        @Test
        @DisplayName("caller 함수 non Tx, 이벤트는 Tx인 경우 수정 내역이 DB에 반영되지 않음")
        @Order(2)
        public void updateByEventTx() {
            long id = initDBService.initCommentAndPost().getId();
            updateEventOuterService.updateByEventTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }
    }

    @Nested
    @DisplayName("동기식 @TxEventListener")
    class syncTransactionalEventListener {

        @Test
        @DisplayName("핸들러에서 엔티티를 바로 수정하는 경우, Transactional 유무, 전파레벨 관걔없이 수정 값이 DB에 반영되지 않는다.")
        @Order(3)
        public void updateByTxListenerNonTx() {
            long id = initDBService.initCommentAndPost().getId();
            updateEventOuterService.updateByTxListenerNonTx(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }

        @Test
        @DisplayName("핸들러에서 수정하기 위해서는 REQUIRES_NEW와 함께 다시 엔티티를 조회해서 수정해야 한다.")
        @Order(4)
        public void updateByTxListenerTxWithId() {
            long id = initDBService.initCommentAndPost().getId();
            updateEventOuterService.updateByTxListenerTxWithId(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Event Service Comment]");
        }
    }
}