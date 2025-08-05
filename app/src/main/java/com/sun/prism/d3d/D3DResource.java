package com.sun.prism.d3d;

import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DResource.class */
class D3DResource extends BaseGraphicsResource {
    protected final D3DRecord d3dResRecord;

    D3DResource(D3DRecord disposerRecord) {
        super(disposerRecord);
        this.d3dResRecord = disposerRecord;
    }

    @Override // com.sun.prism.impl.BaseGraphicsResource, com.sun.prism.GraphicsResource
    public void dispose() {
        this.d3dResRecord.dispose();
    }

    /* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DResource$D3DRecord.class */
    static class D3DRecord implements Disposer.Record {
        private final D3DContext context;
        private long pResource;
        private boolean isDefaultPool;

        D3DRecord(D3DContext context, long pResource) {
            this.context = context;
            this.pResource = pResource;
            if (pResource != 0) {
                context.getResourceFactory().addRecord(this);
                this.isDefaultPool = D3DResourceFactory.nIsDefaultPool(pResource);
            } else {
                this.isDefaultPool = false;
            }
        }

        long getResource() {
            return this.pResource;
        }

        D3DContext getContext() {
            return this.context;
        }

        boolean isDefaultPool() {
            return this.isDefaultPool;
        }

        protected void markDisposed() {
            this.pResource = 0L;
        }

        @Override // com.sun.prism.impl.Disposer.Record
        public void dispose() {
            if (this.pResource != 0) {
                this.context.getResourceFactory().removeRecord(this);
                D3DResourceFactory.nReleaseResource(this.context.getContextHandle(), this.pResource);
                this.pResource = 0L;
            }
        }
    }
}
