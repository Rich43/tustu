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

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/LinearGradientPaint.class */
public final class LinearGradientPaint extends MultipleGradientPaint {
    private Point2D start;
    private Point2D end;

    public LinearGradientPaint(float startX, float startY, float endX, float endY, float[] fractions, Color[] colors) {
        this(new Point2D.Float(startX, startY), new Point2D.Float(endX, endY), fractions, colors, NO_CYCLE, SRGB);
    }

    public LinearGradientPaint(float startX, float startY, float endX, float endY, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod) {
        this(new Point2D.Float(startX, startY), new Point2D.Float(endX, endY), fractions, colors, cycleMethod, SRGB);
    }

    public LinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors) {
        this(start, end, fractions, colors, NO_CYCLE, SRGB);
    }

    public LinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) {
        this(start, end, fractions, colors, cycleMethod, colorSpace, new AffineTransform());
    }

    public LinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace, AffineTransform gradientTransform) {
        super(fractions, colors, cycleMethod, colorSpace, gradientTransform);
        if (start == null || end == null) {
            throw new NullPointerException("Start and end points must benon-null");
        }
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start point cannot equalendpoint");
        }
        this.start = (Point2D) start.clone();
        this.end = (Point2D) end.clone();
    }

    @Override // java.awt.Paint
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform transform, RenderingHints hints) {
        AffineTransform transform2 = new AffineTransform(transform);
        transform2.concatenate(this.gradientTransform);
        try {
            return new LinearGradientPaintContext(cm, deviceBounds, userBounds, transform2, hints, this.start, this.end, this.fractions, getColors(), this.cycleMethod, this.colorSpace);
        } catch (NoninvertibleTransformException e2) {
            throw new IllegalArgumentException("transform should beinvertible");
        }
    }

    public Point2D getStartPoint() {
        return new Point2D.Double(this.start.getX(), this.start.getY());
    }

    public Point2D getEndPoint() {
        return new Point2D.Double(this.end.getX(), this.end.getY());
    }
}
