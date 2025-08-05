package sun.java2d.opengl;

import java.awt.Composite;
import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.BufferedTextPipe;
import sun.java2d.pipe.RenderQueue;

/* loaded from: rt.jar:sun/java2d/opengl/OGLTextRenderer.class */
class OGLTextRenderer extends BufferedTextPipe {
    @Override // sun.java2d.pipe.BufferedTextPipe
    protected native void drawGlyphList(int i2, boolean z2, boolean z3, boolean z4, int i3, float f2, float f3, long[] jArr, float[] fArr);

    OGLTextRenderer(RenderQueue renderQueue) {
        super(renderQueue);
    }

    @Override // sun.java2d.pipe.BufferedTextPipe
    protected void validateContext(SunGraphics2D sunGraphics2D, Composite composite) {
        OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sunGraphics2D.surfaceData;
        OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, sunGraphics2D.getCompClip(), composite, null, sunGraphics2D.paint, sunGraphics2D, 0);
    }

    OGLTextRenderer traceWrap() {
        return new Tracer(this);
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLTextRenderer$Tracer.class */
    private static class Tracer extends OGLTextRenderer {
        Tracer(OGLTextRenderer oGLTextRenderer) {
            super(oGLTextRenderer.rq);
        }

        @Override // sun.java2d.pipe.BufferedTextPipe, sun.java2d.pipe.GlyphListPipe
        protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
            GraphicsPrimitive.tracePrimitive("OGLDrawGlyphs");
            super.drawGlyphList(sunGraphics2D, glyphList);
        }
    }
}
