package sun.java2d.loops;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/TransformHelper.class */
public class TransformHelper extends GraphicsPrimitive {
    public static final String methodSignature = "TransformHelper(...)".toString();
    public static final int primTypeID = makePrimTypeID();
    private static RenderCache helpercache = new RenderCache(10);

    public native void Transform(MaskBlit maskBlit, SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int[] iArr, int i11, int i12);

    public static TransformHelper locate(SurfaceType surfaceType) {
        return (TransformHelper) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
    }

    public static synchronized TransformHelper getFromCache(SurfaceType surfaceType) {
        Object obj = helpercache.get(surfaceType, null, null);
        if (obj != null) {
            return (TransformHelper) obj;
        }
        TransformHelper transformHelperLocate = locate(surfaceType);
        if (transformHelperLocate != null) {
            helpercache.put(surfaceType, null, null, transformHelperLocate);
        }
        return transformHelperLocate;
    }

    protected TransformHelper(SurfaceType surfaceType) {
        super(methodSignature, primTypeID, surfaceType, CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
    }

    public TransformHelper(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return null;
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceTransformHelper(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/TransformHelper$TraceTransformHelper.class */
    private static class TraceTransformHelper extends TransformHelper {
        TransformHelper target;

        public TraceTransformHelper(TransformHelper transformHelper) {
            super(transformHelper.getSourceType());
            this.target = transformHelper;
        }

        @Override // sun.java2d.loops.TransformHelper, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.TransformHelper
        public void Transform(MaskBlit maskBlit, SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int[] iArr, int i11, int i12) {
            tracePrimitive(this.target);
            this.target.Transform(maskBlit, surfaceData, surfaceData2, composite, region, affineTransform, i2, i3, i4, i5, i6, i7, i8, i9, i10, iArr, i11, i12);
        }
    }
}
