package com.sun.xml.internal.ws.client;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.Cancelable;
import com.sun.xml.internal.ws.util.CompletedFuture;
import java.util.Map;
import java.util.concurrent.FutureTask;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/AsyncResponseImpl.class */
public final class AsyncResponseImpl<T> extends FutureTask<T> implements Response<T>, ResponseContextReceiver {
    private final AsyncHandler<T> handler;
    private ResponseContext responseContext;
    private final Runnable callable;
    private Cancelable cancelable;

    public AsyncResponseImpl(Runnable runnable, @Nullable AsyncHandler<T> handler) {
        super(runnable, null);
        this.callable = runnable;
        this.handler = handler;
    }

    @Override // java.util.concurrent.FutureTask, java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        try {
            this.callable.run();
        } catch (WebServiceException e2) {
            set(null, e2);
        } catch (Throwable e3) {
            set(null, new WebServiceException(e3));
        }
    }

    @Override // javax.xml.ws.Response
    public ResponseContext getContext() {
        return this.responseContext;
    }

    @Override // com.sun.xml.internal.ws.client.ResponseContextReceiver
    public void setResponseContext(ResponseContext rc) {
        this.responseContext = rc;
    }

    public void set(T v2, Throwable t2) {
        if (this.handler != null) {
            try {
                this.handler.handleResponse(new C1CallbackFuture(v2, t2));
            } catch (Throwable e2) {
                super.setException(e2);
                return;
            }
        }
        if (t2 != null) {
            super.setException(t2);
        } else {
            super.set(v2);
        }
    }

    /* renamed from: com.sun.xml.internal.ws.client.AsyncResponseImpl$1CallbackFuture, reason: invalid class name */
    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/AsyncResponseImpl$1CallbackFuture.class */
    class C1CallbackFuture<T> extends CompletedFuture<T> implements Response<T> {
        public C1CallbackFuture(T v2, Throwable t2) {
            super(v2, t2);
        }

        @Override // javax.xml.ws.Response
        public Map<String, Object> getContext() {
            return AsyncResponseImpl.this.getContext();
        }
    }

    public void setCancelable(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    @Override // java.util.concurrent.FutureTask, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this.cancelable != null) {
            this.cancelable.cancel(mayInterruptIfRunning);
        }
        return super.cancel(mayInterruptIfRunning);
    }
}
