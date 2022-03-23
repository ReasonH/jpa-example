package com.example.transactionaltestintegration.service.update;


import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.entity.Post;
import com.example.transactionaltestintegration.entity.User;
import com.example.transactionaltestintegration.repository.CommentRepository;
import com.example.transactionaltestintegration.repository.PostRepository;
import com.example.transactionaltestintegration.repository.UserRepository;
import com.example.transactionaltestintegration.service.runtimeexeption.RuntimeExceptionOuterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateInnerService {
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(RuntimeExceptionOuterService.class);

    @Transactional
    public long updateByIdAndGetId(Long id) {
        Comment comment = commentRepository.findById(id).get();
        comment.setContent("[Inner Service Comment]");

        return comment.getId();
    }

    @Transactional
    public long updateByEntityAndGetId(Comment comment) {
        comment.setContent("[Inner Service Comment]");

        return comment.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateByIdWithRequiresNew(Long id) {
        Comment comment = commentRepository.findById(id).get();
        comment.setAuthor("Park");
        comment.setContent("[Inner Service Comment]");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateByEntityWithRequireSNew(Comment comment) {
        comment.setAuthor("Park");
        comment.setContent("[Inner Service Comment]");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Comment updateByIdAndGetEntityWithRequiresNew(Long id) {
        Comment comment = commentRepository.findById(id).get();
        comment.setAuthor("Park");
        comment.setContent("[Inner Service Comment]");

        return comment;
    }
}
