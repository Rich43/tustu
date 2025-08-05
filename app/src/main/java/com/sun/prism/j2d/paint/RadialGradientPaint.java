package com.sun.prism.j2d.paint;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/paint/RadialGradientPaint.class */
public final class RadialGradientPaint extends MultipleGradientPaint {
    private final Point2D focus;
    private final Point2D center;
    private final float radius;

    public RadialGradientPaint(float cx, float cy, float radius, float[] fractions, Color[] colors) {
        this(cx, cy, radius, cx, cy, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    public RadialGradientPaint(Point2D center, float radius, float[] fractions, Color[] colors) {
        this(center, radius, center, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    public RadialGradientPaint(float cx, float cy, float radius, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(cx, cy, radius, cx, cy, fractions, colors, cycleMethod);
    }

    public RadialGradientPaint(Point2D center, float radius, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(center, radius, center, fractions, colors, cycleMethod);
    }

    public RadialGradientPaint(float cx, float cy, float radius, float fx, float fy, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(new Point2D.Float(cx, cy), radius, new Point2D.Float(fx, fy), fractions, colors, cycleMethod);
    }

    public RadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(center, radius, focus, fractions, colors, cycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform());
    }

    public RadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpace, AffineTransform gradientTransform) {
        super(fractions, colors, cycleMethod, colorSpace, gradientTransform);
        if (center == null) {
            throw new NullPointerException("Center point must be non-null");
        }
        if (focus == null) {
            throw new NullPointerException("Focus point must be non-null");
        }
        if (radius < 0.0f) {
            throw new IllegalArgumentException("Radius must be non-negative");
        }
        this.center = new Point2D.Double(center.getX(), center.getY());
        this.focus = new Point2D.Double(focus.getX(), focus.getY());
        this.radius = radius;
    }

    public RadialGradientPaint(Rectangle2D gradientBounds, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(new Point2D.Double(gradientBounds.getCenterX(), gradientBounds.getCenterY()), 1.0f, new Point2D.Double(gradientBounds.getCenterX(), gradientBounds.getCenterY()), fractions, colors, cycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, createGradientTransform(gradientBounds));
        if (gradientBounds.isEmpty()) {
            throw new IllegalArgumentException("Gradient bounds must be non-empty");
        }
    }

    private static AffineTransform createGradientTransform(Rectangle2D r2) {
        double cx = r2.getCenterX();
        double cy = r2.getCenterY();
        AffineTransform xform = AffineTransform.getTranslateInstance(cx, cy);
        xform.scale(r2.getWidth() / 2.0d, r2.getHeight() / 2.0d);
        xform.translate(-cx, -cy);
        return xform;
    }

    @Override // java.awt.Paint
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform transform, RenderingHints hints) {
        AffineTransform transform2 = new AffineTransform(transform);
        transform2.concatenate(this.gradientTransform);
        return new RadialGradientPaintContext(this, cm, deviceBounds, userBounds, transform2, hints, (float) this.center.getX(), (float) this.center.getY(), this.radius, (float) this.focus.getX(), (float) this.focus.getY(), this.fractions, this.colors, this.cycleMethod, this.colorSpace);
    }

    public Point2D getCenterPoint() {
        return new Point2D.Double(this.center.getX(), this.center.getY());
    }

    public Point2D getFocusPoint() {
        return new Point2D.Double(this.focus.getX(), this.focus.getY());
    }

    public float getRadius() {
        return this.radius;
    }
}
