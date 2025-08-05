package com.sun.prism.d3d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DVramPool.class */
class D3DVramPool extends BaseResourcePool<D3DTextureData> implements TextureResourcePool<D3DTextureData> {
    public static final D3DVramPool instance = new D3DVramPool();

    private D3DVramPool() {
        super(PrismSettings.targetVram, PrismSettings.maxVram);
    }

    @Override // com.sun.prism.impl.ResourcePool
    public long size(D3DTextureData resource) {
        return resource.getSize();
    }

    @Override // com.sun.prism.impl.TextureResourcePool
    public long estimateTextureSize(int width, int height, PixelFormat format) {
        return width * height * format.getBytesPerPixelUnit();
    }

    @Override // com.sun.prism.impl.TextureResourcePool
    public long estimateRTTextureSize(int width, int height, boolean hasDepth) {
        return width * height * 4;
    }

    public String toString() {
        return "D3D Vram Pool";
    }
}
