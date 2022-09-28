package com.example.transactionaltestintegration.handler.event.lazyloading;

import com.example.transactionaltestintegration.entity.PostForLazy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LazyAsyncTxListenerTx {
    PostForLazy post;
}
