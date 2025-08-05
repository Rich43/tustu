package sun.java2d.windows;

import java.awt.Composite;
import java.awt.Shape;
import java.awt.geom.Path2D;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.LoopPipe;
import sun.java2d.pipe.PixelDrawPipe;
import sun.java2d.pipe.PixelFillPipe;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.ShapeDrawPipe;
import sun.java2d.pipe.ShapeSpanIterator;
import sun.java2d.pipe.SpanIterator;

/* loaded from: rt.jar:sun/java2d/windows/GDIRenderer.class */
public class GDIRenderer implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe {
    native void doDrawLine(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6);

    native void doDrawRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6);

    native void doDrawRoundRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    native void doDrawOval(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6);

    native void doDrawArc(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    native void doDrawPoly(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int[] iArr, int[] iArr2, int i5, boolean z2);

    native void doFillRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6);

    native void doFillRoundRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    native void doFillOval(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6);

    native void doFillArc(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    native void doFillPoly(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int[] iArr, int[] iArr2, int i5);

    native void doShape(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, Path2D.Float r7, boolean z2);

    public native void devCopyArea(GDIWindowSurfaceData gDIWindowSurfaceData, int i2, int i3, int i4, int i5, int i6, int i7);

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        int i6 = sunGraphics2D.transX;
        int i7 = sunGraphics2D.transY;
        try {
            doDrawLine((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + i6, i3 + i7, i4 + i6, i5 + i7);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        try {
            doDrawRect((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            doDrawRoundRect((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        try {
            doDrawOval((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            doDrawArc((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolyline(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        try {
            doDrawPoly((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, sunGraphics2D.transX, sunGraphics2D.transY, iArr, iArr2, i2, false);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        try {
            doDrawPoly((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, sunGraphics2D.transX, sunGraphics2D.transY, iArr, iArr2, i2, true);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        try {
            doFillRect((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            doFillRoundRect((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        try {
            doFillOval((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            doFillArc((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        try {
            doFillPoly((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, sunGraphics2D.transX, sunGraphics2D.transY, iArr, iArr2, i2);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    void doShape(SunGraphics2D sunGraphics2D, Shape shape, boolean z2) {
        Path2D.Float r14;
        int i2;
        int i3;
        if (sunGraphics2D.transformState <= 1) {
            if (shape instanceof Path2D.Float) {
                r14 = (Path2D.Float) shape;
            } else {
                r14 = new Path2D.Float(shape);
            }
            i2 = sunGraphics2D.transX;
            i3 = sunGraphics2D.transY;
        } else {
            r14 = new Path2D.Float(shape, sunGraphics2D.transform);
            i2 = 0;
            i3 = 0;
        }
        try {
            doShape((GDIWindowSurfaceData) sunGraphics2D.surfaceData, sunGraphics2D.getCompClip(), sunGraphics2D.composite, sunGraphics2D.eargb, i2, i3, r14, z2);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    public void doFillSpans(SunGraphics2D sunGraphics2D, SpanIterator spanIterator) {
        int[] iArr = new int[4];
        try {
            GDIWindowSurfaceData gDIWindowSurfaceData = (GDIWindowSurfaceData) sunGraphics2D.surfaceData;
            Region compClip = sunGraphics2D.getCompClip();
            Composite composite = sunGraphics2D.composite;
            int i2 = sunGraphics2D.eargb;
            while (spanIterator.nextSpan(iArr)) {
                doFillRect(gDIWindowSurfaceData, compClip, composite, i2, iArr[0], iArr[1], iArr[2] - iArr[0], iArr[3] - iArr[1]);
            }
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
        if (sunGraphics2D.strokeState == 0) {
            doShape(sunGraphics2D, shape, false);
            return;
        }
        if (sunGraphics2D.strokeState < 3) {
            ShapeSpanIterator strokeSpans = LoopPipe.getStrokeSpans(sunGraphics2D, shape);
            try {
                doFillSpans(sunGraphics2D, strokeSpans);
                strokeSpans.dispose();
                return;
            } catch (Throwable th) {
                strokeSpans.dispose();
                throw th;
            }
        }
        doShape(sunGraphics2D, sunGraphics2D.stroke.createStrokedShape(shape), true);
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
        doShape(sunGraphics2D, shape, true);
    }

    public GDIRenderer traceWrap() {
        return new Tracer();
    }

    /* loaded from: rt.jar:sun/java2d/windows/GDIRenderer$Tracer.class */
    public static class Tracer extends GDIRenderer {
        @Override // sun.java2d.windows.GDIRenderer
        void doDrawLine(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6) {
            GraphicsPrimitive.tracePrimitive("GDIDrawLine");
            super.doDrawLine(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doDrawRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6) {
            GraphicsPrimitive.tracePrimitive("GDIDrawRect");
            super.doDrawRect(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doDrawRoundRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            GraphicsPrimitive.tracePrimitive("GDIDrawRoundRect");
            super.doDrawRoundRect(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6, i7, i8);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doDrawOval(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6) {
            GraphicsPrimitive.tracePrimitive("GDIDrawOval");
            super.doDrawOval(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doDrawArc(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            GraphicsPrimitive.tracePrimitive("GDIDrawArc");
            super.doDrawArc(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6, i7, i8);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doDrawPoly(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int[] iArr, int[] iArr2, int i5, boolean z2) {
            GraphicsPrimitive.tracePrimitive("GDIDrawPoly");
            super.doDrawPoly(gDIWindowSurfaceData, region, composite, i2, i3, i4, iArr, iArr2, i5, z2);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doFillRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6) {
            GraphicsPrimitive.tracePrimitive("GDIFillRect");
            super.doFillRect(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doFillRoundRect(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            GraphicsPrimitive.tracePrimitive("GDIFillRoundRect");
            super.doFillRoundRect(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6, i7, i8);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doFillOval(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6) {
            GraphicsPrimitive.tracePrimitive("GDIFillOval");
            super.doFillOval(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doFillArc(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            GraphicsPrimitive.tracePrimitive("GDIFillArc");
            super.doFillArc(gDIWindowSurfaceData, region, composite, i2, i3, i4, i5, i6, i7, i8);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doFillPoly(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, int[] iArr, int[] iArr2, int i5) {
            GraphicsPrimitive.tracePrimitive("GDIFillPoly");
            super.doFillPoly(gDIWindowSurfaceData, region, composite, i2, i3, i4, iArr, iArr2, i5);
        }

        @Override // sun.java2d.windows.GDIRenderer
        void doShape(GDIWindowSurfaceData gDIWindowSurfaceData, Region region, Composite composite, int i2, int i3, int i4, Path2D.Float r17, boolean z2) {
            GraphicsPrimitive.tracePrimitive(z2 ? "GDIFillShape" : "GDIDrawShape");
            super.doShape(gDIWindowSurfaceData, region, composite, i2, i3, i4, r17, z2);
        }

        @Override // sun.java2d.windows.GDIRenderer
        public void devCopyArea(GDIWindowSurfaceData gDIWindowSurfaceData, int i2, int i3, int i4, int i5, int i6, int i7) {
            GraphicsPrimitive.tracePrimitive("GDICopyArea");
            super.devCopyArea(gDIWindowSurfaceData, i2, i3, i4, i5, i6, i7);
        }
    }
}
