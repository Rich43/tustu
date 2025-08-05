package com.sun.prism.sw;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWTexturePool.class */
class SWTexturePool extends BaseResourcePool<SWTexture> implements TextureResourcePool<SWTexture> {
    static final SWTexturePool instance = new SWTexturePool();

    private static long maxVram() {
        long heapmax = Runtime.getRuntime().maxMemory();
        long setmax = PrismSettings.maxVram;
        return Math.min(heapmax / 4, setmax);
    }

    private static long targetVram() {
        long max = maxVram();
        return Math.min(max / 2, PrismSettings.targetVram);
    }

    private SWTexturePool() {
        super(null, targetVram(), maxVram());
    }

    @Override // com.sun.prism.impl.BaseResourcePool, com.sun.prism.impl.ResourcePool
    public long used() {
        return 0L;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public long size(SWTexture resource) {
        long size = resource.getPhysicalWidth() * resource.getPhysicalHeight();
        if (resource instanceof SWArgbPreTexture) {
            size *= 4;
        }
        return size;
    }

    @Override // com.sun.prism.impl.TextureResourcePool
    public long estimateTextureSize(int width, int height, PixelFormat format) {
        switch (format) {
            case BYTE_ALPHA:
                return width * height;
            default:
                return width * height * 4;
        }
    }

    @Override // com.sun.prism.impl.TextureResourcePool
    public long estimateRTTextureSize(int width, int height, boolean hasDepth) {
        return width * height * 4;
    }
}
