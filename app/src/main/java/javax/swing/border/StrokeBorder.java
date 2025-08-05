package javax.swing.border;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/border/StrokeBorder.class */
public class StrokeBorder extends AbstractBorder {
    private final BasicStroke stroke;
    private final Paint paint;

    public StrokeBorder(BasicStroke basicStroke) {
        this(basicStroke, null);
    }

    @ConstructorProperties({"stroke", "paint"})
    public StrokeBorder(BasicStroke basicStroke, Paint paint) {
        if (basicStroke == null) {
            throw new NullPointerException("border's stroke");
        }
        this.stroke = basicStroke;
        this.paint = paint;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        float lineWidth = this.stroke.getLineWidth();
        if (lineWidth > 0.0f) {
            Graphics graphicsCreate = graphics.create();
            if (graphicsCreate instanceof Graphics2D) {
                Graphics2D graphics2D = (Graphics2D) graphicsCreate;
                graphics2D.setStroke(this.stroke);
                graphics2D.setPaint(this.paint != null ? this.paint : component == null ? null : component.getForeground());
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.draw(new Rectangle2D.Float(i2 + (lineWidth / 2.0f), i3 + (lineWidth / 2.0f), i4 - lineWidth, i5 - lineWidth));
            }
            graphicsCreate.dispose();
        }
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        int iCeil = (int) Math.ceil(this.stroke.getLineWidth());
        insets.set(iCeil, iCeil, iCeil, iCeil);
        return insets;
    }

    public BasicStroke getStroke() {
        return this.stroke;
    }

    public Paint getPaint() {
        return this.paint;
    }
}
