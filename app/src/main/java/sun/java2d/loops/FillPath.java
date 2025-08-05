package sun.java2d.loops;

import java.awt.geom.Path2D;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/FillPath.class */
public class FillPath extends GraphicsPrimitive {
    public static final String methodSignature = "FillPath(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void FillPath(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, Path2D.Float r5);

    public static FillPath locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (FillPath) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected FillPath(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public FillPath(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("FillPath not implemented for " + ((Object) surfaceType) + " with " + ((Object) compositeType));
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceFillPath(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/FillPath$TraceFillPath.class */
    private static class TraceFillPath extends FillPath {
        FillPath target;

        public TraceFillPath(FillPath fillPath) {
            super(fillPath.getSourceType(), fillPath.getCompositeType(), fillPath.getDestType());
            this.target = fillPath;
        }

        @Override // sun.java2d.loops.FillPath, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.FillPath
        public void FillPath(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, Path2D.Float r12) {
            tracePrimitive(this.target);
            this.target.FillPath(sunGraphics2D, surfaceData, i2, i3, r12);
        }
    }
}
