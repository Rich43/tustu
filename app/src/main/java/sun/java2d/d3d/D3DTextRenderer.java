package sun.java2d.d3d;

import java.awt.Composite;
import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.BufferedTextPipe;
import sun.java2d.pipe.RenderQueue;

/* loaded from: rt.jar:sun/java2d/d3d/D3DTextRenderer.class */
class D3DTextRenderer extends BufferedTextPipe {
    @Override // sun.java2d.pipe.BufferedTextPipe
    protected native void drawGlyphList(int i2, boolean z2, boolean z3, boolean z4, int i3, float f2, float f3, long[] jArr, float[] fArr);

    D3DTextRenderer(RenderQueue renderQueue) {
        super(renderQueue);
    }

    @Override // sun.java2d.pipe.BufferedTextPipe
    protected void validateContext(SunGraphics2D sunGraphics2D, Composite composite) {
        D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sunGraphics2D.surfaceData;
        D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, sunGraphics2D.getCompClip(), composite, null, sunGraphics2D.paint, sunGraphics2D, 0);
    }

    D3DTextRenderer traceWrap() {
        return new Tracer(this);
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DTextRenderer$Tracer.class */
    private static class Tracer extends D3DTextRenderer {
        Tracer(D3DTextRenderer d3DTextRenderer) {
            super(d3DTextRenderer.rq);
        }

        @Override // sun.java2d.pipe.BufferedTextPipe, sun.java2d.pipe.GlyphListPipe
        protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
            GraphicsPrimitive.tracePrimitive("D3DDrawGlyphs");
            super.drawGlyphList(sunGraphics2D, glyphList);
        }
    }
}
