package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class OtherTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void clear() {
        commentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
        //    @Test
//    // 업데이트, 세이브 비교
//    @Rollback(value = false)
//    public void callingInitAndUpdate() {
//        outerService.init();
//        innerService.innerMethodToUpdate();
//        System.out.println(repository.findById(1L).get().getTitle());
//    }
//
//    @Test
//    // 비동기 이벤트 내에서 업데이트 수행 적용 여부
//    public void updateByEvent() {
//        outerService.init();
//        outerService.callingUpdateTransactionalMethodWithEvent();
//        try {
//            // 비동기 메서드 종료까지 잠시 대기
//            Thread.sleep(3000);
//        } catch (Exception e){
//
//        };
//        assertThat(repository.findById(1L).get().getTitle()).isEqualTo("update title");
//        assertThat(repository.findById(1L).get().getUser().getName()).isEqualTo("update name");
//    }
//
//    @Test
//    // 이벤트 내에서 업데이트 수행 적용 여부
//    public void updateByEventSync() {
//        outerService.init();
//        outerService.callingUpdateTransactionalMethodWithEventSync();
//
//        assertThat(repository.findById(1L).get().getTitle()).isEqualTo("update title");
//        assertThat(repository.findById(1L).get().getUser().getName()).isEqualTo("update name");
//    }
//
//    @Test
//    // 내부 메서드 내에서 업데이트 수행 적용 여부
//    public void updateByInnerMethod() {
//        outerService.init();
//        outerService.callingUpdateTransactionalMethodWithInnerMethod();
//
//        assertThat(repository.findById(1L).get().getUser().getName()).isEqualTo("update name");
//    }
//
//    @Test
//    // 비동기 메서드 실행 시 영속성 전파 여부, executor에서 바로 호출 / executor에서 함수 호출
//    public void callingTransactionalMethodInAsync() {
//        outerService.init();
//        outerService.callingTransactionInAsync();
//        try {
//            // 비동기 메서드 종료까지 잠시 대기
//            Thread.sleep(3000);
//        } catch (Exception e){
//
//        };
//        assertThat(repository.findAll().size()).isEqualTo(1);
//        assertThat(repository.findById(1L).get().getTitle()).isEqualTo("thread title");
//    }

}
