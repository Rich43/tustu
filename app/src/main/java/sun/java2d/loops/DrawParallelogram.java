package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/DrawParallelogram.class */
public class DrawParallelogram extends GraphicsPrimitive {
    public static final String methodSignature = "DrawParallelogram(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawParallelogram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    public static DrawParallelogram locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawParallelogram) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawParallelogram(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawParallelogram(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("DrawParallelogram not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawParallelogram(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawParallelogram$TraceDrawParallelogram.class */
    private static class TraceDrawParallelogram extends DrawParallelogram {
        DrawParallelogram target;

        public TraceDrawParallelogram(DrawParallelogram drawParallelogram) {
            super(drawParallelogram.getSourceType(), drawParallelogram.getCompositeType(), drawParallelogram.getDestType());
            this.target = drawParallelogram;
        }

        @Override // sun.java2d.loops.DrawParallelogram, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawParallelogram
        public void DrawParallelogram(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            tracePrimitive(this.target);
            this.target.DrawParallelogram(sunGraphics2D, surfaceData, d2, d3, d4, d5, d6, d7, d8, d9);
        }
    }
}
