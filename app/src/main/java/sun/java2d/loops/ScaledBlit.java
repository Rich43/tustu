package sun.java2d.loops;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/ScaledBlit.class */
public class ScaledBlit extends GraphicsPrimitive {
    public static final String methodSignature = "ScaledBlit(...)".toString();
    public static final int primTypeID = makePrimTypeID();
    private static RenderCache blitcache = new RenderCache(20);

    public native void Scale(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new ScaledBlit(null, null, null));
    }

    public static ScaledBlit locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (ScaledBlit) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public static ScaledBlit getFromCache(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        Object obj = blitcache.get(surfaceType, compositeType, surfaceType2);
        if (obj != null) {
            return (ScaledBlit) obj;
        }
        ScaledBlit scaledBlitLocate = locate(surfaceType, compositeType, surfaceType2);
        if (scaledBlitLocate != null) {
            blitcache.put(surfaceType, compositeType, surfaceType2, scaledBlitLocate);
        }
        return scaledBlitLocate;
    }

    protected ScaledBlit(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public ScaledBlit(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return null;
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceScaledBlit(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/ScaledBlit$TraceScaledBlit.class */
    private static class TraceScaledBlit extends ScaledBlit {
        ScaledBlit target;

        public TraceScaledBlit(ScaledBlit scaledBlit) {
            super(scaledBlit.getSourceType(), scaledBlit.getCompositeType(), scaledBlit.getDestType());
            this.target = scaledBlit;
        }

        @Override // sun.java2d.loops.ScaledBlit, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.ScaledBlit
        public void Scale(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5) {
            tracePrimitive(this.target);
            this.target.Scale(surfaceData, surfaceData2, composite, region, i2, i3, i4, i5, d2, d3, d4, d5);
        }
    }
}
