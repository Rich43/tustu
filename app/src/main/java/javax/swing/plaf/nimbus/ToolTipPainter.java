package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolTipPainter.class */
final class ToolTipPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = decodeColor("info", 0.0f, 0.0f, 0.0f, 0);
    private Object[] componentColors;

    public ToolTipPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(2.0f), decodeY(1.0f), decodeX(3.0f) - decodeX(2.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.0f), decodeY(1.0f), decodeX(1.0f) - decodeX(0.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.0f), decodeY(2.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(3.0f) - decodeY(2.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(1.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect5() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }
}
