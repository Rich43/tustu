package sun.java2d.loops;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/loops/DrawGlyphListLCD.class */
public class DrawGlyphListLCD extends GraphicsPrimitive {
    public static final String methodSignature = "DrawGlyphListLCD(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawGlyphListLCD(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new DrawGlyphListLCD(null, null, null));
    }

    public static DrawGlyphListLCD locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawGlyphListLCD) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawGlyphListLCD(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawGlyphListLCD(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return null;
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawGlyphListLCD(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawGlyphListLCD$TraceDrawGlyphListLCD.class */
    private static class TraceDrawGlyphListLCD extends DrawGlyphListLCD {
        DrawGlyphListLCD target;

        public TraceDrawGlyphListLCD(DrawGlyphListLCD drawGlyphListLCD) {
            super(drawGlyphListLCD.getSourceType(), drawGlyphListLCD.getCompositeType(), drawGlyphListLCD.getDestType());
            this.target = drawGlyphListLCD;
        }

        @Override // sun.java2d.loops.DrawGlyphListLCD, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawGlyphListLCD
        public void DrawGlyphListLCD(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
            tracePrimitive(this.target);
            this.target.DrawGlyphListLCD(sunGraphics2D, surfaceData, glyphList);
        }
    }
}
