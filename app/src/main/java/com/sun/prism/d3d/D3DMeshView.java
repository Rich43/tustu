package com.sun.prism.d3d;

import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.impl.BaseMeshView;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DMeshView.class */
class D3DMeshView extends BaseMeshView {
    static int count = 0;
    private final D3DContext context;
    private final long nativeHandle;
    private final D3DMesh mesh;
    private D3DPhongMaterial material;

    private D3DMeshView(D3DContext context, long nativeHandle, D3DMesh mesh, Disposer.Record disposerRecord) {
        super(disposerRecord);
        this.context = context;
        this.mesh = mesh;
        this.nativeHandle = nativeHandle;
        count++;
    }

    static D3DMeshView create(D3DContext context, D3DMesh mesh) {
        long nativeHandle = context.createD3DMeshView(mesh.getNativeHandle());
        return new D3DMeshView(context, nativeHandle, mesh, new D3DMeshViewDisposerRecord(context, nativeHandle));
    }

    @Override // com.sun.prism.MeshView
    public void setCullingMode(int cullingMode) {
        this.context.setCullingMode(this.nativeHandle, cullingMode);
    }

    @Override // com.sun.prism.MeshView
    public void setMaterial(Material material) {
        this.context.setMaterial(this.nativeHandle, ((D3DPhongMaterial) material).getNativeHandle());
        this.material = (D3DPhongMaterial) material;
    }

    @Override // com.sun.prism.MeshView
    public void setWireframe(boolean wireframe) {
        this.context.setWireframe(this.nativeHandle, wireframe);
    }

    @Override // com.sun.prism.MeshView
    public void setAmbientLight(float r2, float g2, float b2) {
        this.context.setAmbientLight(this.nativeHandle, r2, g2, b2);
    }

    @Override // com.sun.prism.MeshView
    public void setPointLight(int index, float x2, float y2, float z2, float r2, float g2, float b2, float w2) {
        if (index >= 0 && index <= 2) {
            this.context.setPointLight(this.nativeHandle, index, x2, y2, z2, r2, g2, b2, w2);
        }
    }

    @Override // com.sun.prism.MeshView
    public void render(Graphics g2) {
        this.material.lockTextureMaps();
        this.context.renderMeshView(this.nativeHandle, g2);
        this.material.unlockTextureMaps();
    }

    @Override // com.sun.prism.impl.BaseMeshView, com.sun.prism.MeshView
    public boolean isValid() {
        return !this.context.isDisposed();
    }

    @Override // com.sun.prism.impl.BaseGraphicsResource, com.sun.prism.GraphicsResource
    public void dispose() {
        this.material = null;
        this.disposerRecord.dispose();
        count--;
    }

    public int getCount() {
        return count;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DMeshView$D3DMeshViewDisposerRecord.class */
    static class D3DMeshViewDisposerRecord implements Disposer.Record {
        private final D3DContext context;
        private long nativeHandle;

        D3DMeshViewDisposerRecord(D3DContext context, long nativeHandle) {
            this.context = context;
            this.nativeHandle = nativeHandle;
        }

        void traceDispose() {
        }

        @Override // com.sun.prism.impl.Disposer.Record
        public void dispose() {
            if (this.nativeHandle != 0) {
                traceDispose();
                this.context.releaseD3DMeshView(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}
