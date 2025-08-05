package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/DrawRect.class */
public class DrawRect extends GraphicsPrimitive {
    public static final String methodSignature = "DrawRect(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5);

    public static DrawRect locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawRect) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawRect(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawRect(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("DrawRect not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawRect(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawRect$TraceDrawRect.class */
    private static class TraceDrawRect extends DrawRect {
        DrawRect target;

        public TraceDrawRect(DrawRect drawRect) {
            super(drawRect.getSourceType(), drawRect.getCompositeType(), drawRect.getDestType());
            this.target = drawRect;
        }

        @Override // sun.java2d.loops.DrawRect, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawRect
        public void DrawRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
            tracePrimitive(this.target);
            this.target.DrawRect(sunGraphics2D, surfaceData, i2, i3, i4, i5);
        }
    }
}
