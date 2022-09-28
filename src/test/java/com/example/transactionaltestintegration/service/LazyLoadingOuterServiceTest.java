package com.example.transactionaltestintegration.service;

import com.example.transactionaltestintegration.repository.CategoryRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.entityloading.LazyLoadingOuterService;
import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class LazyLoadingOuterServiceTest {

    @Autowired
    private LazyLoadingOuterService lazyLoadingOuterService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void clear() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("서비스 컴포넌트에서는 OSIV가 적용되지 않는다. WEB 요청에서만 OSIV 작동")
    public void bar() {
        Assertions.assertThatThrownBy(() -> lazyLoadingOuterService.osiv())
                .isInstanceOf(LazyInitializationException.class);
    }
}