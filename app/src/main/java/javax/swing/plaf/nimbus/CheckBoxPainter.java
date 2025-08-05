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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/CheckBoxPainter.class */
final class CheckBoxPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int ICON_DISABLED = 3;
    static final int ICON_ENABLED = 4;
    static final int ICON_FOCUSED = 5;
    static final int ICON_MOUSEOVER = 6;
    static final int ICON_MOUSEOVER_FOCUSED = 7;
    static final int ICON_PRESSED = 8;
    static final int ICON_PRESSED_FOCUSED = 9;
    static final int ICON_SELECTED = 10;
    static final int ICON_SELECTED_FOCUSED = 11;
    static final int ICON_PRESSED_SELECTED = 12;
    static final int ICON_PRESSED_SELECTED_FOCUSED = 13;
    static final int ICON_MOUSEOVER_SELECTED = 14;
    static final int ICON_MOUSEOVER_SELECTED_FOCUSED = 15;
    static final int ICON_DISABLED_SELECTED = 16;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.06766917f, 0.07843137f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.06484103f, 0.027450979f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.032459438f, -0.60996324f, 0.36470586f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.02551502f, -0.5996783f, 0.3215686f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.032459438f, -0.59624064f, 0.34509802f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, 0.0f, 0.0f, -89);
    private Color color7 = decodeColor("nimbusBlueGrey", 0.0f, -0.05356429f, -0.12549019f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.015789472f, -0.37254903f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.08801502f, -0.63174605f, 0.43921566f, 0);
    private Color color10 = decodeColor("nimbusBase", 0.032459438f, -0.5953556f, 0.32549018f, 0);
    private Color color11 = decodeColor("nimbusBase", 0.032459438f, -0.59942394f, 0.4235294f, 0);
    private Color color12 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color13 = decodeColor("nimbusBlueGrey", 0.0f, -0.020974077f, -0.21960783f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", 0.01010108f, 0.08947369f, -0.5294118f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.032459438f, -0.5985242f, 0.39999998f, 0);
    private Color color17 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Color color18 = decodeColor("nimbusBlueGrey", 0.055555582f, 0.8894737f, -0.7176471f, 0);
    private Color color19 = decodeColor("nimbusBlueGrey", 0.0f, 0.0016232133f, -0.3254902f, 0);
    private Color color20 = decodeColor("nimbusBase", 0.027408898f, -0.5847884f, 0.2980392f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.029681683f, -0.52701867f, 0.17254901f, 0);
    private Color color22 = decodeColor("nimbusBase", 0.029681683f, -0.5376751f, 0.25098038f, 0);
    private Color color23 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    private Color color24 = decodeColor("nimbusBase", 5.1498413E-4f, -0.10238093f, -0.25490198f, 0);
    private Color color25 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    private Color color26 = decodeColor("nimbusBase", 5.1498413E-4f, -0.44153953f, 0.2588235f, 0);
    private Color color27 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4602757f, 0.34509802f, 0);
    private Color color28 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color29 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color30 = decodeColor("nimbusBase", -3.528595E-5f, 0.026785731f, -0.23529413f, 0);
    private Color color31 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    private Color color32 = decodeColor("nimbusBase", -0.0021489263f, -0.2891234f, 0.14117646f, 0);
    private Color color33 = decodeColor("nimbusBase", -0.006362498f, -0.016311288f, -0.02352941f, 0);
    private Color color34 = decodeColor("nimbusBase", 0.0f, -0.17930403f, 0.21568626f, 0);
    private Color color35 = decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    private Color color36 = decodeColor("nimbusBase", 0.05468172f, 0.3642857f, -0.43137258f, 0);
    private Color color37 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color38 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    private Color color39 = decodeColor("nimbusBase", 5.1498413E-4f, -0.47377098f, 0.41960782f, 0);
    private Color color40 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.03771078f, 0.062745094f, 0);
    private Color color41 = decodeColor("nimbusBlueGrey", -0.02222222f, -0.032806106f, 0.011764705f, 0);
    private Color color42 = decodeColor("nimbusBase", 0.021348298f, -0.59223604f, 0.35294116f, 0);
    private Color color43 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    private Color color44 = decodeColor("nimbusBase", 0.021348298f, -0.56875f, 0.32941175f, 0);
    private Color color45 = decodeColor("nimbusBase", 0.027408898f, -0.5735674f, 0.14509803f, 0);
    private Object[] componentColors;

    public CheckBoxPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 3:
                painticonDisabled(graphics2D);
                break;
            case 4:
                painticonEnabled(graphics2D);
                break;
            case 5:
                painticonFocused(graphics2D);
                break;
            case 6:
                painticonMouseOver(graphics2D);
                break;
            case 7:
                painticonMouseOverAndFocused(graphics2D);
                break;
            case 8:
                painticonPressed(graphics2D);
                break;
            case 9:
                painticonPressedAndFocused(graphics2D);
                break;
            case 10:
                painticonSelected(graphics2D);
                break;
            case 11:
                painticonSelectedAndFocused(graphics2D);
                break;
            case 12:
                painticonPressedAndSelected(graphics2D);
                break;
            case 13:
                painticonPressedAndSelectedAndFocused(graphics2D);
                break;
            case 14:
                painticonMouseOverAndSelected(graphics2D);
                break;
            case 15:
                painticonMouseOverAndSelectedAndFocused(graphics2D);
                break;
            case 16:
                painticonDisabledAndSelected(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void painticonDisabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonMouseOver(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonMouseOverAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonPressed(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonPressedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void painticonSelected(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient10(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void painticonSelectedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient10(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void painticonPressedAndSelected(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(this.color29);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient11(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient12(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void painticonPressedAndSelectedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient11(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient12(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void painticonMouseOverAndSelected(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient13(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient14(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void painticonMouseOverAndSelectedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient13(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient14(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
    }

    private void painticonDisabledAndSelected(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient15(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient16(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color45);
        graphics2D.fill(this.path);
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(0.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.4f), 3.7058823108673096d, 3.7058823108673096d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(0.6f), decodeY(0.6f), decodeX(2.4f) - decodeX(0.6f), decodeY(2.4f) - decodeY(0.6f), 3.7647058963775635d, 3.7647058963775635d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(0.4f), decodeY(1.75f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.8f) - decodeY(1.75f), 5.176470756530762d, 5.176470756530762d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect4() {
        this.roundRect.setRoundRect(decodeX(0.120000005f), decodeY(0.120000005f), decodeX(2.8799999f) - decodeX(0.120000005f), decodeY(2.8799999f) - decodeY(0.120000005f), 8.0d, 8.0d);
        return this.roundRect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0036764f), decodeY(1.382353f));
        this.path.lineTo(decodeX(1.2536764f), decodeY(1.382353f));
        this.path.lineTo(decodeX(1.430147f), decodeY(1.757353f));
        this.path.lineTo(decodeX(1.8235294f), decodeY(0.62352943f));
        this.path.lineTo(decodeX(2.2f), decodeY(0.61764705f));
        this.path.lineTo(decodeX(1.492647f), decodeY(2.0058823f));
        this.path.lineTo(decodeX(1.382353f), decodeY(2.0058823f));
        this.path.lineTo(decodeX(1.0036764f), decodeY(1.382353f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color23, decodeColor(this.color23, this.color24, 0.5f), this.color24});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color28, decodeColor(this.color28, this.color30, 0.5f), this.color30});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.05775076f, 0.11550152f, 0.38003993f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32, decodeColor(this.color32, this.color33, 0.5f), this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34});
    }

    private Paint decodeGradient13(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color35, decodeColor(this.color35, this.color36, 0.5f), this.color36});
    }

    private Paint decodeGradient14(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color37, decodeColor(this.color37, this.color38, 0.5f), this.color38, decodeColor(this.color38, this.color39, 0.5f), this.color39});
    }

    private Paint decodeGradient15(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25210086f * width) + x2, (0.9957983f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color40, decodeColor(this.color40, this.color41, 0.5f), this.color41});
    }

    private Paint decodeGradient16(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.997549f * height) + y2, new float[]{0.0f, 0.32228917f, 0.64457834f, 0.82228917f, 1.0f}, new Color[]{this.color42, decodeColor(this.color42, this.color43, 0.5f), this.color43, decodeColor(this.color43, this.color44, 0.5f), this.color44});
    }
}
