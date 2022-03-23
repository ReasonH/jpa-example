//package com.example.transactionaltestintegration.service;
//
//import com.example.transactionaltestintegration.entity.Post;
//import com.example.transactionaltestintegration.handler.event.AsyncEvent;
//import com.example.transactionaltestintegration.handler.event.SyncEvent;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class Temp {
//
//    @Transactional
//    public void callingTransactionInAsync() {
//        Post post = postRepository.findById(1L).get();
//        log.info("OuterService title {}", post.getTitle());
//        taskExecutor.execute(() -> {
//            // 새로운 스레드에서는 어떤 방식으로든 영속성 전이가 안됨
//            // post title 조회 가능 - user name 조회 불가능
////            post.setTitle("thread title");
////            log.info("OuterService title {}", post.getTitle());
////            log.info("OuterService user {}", post.getUser().getName());
//            newTransactionalInnerService.innerMethodforAsync(post);
//        });
//    }
//
//    @Transactional
//    public void callingUpdateTransactionalMethodWithEvent() {
//        Post post = postRepository.getOne(1L);
//        log.info("OutherService post title {}", post.getTitle());
//
//        eventPublisher.publishEvent(new SyncEvent("Event", post));
//    }
//
//    @Transactional
//    public void callingUpdateTransactionalMethodWithEventSync() {
//        Post post = postRepository.getOne(1L);
//        log.info("OuterService post title {}", post.getTitle());
//
//        eventPublisher.publishEvent(new AsyncEvent("Event", post));
//    }
//
//

//import org.springframework.transaction.annotation.Transactional;
//
//@Transactional
//public void callingUpdateTransactionalMethodWithInnerMethod() {
//        Post post = postRepository.getOne(1L);
//        log.info("OuterService post title {}", post.getTitle());
//
//        newTransactionalInnerService.innerMethodToUpdateByParam(post);
//        }
//
//}


//    private final TaskExecutor taskExecutor;
//    private final ApplicationEventPublisher eventPublisher;
//    @Transactional
//    public void asyncInnerMethod(Post post) {
//        log.error(post.getUser().getName());
//    }
//
//    @Transactional
//    public void innerMethod() {
//        User user = new User("temp user");
//        userRepository.save(user);
//        Post post = new Post("temp post");
//        post.setUser(user);
//        Post result = postRepository.save(post);
//        System.out.println("================= result: " + result.getId());
//    }
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void innerMethodforAsync(Post post) {
//        post.setTitle("thread title");
//        log.info("InnerService title {}", post.getTitle());
////        log.info("InnerService title {}", post.getUser().getName());
//    }

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
