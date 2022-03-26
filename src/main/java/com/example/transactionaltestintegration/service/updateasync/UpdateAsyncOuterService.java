package com.example.transactionaltestintegration.service.updateasync;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateAsyncOuterService {

    private final CommentRepository commentRepository;
    private final UpdateAsyncInnerService updateAsyncInnerService;
    private final TaskExecutor taskExecutor;

    @Transactional
    public void updateWithThreadAndWait(long id) {
        Comment comment = commentRepository.findById(id).get();

        taskExecutor.execute(() -> {
            comment.setContent("[Thread Comment]");
        });
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    @Transactional
    public void updateWithFunctionInThread(long id) {
        Comment comment = commentRepository.findById(id).get();

        taskExecutor.execute(() -> {
            updateAsyncInnerService.updateSync(comment);
        });
    }

    @Transactional
    public void updateWithFunctionInThreadAndWait(long id) {
        Comment comment = commentRepository.findById(id).get();

        taskExecutor.execute(() -> {
            updateAsyncInnerService.updateSync(comment);
        });
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    @Transactional
    public void updateWithTransactionalFunctionInThreadAndWait(long id) {
        Comment comment = commentRepository.findById(id).get();

        taskExecutor.execute(() -> {
            updateAsyncInnerService.updateSyncWithTx(comment);
        });
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    @Transactional
    public void updateWithAsyncFunction(long id) {
        Comment comment = commentRepository.findById(id).get();

        updateAsyncInnerService.updateAsync(comment);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }
}
