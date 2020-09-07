package com.diamonds.server;

public interface AsyncService<T> {
    void handleAsync(T t);
}
