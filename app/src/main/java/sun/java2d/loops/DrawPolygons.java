package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/DrawPolygons.class */
public class DrawPolygons extends GraphicsPrimitive {
    public static final String methodSignature = "DrawPolygons(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawPolygons(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int[] iArr, int[] iArr2, int[] iArr3, int i2, int i3, int i4, boolean z2);

    public static DrawPolygons locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawPolygons) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawPolygons(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawPolygons(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("DrawPolygons not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawPolygons(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawPolygons$TraceDrawPolygons.class */
    private static class TraceDrawPolygons extends DrawPolygons {
        DrawPolygons target;

        public TraceDrawPolygons(DrawPolygons drawPolygons) {
            super(drawPolygons.getSourceType(), drawPolygons.getCompositeType(), drawPolygons.getDestType());
            this.target = drawPolygons;
        }

        @Override // sun.java2d.loops.DrawPolygons, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawPolygons
        public void DrawPolygons(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int[] iArr, int[] iArr2, int[] iArr3, int i2, int i3, int i4, boolean z2) {
            tracePrimitive(this.target);
            this.target.DrawPolygons(sunGraphics2D, surfaceData, iArr, iArr2, iArr3, i2, i3, i4, z2);
        }
    }
}
