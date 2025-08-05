package sun.java2d.pipe;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/SpanShapeRenderer.class */
public abstract class SpanShapeRenderer implements ShapeDrawPipe {
    static final RenderingEngine RenderEngine = RenderingEngine.getInstance();
    public static final int NON_RECTILINEAR_TRANSFORM_MASK = 48;

    public abstract Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr);

    public abstract void renderBox(Object obj, int i2, int i3, int i4, int i5);

    public abstract void endSequence(Object obj);

    /* loaded from: rt.jar:sun/java2d/pipe/SpanShapeRenderer$Composite.class */
    public static class Composite extends SpanShapeRenderer {
        CompositePipe comppipe;

        public Composite(CompositePipe compositePipe) {
            this.comppipe = compositePipe;
        }

        @Override // sun.java2d.pipe.SpanShapeRenderer
        public Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr) {
            return this.comppipe.startSequence(sunGraphics2D, shape, rectangle, iArr);
        }

        @Override // sun.java2d.pipe.SpanShapeRenderer
        public void renderBox(Object obj, int i2, int i3, int i4, int i5) {
            this.comppipe.renderPathTile(obj, null, 0, i4, i2, i3, i4, i5);
        }

        @Override // sun.java2d.pipe.SpanShapeRenderer
        public void endSequence(Object obj) {
            this.comppipe.endSequence(obj);
        }
    }

    /* loaded from: rt.jar:sun/java2d/pipe/SpanShapeRenderer$Simple.class */
    public static class Simple extends SpanShapeRenderer implements LoopBasedPipe {
        @Override // sun.java2d.pipe.SpanShapeRenderer
        public Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr) {
            return sunGraphics2D;
        }

        @Override // sun.java2d.pipe.SpanShapeRenderer
        public void renderBox(Object obj, int i2, int i3, int i4, int i5) {
            SunGraphics2D sunGraphics2D = (SunGraphics2D) obj;
            sunGraphics2D.loops.fillRectLoop.FillRect(sunGraphics2D, sunGraphics2D.getSurfaceData(), i2, i3, i4, i5);
        }

        @Override // sun.java2d.pipe.SpanShapeRenderer
        public void endSequence(Object obj) {
        }
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
        if (sunGraphics2D.stroke instanceof BasicStroke) {
            ShapeSpanIterator strokeSpans = LoopPipe.getStrokeSpans(sunGraphics2D, shape);
            try {
                renderSpans(sunGraphics2D, sunGraphics2D.getCompClip(), shape, strokeSpans);
                strokeSpans.dispose();
                return;
            } catch (Throwable th) {
                strokeSpans.dispose();
                throw th;
            }
        }
        fill(sunGraphics2D, sunGraphics2D.stroke.createStrokedShape(shape));
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
        if ((shape instanceof Rectangle2D) && (sunGraphics2D.transform.getType() & 48) == 0) {
            renderRect(sunGraphics2D, (Rectangle2D) shape);
            return;
        }
        Region compClip = sunGraphics2D.getCompClip();
        ShapeSpanIterator fillSSI = LoopPipe.getFillSSI(sunGraphics2D);
        try {
            fillSSI.setOutputArea(compClip);
            fillSSI.appendPath(shape.getPathIterator(sunGraphics2D.transform));
            renderSpans(sunGraphics2D, compClip, shape, fillSSI);
            fillSSI.dispose();
        } catch (Throwable th) {
            fillSSI.dispose();
            throw th;
        }
    }

    public void renderRect(SunGraphics2D sunGraphics2D, Rectangle2D rectangle2D) {
        double[] dArr = {rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight()};
        dArr[2] = dArr[2] + dArr[0];
        dArr[3] = dArr[3] + dArr[1];
        if (dArr[2] <= dArr[0] || dArr[3] <= dArr[1]) {
            return;
        }
        sunGraphics2D.transform.transform(dArr, 0, dArr, 0, 2);
        if (dArr[2] < dArr[0]) {
            double d2 = dArr[2];
            dArr[2] = dArr[0];
            dArr[0] = d2;
        }
        if (dArr[3] < dArr[1]) {
            double d3 = dArr[3];
            dArr[3] = dArr[1];
            dArr[1] = d3;
        }
        int[] iArr = {(int) dArr[0], (int) dArr[1], (int) dArr[2], (int) dArr[3]};
        Rectangle rectangle = new Rectangle(iArr[0], iArr[1], iArr[2] - iArr[0], iArr[3] - iArr[1]);
        Region compClip = sunGraphics2D.getCompClip();
        compClip.clipBoxToBounds(iArr);
        if (iArr[0] >= iArr[2] || iArr[1] >= iArr[3]) {
            return;
        }
        Object objStartSequence = startSequence(sunGraphics2D, rectangle2D, rectangle, iArr);
        if (compClip.isRectangular()) {
            renderBox(objStartSequence, iArr[0], iArr[1], iArr[2] - iArr[0], iArr[3] - iArr[1]);
        } else {
            SpanIterator spanIterator = compClip.getSpanIterator(iArr);
            while (spanIterator.nextSpan(iArr)) {
                renderBox(objStartSequence, iArr[0], iArr[1], iArr[2] - iArr[0], iArr[3] - iArr[1]);
            }
        }
        endSequence(objStartSequence);
    }

    public void renderSpans(SunGraphics2D sunGraphics2D, Region region, Shape shape, ShapeSpanIterator shapeSpanIterator) {
        Object objStartSequence = null;
        int[] iArr = new int[4];
        try {
            shapeSpanIterator.getPathBox(iArr);
            Rectangle rectangle = new Rectangle(iArr[0], iArr[1], iArr[2] - iArr[0], iArr[3] - iArr[1]);
            region.clipBoxToBounds(iArr);
            if (iArr[0] < iArr[2] && iArr[1] < iArr[3]) {
                shapeSpanIterator.intersectClipBox(iArr[0], iArr[1], iArr[2], iArr[3]);
                objStartSequence = startSequence(sunGraphics2D, shape, rectangle, iArr);
                spanClipLoop(objStartSequence, shapeSpanIterator, region, iArr);
                if (objStartSequence != null) {
                    endSequence(objStartSequence);
                    return;
                }
                return;
            }
            if (0 == 0) {
                return;
            }
            endSequence(null);
        } catch (Throwable th) {
            if (objStartSequence != null) {
                endSequence(objStartSequence);
            }
            throw th;
        }
    }

    public void spanClipLoop(Object obj, SpanIterator spanIterator, Region region, int[] iArr) {
        if (!region.isRectangular()) {
            spanIterator = region.filter(spanIterator);
        }
        while (spanIterator.nextSpan(iArr)) {
            int i2 = iArr[0];
            int i3 = iArr[1];
            renderBox(obj, i2, i3, iArr[2] - i2, iArr[3] - i3);
        }
    }
}
