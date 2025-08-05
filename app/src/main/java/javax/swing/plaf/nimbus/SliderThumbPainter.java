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
import sun.reflect.ClassFileConstants;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SliderThumbPainter.class */
final class SliderThumbPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_FOCUSED = 3;
    static final int BACKGROUND_FOCUSED_MOUSEOVER = 4;
    static final int BACKGROUND_FOCUSED_PRESSED = 5;
    static final int BACKGROUND_MOUSEOVER = 6;
    static final int BACKGROUND_PRESSED = 7;
    static final int BACKGROUND_ENABLED_ARROWSHAPE = 8;
    static final int BACKGROUND_DISABLED_ARROWSHAPE = 9;
    static final int BACKGROUND_MOUSEOVER_ARROWSHAPE = 10;
    static final int BACKGROUND_PRESSED_ARROWSHAPE = 11;
    static final int BACKGROUND_FOCUSED_ARROWSHAPE = 12;
    static final int BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE = 13;
    static final int BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE = 14;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBase", 0.021348298f, -0.5625436f, 0.25490195f, 0);
    private Color color2 = decodeColor("nimbusBase", 0.015098333f, -0.55105823f, 0.19215685f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.021348298f, -0.5924243f, 0.35686272f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.021348298f, -0.56844974f, 0.32549018f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", -0.003968239f, 0.0014736876f, -0.25490198f, -156);
    private Color color7 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    private Color color8 = decodeColor("nimbusBase", -0.0017285943f, -0.11571431f, -0.25490198f, 0);
    private Color color9 = decodeColor("nimbusBase", -0.023096085f, -0.6238095f, 0.43921566f, 0);
    private Color color10 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    private Color color11 = decodeColor("nimbusBase", 5.1498413E-4f, -0.45714286f, 0.32941175f, 0);
    private Color color12 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color13 = decodeColor("nimbusBase", -0.0038217902f, -0.15532213f, -0.14901963f, 0);
    private Color color14 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54509807f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.004681647f, -0.62780917f, 0.44313723f, 0);
    private Color color16 = decodeColor("nimbusBase", 2.9569864E-4f, -0.4653107f, 0.32549018f, 0);
    private Color color17 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4563421f, 0.32549018f, 0);
    private Color color18 = decodeColor("nimbusBase", -0.0017285943f, -0.4732143f, 0.39215684f, 0);
    private Color color19 = decodeColor("nimbusBase", 0.0015952587f, -0.04875779f, -0.18823531f, 0);
    private Color color20 = decodeColor("nimbusBase", 2.9569864E-4f, -0.44943976f, 0.25098038f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.0f, 0.0f, 0.0f, 0);
    private Color color22 = decodeColor("nimbusBase", 8.9377165E-4f, -0.121094406f, 0.12156862f, 0);
    private Color color23 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, ClassFileConstants.opc_i2d);
    private Color color24 = new Color(150, 156, 168, 146);
    private Color color25 = decodeColor("nimbusBase", -0.0033828616f, -0.40608466f, -0.019607842f, 0);
    private Color color26 = decodeColor("nimbusBase", 5.1498413E-4f, -0.17594418f, -0.20784315f, 0);
    private Color color27 = decodeColor("nimbusBase", 0.0023007393f, -0.11332625f, -0.28627452f, 0);
    private Color color28 = decodeColor("nimbusBase", -0.023096085f, -0.62376213f, 0.4352941f, 0);
    private Color color29 = decodeColor("nimbusBase", 0.004681647f, -0.594392f, 0.39999998f, 0);
    private Color color30 = decodeColor("nimbusBase", -0.0017285943f, -0.4454704f, 0.25490195f, 0);
    private Color color31 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);
    private Color color32 = decodeColor("nimbusBase", 5.1498413E-4f, -0.47442397f, 0.4235294f, 0);
    private Object[] componentColors;

    public SliderThumbPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundFocusedAndMouseOver(graphics2D);
                break;
            case 5:
                paintBackgroundFocusedAndPressed(graphics2D);
                break;
            case 6:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 7:
                paintBackgroundPressed(graphics2D);
                break;
            case 8:
                paintBackgroundEnabledAndArrowShape(graphics2D);
                break;
            case 9:
                paintBackgroundDisabledAndArrowShape(graphics2D);
                break;
            case 10:
                paintBackgroundMouseOverAndArrowShape(graphics2D);
                break;
            case 11:
                paintBackgroundPressedAndArrowShape(graphics2D);
                break;
            case 12:
                paintBackgroundFocusedAndArrowShape(graphics2D);
                break;
            case 13:
                paintBackgroundFocusedAndMouseOverAndArrowShape(graphics2D);
                break;
            case 14:
                paintBackgroundFocusedAndPressedAndArrowShape(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient1(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient2(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient3(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient4(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundFocused(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient3(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient4(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundFocusedAndMouseOver(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient5(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient6(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundFocusedAndPressed(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient7(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient8(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient5(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient6(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse3();
        graphics2D.setPaint(this.color23);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(decodeGradient7(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(decodeGradient8(this.ellipse));
        graphics2D.fill(this.ellipse);
    }

    private void paintBackgroundEnabledAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color24);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundDisabledAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOverAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color24);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient13(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient14(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressedAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color24);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient15(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient16(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundFocusedAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient17(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundFocusedAndMouseOverAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient13(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient14(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundFocusedAndPressedAndArrowShape(Graphics2D graphics2D) {
        this.path = decodePath4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient15(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient16(this.path));
        graphics2D.fill(this.path);
    }

    private Ellipse2D decodeEllipse1() {
        this.ellipse.setFrame(decodeX(0.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.4f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse2() {
        this.ellipse.setFrame(decodeX(0.6f), decodeY(0.6f), decodeX(2.4f) - decodeX(0.6f), decodeY(2.4f) - decodeY(0.6f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse3() {
        this.ellipse.setFrame(decodeX(0.4f), decodeY(0.6f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.8f) - decodeY(0.6f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse4() {
        this.ellipse.setFrame(decodeX(0.120000005f), decodeY(0.120000005f), decodeX(2.8799999f) - decodeX(0.120000005f), decodeY(2.8799999f) - decodeY(0.120000005f));
        return this.ellipse;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.8166667f), decodeY(0.5007576f));
        this.path.curveTo(decodeAnchorX(0.8166667f, 1.5643269f), decodeAnchorY(0.5007576f, -0.3097513f), decodeAnchorX(2.7925456f, 0.058173586f), decodeAnchorY(1.6116884f, -0.4647635f), decodeX(2.7925456f), decodeY(1.6116884f));
        this.path.curveTo(decodeAnchorX(2.7925456f, -0.34086856f), decodeAnchorY(1.6116884f, 2.7232852f), decodeAnchorX(0.7006364f, 4.568128f), decodeAnchorY(2.7693636f, -0.006014915f), decodeX(0.7006364f), decodeY(2.7693636f));
        this.path.curveTo(decodeAnchorX(0.7006364f, -3.5233955f), decodeAnchorY(2.7693636f, 0.004639302f), decodeAnchorX(0.8166667f, -1.8635255f), decodeAnchorY(0.5007576f, 0.36899543f), decodeX(0.8166667f), decodeY(0.5007576f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6155303f), decodeY(2.5954547f));
        this.path.curveTo(decodeAnchorX(0.6155303f, 0.90980893f), decodeAnchorY(2.5954547f, 1.3154242f), decodeAnchorX(2.6151516f, 0.014588808f), decodeAnchorY(1.6112013f, 0.9295521f), decodeX(2.6151516f), decodeY(1.6112013f));
        this.path.curveTo(decodeAnchorX(2.6151516f, -0.01365518f), decodeAnchorY(1.6112013f, -0.8700643f), decodeAnchorX(0.60923916f, 0.9729935f), decodeAnchorY(0.40716404f, -1.4248644f), decodeX(0.60923916f), decodeY(0.40716404f));
        this.path.curveTo(decodeAnchorX(0.60923916f, -0.7485209f), decodeAnchorY(0.40716404f, 1.0961438f), decodeAnchorX(0.6155303f, -0.74998796f), decodeAnchorY(2.5954547f, -1.0843511f), decodeX(0.6155303f), decodeY(2.5954547f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.8055606f), decodeY(0.6009697f));
        this.path.curveTo(decodeAnchorX(0.8055606f, 0.50820893f), decodeAnchorY(0.6009697f, -0.8490881f), decodeAnchorX(2.3692727f, 0.0031846066f), decodeAnchorY(1.613117f, -0.60668826f), decodeX(2.3692727f), decodeY(1.613117f));
        this.path.curveTo(decodeAnchorX(2.3692727f, -0.003890196f), decodeAnchorY(1.613117f, 0.74110764f), decodeAnchorX(0.7945455f, 0.3870974f), decodeAnchorY(2.3932729f, 1.240782f), decodeX(0.7945455f), decodeY(2.3932729f));
        this.path.curveTo(decodeAnchorX(0.7945455f, -0.38636583f), decodeAnchorY(2.3932729f, -1.2384372f), decodeAnchorX(0.8055606f, -0.995154f), decodeAnchorY(0.6009697f, 1.6626496f), decodeX(0.8055606f), decodeY(0.6009697f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(0.60059524f), decodeY(0.11727543f));
        this.path.curveTo(decodeAnchorX(0.60059524f, 1.5643269f), decodeAnchorY(0.11727543f, -0.3097513f), decodeAnchorX(2.7925456f, 0.004405844f), decodeAnchorY(1.6116884f, -1.1881162f), decodeX(2.7925456f), decodeY(1.6116884f));
        this.path.curveTo(decodeAnchorX(2.7925456f, -0.007364541f), decodeAnchorY(1.6116884f, 1.9859827f), decodeAnchorX(0.7006364f, 2.7716863f), decodeAnchorY(2.8693638f, -0.008974582f), decodeX(0.7006364f), decodeY(2.8693638f));
        this.path.curveTo(decodeAnchorX(0.7006364f, -3.754899f), decodeAnchorY(2.8693638f, 0.012158176f), decodeAnchorX(0.60059524f, -1.8635255f), decodeAnchorY(0.11727543f, 0.36899543f), decodeX(0.60059524f), decodeY(0.11727543f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5106101f * width) + x2, ((-4.553649E-18f) * height) + y2, (0.49933687f * width) + x2, (1.0039787f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5023511f * width) + x2, (0.0015673981f * height) + y2, (0.5023511f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.21256684f, 0.42513368f, 0.71256685f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.51f * width) + x2, ((-4.553649E-18f) * height) + y2, (0.51f * width) + x2, (1.0039787f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0015673981f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.21256684f, 0.42513368f, 0.56149733f, 0.69786096f, 0.8489305f, 1.0f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5106101f * width) + x2, ((-4.553649E-18f) * height) + y2, (0.49933687f * width) + x2, (1.0039787f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5023511f * width) + x2, (0.0015673981f * height) + y2, (0.5023511f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.21256684f, 0.42513368f, 0.56149733f, 0.69786096f, 0.8489305f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5106101f * width) + x2, ((-4.553649E-18f) * height) + y2, (0.49933687f * width) + x2, (1.0039787f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color14, decodeColor(this.color14, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5023511f * width) + x2, (0.0015673981f * height) + y2, (0.5023511f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.23796791f, 0.47593582f, 0.5360962f, 0.5962567f, 0.79812837f, 1.0f}, new Color[]{this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.24032257f, 0.48064515f, 0.7403226f, 1.0f}, new Color[]{this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.061290324f, 0.1016129f, 0.14193548f, 0.3016129f, 0.46129033f, 0.5983871f, 0.7354839f, 0.7935484f, 0.8516129f}, new Color[]{this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30, decodeColor(this.color30, this.color31, 0.5f), this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.21256684f, 0.42513368f, 0.71256685f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient13(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient14(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.21256684f, 0.42513368f, 0.56149733f, 0.69786096f, 0.8489305f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18});
    }

    private Paint decodeGradient15(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color14, decodeColor(this.color14, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient16(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.23796791f, 0.47593582f, 0.5360962f, 0.5962567f, 0.79812837f, 1.0f}, new Color[]{this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient17(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.4925773f * width) + x2, (0.082019866f * height) + y2, (0.4925773f * width) + x2, (0.91798013f * height) + y2, new float[]{0.061290324f, 0.1016129f, 0.14193548f, 0.3016129f, 0.46129033f, 0.5983871f, 0.7354839f, 0.7935484f, 0.8516129f}, new Color[]{this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30, decodeColor(this.color30, this.color31, 0.5f), this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32});
    }
}
