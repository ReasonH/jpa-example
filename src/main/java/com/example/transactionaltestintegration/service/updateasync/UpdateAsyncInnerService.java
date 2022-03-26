package com.example.transactionaltestintegration.service.updateasync;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateAsyncInnerService {

    private final CommentRepository commentRepository;

    @Async
    public void updateAsync(Comment comment) {
        comment.setContent("[Async Inner Service Comment]");
    }

    public void updateSync(Comment comment) {
        comment.setContent("[Async Inner Service Comment]");
    }

    @Transactional
    public void updateSyncWithTx(Comment comment) {
        commentRepository.findById(comment.getId()).get().setContent("[Async Inner Service Comment]");
    }
}
