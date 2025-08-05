package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/Invoker.class */
public abstract class Invoker extends com.sun.xml.internal.ws.server.sei.Invoker {
    private static final Method invokeMethod;
    private static final Method asyncInvokeMethod;

    public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint) {
        start(wsc);
    }

    public void start(@NotNull WebServiceContext wsc) {
        throw new IllegalStateException("deprecated version called");
    }

    public void dispose() {
    }

    public <T> T invokeProvider(@NotNull Packet packet, T t2) throws IllegalAccessException, InvocationTargetException {
        return (T) invoke(packet, invokeMethod, t2);
    }

    public <T> void invokeAsyncProvider(@NotNull Packet p2, T arg, AsyncProviderCallback cbak, WebServiceContext ctxt) throws IllegalAccessException, InvocationTargetException {
        invoke(p2, asyncInvokeMethod, arg, cbak, ctxt);
    }

    static {
        try {
            invokeMethod = Provider.class.getMethod("invoke", Object.class);
            try {
                asyncInvokeMethod = AsyncProvider.class.getMethod("invoke", Object.class, AsyncProviderCallback.class, WebServiceContext.class);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        } catch (NoSuchMethodException e3) {
            throw new AssertionError(e3);
        }
    }
}
