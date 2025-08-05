package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCPath.class */
public abstract class WCPath<P> extends Ref {
    public static final int RULE_NONZERO = 0;
    public static final int RULE_EVENODD = 1;

    public abstract void addRect(double d2, double d3, double d4, double d5);

    public abstract void addEllipse(double d2, double d3, double d4, double d5);

    public abstract void addArcTo(double d2, double d3, double d4, double d5, double d6);

    public abstract void addArc(double d2, double d3, double d4, double d5, double d6, boolean z2);

    public abstract boolean contains(int i2, double d2, double d3);

    public abstract WCRectangle getBounds();

    public abstract void clear();

    public abstract void moveTo(double d2, double d3);

    public abstract void addLineTo(double d2, double d3);

    public abstract void addQuadCurveTo(double d2, double d3, double d4, double d5);

    public abstract void addBezierCurveTo(double d2, double d3, double d4, double d5, double d6, double d7);

    public abstract void addPath(WCPath wCPath);

    public abstract void closeSubpath();

    public abstract boolean isEmpty();

    public abstract void translate(double d2, double d3);

    public abstract void transform(double d2, double d3, double d4, double d5, double d6, double d7);

    public abstract int getWindingRule();

    public abstract void setWindingRule(int i2);

    public abstract P getPlatformPath();

    public abstract WCPathIterator getPathIterator();

    public abstract boolean strokeContains(double d2, double d3, double d4, double d5, int i2, int i3, double d6, double[] dArr);
}
