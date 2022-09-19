package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.fetch.OneToManyLazyLoadingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mtalk-test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FetchJoinTest {

    @Autowired
    private InitDBService initDBService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OneToManyLazyLoadingService oneToManyLazyLoadingService;

    @AfterEach
    void clear() {
        commentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Comment로 Post를 fetch join 했을 때, Post의 User 필드가 EAGER 로딩이면 조회 쿼리가 추가 발생한다")
    @Order(1)
    public void foo1() {
        Comment result = initDBService.initCommentAndPostAndUser();
        System.out.println("JPQL로 조회 범위 지정 시, Post -> User를 추가 조회");
        Comment queriedComment = commentRepository.findById2(result.getId()).orElse(null);
        System.out.println("함수형 쿼리 사용 시, Comment -> Post -> User 한 번에 fetch하는 쿼리를 생성");
        Comment queriedComment2 = commentRepository.findById(result.getId()).orElse(null);
        System.out.println("==================");

        Assertions.assertThat(queriedComment.getPost().getUser().getName()).isEqualTo(queriedComment2.getPost().getUser().getName());
    }

    @Test
    @DisplayName("User에서 Post를 Lazy Loading 했을 때, Post의 EAGER인 Sector 까지 한 번에 조회된다.")
    @Order(1)
    public void foo2() {
        long userId = initDBService.initPostAndSectorAndUser();
        oneToManyLazyLoadingService.foo(userId);
    }

    @Test
    @DisplayName("loading 대상이 없는 경우? (테스트를 위해 auto created index 삭제 필요 / hibernate ddl-auto validate 전환")
    @Order(1)
    public void foo3() {
        Post post = initDBService.initPostAndUser();
        System.out.println("============================" + post.getId() + " ==" + post.getUser().getId());
        userRepository.deleteById(post.getUser().getId());
        // lazy loading 시 -> post 까지는 찾아오지만, user조회 시 EntityNotFoundException 예외 발생
        // eager loading 시 post + user를 left outer join -> user에 대한 정보가 null -> user 재조회 -> 비어있음 -> post 로딩 실패 -> null
        // jpql로 직접 fetch 사용 시 -> inner join으로 가져오기 때문에 애초에 못가져옴 -> null
        // eager + jpql로 fetch하지 않을 시 -> user 필드 값 참조해서 JpaObjectRetrievalFailureException 예외 발생
        System.out.println("============================");
        String result1 = oneToManyLazyLoadingService.foo2(post.getId());
        System.out.println(result1);
//        Assertions.assertThatThrownBy(() -> ).isInstanceOf(LazyInitializationException.class);
        System.out.println("============================");
        Assertions.assertThat(postRepository.findByIdUserFetch(post.getId())).isNull();
        System.out.println("============================");
        Assertions.assertThat(postRepository.findByIdNotUserFetch(post.getId())).isNotNull();
        System.out.println("============================");
//        System.out.println(result.getTitle());
//        System.out.println(result.getUser());
    }
}
