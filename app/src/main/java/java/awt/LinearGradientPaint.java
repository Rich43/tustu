package java.awt;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.MultipleGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:java/awt/LinearGradientPaint.class */
public final class LinearGradientPaint extends MultipleGradientPaint {
    private final Point2D start;
    private final Point2D end;

    public LinearGradientPaint(float f2, float f3, float f4, float f5, float[] fArr, Color[] colorArr) {
        this(new Point2D.Float(f2, f3), new Point2D.Float(f4, f5), fArr, colorArr, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    public LinearGradientPaint(float f2, float f3, float f4, float f5, float[] fArr, Color[] colorArr, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(new Point2D.Float(f2, f3), new Point2D.Float(f4, f5), fArr, colorArr, cycleMethod);
    }

    public LinearGradientPaint(Point2D point2D, Point2D point2D2, float[] fArr, Color[] colorArr) {
        this(point2D, point2D2, fArr, colorArr, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    public LinearGradientPaint(Point2D point2D, Point2D point2D2, float[] fArr, Color[] colorArr, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(point2D, point2D2, fArr, colorArr, cycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform());
    }

    @ConstructorProperties({"startPoint", "endPoint", "fractions", "colors", "cycleMethod", "colorSpace", Constants.ELEMNAME_TRANSFORM_STRING})
    public LinearGradientPaint(Point2D point2D, Point2D point2D2, float[] fArr, Color[] colorArr, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType, AffineTransform affineTransform) {
        super(fArr, colorArr, cycleMethod, colorSpaceType, affineTransform);
        if (point2D == null || point2D2 == null) {
            throw new NullPointerException("Start and end points must benon-null");
        }
        if (point2D.equals(point2D2)) {
            throw new IllegalArgumentException("Start point cannot equalendpoint");
        }
        this.start = new Point2D.Double(point2D.getX(), point2D.getY());
        this.end = new Point2D.Double(point2D2.getX(), point2D2.getY());
    }

    @Override // java.awt.Paint
    public PaintContext createContext(ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints) {
        AffineTransform affineTransform2 = new AffineTransform(affineTransform);
        affineTransform2.concatenate(this.gradientTransform);
        if (this.fractions.length == 2 && this.cycleMethod != MultipleGradientPaint.CycleMethod.REPEAT && this.colorSpace == MultipleGradientPaint.ColorSpaceType.SRGB) {
            return new GradientPaintContext(colorModel, this.start, this.end, affineTransform2, this.colors[0], this.colors[1], this.cycleMethod != MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }
        return new LinearGradientPaintContext(this, colorModel, rectangle, rectangle2D, affineTransform2, renderingHints, this.start, this.end, this.fractions, this.colors, this.cycleMethod, this.colorSpace);
    }

    public Point2D getStartPoint() {
        return new Point2D.Double(this.start.getX(), this.start.getY());
    }

    public Point2D getEndPoint() {
        return new Point2D.Double(this.end.getX(), this.end.getY());
    }
}
