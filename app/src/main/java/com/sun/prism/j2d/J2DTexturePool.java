package com.sun.prism.j2d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import java.awt.image.BufferedImage;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DTexturePool.class */
class J2DTexturePool extends BaseResourcePool<BufferedImage> implements TextureResourcePool<BufferedImage> {
    static final J2DTexturePool instance = new J2DTexturePool();

    private static long maxVram() {
        long heapmax = Runtime.getRuntime().maxMemory();
        long setmax = PrismSettings.maxVram;
        return Math.min(heapmax / 4, setmax);
    }

    private static long targetVram() {
        long max = maxVram();
        return Math.min(max / 2, PrismSettings.targetVram);
    }

    private J2DTexturePool() {
        super(null, targetVram(), maxVram());
    }

    @Override // com.sun.prism.impl.BaseResourcePool, com.sun.prism.impl.ResourcePool
    public long used() {
        Runtime r2 = Runtime.getRuntime();
        long heapused = r2.totalMemory() - r2.freeMemory();
        long heapfree = r2.maxMemory() - heapused;
        long managedfree = max() - managed();
        return max() - Math.min(heapfree, managedfree);
    }

    static long size(int w2, int h2, int type) {
        long size = w2 * h2;
        switch (type) {
            case 3:
                return size * 4;
            case 5:
                return size * 3;
            case 10:
                return size;
            default:
                throw new InternalError("Unrecognized BufferedImage");
        }
    }

    @Override // com.sun.prism.impl.ResourcePool
    public long size(BufferedImage resource) {
        return size(resource.getWidth(), resource.getHeight(), resource.getType());
    }

    @Override // com.sun.prism.impl.TextureResourcePool
    public long estimateTextureSize(int width, int height, PixelFormat format) {
        int type;
        switch (format) {
            case BYTE_RGB:
                type = 5;
                break;
            case BYTE_GRAY:
                type = 10;
                break;
            case INT_ARGB_PRE:
            case BYTE_BGRA_PRE:
                type = 3;
                break;
            default:
                throw new InternalError("Unrecognized PixelFormat (" + ((Object) format) + ")!");
        }
        return size(width, height, type);
    }

    @Override // com.sun.prism.impl.TextureResourcePool
    public long estimateRTTextureSize(int width, int height, boolean hasDepth) {
        return size(width, height, 3);
    }

    public String toString() {
        return "J2D Texture Pool";
    }
}
