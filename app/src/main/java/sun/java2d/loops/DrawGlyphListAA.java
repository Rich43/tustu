package sun.java2d.loops;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/DrawGlyphListAA.class */
public class DrawGlyphListAA extends GraphicsPrimitive {
    public static final String methodSignature = "DrawGlyphListAA(...)".toString();
    public static final int primTypeID = makePrimTypeID();

    public native void DrawGlyphListAA(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new DrawGlyphListAA(null, null, null));
    }

    public static DrawGlyphListAA locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (DrawGlyphListAA) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    protected DrawGlyphListAA(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public DrawGlyphListAA(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return new General(surfaceType, compositeType, surfaceType2);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawGlyphListAA$General.class */
    public static class General extends DrawGlyphListAA {
        MaskFill maskop;

        public General(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
            this.maskop = MaskFill.locate(surfaceType, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.DrawGlyphListAA
        public void DrawGlyphListAA(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
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
        return new TraceDrawGlyphListAA(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/DrawGlyphListAA$TraceDrawGlyphListAA.class */
    private static class TraceDrawGlyphListAA extends DrawGlyphListAA {
        DrawGlyphListAA target;

        public TraceDrawGlyphListAA(DrawGlyphListAA drawGlyphListAA) {
            super(drawGlyphListAA.getSourceType(), drawGlyphListAA.getCompositeType(), drawGlyphListAA.getDestType());
            this.target = drawGlyphListAA;
        }

        @Override // sun.java2d.loops.DrawGlyphListAA, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.DrawGlyphListAA
        public void DrawGlyphListAA(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
            tracePrimitive(this.target);
            this.target.DrawGlyphListAA(sunGraphics2D, surfaceData, glyphList);
        }
    }
}
