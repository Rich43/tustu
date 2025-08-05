package sun.java2d.loops;

import java.awt.Composite;
import java.awt.image.BufferedImage;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/MaskFill.class */
public class MaskFill extends GraphicsPrimitive {
    public static final String methodSignature = "MaskFill(...)".toString();
    public static final String fillPgramSignature = "FillAAPgram(...)".toString();
    public static final String drawPgramSignature = "DrawAAPgram(...)".toString();
    public static final int primTypeID = makePrimTypeID();
    private static RenderCache fillcache = new RenderCache(10);

    public native void MaskFill(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7);

    public native void FillAAPgram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, double d2, double d3, double d4, double d5, double d6, double d7);

    public native void DrawAAPgram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new MaskFill(null, null, null));
    }

    public static MaskFill locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (MaskFill) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public static MaskFill locatePrim(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (MaskFill) GraphicsPrimitiveMgr.locatePrim(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public static MaskFill getFromCache(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        Object obj = fillcache.get(surfaceType, compositeType, surfaceType2);
        if (obj != null) {
            return (MaskFill) obj;
        }
        MaskFill maskFillLocatePrim = locatePrim(surfaceType, compositeType, surfaceType2);
        if (maskFillLocatePrim != null) {
            fillcache.put(surfaceType, compositeType, surfaceType2, maskFillLocatePrim);
        }
        return maskFillLocatePrim;
    }

    protected MaskFill(String str, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(str, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected MaskFill(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public MaskFill(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public boolean canDoParallelograms() {
        return getNativePrim() != 0;
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        if (SurfaceType.OpaqueColor.equals(surfaceType) || SurfaceType.AnyColor.equals(surfaceType)) {
            if (CompositeType.Xor.equals(compositeType)) {
                throw new InternalError("Cannot construct MaskFill for XOR mode");
            }
            return new General(surfaceType, compositeType, surfaceType2);
        }
        throw new InternalError("MaskFill can only fill with colors");
    }

    /* loaded from: rt.jar:sun/java2d/loops/MaskFill$General.class */
    private static class General extends MaskFill {
        FillRect fillop;
        MaskBlit maskop;

        public General(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
            this.fillop = FillRect.locate(surfaceType, CompositeType.SrcNoEa, SurfaceType.IntArgb);
            this.maskop = MaskBlit.locate(SurfaceType.IntArgb, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.MaskFill
        public void MaskFill(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7) {
            SurfaceData surfaceDataCreateData = BufImgSurfaceData.createData(new BufferedImage(i4, i5, 2));
            Region region = sunGraphics2D.clipRegion;
            sunGraphics2D.clipRegion = null;
            int i8 = sunGraphics2D.pixel;
            sunGraphics2D.pixel = surfaceDataCreateData.pixelFor(sunGraphics2D.getColor());
            this.fillop.FillRect(sunGraphics2D, surfaceDataCreateData, 0, 0, i4, i5);
            sunGraphics2D.pixel = i8;
            sunGraphics2D.clipRegion = region;
            this.maskop.MaskBlit(surfaceDataCreateData, surfaceData, composite, null, 0, 0, i2, i3, i4, i5, bArr, i6, i7);
        }
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceMaskFill(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/MaskFill$TraceMaskFill.class */
    private static class TraceMaskFill extends MaskFill {
        MaskFill target;
        MaskFill fillPgramTarget;
        MaskFill drawPgramTarget;

        public TraceMaskFill(MaskFill maskFill) {
            super(maskFill.getSourceType(), maskFill.getCompositeType(), maskFill.getDestType());
            this.target = maskFill;
            this.fillPgramTarget = new MaskFill(fillPgramSignature, maskFill.getSourceType(), maskFill.getCompositeType(), maskFill.getDestType());
            this.drawPgramTarget = new MaskFill(drawPgramSignature, maskFill.getSourceType(), maskFill.getCompositeType(), maskFill.getDestType());
        }

        @Override // sun.java2d.loops.MaskFill, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.MaskFill
        public void MaskFill(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7) {
            tracePrimitive(this.target);
            this.target.MaskFill(sunGraphics2D, surfaceData, composite, i2, i3, i4, i5, bArr, i6, i7);
        }

        @Override // sun.java2d.loops.MaskFill
        public void FillAAPgram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, double d2, double d3, double d4, double d5, double d6, double d7) {
            tracePrimitive(this.fillPgramTarget);
            this.target.FillAAPgram(sunGraphics2D, surfaceData, composite, d2, d3, d4, d5, d6, d7);
        }

        @Override // sun.java2d.loops.MaskFill
        public void DrawAAPgram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            tracePrimitive(this.drawPgramTarget);
            this.target.DrawAAPgram(sunGraphics2D, surfaceData, composite, d2, d3, d4, d5, d6, d7, d8, d9);
        }

        @Override // sun.java2d.loops.MaskFill
        public boolean canDoParallelograms() {
            return this.target.canDoParallelograms();
        }
    }
}
