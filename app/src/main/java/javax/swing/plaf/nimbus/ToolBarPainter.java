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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolBarPainter.class */
final class ToolBarPainter extends AbstractRegionPainter {
    static final int BORDER_NORTH = 1;
    static final int BORDER_SOUTH = 2;
    static final int BORDER_EAST = 3;
    static final int BORDER_WEST = 4;
    static final int HANDLEICON_ENABLED = 5;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);
    private Color color4 = decodeColor("nimbusBorder", 0.0f, -0.029675633f, 0.109803915f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", -0.008547008f, -0.03494492f, -0.07058823f, 0);
    private Object[] componentColors;

    public ToolBarPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintBorderNorth(graphics2D);
                break;
            case 2:
                paintBorderSouth(graphics2D);
                break;
            case 3:
                paintBorderEast(graphics2D);
                break;
            case 4:
                paintBorderWest(graphics2D);
                break;
            case 5:
                painthandleIconEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBorderNorth(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintBorderSouth(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintBorderEast(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintBorderWest(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void painthandleIconEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.rect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color5);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color5);
        graphics2D.fill(this.path);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.0f), decodeY(2.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(3.0f) - decodeY(2.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(1.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(2.8f) - decodeX(0.0f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(2.8f), decodeY(0.0f), decodeX(3.0f) - decodeX(2.8f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.4f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.5f * height) + y2, (1.0f * width) + x2, (0.5f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3});
    }
}
