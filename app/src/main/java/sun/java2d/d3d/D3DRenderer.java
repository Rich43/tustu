package sun.java2d.d3d;

import java.awt.geom.Path2D;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.BufferedRenderPipe;
import sun.java2d.pipe.ParallelogramPipe;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.SpanIterator;

/* loaded from: rt.jar:sun/java2d/d3d/D3DRenderer.class */
class D3DRenderer extends BufferedRenderPipe {
    @Override // sun.java2d.pipe.BufferedRenderPipe
    protected native void drawPoly(int[] iArr, int[] iArr2, int i2, boolean z2, int i3, int i4);

    D3DRenderer(RenderQueue renderQueue) {
        super(renderQueue);
    }

    @Override // sun.java2d.pipe.BufferedRenderPipe
    protected void validateContext(SunGraphics2D sunGraphics2D) {
        int i2 = sunGraphics2D.paint.getTransparency() == 1 ? 1 : 0;
        try {
            D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sunGraphics2D.surfaceData;
            D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, null, sunGraphics2D.paint, sunGraphics2D, i2);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.BufferedRenderPipe
    protected void validateContextAA(SunGraphics2D sunGraphics2D) {
        try {
            D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sunGraphics2D.surfaceData;
            D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, null, sunGraphics2D.paint, sunGraphics2D, 0);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    void copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.rq.lock();
        try {
            int i8 = sunGraphics2D.surfaceData.getTransparency() == 1 ? 1 : 0;
            try {
                D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sunGraphics2D.surfaceData;
                D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, null, null, null, i8);
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

    D3DRenderer traceWrap() {
        return new Tracer(this);
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DRenderer$Tracer.class */
    private class Tracer extends D3DRenderer {
        private D3DRenderer d3dr;

        Tracer(D3DRenderer d3DRenderer) {
            super(d3DRenderer.rq);
            this.d3dr = d3DRenderer;
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe
        public ParallelogramPipe getAAParallelogramPipe() {
            final ParallelogramPipe aAParallelogramPipe = this.d3dr.getAAParallelogramPipe();
            return new ParallelogramPipe() { // from class: sun.java2d.d3d.D3DRenderer.Tracer.1
                @Override // sun.java2d.pipe.ParallelogramPipe
                public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
                    GraphicsPrimitive.tracePrimitive("D3DFillAAParallelogram");
                    aAParallelogramPipe.fillParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11);
                }

                @Override // sun.java2d.pipe.ParallelogramPipe
                public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
                    GraphicsPrimitive.tracePrimitive("D3DDrawAAParallelogram");
                    aAParallelogramPipe.drawParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
                }
            };
        }

        @Override // sun.java2d.d3d.D3DRenderer, sun.java2d.pipe.BufferedRenderPipe
        protected void validateContext(SunGraphics2D sunGraphics2D) {
            this.d3dr.validateContext(sunGraphics2D);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.PixelDrawPipe
        public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
            GraphicsPrimitive.tracePrimitive("D3DDrawLine");
            this.d3dr.drawLine(sunGraphics2D, i2, i3, i4, i5);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.PixelDrawPipe
        public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
            GraphicsPrimitive.tracePrimitive("D3DDrawRect");
            this.d3dr.drawRect(sunGraphics2D, i2, i3, i4, i5);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void drawPoly(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2, boolean z2) {
            GraphicsPrimitive.tracePrimitive("D3DDrawPoly");
            this.d3dr.drawPoly(sunGraphics2D, iArr, iArr2, i2, z2);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.PixelFillPipe
        public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
            GraphicsPrimitive.tracePrimitive("D3DFillRect");
            this.d3dr.fillRect(sunGraphics2D, i2, i3, i4, i5);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void drawPath(SunGraphics2D sunGraphics2D, Path2D.Float r8, int i2, int i3) {
            GraphicsPrimitive.tracePrimitive("D3DDrawPath");
            this.d3dr.drawPath(sunGraphics2D, r8, i2, i3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void fillPath(SunGraphics2D sunGraphics2D, Path2D.Float r8, int i2, int i3) {
            GraphicsPrimitive.tracePrimitive("D3DFillPath");
            this.d3dr.fillPath(sunGraphics2D, r8, i2, i3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.pipe.BufferedRenderPipe
        public void fillSpans(SunGraphics2D sunGraphics2D, SpanIterator spanIterator, int i2, int i3) {
            GraphicsPrimitive.tracePrimitive("D3DFillSpans");
            this.d3dr.fillSpans(sunGraphics2D, spanIterator, i2, i3);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.ParallelogramPipe
        public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
            GraphicsPrimitive.tracePrimitive("D3DFillParallelogram");
            this.d3dr.fillParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11);
        }

        @Override // sun.java2d.pipe.BufferedRenderPipe, sun.java2d.pipe.ParallelogramPipe
        public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
            GraphicsPrimitive.tracePrimitive("D3DDrawParallelogram");
            this.d3dr.drawParallelogram(sunGraphics2D, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        }

        @Override // sun.java2d.d3d.D3DRenderer
        public void copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
            GraphicsPrimitive.tracePrimitive("D3DCopyArea");
            this.d3dr.copyArea(sunGraphics2D, i2, i3, i4, i5, i6, i7);
        }
    }
}
