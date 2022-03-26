package com.example.transactionaltestintegration.service.updateevent;

import com.example.transactionaltestintegration.entity.Comment;
import com.example.transactionaltestintegration.handler.event.listenerupdate.UpdateEvent;
import com.example.transactionaltestintegration.handler.event.listenerupdate.UpdateIdTxEvent;
import com.example.transactionaltestintegration.handler.event.listenerupdate.UpdateTxEvent;
import com.example.transactionaltestintegration.handler.event.txlistenerupdate.UpdateNonTxEventTxListener;
import com.example.transactionaltestintegration.handler.event.txlistenerupdate.UpdateTxEventTxListenerWithId;
import com.example.transactionaltestintegration.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateEventOuterService {

    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void updateByEventNonTx(long id) {
        Comment comment = commentRepository.findById(id).get();
        eventPublisher.publishEvent(new UpdateEvent("[Event Service Comment]", comment));
    }

    public void updateByEventTx(long id) {
        Comment comment = commentRepository.findById(id).get();
        eventPublisher.publishEvent(new UpdateTxEvent("[Event Service Comment]", comment));
    }

    public void updateByEventWithIdTx(long id) {
        Comment comment = commentRepository.findById(id).get();
        eventPublisher.publishEvent(new UpdateIdTxEvent("[Event Service Comment]", comment));
    }

    @Transactional
    public void updateByTxListenerNonTx(long id) {
        Comment comment = commentRepository.findById(id).get();
        eventPublisher.publishEvent(new UpdateNonTxEventTxListener("[Event Service Comment]", comment));
    }

    @Transactional
    public void updateByTxListenerTxWithId(long id) {
        Comment comment = commentRepository.findById(id).get();
        eventPublisher.publishEvent(new UpdateTxEventTxListenerWithId("[Event Service Comment]", comment));
    }
}
