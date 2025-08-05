package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/FillRect.class */
public class FillRect extends GraphicsPrimitive {
    public static final String methodSignature = "FillRect(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void FillRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new FillRect(null, null, null));
    }

    public static FillRect locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (FillRect) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected FillRect(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public FillRect(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return new General(surfaceType, compositeType, surfaceType2);
    }

    /* loaded from: rt.jar:sun/java2d/loops/FillRect$General.class */
    public static class General extends FillRect {
        public MaskFill fillop;

        public General(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
            this.fillop = MaskFill.locate(surfaceType, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.FillRect
        public void FillRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
            this.fillop.MaskFill(sunGraphics2D, surfaceData, sunGraphics2D.composite, i2, i3, i4, i5, null, 0, 0);
        }
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceFillRect(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/FillRect$TraceFillRect.class */
    private static class TraceFillRect extends FillRect {
        FillRect target;

        public TraceFillRect(FillRect fillRect) {
            super(fillRect.getSourceType(), fillRect.getCompositeType(), fillRect.getDestType());
            this.target = fillRect;
        }

        @Override // sun.java2d.loops.FillRect, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.FillRect
        public void FillRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
            tracePrimitive(this.target);
            this.target.FillRect(sunGraphics2D, surfaceData, i2, i3, i4, i5);
        }
    }
}
