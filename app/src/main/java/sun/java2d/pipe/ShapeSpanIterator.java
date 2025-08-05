package sun.java2d.pipe;

import java.awt.Rectangle;
import java.awt.geom.PathIterator;
import sun.awt.geom.PathConsumer2D;

/* loaded from: rt.jar:sun/java2d/pipe/ShapeSpanIterator.class */
public final class ShapeSpanIterator implements SpanIterator, PathConsumer2D {
    long pData;

    public static native void initIDs();

    public native void appendPoly(int[] iArr, int[] iArr2, int i2, int i3, int i4);

    private native void setNormalize(boolean z2);

    public native void setOutputAreaXYXY(int i2, int i3, int i4, int i5);

    public native void setRule(int i2);

    public native void addSegment(int i2, float[] fArr);

    @Override // sun.java2d.pipe.SpanIterator
    public native void getPathBox(int[] iArr);

    @Override // sun.java2d.pipe.SpanIterator
    public native void intersectClipBox(int i2, int i3, int i4, int i5);

    @Override // sun.java2d.pipe.SpanIterator
    public native boolean nextSpan(int[] iArr);

    @Override // sun.java2d.pipe.SpanIterator
    public native void skipDownTo(int i2);

    @Override // sun.java2d.pipe.SpanIterator
    public native long getNativeIterator();

    public native void dispose();

    @Override // sun.awt.geom.PathConsumer2D
    public native void moveTo(float f2, float f3);

    @Override // sun.awt.geom.PathConsumer2D
    public native void lineTo(float f2, float f3);

    @Override // sun.awt.geom.PathConsumer2D
    public native void quadTo(float f2, float f3, float f4, float f5);

    @Override // sun.awt.geom.PathConsumer2D
    public native void curveTo(float f2, float f3, float f4, float f5, float f6, float f7);

    @Override // sun.awt.geom.PathConsumer2D
    public native void closePath();

    @Override // sun.awt.geom.PathConsumer2D
    public native void pathDone();

    @Override // sun.awt.geom.PathConsumer2D
    public native long getNativeConsumer();

    static {
        initIDs();
    }

    public ShapeSpanIterator(boolean z2) {
        setNormalize(z2);
    }

    public void appendPath(PathIterator pathIterator) {
        float[] fArr = new float[6];
        setRule(pathIterator.getWindingRule());
        while (!pathIterator.isDone()) {
            addSegment(pathIterator.currentSegment(fArr), fArr);
            pathIterator.next();
        }
        pathDone();
    }

    public void setOutputAreaXYWH(int i2, int i3, int i4, int i5) {
        setOutputAreaXYXY(i2, i3, Region.dimAdd(i2, i4), Region.dimAdd(i3, i5));
    }

    public void setOutputArea(Rectangle rectangle) {
        setOutputAreaXYWH(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public void setOutputArea(Region region) {
        setOutputAreaXYXY(region.lox, region.loy, region.hix, region.hiy);
    }
}
