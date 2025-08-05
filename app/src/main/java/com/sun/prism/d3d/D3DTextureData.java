package com.sun.prism.d3d;

import com.sun.prism.PixelFormat;
import com.sun.prism.d3d.D3DResource;
import com.sun.prism.impl.PrismTrace;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DTextureData.class */
public class D3DTextureData extends D3DResource.D3DRecord {
    private final long size;
    private final boolean isRTT;
    private final int samples;

    static long estimateSize(int physicalWidth, int physicalHeight, PixelFormat format) {
        return physicalWidth * physicalHeight * format.getBytesPerPixelUnit();
    }

    static long estimateRTSize(int physicalWidth, int physicalHeight, boolean hasDepth) {
        return physicalWidth * physicalHeight * 4;
    }

    D3DTextureData(D3DContext context, long pResource, boolean isRTT, int physicalWidth, int physicalHeight, PixelFormat format, int numberOfSamples) {
        long jEstimateSize;
        super(context, pResource);
        if (isRTT) {
            jEstimateSize = estimateRTSize(physicalWidth, physicalHeight, false);
        } else {
            jEstimateSize = estimateSize(physicalWidth, physicalHeight, format);
        }
        this.size = jEstimateSize;
        this.isRTT = isRTT;
        this.samples = numberOfSamples;
        if (isRTT) {
            PrismTrace.rttCreated(pResource, physicalWidth, physicalHeight, this.size);
        } else {
            PrismTrace.textureCreated(pResource, physicalWidth, physicalHeight, this.size);
        }
    }

    int getSamples() {
        return this.samples;
    }

    long getSize() {
        return this.size;
    }

    @Override // com.sun.prism.d3d.D3DResource.D3DRecord
    protected void markDisposed() {
        long pResource = getResource();
        if (pResource != 0) {
            if (this.isRTT) {
                PrismTrace.rttDisposed(pResource);
            } else {
                PrismTrace.textureDisposed(pResource);
            }
        }
        super.markDisposed();
    }

    @Override // com.sun.prism.d3d.D3DResource.D3DRecord, com.sun.prism.impl.Disposer.Record
    public void dispose() {
        long pResource = getResource();
        if (pResource != 0) {
            if (this.isRTT) {
                PrismTrace.rttDisposed(pResource);
            } else {
                PrismTrace.textureDisposed(pResource);
            }
        }
        super.dispose();
    }
}
