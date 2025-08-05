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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SpinnerPreviousButtonPainter.class */
final class SpinnerPreviousButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_FOCUSED = 3;
    static final int BACKGROUND_MOUSEOVER_FOCUSED = 4;
    static final int BACKGROUND_PRESSED_FOCUSED = 5;
    static final int BACKGROUND_MOUSEOVER = 6;
    static final int BACKGROUND_PRESSED = 7;
    static final int FOREGROUND_DISABLED = 8;
    static final int FOREGROUND_ENABLED = 9;
    static final int FOREGROUND_FOCUSED = 10;
    static final int FOREGROUND_MOUSEOVER_FOCUSED = 11;
    static final int FOREGROUND_PRESSED_FOCUSED = 12;
    static final int FOREGROUND_MOUSEOVER = 13;
    static final int FOREGROUND_PRESSED = 14;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBase", 0.015098333f, -0.5557143f, 0.2352941f, 0);
    private Color color2 = decodeColor("nimbusBase", 0.010237217f, -0.55799407f, 0.20784312f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.018570602f, -0.5821429f, 0.32941175f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.021348298f, -0.567841f, 0.31764704f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, -0.0033834577f, -0.30588236f, -148);
    private Color color7 = decodeColor("nimbusBase", 5.1498413E-4f, -0.2583558f, -0.13333336f, 0);
    private Color color8 = decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.004681647f, -0.5383692f, 0.33725488f, 0);
    private Color color10 = decodeColor("nimbusBase", -0.0017285943f, -0.44453782f, 0.25098038f, 0);
    private Color color11 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    private Color color12 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);
    private Color color13 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color14 = decodeColor("nimbusBase", 0.0013483167f, 0.088923395f, -0.2784314f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.0010585189f, -0.541452f, 0.4078431f, 0);
    private Color color17 = decodeColor("nimbusBase", 0.00254488f, -0.4608264f, 0.32549018f, 0);
    private Color color18 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    private Color color19 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4757143f, 0.43137252f, 0);
    private Color color20 = decodeColor("nimbusBase", 0.061133325f, 0.3642857f, -0.427451f, 0);
    private Color color21 = decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    private Color color22 = decodeColor("nimbusBase", 8.354783E-4f, -0.2578073f, 0.12549019f, 0);
    private Color color23 = decodeColor("nimbusBase", 8.9377165E-4f, -0.01599598f, 0.007843137f, 0);
    private Color color24 = decodeColor("nimbusBase", 0.0f, -0.00895375f, 0.007843137f, 0);
    private Color color25 = decodeColor("nimbusBase", 8.9377165E-4f, -0.13853917f, 0.14509803f, 0);
    private Color color26 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.63529414f, -179);
    private Color color27 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -186);
    private Color color28 = decodeColor("nimbusBase", 0.018570602f, -0.56714284f, 0.1372549f, 0);
    private Color color29 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color30 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Object[] componentColors;

    public SpinnerPreviousButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundFocused(graphics2D);
                break;
            case 4:
                paintBackgroundMouseOverAndFocused(graphics2D);
                break;
            case 5:
                paintBackgroundPressedAndFocused(graphics2D);
                break;
            case 6:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 7:
                paintBackgroundPressed(graphics2D);
                break;
            case 8:
                paintForegroundDisabled(graphics2D);
                break;
            case 9:
                paintForegroundEnabled(graphics2D);
                break;
            case 10:
                paintForegroundFocused(graphics2D);
                break;
            case 11:
                paintForegroundMouseOverAndFocused(graphics2D);
                break;
            case 12:
                paintForegroundPressedAndFocused(graphics2D);
                break;
            case 13:
                paintForegroundMouseOver(graphics2D);
                break;
            case 14:
                paintForegroundPressed(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.path);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundFocused(Graphics2D graphics2D) {
        this.path = decodePath4();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOverAndFocused(Graphics2D graphics2D) {
        this.path = decodePath5();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
        this.path = decodePath6();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressedAndFocused(Graphics2D graphics2D) {
        this.path = decodePath4();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.path = decodePath3();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.path);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.path = decodePath8();
        graphics2D.setPaint(this.color27);
        graphics2D.fill(this.path);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintForegroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void paintForegroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color29);
        graphics2D.fill(this.path);
    }

    private void paintForegroundFocused(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color29);
        graphics2D.fill(this.path);
    }

    private void paintForegroundMouseOverAndFocused(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color29);
        graphics2D.fill(this.path);
    }

    private void paintForegroundPressedAndFocused(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color30);
        graphics2D.fill(this.path);
    }

    private void paintForegroundMouseOver(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color29);
        graphics2D.fill(this.path);
    }

    private void paintForegroundPressed(Graphics2D graphics2D) {
        this.path = decodePath9();
        graphics2D.setPaint(this.color30);
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6666667f));
        this.path.lineTo(decodeX(2.142857f), decodeY(2.6666667f));
        this.path.curveTo(decodeAnchorX(2.142857f, 3.0f), decodeAnchorY(2.6666667f, 0.0f), decodeAnchorX(2.7142859f, 0.0f), decodeAnchorY(2.0f, 2.0f), decodeX(2.7142859f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.7142859f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(2.5f));
        this.path.lineTo(decodeX(2.142857f), decodeY(2.5f));
        this.path.curveTo(decodeAnchorX(2.142857f, 2.0f), decodeAnchorY(2.5f, 0.0f), decodeAnchorX(2.5714285f, 0.0f), decodeAnchorY(2.0f, 1.0f), decodeX(2.5714285f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.5714285f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.6666667f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8333333f));
        this.path.lineTo(decodeX(2.0324676f), decodeY(2.8333333f));
        this.path.curveTo(decodeAnchorX(2.0324676f, 2.1136363f), decodeAnchorY(2.8333333f, 0.0f), decodeAnchorX(2.7142859f, 0.0f), decodeAnchorY(2.0f, 3.0f), decodeX(2.7142859f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6666667f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8999999f));
        this.path.lineTo(decodeX(2.2f), decodeY(2.8999999f));
        this.path.curveTo(decodeAnchorX(2.2f, 3.0f), decodeAnchorY(2.8999999f, 0.0f), decodeAnchorX(2.9142857f, 0.0f), decodeAnchorY(2.2333333f, 3.0f), decodeX(2.9142857f), decodeY(2.2333333f));
        this.path.lineTo(decodeX(2.9142857f), decodeY(1.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8999999f));
        this.path.lineTo(decodeX(2.2f), decodeY(2.8999999f));
        this.path.curveTo(decodeAnchorX(2.2f, 3.0f), decodeAnchorY(2.8999999f, 0.0f), decodeAnchorX(2.9142857f, 0.0f), decodeAnchorY(2.2333333f, 3.0f), decodeX(2.9142857f), decodeY(2.2333333f));
        this.path.lineTo(decodeX(2.9142857f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6666667f));
        this.path.lineTo(decodeX(2.142857f), decodeY(2.6666667f));
        this.path.curveTo(decodeAnchorX(2.142857f, 3.0f), decodeAnchorY(2.6666667f, 0.0f), decodeAnchorX(2.7142859f, 0.0f), decodeAnchorY(2.0f, 2.0f), decodeX(2.7142859f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.7142859f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath7() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(2.5f));
        this.path.lineTo(decodeX(2.142857f), decodeY(2.5f));
        this.path.curveTo(decodeAnchorX(2.142857f, 2.0f), decodeAnchorY(2.5f, 0.0f), decodeAnchorX(2.5714285f, 0.0f), decodeAnchorY(2.0f, 1.0f), decodeX(2.5714285f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.5714285f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath8() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.6666667f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8333333f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(2.8333333f, 0.0f), decodeAnchorX(2.0324676f, -2.1136363f), decodeAnchorY(2.8333333f, 0.0f), decodeX(2.0324676f), decodeY(2.8333333f));
        this.path.curveTo(decodeAnchorX(2.0324676f, 2.1136363f), decodeAnchorY(2.8333333f, 0.0f), decodeAnchorX(2.7142859f, 0.0f), decodeAnchorY(2.0f, 3.0f), decodeX(2.7142859f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6666667f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath9() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.5045455f), decodeY(1.9943181f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.0f));
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

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.05748663f, 0.11497326f, 0.55748665f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.05748663f, 0.11497326f, 0.2419786f, 0.36898395f, 0.684492f, 1.0f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.05748663f, 0.11497326f, 0.2419786f, 0.36898395f, 0.684492f, 1.0f}, new Color[]{this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.05748663f, 0.11497326f, 0.2419786f, 0.36898395f, 0.684492f, 1.0f}, new Color[]{this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23, decodeColor(this.color23, this.color24, 0.5f), this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25});
    }
}
