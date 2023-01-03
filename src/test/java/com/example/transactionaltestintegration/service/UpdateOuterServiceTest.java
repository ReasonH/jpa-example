package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.service.update.UpdateOuterService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UpdateOuterServiceTest {
    @Autowired
    private UpdateOuterService updateOuterService;
    @Autowired
    private InitDBService initDBService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void clear() {
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("caller 함수 Tx, callee 함수 Tx")
    class NonTransactionalOuterMethod {
        @Test
        @DisplayName("내부에서 DB 조회 후 수정하는 경우 DB에 반영된다.")
        @Order(1)
        public void updateWithId() {

            long id = updateOuterService.updateById();
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Inner Service Comment]");
        }

        @Test
        @DisplayName("영속성이 해제된 객체를 전달받아 수정하는 경우 DB에 반영되지 않는다.")
        @Order(2)
        public void updateWithEntityParam() {

            long id = updateOuterService.updateByEntity();
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getContent()).isEqualTo("[Init Comment]");
        }
    }

    @Nested
    @DisplayName("caller 함수 Tx, callee 함수 Tx(propagation = REQUIRES_NEW)")
    class TransactionalOuterNewInnerMethod {

        @Test
        @DisplayName("부모에서 조회, 자식에서 객체 조회 및 수정, 부모에서 객체 수정하면, dirty-checking에 의해 부모 객체 상태가 DB에 반영된다.")
        @Order(3)
        public void updateByIdWithRequiresNewAndUpdateOutside() {
            long id = initDBService.initCommentAndPost().getId();
            updateOuterService.updateByIdAndUpdateOutside(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getAuthor()).isEqualTo("Lee");
            assertThat(comment.getContent()).isEqualTo("[Outer Service Comment]");
        }

        @Test
        @DisplayName("부모에서 조회 후 전달, 자식에서 전달받은 객체 수정, 부모에서 객체 수정하면, dirty-checking에 의해 최종 객체 상태가 DB에 반영된다.")
        @Order(4)
        public void updateByEntityAndUpdateOutside() {
            long id = initDBService.initCommentAndPost().getId();
            updateOuterService.updateByEntityAndUpdateOutside(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getAuthor()).isEqualTo("Park");
            assertThat(comment.getContent()).isEqualTo("[Outer Service Comment]");
        }

        @Test
        @DisplayName("자식에서 객체 조회 및 수정 후 부모로 반환해서 재수정하면, dirty-checking 미동작, 내부 객체 상태가 DB에 반영된다.")
        @Order(5)
        public void updateByIdAndGetEntityAndUpdateOutside() {
            long id = initDBService.initCommentAndPost().getId();
            updateOuterService.updateByIdAndGetEntityAndUpdateOutside(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getAuthor()).isEqualTo("Park");
            assertThat(comment.getContent()).isEqualTo("[Inner Service Comment]");
        }
    }
}
