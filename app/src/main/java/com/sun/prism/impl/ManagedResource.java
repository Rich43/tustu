package com.sun.prism.impl;

import com.sun.prism.GraphicsResource;
import java.util.ArrayList;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ManagedResource.class */
public abstract class ManagedResource<T> implements GraphicsResource {
    static final boolean trackLockSources = false;
    protected T resource;
    private final ResourcePool<T> pool;
    private int lockcount;
    private int employcount;
    ArrayList<Throwable> lockedFrom;
    private boolean permanent;
    private boolean mismatchDetected;
    private boolean disposalRequested;
    private int age;

    static boolean _isgone(ManagedResource<?> mr) {
        if (mr == null) {
            return true;
        }
        if (!((ManagedResource) mr).disposalRequested) {
            return !mr.isValid();
        }
        mr.free();
        mr.resource = null;
        ((ManagedResource) mr).disposalRequested = false;
        return true;
    }

    protected ManagedResource(T resource, ResourcePool<T> pool) {
        this.resource = resource;
        this.pool = pool;
        manage();
        lock();
    }

    private void manage() {
        this.pool.resourceManaged(this);
    }

    public final T getResource() {
        assertLocked();
        return this.resource;
    }

    public final ResourcePool<T> getPool() {
        return this.pool;
    }

    public boolean isValid() {
        return (this.resource == null || this.disposalRequested) ? false : true;
    }

    public boolean isDisposalRequested() {
        return this.disposalRequested;
    }

    public final boolean isLocked() {
        return this.lockcount > 0;
    }

    public final int getLockCount() {
        return this.lockcount;
    }

    public final void assertLocked() {
        if (this.lockcount <= 0) {
            throw new IllegalStateException("Operation requires resource lock");
        }
    }

    public final boolean isPermanent() {
        return this.permanent;
    }

    public final boolean isInteresting() {
        return this.employcount > 0;
    }

    public final int getInterestCount() {
        return this.employcount;
    }

    public void free() {
    }

    public int getAge() {
        return this.age;
    }

    @Override // com.sun.prism.GraphicsResource
    public final void dispose() {
        if (this.pool.isManagerThread()) {
            T r2 = this.resource;
            if (r2 != null) {
                free();
                this.disposalRequested = false;
                this.resource = null;
                this.pool.resourceFreed(this);
                return;
            }
            return;
        }
        this.disposalRequested = true;
    }

    public final void makePermanent() {
        assertLocked();
        this.permanent = true;
    }

    public final T lock() {
        this.lockcount++;
        this.age = 0;
        return this.resource;
    }

    void unlockall() {
        this.lockcount = 0;
    }

    public final void unlock() {
        assertLocked();
        this.lockcount--;
    }

    public final void contentsUseful() {
        assertLocked();
        this.employcount++;
    }

    public final void contentsNotUseful() {
        if (this.employcount <= 0) {
            throw new IllegalStateException("Resource obsoleted too many times");
        }
        this.employcount--;
    }

    public final boolean wasMismatched() {
        return this.mismatchDetected;
    }

    public final void setMismatched() {
        this.mismatchDetected = true;
    }

    public final void bumpAge(int forever) {
        int a2 = this.age;
        if (a2 < forever) {
            this.age = a2 + 1;
        }
    }
}
