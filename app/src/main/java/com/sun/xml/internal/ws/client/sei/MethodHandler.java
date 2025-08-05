package com.sun.xml.internal.ws.client.sei;

import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/MethodHandler.class */
public abstract class MethodHandler {
    protected final SEIStub owner;
    protected Method method;

    abstract Object invoke(Object obj, Object[] objArr) throws Throwable;

    protected MethodHandler(SEIStub owner, Method m2) {
        this.owner = owner;
        this.method = m2;
    }
}
