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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ScrollBarThumbPainter.class */
final class ScrollBarThumbPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_FOCUSED = 3;
    static final int BACKGROUND_MOUSEOVER = 4;
    static final int BACKGROUND_PRESSED = 5;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBase", 5.1498413E-4f, 0.18061227f, -0.35686278f, 0);
    private Color color2 = decodeColor("nimbusBase", 5.1498413E-4f, -0.21018237f, -0.18039218f, 0);
    private Color color3 = decodeColor("nimbusBase", 7.13408E-4f, -0.53277314f, 0.25098038f, 0);
    private Color color4 = decodeColor("nimbusBase", -0.07865167f, -0.6317617f, 0.44313723f, 0);
    private Color color5 = decodeColor("nimbusBase", 5.1498413E-4f, -0.44340658f, 0.26666665f, 0);
    private Color color6 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4669379f, 0.38039213f, 0);
    private Color color7 = decodeColor("nimbusBase", -0.07865167f, -0.56512606f, 0.45098037f, 0);
    private Color color8 = decodeColor("nimbusBase", -0.0017285943f, -0.362987f, 0.011764705f, 0);
    private Color color9 = decodeColor("nimbusBase", 5.2034855E-5f, -0.41753247f, 0.09803921f, -222);
    private Color color10 = new Color(255, 200, 0, 255);
    private Color color11 = decodeColor("nimbusBase", -0.0017285943f, -0.362987f, 0.011764705f, -255);
    private Color color12 = decodeColor("nimbusBase", 0.010237217f, -0.5621849f, 0.25098038f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);
    private Color color14 = decodeColor("nimbusBase", 5.1498413E-4f, -0.45950285f, 0.34117645f, 0);
    private Color color15 = decodeColor("nimbusBase", -0.0017285943f, -0.48277313f, 0.45098037f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Color color17 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color18 = decodeColor("nimbusBase", 0.0013483167f, 0.29021162f, -0.33725494f, 0);
    private Color color19 = decodeColor("nimbusBase", 0.002908647f, -0.29012606f, -0.015686274f, 0);
    private Color color20 = decodeColor("nimbusBase", -8.738637E-4f, -0.40612245f, 0.21960783f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.0f, -0.01765871f, 0.015686274f, 0);
    private Color color22 = decodeColor("nimbusBase", 0.0f, -0.12714285f, 0.1372549f, 0);
    private Color color23 = decodeColor("nimbusBase", 0.0018727183f, -0.23116884f, 0.31372547f, 0);
    private Color color24 = decodeColor("nimbusBase", -8.738637E-4f, -0.3579365f, -0.33725494f, 0);
    private Color color25 = decodeColor("nimbusBase", 0.004681647f, -0.3857143f, -0.36078435f, 0);
    private Object[] componentColors;

    public ScrollBarThumbPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 2:
                paintBackgroundEnabled(graphics2D);
                break;
            case 4:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 5:
                paintBackgroundPressed(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
        this.path = decodePath6();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.0666667f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(1.0666667f, 6.0f), decodeAnchorX(1.0f, -10.0f), decodeAnchorY(2.0f, 0.0f), decodeX(1.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.0f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(2.0f, 10.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(1.0666667f, 6.0f), decodeX(3.0f), decodeY(1.0666667f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.06666667f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.06666667f), decodeY(1.0666667f));
        this.path.curveTo(decodeAnchorX(0.06666667f, -0.045454547f), decodeAnchorY(1.0666667f, 8.454545f), decodeAnchorX(1.0f, -5.8636365f), decodeAnchorY(1.9333334f, 0.0f), decodeX(1.0f), decodeY(1.9333334f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.9333334f));
        this.path.curveTo(decodeAnchorX(2.0f, 5.909091f), decodeAnchorY(1.9333334f, -3.5527137E-15f), decodeAnchorX(2.9333334f, -0.045454547f), decodeAnchorY(1.0666667f, 8.363636f), decodeX(2.9333334f), decodeY(1.0666667f));
        this.path.lineTo(decodeX(2.9333334f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.06666667f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.06666667f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.16060607f), decodeY(1.5090909f));
        this.path.curveTo(decodeAnchorX(0.16060607f, 0.0f), decodeAnchorY(1.5090909f, 0.0f), decodeAnchorX(0.2f, -0.95454544f), decodeAnchorY(1.1363636f, 1.5454545f), decodeX(0.2f), decodeY(1.1363636f));
        this.path.curveTo(decodeAnchorX(0.2f, 0.95454544f), decodeAnchorY(1.1363636f, -1.5454545f), decodeAnchorX(0.4f, 0.0f), decodeAnchorY(1.0f, 0.0f), decodeX(0.4f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(2.4242425f), decodeY(1.5121212f));
        this.path.lineTo(decodeX(2.4242425f), decodeY(1.5121212f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(2.9363637f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.6030304f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(2.6030304f, 0.0f), decodeAnchorY(1.0f, 0.0f), decodeAnchorX(2.778788f, -0.6818182f), decodeAnchorY(1.1333333f, -1.2272727f), decodeX(2.778788f), decodeY(1.1333333f));
        this.path.curveTo(decodeAnchorX(2.778788f, 0.6818182f), decodeAnchorY(1.1333333f, 1.2272727f), decodeAnchorX(2.8393939f, 0.0f), decodeAnchorY(1.5060606f, 0.0f), decodeX(2.8393939f), decodeY(1.5060606f));
        this.path.lineTo(decodeX(2.9363637f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(2.9363637f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.5563636f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(2.5563636f, 0.0f), decodeAnchorY(1.0f, 0.0f), decodeAnchorX(2.7587879f, -0.6818182f), decodeAnchorY(1.14f, -1.2272727f), decodeX(2.7587879f), decodeY(1.14f));
        this.path.curveTo(decodeAnchorX(2.7587879f, 0.6818182f), decodeAnchorY(1.14f, 1.2272727f), decodeAnchorX(2.8393939f, 0.0f), decodeAnchorY(1.5060606f, 0.0f), decodeX(2.8393939f), decodeY(1.5060606f));
        this.path.lineTo(decodeX(2.9363637f), decodeY(1.0f));
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

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038922157f, 0.0508982f, 0.06287425f, 0.19610777f, 0.32934132f, 0.48952097f, 0.6497006f, 0.8248503f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.06818182f * width) + x2, ((-0.005952381f) * height) + y2, (0.3689091f * width) + x2, (0.23929171f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.9409091f * width) + x2, (0.035928145f * height) + y2, (0.5954546f * width) + x2, (0.26347303f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color8, decodeColor(this.color8, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038922157f, 0.0508982f, 0.06287425f, 0.19610777f, 0.32934132f, 0.48952097f, 0.6497006f, 0.8248503f, 1.0f}, new Color[]{this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038922157f, 0.0508982f, 0.06287425f, 0.19610777f, 0.32934132f, 0.48952097f, 0.6497006f, 0.8248503f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.06818182f * width) + x2, ((-0.005952381f) * height) + y2, (0.3689091f * width) + x2, (0.23929171f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.9409091f * width) + x2, (0.035928145f * height) + y2, (0.37615633f * width) + x2, (0.34910178f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color25, decodeColor(this.color25, this.color11, 0.5f), this.color11});
    }
}
