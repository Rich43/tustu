package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ScrollPanePainter.class */
final class ScrollPanePainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BORDER_ENABLED_FOCUSED = 2;
    static final int BORDER_ENABLED = 3;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Object[] componentColors;

    public ScrollPanePainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 2:
                paintBorderEnabledAndFocused(graphics2D);
                break;
            case 3:
                paintBorderEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBorderEnabledAndFocused(Graphics2D graphics2D) {
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
        this.path = decodePath1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.path);
    }

    private void paintBorderEnabled(Graphics2D graphics2D) {
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
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.6f), decodeY(0.4f), decodeX(2.4f) - decodeX(0.6f), decodeY(0.6f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.4f), decodeY(0.4f), decodeX(0.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(2.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(2.4f), decodeY(2.6f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(0.6f), decodeY(2.4f), decodeX(2.4f) - decodeX(0.6f), decodeY(2.6f) - decodeY(2.4f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.4f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.4f));
        this.path.curveTo(decodeAnchorX(2.6f, 0.0f), decodeAnchorY(0.4f, 0.0f), decodeAnchorX(2.8800004f, 0.1f), decodeAnchorY(0.4f, 0.0f), decodeX(2.8800004f), decodeY(0.4f));
        this.path.curveTo(decodeAnchorX(2.8800004f, 0.1f), decodeAnchorY(0.4f, 0.0f), decodeAnchorX(2.8800004f, 0.0f), decodeAnchorY(2.8799999f, 0.0f), decodeX(2.8800004f), decodeY(2.8799999f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(2.8799999f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(2.8800004f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(2.8800004f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.4f));
        this.path.closePath();
        return this.path;
    }
}
