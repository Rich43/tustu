package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/FillParallelogram.class */
public class FillParallelogram extends GraphicsPrimitive {
    public static final String methodSignature = "FillParallelogram(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void FillParallelogram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, double d2, double d3, double d4, double d5, double d6, double d7);

    public static FillParallelogram locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (FillParallelogram) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected FillParallelogram(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public FillParallelogram(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("FillParallelogram not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceFillParallelogram(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/FillParallelogram$TraceFillParallelogram.class */
    private static class TraceFillParallelogram extends FillParallelogram {
        FillParallelogram target;

        public TraceFillParallelogram(FillParallelogram fillParallelogram) {
            super(fillParallelogram.getSourceType(), fillParallelogram.getCompositeType(), fillParallelogram.getDestType());
            this.target = fillParallelogram;
        }

        @Override // sun.java2d.loops.FillParallelogram, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.FillParallelogram
        public void FillParallelogram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, double d2, double d3, double d4, double d5, double d6, double d7) {
            tracePrimitive(this.target);
            this.target.FillParallelogram(sunGraphics2D, surfaceData, d2, d3, d4, d5, d6, d7);
        }
    }
}
