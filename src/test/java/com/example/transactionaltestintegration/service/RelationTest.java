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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RelationTest {

    @Autowired
    private OneToManyLazyLoadingService oneToManyLazyLoadingService;
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

    @Test
    @DisplayName("JPQL로 Comment와 Post를 fetch 하면, Post의 EAGER 필드를 조회하는 추가 쿼리가 발생한다.")
    public void test1() {
        Comment comment = initDBService.initCommentAndPostAndUser();
        System.out.println("JPQL로 조회 범위 지정 시, Post -> User를 추가 조회");
        Comment result = commentRepository.findById2(comment.getId()).orElse(null);

        Assertions.assertThat(result.getPost().getUser().getName()).isEqualTo("[Init User]");
    }

    @Test
    @DisplayName("함수형 쿼리로 Comment를 조회하면, Comment -> EAGER Post -> EAGER User를 left outer join으로 조회한다.")
    public void test2() {
        Comment comment = initDBService.initCommentAndPostAndUser();
        Comment result = commentRepository.findById(comment.getId()).orElse(null);

        Assertions.assertThat(result.getPost().getUser().getName()).isEqualTo("[Init User]");
    }

    @Test
    @DisplayName("loading 대상이 없는 경우? (테스트를 위해, 자동 생성되는 외래키 인덱스 제거 필요)")
    @Disabled
    public void test3() {
        Post post = initDBService.initPostAndUser();
        userRepository.deleteById(post.getUser().getId());
        // fixme test 쿼리 추가
        // fixme 아래 내용 정리
        // method query lazy loading 시 -> post 조회 O -> user조회 시 EntityNotFoundException
        // method query eager loading 시 -> POST LEFT OUTER JOIN USER  -> user에 대한 정보가 null -> user 재조회 -> 실패 -> post 로딩 실패 -> null
        // jpql fetch 사용 시 -> POST INNER JOIN USER -> 결과 없음
        // jpql no fetch + eager 사용 시 -> user 필드 참조해서 JpaObjectRetrievalFailureException 예외 발생
    }

    @Test
    @Transactional
    @DisplayName("User를 조회한 뒤, Post를 Lazy Loading 하면, Post의 EAGER 필드를 조회하는 쿼리가 추가 발생한다.")
    @Disabled
    public void test4() {
        long userId = initDBService.initPostAndSectorAndUser();
        oneToManyLazyLoadingService.foo(userId);
        User findUser = userRepository.findById(userId).get();
        List<Post> postList = findUser.getPostList();
    }
}