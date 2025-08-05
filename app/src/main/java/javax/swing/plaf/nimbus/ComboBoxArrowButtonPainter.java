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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ComboBoxArrowButtonPainter.class */
final class ComboBoxArrowButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_ENABLED_MOUSEOVER = 3;
    static final int BACKGROUND_ENABLED_PRESSED = 4;
    static final int BACKGROUND_DISABLED_EDITABLE = 5;
    static final int BACKGROUND_ENABLED_EDITABLE = 6;
    static final int BACKGROUND_MOUSEOVER_EDITABLE = 7;
    static final int BACKGROUND_PRESSED_EDITABLE = 8;
    static final int BACKGROUND_SELECTED_EDITABLE = 9;
    static final int FOREGROUND_ENABLED = 10;
    static final int FOREGROUND_MOUSEOVER = 11;
    static final int FOREGROUND_DISABLED = 12;
    static final int FOREGROUND_PRESSED = 13;
    static final int FOREGROUND_SELECTED = 14;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -247);
    private Color color2 = decodeColor("nimbusBase", 0.021348298f, -0.56289876f, 0.2588235f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.010237217f, -0.55799407f, 0.20784312f, 0);
    private Color color4 = new Color(255, 200, 0, 255);
    private Color color5 = decodeColor("nimbusBase", 0.021348298f, -0.59223604f, 0.35294116f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.02391243f, -0.5774183f, 0.32549018f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    private Color color8 = decodeColor("nimbusBase", 0.021348298f, -0.567841f, 0.31764704f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -191);
    private Color color10 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    private Color color11 = decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    private Color color12 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.0023007393f, -0.46825016f, 0.27058822f, 0);
    private Color color14 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    private Color color15 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    private Color color17 = decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    private Color color18 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color19 = decodeColor("nimbusBase", 0.0023007393f, -0.48084703f, 0.33725488f, 0);
    private Color color20 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    private Color color21 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4757143f, 0.43137252f, 0);
    private Color color22 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color23 = decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    private Color color24 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    private Color color25 = decodeColor("nimbusBase", 7.13408E-4f, -0.064285696f, 0.027450979f, 0);
    private Color color26 = decodeColor("nimbusBase", 0.0f, -0.00895375f, 0.007843137f, 0);
    private Color color27 = decodeColor("nimbusBase", 8.9377165E-4f, -0.13853917f, 0.14509803f, 0);
    private Color color28 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.37254906f, 0);
    private Color color29 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.5254902f, 0);
    private Color color30 = decodeColor("nimbusBase", 0.027408898f, -0.57391655f, 0.1490196f, 0);
    private Color color31 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Object[] componentColors;

    public ComboBoxArrowButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 5:
                paintBackgroundDisabledAndEditable(graphics2D);
                break;
            case 6:
                paintBackgroundEnabledAndEditable(graphics2D);
                break;
            case 7:
                paintBackgroundMouseOverAndEditable(graphics2D);
                break;
            case 8:
                paintBackgroundPressedAndEditable(graphics2D);
                break;
            case 9:
                paintBackgroundSelectedAndEditable(graphics2D);
                break;
            case 10:
                paintForegroundEnabled(graphics2D);
                break;
            case 11:
                paintForegroundMouseOver(graphics2D);
                break;
            case 12:
                paintForegroundDisabled(graphics2D);
                break;
            case 13:
                paintForegroundPressed(graphics2D);
                break;
            case 14:
                paintForegroundSelected(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabledAndEditable(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabledAndEditable(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOverAndEditable(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressedAndEditable(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndEditable(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintForegroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
    }

    private void paintForegroundMouseOver(Graphics2D graphics2D) {
        this.path = decodePath6();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
    }

    private void paintForegroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath7();
        graphics2D.setPaint(this.color30);
        graphics2D.fill(this.path);
    }

    private void paintForegroundPressed(Graphics2D graphics2D) {
        this.path = decodePath8();
        graphics2D.setPaint(this.color31);
        graphics2D.fill(this.path);
    }

    private void paintForegroundSelected(Graphics2D graphics2D) {
        this.path = decodePath7();
        graphics2D.setPaint(this.color31);
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.75f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.75f), decodeY(2.25f));
        this.path.curveTo(decodeAnchorX(2.75f, 0.0f), decodeAnchorY(2.25f, 4.0f), decodeAnchorX(2.125f, 3.0f), decodeAnchorY(2.875f, 0.0f), decodeX(2.125f), decodeY(2.875f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.875f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.25f));
        this.path.lineTo(decodeX(2.125f), decodeY(0.25f));
        this.path.curveTo(decodeAnchorX(2.125f, 3.0f), decodeAnchorY(0.25f, 0.0f), decodeAnchorX(2.75f, 0.0f), decodeAnchorY(0.875f, -3.0f), decodeX(2.75f), decodeY(0.875f));
        this.path.lineTo(decodeX(2.75f), decodeY(2.125f));
        this.path.curveTo(decodeAnchorX(2.75f, 0.0f), decodeAnchorY(2.125f, 3.0f), decodeAnchorX(2.125f, 3.0f), decodeAnchorY(2.75f, 0.0f), decodeX(2.125f), decodeY(2.75f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.75f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.25f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.85294116f), decodeY(2.639706f));
        this.path.lineTo(decodeX(0.85294116f), decodeY(2.639706f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(0.375f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.375f));
        this.path.curveTo(decodeAnchorX(2.0f, 4.0f), decodeAnchorY(0.375f, 0.0f), decodeAnchorX(2.625f, 0.0f), decodeAnchorY(1.0f, -4.0f), decodeX(2.625f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.625f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(2.625f, 0.0f), decodeAnchorY(2.0f, 4.0f), decodeAnchorX(2.0f, 4.0f), decodeAnchorY(2.625f, 0.0f), decodeX(2.0f), decodeY(2.625f));
        this.path.lineTo(decodeX(1.0f), decodeY(2.625f));
        this.path.lineTo(decodeX(1.0f), decodeY(0.375f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(0.9995915f), decodeY(1.3616071f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.8333333f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.8571429f));
        this.path.lineTo(decodeX(0.9995915f), decodeY(1.3616071f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(1.00625f), decodeY(1.3526785f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.8333333f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.8571429f));
        this.path.lineTo(decodeX(1.00625f), decodeY(1.3526785f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath7() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0117648f), decodeY(1.3616071f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.8333333f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.8571429f));
        this.path.lineTo(decodeX(1.0117648f), decodeY(1.3616071f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath8() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0242647f), decodeY(1.3526785f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.8333333f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.8571429f));
        this.path.lineTo(decodeX(1.0242647f), decodeY(1.3526785f));
        this.path.closePath();
        return this.path;
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
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.171875f, 0.34375f, 0.4815341f, 0.6193182f, 0.8096591f, 1.0f}, new Color[]{this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12299465f, 0.44652405f, 0.5441176f, 0.64171124f, 0.8208556f, 1.0f}, new Color[]{this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12299465f, 0.44652405f, 0.5441176f, 0.64171124f, 0.81283426f, 0.98395723f}, new Color[]{this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12299465f, 0.44652405f, 0.5441176f, 0.64171124f, 0.8208556f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((1.0f * width) + x2, (0.5f * height) + y2, (0.0f * width) + x2, (0.5f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29});
    }
}
