package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.firstcache.FirstCacheService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirstCacheTest {

    @Autowired
    private InitDBService initDBService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FirstCacheService firstCacheService;

    @AfterEach
    void clear() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @DisplayName("JPQL 조회 시, 1차 캐시에 값이 있으면 DB 값 대신 캐시 값을 사용한다. 즉, User가 Post를 fetch 해도 postList는 비어있다.")
    @Order(1)
    public void foo1() {
        User user = new User("[First Cache User]");
        userRepository.save(user);

        Post post1 = new Post("[First Cache Post1]");
        Post post2 = new Post("[First Cache Post2]");
        post1.setUser(user);
        post2.setUser(user);

        postRepository.save(post1);
        postRepository.save(post2);

        List<User> userList = userRepository.findAllByJoinFetch();
        assertThat(userList.get(0).getPostList().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @DisplayName("JPQL 조회 시, 현재 1차 캐시 값을 flush 한다. 즉, Post는 변경된 값인 [Post]를 통해 조회할 수 있다.")
    @Order(2)
    public void foo2() {
        Post initPost = postRepository.save(new Post("[Init Post]"));
        initPost.setContent("[Init Content]");
        Long id = initPost.getId();

        Post post = postRepository.findById(id).get();

        assertThat(post.getTitle()).isEqualTo("[Init Post]");
        post.setTitle("[Post]");

        assertThat(postRepository.findByTitle("[Init Post]")).isNull();
        assertThat(postRepository.findByTitle("[Post]")).isNotNull();
    }

    @Nested
    @DisplayName("객체 A의 유니크 값 a를 b로 수정, a를 이용한 새로운 객체 B를 만들어서 save")
    class FlushAndJPQL {
        @Test
        @DisplayName("update 쿼리가 insert 쿼리 이후에 수행되므로 실패한다.")
        @Order(3)
        public void foo3() {
            Long id = initDBService.initPostAndUser().getId();
            assertThatThrownBy(() -> firstCacheService.updateUniqueColumnAndSaveNewEntity(id))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("기존 객체 수정 후 save 호출, commit 시점에 update 쿼리로 수행되므로 실패한다.")
        @Order(4)
        public void foo4() {
            Long id = initDBService.initPostAndUser().getId();
            assertThatThrownBy(() -> firstCacheService.updateUniqueColumnAndEarlySaveOldAndSaveNew(id))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("기존 객체 수정 후 saveAndFlush 호출, update가 미리 반영된다.")
        @Order(5)
        public void foo5() {
            Long id = initDBService.initPostAndUser().getId();
            firstCacheService.updateUniqueColumnAndEarlySaveFlushOldAndSaveNew(id);

            assertThat(postRepository.findAll().size()).isEqualTo(2);
        }
    }

}

