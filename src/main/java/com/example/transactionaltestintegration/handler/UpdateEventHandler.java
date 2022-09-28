package com.example.transactionaltestintegration.handler;

import com.example.transactionaltestintegration.handler.event.listenerupdate.UpdateByIdTxEvent;
import com.example.transactionaltestintegration.handler.event.listenerupdate.UpdateTxEvent;
import com.example.transactionaltestintegration.handler.event.txlistenerupdate.UpdateNonTxEventTxListener;
import com.example.transactionaltestintegration.handler.event.txlistenerupdate.UpdateByIdTxEventTxListener;
import com.example.transactionaltestintegration.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UpdateEventHandler {

    private final CommentRepository commentRepository;

    @EventListener
    @Transactional
    public void onMyEvent(UpdateTxEvent event) {
        event.getComment().setContent(event.getContent());
    }

    @EventListener
    @Transactional
    public void onMyEvent(UpdateByIdTxEvent event) {
        commentRepository.findById(event.getComment().getId()).get().setContent(event.getContent());
    }

    @TransactionalEventListener
    @Transactional
    public void onMyEvent(UpdateNonTxEventTxListener event) {
        event.getComment().setContent(event.getContent());
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMyEvent(UpdateByIdTxEventTxListener event) {
        commentRepository.findById(event.getComment().getId()).get().setContent(event.getContent());
    }
}
