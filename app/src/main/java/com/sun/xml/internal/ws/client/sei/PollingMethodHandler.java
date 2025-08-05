package com.sun.xml.internal.ws.client.sei;

import java.lang.reflect.Method;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/PollingMethodHandler.class */
final class PollingMethodHandler extends AsyncMethodHandler {
    PollingMethodHandler(SEIStub owner, Method m2) {
        super(owner, m2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.internal.ws.client.sei.MethodHandler
    public Response<?> invoke(Object proxy, Object[] args) throws WebServiceException {
        return doInvoke(proxy, args, null);
    }
}
