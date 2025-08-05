package com.sun.prism.impl;

import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/impl/DisposerManagedResource.class */
public abstract class DisposerManagedResource<T> extends ManagedResource<T> {
    Object referent;

    public DisposerManagedResource(T resource, ResourcePool pool, Disposer.Record record) {
        super(resource, pool);
        this.referent = new Object();
        Disposer.addRecord(this.referent, record);
    }
}
