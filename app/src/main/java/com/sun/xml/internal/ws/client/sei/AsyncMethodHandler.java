package com.sun.xml.internal.ws.client.sei;

import com.oracle.webservices.internal.api.databinding.JavaCallInfo;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.client.AsyncInvoker;
import com.sun.xml.internal.ws.client.AsyncResponseImpl;
import com.sun.xml.internal.ws.client.RequestContext;
import com.sun.xml.internal.ws.client.ResponseContext;
import java.lang.reflect.Method;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/AsyncMethodHandler.class */
abstract class AsyncMethodHandler extends MethodHandler {
    AsyncMethodHandler(SEIStub owner, Method m2) {
        super(owner, m2);
    }

    protected final Response<Object> doInvoke(Object proxy, Object[] args, AsyncHandler handler) {
        AsyncInvoker invoker = new SEIAsyncInvoker(proxy, args);
        invoker.setNonNullAsyncHandlerGiven(handler != null);
        AsyncResponseImpl<Object> ft = new AsyncResponseImpl<>(invoker, handler);
        invoker.setReceiver(ft);
        ft.run();
        return ft;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/AsyncMethodHandler$SEIAsyncInvoker.class */
    private class SEIAsyncInvoker extends AsyncInvoker {
        private final RequestContext rc;
        private final Object[] args;

        SEIAsyncInvoker(Object proxy, Object[] args) {
            this.rc = AsyncMethodHandler.this.owner.requestContext.copy();
            this.args = args;
        }

        @Override // com.sun.xml.internal.ws.client.AsyncInvoker
        public void do_run() {
            JavaCallInfo call = AsyncMethodHandler.this.owner.databinding.createJavaCallInfo(AsyncMethodHandler.this.method, this.args);
            Packet req = (Packet) AsyncMethodHandler.this.owner.databinding.serializeRequest(call);
            Fiber.CompletionCallback callback = new Fiber.CompletionCallback() { // from class: com.sun.xml.internal.ws.client.sei.AsyncMethodHandler.SEIAsyncInvoker.1
                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Packet response) {
                    SEIAsyncInvoker.this.responseImpl.setResponseContext(new ResponseContext(response));
                    Message msg = response.getMessage();
                    if (msg == null) {
                        return;
                    }
                    try {
                        Object[] rargs = new Object[1];
                        JavaCallInfo call2 = AsyncMethodHandler.this.owner.databinding.deserializeResponse(response, AsyncMethodHandler.this.owner.databinding.createJavaCallInfo(AsyncMethodHandler.this.method, rargs));
                        if (call2.getException() == null) {
                            SEIAsyncInvoker.this.responseImpl.set(rargs[0], null);
                            return;
                        }
                        throw call2.getException();
                    } catch (Throwable t2) {
                        if (t2 instanceof RuntimeException) {
                            if (t2 instanceof WebServiceException) {
                                SEIAsyncInvoker.this.responseImpl.set(null, t2);
                                return;
                            }
                        } else if (t2 instanceof Exception) {
                            SEIAsyncInvoker.this.responseImpl.set(null, t2);
                            return;
                        }
                        SEIAsyncInvoker.this.responseImpl.set(null, new WebServiceException(t2));
                    }
                }

                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Throwable error) {
                    if (error instanceof WebServiceException) {
                        SEIAsyncInvoker.this.responseImpl.set(null, error);
                    } else {
                        SEIAsyncInvoker.this.responseImpl.set(null, new WebServiceException(error));
                    }
                }
            };
            AsyncMethodHandler.this.owner.doProcessAsync(this.responseImpl, req, this.rc, callback);
        }
    }

    ValueGetterFactory getValueGetterFactory() {
        return ValueGetterFactory.ASYNC;
    }
}
