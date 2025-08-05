package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ArrowButtonPainter.class */
final class ArrowButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int FOREGROUND_DISABLED = 2;
    static final int FOREGROUND_ENABLED = 3;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBase", 0.027408898f, -0.57391655f, 0.1490196f, 0);
    private Color color2 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.37254906f, 0);
    private Object[] componentColors;

    public ArrowButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 2:
                paintForegroundDisabled(graphics2D);
                break;
            case 3:
                paintForegroundEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintForegroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
    }

    private void paintForegroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8f), decodeY(1.2f));
        this.path.lineTo(decodeX(1.2f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.8f), decodeY(1.8f));
        this.path.lineTo(decodeX(1.8f), decodeY(1.2f));
        this.path.closePath();
        return this.path;
    }
}
