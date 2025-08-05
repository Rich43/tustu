package com.sun.tracing;

import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/tracing/Provider.class */
public interface Provider {
    Probe getProbe(Method method);

    void dispose();
}
