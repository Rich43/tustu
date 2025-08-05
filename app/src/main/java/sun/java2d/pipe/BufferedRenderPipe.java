package sun.java2d.pipe;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.ProcessPath;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedRenderPipe.class */
public abstract class BufferedRenderPipe implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe, ParallelogramPipe {
    static final int BYTES_PER_POLY_POINT = 8;
    static final int BYTES_PER_SCANLINE = 12;
    static final int BYTES_PER_SPAN = 16;
    protected RenderQueue rq;
    protected RenderBuffer buf;
    ParallelogramPipe aapgrampipe = new AAParallelogramPipe();
    private BufferedDrawHandler drawHandler = new BufferedDrawHandler();

    protected abstract void validateContext(SunGraphics2D sunGraphics2D);

    protected abstract void validateContextAA(SunGraphics2D sunGraphics2D);

    protected abstract void drawPoly(int[] iArr, int[] iArr2, int i2, boolean z2, int i3, int i4);

    private native int fillSpans(RenderQueue renderQueue, long j2, int i2, int i3, SpanIterator spanIterator, long j3, int i4, int i5);

    public BufferedRenderPipe(RenderQueue renderQueue) {
        this.rq = renderQueue;
        this.buf = renderQueue.getBuffer();
    }

    public ParallelogramPipe getAAParallelogramPipe() {
        return this.aapgrampipe;
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        int i6 = sunGraphics2D.transX;
        int i7 = sunGraphics2D.transY;
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.rq.ensureCapacity(20);
            this.buf.putInt(10);
            this.buf.putInt(i2 + i6);
            this.buf.putInt(i3 + i7);
            this.buf.putInt(i4 + i6);
            this.buf.putInt(i5 + i7);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.rq.ensureCapacity(20);
            this.buf.putInt(11);
            this.buf.putInt(i2 + sunGraphics2D.transX);
            this.buf.putInt(i3 + sunGraphics2D.transY);
            this.buf.putInt(i4);
            this.buf.putInt(i5);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.rq.ensureCapacity(20);
            this.buf.putInt(20);
            this.buf.putInt(i2 + sunGraphics2D.transX);
            this.buf.putInt(i3 + sunGraphics2D.transY);
            this.buf.putInt(i4);
            this.buf.putInt(i5);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        draw(sunGraphics2D, new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        fill(sunGraphics2D, new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        draw(sunGraphics2D, new Ellipse2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        fill(sunGraphics2D, new Ellipse2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        draw(sunGraphics2D, new Arc2D.Float(i2, i3, i4, i5, i6, i7, 0));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        fill(sunGraphics2D, new Arc2D.Float(i2, i3, i4, i5, i6, i7, 2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void drawPoly(final SunGraphics2D sunGraphics2D, final int[] iArr, final int[] iArr2, final int i2, final boolean z2) {
        if (iArr == null || iArr2 == null) {
            throw new NullPointerException("coordinate array");
        }
        if (iArr.length < i2 || iArr2.length < i2) {
            throw new ArrayIndexOutOfBoundsException("coordinate array");
        }
        if (i2 < 2) {
            return;
        }
        if (i2 == 2 && !z2) {
            drawLine(sunGraphics2D, iArr[0], iArr2[0], iArr[1], iArr2[1]);
            return;
        }
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            int i3 = 20 + (i2 * 8);
            if (i3 <= this.buf.capacity()) {
                if (i3 > this.buf.remaining()) {
                    this.rq.flushNow();
                }
                this.buf.putInt(12);
                this.buf.putInt(i2);
                this.buf.putInt(z2 ? 1 : 0);
                this.buf.putInt(sunGraphics2D.transX);
                this.buf.putInt(sunGraphics2D.transY);
                this.buf.put(iArr, 0, i2);
                this.buf.put(iArr2, 0, i2);
            } else {
                this.rq.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.pipe.BufferedRenderPipe.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BufferedRenderPipe.this.drawPoly(iArr, iArr2, i2, z2, sunGraphics2D.transX, sunGraphics2D.transY);
                    }
                });
            }
        } finally {
            this.rq.unlock();
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolyline(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        drawPoly(sunGraphics2D, iArr, iArr2, i2, false);
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        drawPoly(sunGraphics2D, iArr, iArr2, i2, true);
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        fill(sunGraphics2D, new Polygon(iArr, iArr2, i2));
    }

    /* loaded from: rt.jar:sun/java2d/pipe/BufferedRenderPipe$BufferedDrawHandler.class */
    private class BufferedDrawHandler extends ProcessPath.DrawHandler {
        private int scanlineCount;
        private int scanlineCountIndex;
        private int remainingScanlines;

        BufferedDrawHandler() {
            super(0, 0, 0, 0);
        }

        void validate(SunGraphics2D sunGraphics2D) {
            Region compClip = sunGraphics2D.getCompClip();
            setBounds(compClip.getLoX(), compClip.getLoY(), compClip.getHiX(), compClip.getHiY(), sunGraphics2D.strokeHint);
        }

        @Override // sun.java2d.loops.ProcessPath.DrawHandler
        public void drawLine(int i2, int i3, int i4, int i5) {
            BufferedRenderPipe.this.rq.ensureCapacity(20);
            BufferedRenderPipe.this.buf.putInt(10);
            BufferedRenderPipe.this.buf.putInt(i2);
            BufferedRenderPipe.this.buf.putInt(i3);
            BufferedRenderPipe.this.buf.putInt(i4);
            BufferedRenderPipe.this.buf.putInt(i5);
        }

        @Override // sun.java2d.loops.ProcessPath.DrawHandler
        public void drawPixel(int i2, int i3) {
            BufferedRenderPipe.this.rq.ensureCapacity(12);
            BufferedRenderPipe.this.buf.putInt(13);
            BufferedRenderPipe.this.buf.putInt(i2);
            BufferedRenderPipe.this.buf.putInt(i3);
        }

        private void resetFillPath() {
            BufferedRenderPipe.this.buf.putInt(14);
            this.scanlineCountIndex = BufferedRenderPipe.this.buf.position();
            BufferedRenderPipe.this.buf.putInt(0);
            this.scanlineCount = 0;
            this.remainingScanlines = BufferedRenderPipe.this.buf.remaining() / 12;
        }

        private void updateScanlineCount() {
            BufferedRenderPipe.this.buf.putInt(this.scanlineCountIndex, this.scanlineCount);
        }

        public void startFillPath() {
            BufferedRenderPipe.this.rq.ensureCapacity(20);
            resetFillPath();
        }

        @Override // sun.java2d.loops.ProcessPath.DrawHandler
        public void drawScanline(int i2, int i3, int i4) {
            if (this.remainingScanlines == 0) {
                updateScanlineCount();
                BufferedRenderPipe.this.rq.flushNow();
                resetFillPath();
            }
            BufferedRenderPipe.this.buf.putInt(i2);
            BufferedRenderPipe.this.buf.putInt(i3);
            BufferedRenderPipe.this.buf.putInt(i4);
            this.scanlineCount++;
            this.remainingScanlines--;
        }

        public void endFillPath() {
            updateScanlineCount();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void drawPath(SunGraphics2D sunGraphics2D, Path2D.Float r7, int i2, int i3) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.drawHandler.validate(sunGraphics2D);
            ProcessPath.drawPath(this.drawHandler, r7, i2, i3);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fillPath(SunGraphics2D sunGraphics2D, Path2D.Float r7, int i2, int i3) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.drawHandler.validate(sunGraphics2D);
            this.drawHandler.startFillPath();
            ProcessPath.fillPath(this.drawHandler, r7, i2, i3);
            this.drawHandler.endFillPath();
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fillSpans(SunGraphics2D sunGraphics2D, SpanIterator spanIterator, int i2, int i3) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.rq.ensureCapacity(24);
            this.buf.position(fillSpans(this.rq, this.buf.getAddress(), this.buf.position(), this.buf.capacity(), spanIterator, spanIterator.getNativeIterator(), i2, i3));
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.rq.ensureCapacity(28);
            this.buf.putInt(22);
            this.buf.putFloat((float) d6);
            this.buf.putFloat((float) d7);
            this.buf.putFloat((float) d8);
            this.buf.putFloat((float) d9);
            this.buf.putFloat((float) d10);
            this.buf.putFloat((float) d11);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        this.rq.lock();
        try {
            validateContext(sunGraphics2D);
            this.rq.ensureCapacity(36);
            this.buf.putInt(15);
            this.buf.putFloat((float) d6);
            this.buf.putFloat((float) d7);
            this.buf.putFloat((float) d8);
            this.buf.putFloat((float) d9);
            this.buf.putFloat((float) d10);
            this.buf.putFloat((float) d11);
            this.buf.putFloat((float) d12);
            this.buf.putFloat((float) d13);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/java2d/pipe/BufferedRenderPipe$AAParallelogramPipe.class */
    private class AAParallelogramPipe implements ParallelogramPipe {
        private AAParallelogramPipe() {
        }

        @Override // sun.java2d.pipe.ParallelogramPipe
        public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
            BufferedRenderPipe.this.rq.lock();
            try {
                BufferedRenderPipe.this.validateContextAA(sunGraphics2D);
                BufferedRenderPipe.this.rq.ensureCapacity(28);
                BufferedRenderPipe.this.buf.putInt(23);
                BufferedRenderPipe.this.buf.putFloat((float) d6);
                BufferedRenderPipe.this.buf.putFloat((float) d7);
                BufferedRenderPipe.this.buf.putFloat((float) d8);
                BufferedRenderPipe.this.buf.putFloat((float) d9);
                BufferedRenderPipe.this.buf.putFloat((float) d10);
                BufferedRenderPipe.this.buf.putFloat((float) d11);
                BufferedRenderPipe.this.rq.unlock();
            } catch (Throwable th) {
                BufferedRenderPipe.this.rq.unlock();
                throw th;
            }
        }

        @Override // sun.java2d.pipe.ParallelogramPipe
        public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
            BufferedRenderPipe.this.rq.lock();
            try {
                BufferedRenderPipe.this.validateContextAA(sunGraphics2D);
                BufferedRenderPipe.this.rq.ensureCapacity(36);
                BufferedRenderPipe.this.buf.putInt(16);
                BufferedRenderPipe.this.buf.putFloat((float) d6);
                BufferedRenderPipe.this.buf.putFloat((float) d7);
                BufferedRenderPipe.this.buf.putFloat((float) d8);
                BufferedRenderPipe.this.buf.putFloat((float) d9);
                BufferedRenderPipe.this.buf.putFloat((float) d10);
                BufferedRenderPipe.this.buf.putFloat((float) d11);
                BufferedRenderPipe.this.buf.putFloat((float) d12);
                BufferedRenderPipe.this.buf.putFloat((float) d13);
                BufferedRenderPipe.this.rq.unlock();
            } catch (Throwable th) {
                BufferedRenderPipe.this.rq.unlock();
                throw th;
            }
        }
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
        Path2D.Float r9;
        int i2;
        int i3;
        if (sunGraphics2D.strokeState != 0) {
            if (sunGraphics2D.strokeState < 3) {
                ShapeSpanIterator strokeSpans = LoopPipe.getStrokeSpans(sunGraphics2D, shape);
                try {
                    fillSpans(sunGraphics2D, strokeSpans, 0, 0);
                    strokeSpans.dispose();
                    return;
                } catch (Throwable th) {
                    strokeSpans.dispose();
                    throw th;
                }
            }
            fill(sunGraphics2D, sunGraphics2D.stroke.createStrokedShape(shape));
            return;
        }
        if ((shape instanceof Polygon) && sunGraphics2D.transformState < 3) {
            Polygon polygon = (Polygon) shape;
            drawPolygon(sunGraphics2D, polygon.xpoints, polygon.ypoints, polygon.npoints);
            return;
        }
        if (sunGraphics2D.transformState <= 1) {
            if (shape instanceof Path2D.Float) {
                r9 = (Path2D.Float) shape;
            } else {
                r9 = new Path2D.Float(shape);
            }
            i2 = sunGraphics2D.transX;
            i3 = sunGraphics2D.transY;
        } else {
            r9 = new Path2D.Float(shape, sunGraphics2D.transform);
            i2 = 0;
            i3 = 0;
        }
        drawPath(sunGraphics2D, r9, i2, i3);
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
        AffineTransform affineTransform;
        int i2;
        int i3;
        Path2D.Float r12;
        int i4;
        int i5;
        if (sunGraphics2D.strokeState == 0) {
            if (sunGraphics2D.transformState <= 1) {
                if (shape instanceof Path2D.Float) {
                    r12 = (Path2D.Float) shape;
                } else {
                    r12 = new Path2D.Float(shape);
                }
                i4 = sunGraphics2D.transX;
                i5 = sunGraphics2D.transY;
            } else {
                r12 = new Path2D.Float(shape, sunGraphics2D.transform);
                i4 = 0;
                i5 = 0;
            }
            fillPath(sunGraphics2D, r12, i4, i5);
            return;
        }
        if (sunGraphics2D.transformState <= 1) {
            affineTransform = null;
            i3 = sunGraphics2D.transX;
            i2 = sunGraphics2D.transY;
        } else {
            affineTransform = sunGraphics2D.transform;
            i2 = 0;
            i3 = 0;
        }
        ShapeSpanIterator fillSSI = LoopPipe.getFillSSI(sunGraphics2D);
        try {
            Region compClip = sunGraphics2D.getCompClip();
            fillSSI.setOutputAreaXYXY(compClip.getLoX() - i3, compClip.getLoY() - i2, compClip.getHiX() - i3, compClip.getHiY() - i2);
            fillSSI.appendPath(shape.getPathIterator(affineTransform));
            fillSpans(sunGraphics2D, fillSSI, i3, i2);
            fillSSI.dispose();
        } catch (Throwable th) {
            fillSSI.dispose();
            throw th;
        }
    }
}
