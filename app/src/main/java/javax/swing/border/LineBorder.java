package javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/border/LineBorder.class */
public class LineBorder extends AbstractBorder {
    private static Border blackLine;
    private static Border grayLine;
    protected int thickness;
    protected Color lineColor;
    protected boolean roundedCorners;

    public static Border createBlackLineBorder() {
        if (blackLine == null) {
            blackLine = new LineBorder(Color.black, 1);
        }
        return blackLine;
    }

    public static Border createGrayLineBorder() {
        if (grayLine == null) {
            grayLine = new LineBorder(Color.gray, 1);
        }
        return grayLine;
    }

    public LineBorder(Color color) {
        this(color, 1, false);
    }

    public LineBorder(Color color, int i2) {
        this(color, i2, false);
    }

    @ConstructorProperties({"lineColor", "thickness", "roundedCorners"})
    public LineBorder(Color color, int i2, boolean z2) {
        this.lineColor = color;
        this.thickness = i2;
        this.roundedCorners = z2;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        Shape shape;
        Shape shape2;
        if (this.thickness > 0 && (graphics instanceof Graphics2D)) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Color color = graphics2D.getColor();
            graphics2D.setColor(this.lineColor);
            int i6 = this.thickness;
            int i7 = i6 + i6;
            if (this.roundedCorners) {
                float f2 = 0.2f * i6;
                shape = new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i6);
                shape2 = new RoundRectangle2D.Float(i2 + i6, i3 + i6, i4 - i7, i5 - i7, f2, f2);
            } else {
                shape = new Rectangle2D.Float(i2, i3, i4, i5);
                shape2 = new Rectangle2D.Float(i2 + i6, i3 + i6, i4 - i7, i5 - i7);
            }
            Path2D.Float r0 = new Path2D.Float(0);
            r0.append(shape, false);
            r0.append(shape2, false);
            graphics2D.fill(r0);
            graphics2D.setColor(color);
        }
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.set(this.thickness, this.thickness, this.thickness, this.thickness);
        return insets;
    }

    public Color getLineColor() {
        return this.lineColor;
    }

    public int getThickness() {
        return this.thickness;
    }

    public boolean getRoundedCorners() {
        return this.roundedCorners;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return !this.roundedCorners;
    }
}
