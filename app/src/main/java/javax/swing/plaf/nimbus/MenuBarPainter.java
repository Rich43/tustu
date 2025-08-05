package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/MenuBarPainter.class */
final class MenuBarPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BORDER_ENABLED = 2;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.10255819f, 0.23921567f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.10654225f, 0.23921567f, -29);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -255);
    private Color color5 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
    private Object[] componentColors;

    public MenuBarPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintBackgroundEnabled(graphics2D);
                break;
            case 2:
                paintBorderEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBorderEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color5);
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(1.9523809f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(1.0f), decodeY(2.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(3.0f) - decodeY(2.0f));
        return this.rect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((1.0f * width) + x2, (0.0f * height) + y2, (1.0f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.015f, 0.03f, 0.23354445f, 0.7569444f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4});
    }
}
