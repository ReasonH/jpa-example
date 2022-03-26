package com.example.transactionaltestintegration.handler.event.lazyloading;

import com.example.transactionaltestintegration.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LazyAsyncListener {
    Post post;
}
