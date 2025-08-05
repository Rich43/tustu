package com.sun.prism.d3d;

import com.sun.prism.impl.DisposerManagedResource;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DTextureResource.class */
class D3DTextureResource extends DisposerManagedResource<D3DTextureData> {
    public D3DTextureResource(D3DTextureData resource) {
        super(resource, D3DVramPool.instance, resource);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.prism.impl.ManagedResource
    public void free() {
        if (this.resource != 0) {
            ((D3DTextureData) this.resource).dispose();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.prism.impl.ManagedResource
    public boolean isValid() {
        return (this.resource == 0 || ((D3DTextureData) this.resource).getResource() == 0) ? false : true;
    }
}
