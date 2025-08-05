package com.sun.xml.internal.ws.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/CompletedFuture.class */
public class CompletedFuture<T> implements Future<T> {

    /* renamed from: v, reason: collision with root package name */
    private final T f12091v;
    private final Throwable re;

    public CompletedFuture(T v2, Throwable re) {
        this.f12091v = v2;
        this.re = re;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    @Override // java.util.concurrent.Future
    public T get() throws ExecutionException {
        if (this.re != null) {
            throw new ExecutionException(this.re);
        }
        return this.f12091v;
    }

    @Override // java.util.concurrent.Future
    public T get(long timeout, TimeUnit unit) throws ExecutionException {
        return get();
    }
}
