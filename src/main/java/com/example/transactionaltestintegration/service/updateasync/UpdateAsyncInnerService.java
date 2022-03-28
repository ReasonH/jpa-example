package com.example.transactionaltestintegration.service.updateasync;

import com.example.transactionaltestintegration.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateAsyncInnerService {

    @Async
    public void updateAsync(Comment comment) {
        comment.setContent("[Async Inner Service Comment]");
    }

    public void updateSync(Comment comment) {
        comment.setContent("[Async Inner Service Comment]");
    }
}
