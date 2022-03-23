package com.example.transactionaltestintegration.service.update;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.service.InitDBService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateOuterService {
    private final InitDBService initDBService;
    private final UpdateInnerService updateInnerService;
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(UpdateOuterService.class);

    public long updateById() {
        Comment comment = initDBService.initComment();

        return updateInnerService.updateByIdAndGetId(comment.getId());
    }

    public long updateByEntity() {
        Comment comment = initDBService.initComment();

        return updateInnerService.updateByEntityAndGetId(comment);
    }

    @Transactional
    public void updateByIdAndUpdateOutside(long id) {
        Comment comment = commentRepository.findById(id).get();
        updateInnerService.updateByIdWithRequiresNew(comment.getId());
        comment.setContent("[Outer Service Comment]");
    }

    @Transactional
    public void updateByEntityAndUpdateOutside(long id) {
        Comment comment = commentRepository.findById(id).get();
        updateInnerService.updateByEntityWithRequireSNew(comment);
        comment.setContent("[Outer Service Comment]");
    }

    @Transactional
    public void updateByIdAndGetEntityAndUpdateOutside(long id) {
        Comment comment = updateInnerService.updateByIdAndGetEntityWithRequiresNew(id);
        comment.setAuthor("Kim");
        comment.setContent("[Outer Service Comment]");
    }
}
