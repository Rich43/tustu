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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TabbedPaneTabAreaPainter.class */
final class TabbedPaneTabAreaPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BACKGROUND_DISABLED = 2;
    static final int BACKGROUND_ENABLED_MOUSEOVER = 3;
    static final int BACKGROUND_ENABLED_PRESSED = 4;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = new Color(255, 200, 0, 255);
    private Color color2 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.4784314f, 0);
    private Color color3 = decodeColor("nimbusBase", 5.1498413E-4f, -0.45471883f, 0.31764704f, 0);
    private Color color4 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4633005f, 0.3607843f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.05468172f, -0.58308274f, 0.19607842f, 0);
    private Color color6 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color7 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4690476f, 0.39215684f, 0);
    private Color color8 = decodeColor("nimbusBase", 5.1498413E-4f, -0.47635174f, 0.4352941f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.0f, -0.05401492f, 0.05098039f, 0);
    private Color color10 = decodeColor("nimbusBase", 0.0f, -0.09303135f, 0.09411764f, 0);
    private Object[] componentColors;

    public TabbedPaneTabAreaPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundDisabled(graphics2D);
                break;
            case 3:
                paintBackgroundEnabledAndMouseOver(graphics2D);
                break;
            case 4:
                paintBackgroundEnabledAndPressed(graphics2D);
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

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndMouseOver(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient3(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndPressed(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.0f), decodeY(1.0f), decodeX(0.0f) - decodeX(0.0f), decodeY(1.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.0f), decodeY(2.1666667f), decodeX(3.0f) - decodeX(0.0f), decodeY(3.0f) - decodeY(2.1666667f));
        return this.rect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.08387097f, 0.09677419f, 0.10967742f, 0.43709677f, 0.7645161f, 0.7758064f, 0.7870968f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.08387097f, 0.09677419f, 0.10967742f, 0.43709677f, 0.7645161f, 0.7758064f, 0.7870968f}, new Color[]{this.color5, decodeColor(this.color5, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.08387097f, 0.09677419f, 0.10967742f, 0.43709677f, 0.7645161f, 0.7758064f, 0.7870968f}, new Color[]{this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8, decodeColor(this.color8, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.08387097f, 0.09677419f, 0.10967742f, 0.43709677f, 0.7645161f, 0.7758064f, 0.7870968f}, new Color[]{this.color2, decodeColor(this.color2, this.color9, 0.5f), this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color2, 0.5f), this.color2});
    }
}
