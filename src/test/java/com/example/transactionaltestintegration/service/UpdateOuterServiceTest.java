package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.service.update.UpdateOuterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateOuterServiceTest {
    @Autowired
    private UpdateOuterService updateOuterService;
    @Autowired
    private InitDBService initDBService;
    @Autowired
    private CommentRepository commentRepository;

    @Nested
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
    @DisplayName("외부 함수 @Transactional, 내부 함수 @Transactional(propagation = REQUIRES_NEW)")
    class TransactionalOuterNewInnerMethod {

        @Test
        @DisplayName("외부에서 조회, 내부에서 객체 조회 및 수정, 외부에서 객체 수정하면, dirty-checking에 의해 외부 객체 상태가 DB에 반영된다.")
        @Order(3)
        public void updateByIdWithRequiresNewAndUpdateOutside() {
            long id = initDBService.initComment().getId();
            updateOuterService.updateByIdAndUpdateOutside(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getAuthor()).isEqualTo("Lee");
            assertThat(comment.getContent()).isEqualTo("[Outer Service Comment]");
        }

        @Test
        @DisplayName("외부에서 조회, 내부에서 전달받은 객체 수정, 외부에서 객체 수정하면, dirty-checking에 의해 최종 객체 상태가 DB에 반영된다.")
        @Order(4)
        public void updateByEntityAndUpdateOutside() {
            long id = initDBService.initComment().getId();
            updateOuterService.updateByEntityAndUpdateOutside(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getAuthor()).isEqualTo("Park");
            assertThat(comment.getContent()).isEqualTo("[Outer Service Comment]");
        }

        @Test
        @DisplayName("내부에서 객체 조회 및 수정 후 반환, 외부에서 해당 객체 수정하면, dirty-checking 미동작, 내부 함수 객체 상태가 DB에 반영된다.")
        @Order(5)
        public void updateByIdAndGetEntityAndUpdateOutside() {
            long id = initDBService.initComment().getId();
            updateOuterService.updateByIdAndGetEntityAndUpdateOutside(id);
            Comment comment = commentRepository.findById(id).get();

            assertThat(comment.getAuthor()).isEqualTo("Park");
            assertThat(comment.getContent()).isEqualTo("[Inner Service Comment]");
        }

    }
}
