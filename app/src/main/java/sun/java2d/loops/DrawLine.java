package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/DrawLine.class */
public class DrawLine extends GraphicsPrimitive {
    public static final String methodSignature = "DrawLine(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawLine(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5);

    public static DrawLine locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawLine) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawLine(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawLine(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("DrawLine not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawLine(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawLine$TraceDrawLine.class */
    private static class TraceDrawLine extends DrawLine {
        DrawLine target;

        public TraceDrawLine(DrawLine drawLine) {
            super(drawLine.getSourceType(), drawLine.getCompositeType(), drawLine.getDestType());
            this.target = drawLine;
        }

        @Override // sun.java2d.loops.DrawLine, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawLine
        public void DrawLine(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
            tracePrimitive(this.target);
            this.target.DrawLine(sunGraphics2D, surfaceData, i2, i3, i4, i5);
        }
    }
}
