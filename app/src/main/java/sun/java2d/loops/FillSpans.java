package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.SpanIterator;

/* loaded from: rt.jar:sun/java2d/loops/FillSpans.class */
public class FillSpans extends GraphicsPrimitive {
    public static final String methodSignature = "FillSpans(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    private native void FillSpans(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, long j2, SpanIterator spanIterator);

    public static FillSpans locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (FillSpans) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected FillSpans(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public FillSpans(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public void FillSpans(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, SpanIterator spanIterator) {
        FillSpans(sunGraphics2D, surfaceData, sunGraphics2D.pixel, spanIterator.getNativeIterator(), spanIterator);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("FillSpans not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceFillSpans(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/FillSpans$TraceFillSpans.class */
    private static class TraceFillSpans extends FillSpans {
        FillSpans target;

        public TraceFillSpans(FillSpans fillSpans) {
            super(fillSpans.getSourceType(), fillSpans.getCompositeType(), fillSpans.getDestType());
            this.target = fillSpans;
        }

        @Override // sun.java2d.loops.FillSpans, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.FillSpans
        public void FillSpans(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, SpanIterator spanIterator) {
            tracePrimitive(this.target);
            this.target.FillSpans(sunGraphics2D, surfaceData, spanIterator);
        }
    }
}
