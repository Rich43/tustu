package com.sun.xml.internal.ws.client.sei;

import java.lang.reflect.Method;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/CallbackMethodHandler.class */
final class CallbackMethodHandler extends AsyncMethodHandler {
    private final int handlerPos;

    CallbackMethodHandler(SEIStub owner, Method m2, int handlerPos) {
        super(owner, m2);
        this.handlerPos = handlerPos;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.internal.ws.client.sei.MethodHandler
    public Future<?> invoke(Object proxy, Object[] args) throws WebServiceException {
        AsyncHandler handler = (AsyncHandler) args[this.handlerPos];
        return doInvoke(proxy, args, handler);
    }
}
