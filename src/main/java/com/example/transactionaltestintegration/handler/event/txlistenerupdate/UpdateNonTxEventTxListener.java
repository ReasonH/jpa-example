package com.example.transactionaltestintegration.handler.event.txlistenerupdate;

import com.example.transactionaltestintegration.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNonTxEventTxListener {
    private String content;
    private Comment comment;
}
