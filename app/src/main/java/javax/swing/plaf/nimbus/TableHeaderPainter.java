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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TableHeaderPainter.class */
final class TableHeaderPainter extends AbstractRegionPainter {
    static final int ASCENDINGSORTICON_ENABLED = 1;
    static final int DESCENDINGSORTICON_ENABLED = 2;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBase", 0.0057927966f, -0.21904764f, 0.15686274f, 0);
    private Color color2 = decodeColor("nimbusBase", 0.0038565993f, 0.02012986f, 0.054901958f, 0);
    private Object[] componentColors;

    public TableHeaderPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintascendingSortIconEnabled(graphics2D);
                break;
            case 2:
                paintdescendingSortIconEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintascendingSortIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
    }

    private void paintdescendingSortIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(1.7070175f), decodeY(0.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.5025063f), decodeY(2.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2});
    }
}
