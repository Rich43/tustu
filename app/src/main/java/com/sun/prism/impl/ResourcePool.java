package com.sun.prism.impl;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ResourcePool.class */
public interface ResourcePool<T> {
    void freeDisposalRequestedAndCheckResources(boolean z2);

    boolean isManagerThread();

    long used();

    long managed();

    long max();

    long target();

    long origTarget();

    void setTarget(long j2);

    long size(T t2);

    void recordAllocated(long j2);

    void recordFree(long j2);

    void resourceManaged(ManagedResource<T> managedResource);

    void resourceFreed(ManagedResource<T> managedResource);

    boolean prepareForAllocation(long j2);
}
