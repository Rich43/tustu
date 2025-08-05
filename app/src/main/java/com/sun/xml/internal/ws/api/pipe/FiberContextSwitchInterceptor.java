package com.sun.xml.internal.ws.api.pipe;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/FiberContextSwitchInterceptor.class */
public interface FiberContextSwitchInterceptor {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/FiberContextSwitchInterceptor$Work.class */
    public interface Work<R, P> {
        R execute(P p2);
    }

    <R, P> R execute(Fiber fiber, P p2, Work<R, P> work);
}
