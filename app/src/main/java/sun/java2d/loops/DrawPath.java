package sun.java2d.loops;

import java.awt.geom.Path2D;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/DrawPath.class */
public class DrawPath extends GraphicsPrimitive {
    public static final String methodSignature = "DrawPath(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawPath(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, Path2D.Float r5);

    public static DrawPath locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawPath) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawPath(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawPath(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("DrawPath not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawPath(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawPath$TraceDrawPath.class */
    private static class TraceDrawPath extends DrawPath {
        DrawPath target;

        public TraceDrawPath(DrawPath drawPath) {
            super(drawPath.getSourceType(), drawPath.getCompositeType(), drawPath.getDestType());
            this.target = drawPath;
        }

        @Override // sun.java2d.loops.DrawPath, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawPath
        public void DrawPath(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, Path2D.Float r12) {
            tracePrimitive(this.target);
            this.target.DrawPath(sunGraphics2D, surfaceData, i2, i3, r12);
        }
    }
}
