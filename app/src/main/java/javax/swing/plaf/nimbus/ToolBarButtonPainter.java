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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolBarButtonPainter.class */
final class ToolBarButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BACKGROUND_FOCUSED = 2;
    static final int BACKGROUND_MOUSEOVER = 3;
    static final int BACKGROUND_MOUSEOVER_FOCUSED = 4;
    static final int BACKGROUND_PRESSED = 5;
    static final int BACKGROUND_PRESSED_FOCUSED = 6;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.06885965f, -0.36862746f, -153);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.0f, -0.020974077f, -0.21960783f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.0f, 0.11169591f, -0.53333336f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10658931f, 0.25098038f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, -0.098526314f, 0.2352941f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", -0.00505054f, -0.05960039f, 0.10196078f, 0);
    private Color color10 = decodeColor("nimbusBlueGrey", -0.008547008f, -0.04772438f, 0.06666666f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0212406f, 0.13333333f, 0);
    private Color color13 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.030845039f, 0.23921567f, 0);
    private Object[] componentColors;

    public ToolBarButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 2:
                paintBackgroundFocused(graphics2D);
                break;
            case 3:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 4:
                paintBackgroundMouseOverAndFocused(graphics2D);
                break;
            case 5:
                paintBackgroundPressed(graphics2D);
                break;
            case 6:
                paintBackgroundPressedAndFocused(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundFocused(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundMouseOverAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundPressedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.4133738f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(1.9893618f), decodeY(0.120000005f));
        this.path.curveTo(decodeAnchorX(1.9893618f, 3.0f), decodeAnchorY(0.120000005f, 0.0f), decodeAnchorX(2.8857148f, 0.0f), decodeAnchorY(1.0434783f, -3.0f), decodeX(2.8857148f), decodeY(1.0434783f));
        this.path.lineTo(decodeX(2.9f), decodeY(1.9565217f));
        this.path.curveTo(decodeAnchorX(2.9f, 0.0f), decodeAnchorY(1.9565217f, 3.0f), decodeAnchorX(1.9893618f, 3.0f), decodeAnchorY(2.8714287f, 0.0f), decodeX(1.9893618f), decodeY(2.8714287f));
        this.path.lineTo(decodeX(1.0106384f), decodeY(2.8714287f));
        this.path.curveTo(decodeAnchorX(1.0106384f, -3.0f), decodeAnchorY(2.8714287f, 0.0f), decodeAnchorX(0.120000005f, 0.0f), decodeAnchorY(1.9565217f, 3.0f), decodeX(0.120000005f), decodeY(1.9565217f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(1.0465839f));
        this.path.curveTo(decodeAnchorX(0.120000005f, 0.0f), decodeAnchorY(1.0465839f, -3.0f), decodeAnchorX(1.0106384f, -3.0f), decodeAnchorY(0.120000005f, 0.0f), decodeX(1.0106384f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(1.4148936f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(1.4148936f), decodeY(0.4857143f));
        this.path.lineTo(decodeX(1.0106384f), decodeY(0.4857143f));
        this.path.curveTo(decodeAnchorX(1.0106384f, -1.9285715f), decodeAnchorY(0.4857143f, 0.0f), decodeAnchorX(0.47142857f, -0.044279482f), decodeAnchorY(1.0403726f, -2.429218f), decodeX(0.47142857f), decodeY(1.0403726f));
        this.path.lineTo(decodeX(0.47142857f), decodeY(1.9565217f));
        this.path.curveTo(decodeAnchorX(0.47142857f, 0.0f), decodeAnchorY(1.9565217f, 2.2142856f), decodeAnchorX(1.0106384f, -1.7857143f), decodeAnchorY(2.5142856f, 0.0f), decodeX(1.0106384f), decodeY(2.5142856f));
        this.path.lineTo(decodeX(1.9893618f), decodeY(2.5142856f));
        this.path.curveTo(decodeAnchorX(1.9893618f, 2.0714285f), decodeAnchorY(2.5142856f, 0.0f), decodeAnchorX(2.5f, 0.0f), decodeAnchorY(1.9565217f, 2.2142856f), decodeX(2.5f), decodeY(1.9565217f));
        this.path.lineTo(decodeX(2.5142853f), decodeY(1.0434783f));
        this.path.curveTo(decodeAnchorX(2.5142853f, 0.0f), decodeAnchorY(1.0434783f, -2.142857f), decodeAnchorX(1.9901216f, 2.142857f), decodeAnchorY(0.47142857f, 0.0f), decodeX(1.9901216f), decodeY(0.47142857f));
        this.path.lineTo(decodeX(1.4148936f), decodeY(0.4857143f));
        this.path.lineTo(decodeX(1.4133738f), decodeY(0.120000005f));
        this.path.closePath();
        return this.path;
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(0.4f), decodeY(0.6f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.8f) - decodeY(0.6f), 12.0d, 12.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(0.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.4f), 12.0d, 12.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(0.6f), decodeY(0.6f), decodeX(2.4f) - decodeX(0.6f), decodeY(2.4f) - decodeY(0.6f), 9.0d, 9.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect4() {
        this.roundRect.setRoundRect(decodeX(0.120000005f), decodeY(0.120000005f), decodeX(2.8800004f) - decodeX(0.120000005f), decodeY(2.8800004f) - decodeY(0.120000005f), 13.0d, 13.0d);
        return this.roundRect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.09f, 0.52f, 0.95f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.03f, 0.06f, 0.33f, 0.6f, 0.65f, 0.7f, 0.825f, 0.95f, 0.975f, 1.0f}, new Color[]{this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8, decodeColor(this.color8, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.03f, 0.06f, 0.33f, 0.6f, 0.65f, 0.7f, 0.825f, 0.95f, 0.975f, 1.0f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11, decodeColor(this.color11, this.color11, 0.5f), this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13});
    }
}
