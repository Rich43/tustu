package sun.java2d.loops;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/DrawGlyphList.class */
public class DrawGlyphList extends GraphicsPrimitive {
    public static final String methodSignature = "DrawGlyphList(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawGlyphList(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new DrawGlyphList(null, null, null));
    }

    public static DrawGlyphList locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawGlyphList) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawGlyphList(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawGlyphList(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return new General(surfaceType, compositeType, surfaceType2);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawGlyphList$General.class */
    private static class General extends DrawGlyphList {
        MaskFill maskop;

        public General(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
            this.maskop = MaskFill.locate(surfaceType, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.DrawGlyphList
        public void DrawGlyphList(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
            glyphList.getBounds();
            int numGlyphs = glyphList.getNumGlyphs();
            Region compClip = sunGraphics2D.getCompClip();
            int loX = compClip.getLoX();
            int loY = compClip.getLoY();
            int hiX = compClip.getHiX();
            int hiY = compClip.getHiY();
            for (int i2 = 0; i2 < numGlyphs; i2++) {
                glyphList.setGlyphIndex(i2);
                int[] metrics = glyphList.getMetrics();
                int i3 = metrics[0];
                int i4 = metrics[1];
                int i5 = metrics[2];
                int i6 = i3 + i5;
                int i7 = i4 + metrics[3];
                int i8 = 0;
                if (i3 < loX) {
                    i8 = loX - i3;
                    i3 = loX;
                }
                if (i4 < loY) {
                    i8 += (loY - i4) * i5;
                    i4 = loY;
                }
                if (i6 > hiX) {
                    i6 = hiX;
                }
                if (i7 > hiY) {
                    i7 = hiY;
                }
                if (i6 > i3 && i7 > i4) {
                    this.maskop.MaskFill(sunGraphics2D, surfaceData, sunGraphics2D.composite, i3, i4, i6 - i3, i7 - i4, glyphList.getGrayBits(), i8, i5);
                }
            }
        }
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceDrawGlyphList(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawGlyphList$TraceDrawGlyphList.class */
    private static class TraceDrawGlyphList extends DrawGlyphList {
        DrawGlyphList target;

        public TraceDrawGlyphList(DrawGlyphList drawGlyphList) {
            super(drawGlyphList.getSourceType(), drawGlyphList.getCompositeType(), drawGlyphList.getDestType());
            this.target = drawGlyphList;
        }

        @Override // sun.java2d.loops.DrawGlyphList, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawGlyphList
        public void DrawGlyphList(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
            tracePrimitive(this.target);
            this.target.DrawGlyphList(sunGraphics2D, surfaceData, glyphList);
        }
    }
}
