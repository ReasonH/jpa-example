package com.example.transactionaltestintegration.handler.event;

import com.example.transactionaltestintegration.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AsyncEvent {
    private String name;
    private Post post;
}
