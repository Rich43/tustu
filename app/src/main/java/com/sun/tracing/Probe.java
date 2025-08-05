package com.sun.tracing;

/* loaded from: rt.jar:com/sun/tracing/Probe.class */
public interface Probe {
    boolean isEnabled();

    void trigger(Object... objArr);
}
