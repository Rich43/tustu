package sun.java2d.loops;

import java.awt.Composite;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/MaskBlit.class */
public class MaskBlit extends GraphicsPrimitive {
    public static final String methodSignature = "MaskBlit(...)".toString();
    public static final int primTypeID = makePrimTypeID();
    private static RenderCache blitcache = new RenderCache(20);

    public native void MaskBlit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, byte[] bArr, int i8, int i9);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new MaskBlit(null, null, null));
    }

    public static MaskBlit locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (MaskBlit) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public static MaskBlit getFromCache(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        Object obj = blitcache.get(surfaceType, compositeType, surfaceType2);
        if (obj != null) {
            return (MaskBlit) obj;
        }
        MaskBlit maskBlitLocate = locate(surfaceType, compositeType, surfaceType2);
        if (maskBlitLocate == null) {
            System.out.println("mask blit loop not found for:");
            System.out.println("src:  " + ((Object) surfaceType));
            System.out.println("comp: " + ((Object) compositeType));
            System.out.println("dst:  " + ((Object) surfaceType2));
        } else {
            blitcache.put(surfaceType, compositeType, surfaceType2, maskBlitLocate);
        }
        return maskBlitLocate;
    }

    protected MaskBlit(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public MaskBlit(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        if (CompositeType.Xor.equals(compositeType)) {
            throw new InternalError("Cannot construct MaskBlit for XOR mode");
        }
        General general = new General(surfaceType, compositeType, surfaceType2);
        setupGeneralBinaryOp(general);
        return general;
    }

    /* loaded from: rt.jar:sun/java2d/loops/MaskBlit$General.class */
    private static class General extends MaskBlit implements GraphicsPrimitive.GeneralBinaryOp {
        Blit convertsrc;
        Blit convertdst;
        MaskBlit performop;
        Blit convertresult;
        WeakReference srcTmp;
        WeakReference dstTmp;

        public General(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.GraphicsPrimitive.GeneralBinaryOp
        public void setPrimitives(Blit blit, Blit blit2, GraphicsPrimitive graphicsPrimitive, Blit blit3) {
            this.convertsrc = blit;
            this.convertdst = blit2;
            this.performop = (MaskBlit) graphicsPrimitive;
            this.convertresult = blit3;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.java2d.loops.MaskBlit
        public synchronized void MaskBlit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, byte[] bArr, int i8, int i9) {
            SurfaceData surfaceDataConvertFrom;
            int i10;
            int i11;
            SurfaceData surfaceDataConvertFrom2;
            int i12;
            int i13;
            Region region2;
            if (this.convertsrc == null) {
                surfaceDataConvertFrom = surfaceData;
                i10 = i2;
                i11 = i3;
            } else {
                SurfaceData surfaceData3 = null;
                if (this.srcTmp != null) {
                    surfaceData3 = (SurfaceData) this.srcTmp.get();
                }
                surfaceDataConvertFrom = convertFrom(this.convertsrc, surfaceData, i2, i3, i6, i7, surfaceData3);
                i10 = 0;
                i11 = 0;
                if (surfaceDataConvertFrom != surfaceData3) {
                    this.srcTmp = new WeakReference(surfaceDataConvertFrom);
                }
            }
            if (this.convertdst == null) {
                surfaceDataConvertFrom2 = surfaceData2;
                i12 = i4;
                i13 = i5;
                region2 = region;
            } else {
                SurfaceData surfaceData4 = null;
                if (this.dstTmp != null) {
                    surfaceData4 = (SurfaceData) this.dstTmp.get();
                }
                surfaceDataConvertFrom2 = convertFrom(this.convertdst, surfaceData2, i4, i5, i6, i7, surfaceData4);
                i12 = 0;
                i13 = 0;
                region2 = null;
                if (surfaceDataConvertFrom2 != surfaceData4) {
                    this.dstTmp = new WeakReference(surfaceDataConvertFrom2);
                }
            }
            this.performop.MaskBlit(surfaceDataConvertFrom, surfaceDataConvertFrom2, composite, region2, i10, i11, i12, i13, i6, i7, bArr, i8, i9);
            if (this.convertresult != null) {
                convertTo(this.convertresult, surfaceDataConvertFrom2, surfaceData2, region, i4, i5, i6, i7);
            }
        }
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceMaskBlit(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/MaskBlit$TraceMaskBlit.class */
    private static class TraceMaskBlit extends MaskBlit {
        MaskBlit target;

        public TraceMaskBlit(MaskBlit maskBlit) {
            super(maskBlit.getNativePrim(), maskBlit.getSourceType(), maskBlit.getCompositeType(), maskBlit.getDestType());
            this.target = maskBlit;
        }

        @Override // sun.java2d.loops.MaskBlit, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.MaskBlit
        public void MaskBlit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, byte[] bArr, int i8, int i9) {
            tracePrimitive(this.target);
            this.target.MaskBlit(surfaceData, surfaceData2, composite, region, i2, i3, i4, i5, i6, i7, bArr, i8, i9);
        }
    }
}
