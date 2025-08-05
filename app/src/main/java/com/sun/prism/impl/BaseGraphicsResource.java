package com.sun.prism.impl;

import com.sun.prism.GraphicsResource;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseGraphicsResource.class */
public abstract class BaseGraphicsResource implements GraphicsResource {
    private final Object disposerReferent;
    protected final Disposer.Record disposerRecord;

    @Override // com.sun.prism.GraphicsResource
    public abstract void dispose();

    public BaseGraphicsResource(BaseGraphicsResource sharedResource) {
        this.disposerReferent = sharedResource.disposerReferent;
        this.disposerRecord = sharedResource.disposerRecord;
    }

    protected BaseGraphicsResource(Disposer.Record disposerRecord) {
        this.disposerReferent = new Object();
        this.disposerRecord = disposerRecord;
        Disposer.addRecord(this.disposerReferent, disposerRecord);
    }
}
