package org.icepdf.core.pobjects.graphics.batik.ext.awt;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaint;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/RadialGradientPaint.class */
public final class RadialGradientPaint extends MultipleGradientPaint {
    private Point2D focus;
    private Point2D center;
    private float radius;

    public RadialGradientPaint(float cx, float cy, float radius, float[] fractions, Color[] colors) {
        this(cx, cy, radius, cx, cy, fractions, colors);
    }

    public RadialGradientPaint(Point2D center, float radius, float[] fractions, Color[] colors) {
        this(center, radius, center, fractions, colors);
    }

    public RadialGradientPaint(float cx, float cy, float radius, float fx, float fy, float[] fractions, Color[] colors) {
        this(new Point2D.Float(cx, cy), radius, new Point2D.Float(fx, fy), fractions, colors, NO_CYCLE, SRGB);
    }

    public RadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors) {
        this(center, radius, focus, fractions, colors, NO_CYCLE, SRGB);
    }

    public RadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) {
        this(center, radius, focus, fractions, colors, cycleMethod, colorSpace, new AffineTransform());
    }

    public RadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace, AffineTransform gradientTransform) {
        super(fractions, colors, cycleMethod, colorSpace, gradientTransform);
        if (center == null) {
            throw new NullPointerException("Center point should not be null.");
        }
        if (focus == null) {
            throw new NullPointerException("Focus point should not be null.");
        }
        if (radius <= 0.0f) {
            throw new IllegalArgumentException("radius should be greater than zero");
        }
        this.center = (Point2D) center.clone();
        this.focus = (Point2D) focus.clone();
        this.radius = radius;
    }

    public RadialGradientPaint(Rectangle2D gradientBounds, float[] fractions, Color[] colors) {
        this(((float) gradientBounds.getX()) + (((float) gradientBounds.getWidth()) / 2.0f), ((float) gradientBounds.getY()) + (((float) gradientBounds.getWidth()) / 2.0f), ((float) gradientBounds.getWidth()) / 2.0f, fractions, colors);
    }

    @Override // java.awt.Paint
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform transform, RenderingHints hints) {
        AffineTransform transform2 = new AffineTransform(transform);
        transform2.concatenate(this.gradientTransform);
        try {
            return new RadialGradientPaintContext(cm, deviceBounds, userBounds, transform2, hints, (float) this.center.getX(), (float) this.center.getY(), this.radius, (float) this.focus.getX(), (float) this.focus.getY(), this.fractions, this.colors, this.cycleMethod, this.colorSpace);
        } catch (NoninvertibleTransformException e2) {
            throw new IllegalArgumentException("transform should be invertible");
        }
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
