package com.sun.scenario.effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/LockableResource.class */
public interface LockableResource {
    void lock();

    void unlock();

    boolean isLost();
}
