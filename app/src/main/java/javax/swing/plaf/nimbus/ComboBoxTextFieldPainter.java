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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ComboBoxTextFieldPainter.class */
final class ComboBoxTextFieldPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_SELECTED = 3;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -237);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07703349f, 0.0745098f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07968931f, 0.14509803f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07856284f, 0.11372548f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.040395975f, -0.60315615f, 0.29411763f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.016586483f, -0.6051466f, 0.3490196f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);
    private Color color10 = decodeColor("nimbusLightBackground", 0.6666667f, 0.004901961f, -0.19999999f, 0);
    private Color color11 = decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.105344966f, 0.011764705f, 0);
    private Object[] componentColors;

    public ComboBoxTextFieldPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintBackgroundDisabled(graphics2D);
                break;
            case 2:
                paintBackgroundEnabled(graphics2D);
                break;
            case 3:
                paintBackgroundSelected(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color7);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient3(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color11);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundSelected(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient3(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color11);
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(2.3333333f), decodeX(3.0f) - decodeX(0.6666667f), decodeY(2.6666667f) - decodeY(2.3333333f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(0.4f), decodeX(3.0f) - decodeX(0.6666667f), decodeY(1.0f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.6f), decodeX(3.0f) - decodeX(1.0f), decodeY(1.0f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(1.0f), decodeX(3.0f) - decodeX(0.6666667f), decodeY(2.3333333f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect5() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(3.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (1.0f * height) + y2, (0.5f * width) + x2, (0.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.49573863f, 0.99147725f}, new Color[]{this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.1f, 0.49999997f, 0.9f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }
}
