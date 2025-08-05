package sun.java2d.opengl;

import java.awt.geom.Path2D;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.BufferedRenderPipe;
import sun.java2d.pipe.ParallelogramPipe;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.SpanIterator;

/* loaded from: rt.jar:sun/java2d/opengl/OGLRenderer.class */
class OGLRenderer extends BufferedRenderPipe {
    @Override // sun.java2d.pipe.BufferedRenderPipe
    protected native void drawPoly(int[] iArr, int[] iArr2, int i2, boolean z2, int i3, int i4);

    OGLRenderer(RenderQueue renderQueue) {
        super(renderQueue);
    }

    @Override // sun.java2d.pipe.BufferedRenderPipe
    protected void validateContext(SunGraphics2D sunGraphics2D) {
        int i2 = sunGraphics2D.paint.getTransparency() == 1 ? 1 : 0;
        try {
            OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sunGraphics2D.surfaceData;
            OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, null, sunGraphics2D.paint, sunGraphics2D, i2);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.BufferedRenderPipe
    protected void validateContextAA(SunGraphics2D sunGraphics2D) {
        try {
            OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sunGraphics2D.surfaceData;
            OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, null, sunGraphics2D.paint, sunGraphics2D, 0);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    void copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.rq.lock();
        try {
            int i8 = sunGraphics2D.surfaceData.getTransparency() == 1 ? 1 : 0;
            try {
                OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sunGraphics2D.surfaceData;
                OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, null, null, null, i8);
                this.rq.ensureCapacity(28);
                this.buf.putInt(30);
                this.buf.putInt(i2).putInt(i3).putInt(i4).putInt(i5);
                this.buf.putInt(i6).putInt(i7);
                this.rq.unlock();
            } catch (ClassCastException e2) {
                throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
            }
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    OGLRenderer traceWrap() {
        return new Tracer(this);
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLRenderer$Tracer.class */
    private class Tracer extends OGLRenderer {
        private OGLRenderer oglr;

        Tracer(OGLRenderer oGLRenderer) {
            super(oGLRenderer.rq);
            this.oglr = oGLRenderer;
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe
        public ParallelogramPipe getAAParallelogramPipe() {
            final ParallelogramPipe aAParallelogramPipe = this.oglr.getAAParallelogramPipe();
            return new ParallelogramPipe() { // from class: sun.java2d.opengl.OGLRenderer.Tracer.1
                @Override // sun.java2d.pipe.ParallelogramPipe
                public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
                    GraphicsPrimitive.tracePrimitive("OGLFillAAParallelogram");
                    aAParallelogramPipe.fillParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11);
                }

                @Override // sun.java2d.pipe.ParallelogramPipe
                public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
                    GraphicsPrimitive.tracePrimitive("OGLDrawAAParallelogram");
                    aAParallelogramPipe.drawParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
                }
            };
        }

        @Override // sun.java2d.opengl.OGLRenderer, sun.java2d.pipe.BufferedRenderPipe
        protected void validateContext(SunGraphics2D sunGraphics2D) {
            this.oglr.validateContext(sunGraphics2D);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.PixelDrawPipe
        public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
            GraphicsPrimitive.tracePrimitive("OGLDrawLine");
            this.oglr.drawLine(sunGraphics2D, i2, i3, i4, i5);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.PixelDrawPipe
        public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
            GraphicsPrimitive.tracePrimitive("OGLDrawRect");
            this.oglr.drawRect(sunGraphics2D, i2, i3, i4, i5);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void drawPoly(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2, boolean z2) {
            GraphicsPrimitive.tracePrimitive("OGLDrawPoly");
            this.oglr.drawPoly(sunGraphics2D, iArr, iArr2, i2, z2);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.PixelFillPipe
        public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
            GraphicsPrimitive.tracePrimitive("OGLFillRect");
            this.oglr.fillRect(sunGraphics2D, i2, i3, i4, i5);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void drawPath(SunGraphics2D sunGraphics2D, Path2D.Float r8, int i2, int i3) {
            GraphicsPrimitive.tracePrimitive("OGLDrawPath");
            this.oglr.drawPath(sunGraphics2D, r8, i2, i3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void fillPath(SunGraphics2D sunGraphics2D, Path2D.Float r8, int i2, int i3) {
            GraphicsPrimitive.tracePrimitive("OGLFillPath");
            this.oglr.fillPath(sunGraphics2D, r8, i2, i3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void fillSpans(SunGraphics2D sunGraphics2D, SpanIterator spanIterator, int i2, int i3) {
            GraphicsPrimitive.tracePrimitive("OGLFillSpans");
            this.oglr.fillSpans(sunGraphics2D, spanIterator, i2, i3);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.ParallelogramPipe
        public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
            GraphicsPrimitive.tracePrimitive("OGLFillParallelogram");
            this.oglr.fillParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.ParallelogramPipe
        public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
            GraphicsPrimitive.tracePrimitive("OGLDrawParallelogram");
            this.oglr.drawParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        }

        @Override // sun.java2d.opengl.OGLRenderer
        public void copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
            GraphicsPrimitive.tracePrimitive("OGLCopyArea");
            this.oglr.copyArea(sunGraphics2D, i2, i3, i4, i5, i6, i7);
        }
    }
}
