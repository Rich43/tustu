package com.sun.prism.d3d;

import com.sun.prism.impl.BaseMesh;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DMesh.class */
class D3DMesh extends BaseMesh {
    static int count = 0;
    private final D3DContext context;
    private final long nativeHandle;

    private D3DMesh(D3DContext context, long nativeHandle, Disposer.Record disposerRecord) {
        super(disposerRecord);
        this.context = context;
        this.nativeHandle = nativeHandle;
        count++;
    }

    static D3DMesh create(D3DContext context) {
        long nativeHandle = context.createD3DMesh();
        return new D3DMesh(context, nativeHandle, new D3DMeshDisposerRecord(context, nativeHandle));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override // com.sun.prism.impl.BaseMesh, com.sun.prism.Mesh
    public boolean isValid() {
        return !this.context.isDisposed();
    }

    @Override // com.sun.prism.impl.BaseGraphicsResource, com.sun.prism.GraphicsResource
    public void dispose() {
        this.disposerRecord.dispose();
        count--;
    }

    @Override // com.sun.prism.Mesh
    public int getCount() {
        return count;
    }

    @Override // com.sun.prism.impl.BaseMesh
    public boolean buildNativeGeometry(float[] vertexBuffer, int vertexBufferLength, int[] indexBufferInt, int indexBufferLength) {
        return this.context.buildNativeGeometry(this.nativeHandle, vertexBuffer, vertexBufferLength, indexBufferInt, indexBufferLength);
    }

    @Override // com.sun.prism.impl.BaseMesh
    public boolean buildNativeGeometry(float[] vertexBuffer, int vertexBufferLength, short[] indexBufferShort, int indexBufferLength) {
        return this.context.buildNativeGeometry(this.nativeHandle, vertexBuffer, vertexBufferLength, indexBufferShort, indexBufferLength);
    }

    /* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DMesh$D3DMeshDisposerRecord.class */
    static class D3DMeshDisposerRecord implements Disposer.Record {
        private final D3DContext context;
        private long nativeHandle;

        D3DMeshDisposerRecord(D3DContext context, long nativeHandle) {
            this.context = context;
            this.nativeHandle = nativeHandle;
        }

        void traceDispose() {
        }

        @Override // com.sun.prism.impl.Disposer.Record
        public void dispose() {
            if (this.nativeHandle != 0) {
                traceDispose();
                this.context.releaseD3DMesh(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}
