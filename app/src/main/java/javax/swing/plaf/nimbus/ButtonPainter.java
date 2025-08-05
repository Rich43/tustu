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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ButtonPainter.class */
final class ButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DEFAULT = 1;
    static final int BACKGROUND_DEFAULT_FOCUSED = 2;
    static final int BACKGROUND_MOUSEOVER_DEFAULT = 3;
    static final int BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED = 4;
    static final int BACKGROUND_PRESSED_DEFAULT = 5;
    static final int BACKGROUND_PRESSED_DEFAULT_FOCUSED = 6;
    static final int BACKGROUND_DISABLED = 7;
    static final int BACKGROUND_ENABLED = 8;
    static final int BACKGROUND_FOCUSED = 9;
    static final int BACKGROUND_MOUSEOVER = 10;
    static final int BACKGROUND_MOUSEOVER_FOCUSED = 11;
    static final int BACKGROUND_PRESSED = 12;
    static final int BACKGROUND_PRESSED_FOCUSED = 13;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.06885965f, -0.36862746f, -190);
    private Color color2 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    private Color color3 = decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.004681647f, -0.5766426f, 0.38039213f, 0);
    private Color color6 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    private Color color7 = decodeColor("nimbusBase", 5.1498413E-4f, -0.46404046f, 0.36470586f, 0);
    private Color color8 = decodeColor("nimbusBase", 5.1498413E-4f, -0.47761154f, 0.44313723f, 0);
    private Color color9 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color10 = decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    private Color color11 = decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    private Color color12 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color13 = decodeColor("nimbusBase", -0.0017285943f, -0.5822163f, 0.40392154f, 0);
    private Color color14 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    private Color color15 = decodeColor("nimbusBase", 5.1498413E-4f, -0.47698414f, 0.43921566f, 0);
    private Color color16 = decodeColor("nimbusBase", -0.06415892f, -0.5455182f, 0.45098037f, 0);
    private Color color17 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -95);
    private Color color18 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color19 = decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    private Color color20 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.001903832f, -0.29863563f, 0.1490196f, 0);
    private Color color22 = decodeColor("nimbusBase", 0.0f, 0.0f, 0.0f, 0);
    private Color color23 = decodeColor("nimbusBase", 0.0018727183f, -0.14126986f, 0.15686274f, 0);
    private Color color24 = decodeColor("nimbusBase", 8.9377165E-4f, -0.20852983f, 0.2588235f, 0);
    private Color color25 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.06885965f, -0.36862746f, -232);
    private Color color26 = decodeColor("nimbusBlueGrey", 0.0f, -0.06766917f, 0.07843137f, 0);
    private Color color27 = decodeColor("nimbusBlueGrey", 0.0f, -0.06484103f, 0.027450979f, 0);
    private Color color28 = decodeColor("nimbusBlueGrey", 0.0f, -0.08477524f, 0.16862744f, 0);
    private Color color29 = decodeColor("nimbusBlueGrey", -0.015872955f, -0.080091536f, 0.15686274f, 0);
    private Color color30 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    private Color color31 = decodeColor("nimbusBlueGrey", 0.0f, -0.07052632f, 0.1372549f, 0);
    private Color color32 = decodeColor("nimbusBlueGrey", 0.0f, -0.070878744f, 0.14509803f, 0);
    private Color color33 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.05356429f, -0.12549019f, 0);
    private Color color34 = decodeColor("nimbusBlueGrey", 0.0f, -0.0147816315f, -0.3764706f, 0);
    private Color color35 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10655806f, 0.24313724f, 0);
    private Color color36 = decodeColor("nimbusBlueGrey", 0.0f, -0.09823123f, 0.2117647f, 0);
    private Color color37 = decodeColor("nimbusBlueGrey", 0.0f, -0.0749532f, 0.24705881f, 0);
    private Color color38 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color39 = decodeColor("nimbusBlueGrey", 0.0f, -0.020974077f, -0.21960783f, 0);
    private Color color40 = decodeColor("nimbusBlueGrey", 0.0f, 0.11169591f, -0.53333336f, 0);
    private Color color41 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10658931f, 0.25098038f, 0);
    private Color color42 = decodeColor("nimbusBlueGrey", 0.0f, -0.098526314f, 0.2352941f, 0);
    private Color color43 = decodeColor("nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
    private Color color44 = new Color(245, 250, 255, 160);
    private Color color45 = decodeColor("nimbusBlueGrey", 0.055555582f, 0.8894737f, -0.7176471f, 0);
    private Color color46 = decodeColor("nimbusBlueGrey", 0.0f, 5.847961E-4f, -0.32156864f, 0);
    private Color color47 = decodeColor("nimbusBlueGrey", -0.00505054f, -0.05960039f, 0.10196078f, 0);
    private Color color48 = decodeColor("nimbusBlueGrey", -0.008547008f, -0.04772438f, 0.06666666f, 0);
    private Color color49 = decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
    private Color color50 = decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0212406f, 0.13333333f, 0);
    private Color color51 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.030845039f, 0.23921567f, 0);
    private Object[] componentColors;

    public ButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintBackgroundDefault(graphics2D);
                break;
            case 2:
                paintBackgroundDefaultAndFocused(graphics2D);
                break;
            case 3:
                paintBackgroundMouseOverAndDefault(graphics2D);
                break;
            case 4:
                paintBackgroundMouseOverAndDefaultAndFocused(graphics2D);
                break;
            case 5:
                paintBackgroundPressedAndDefault(graphics2D);
                break;
            case 6:
                paintBackgroundPressedAndDefaultAndFocused(graphics2D);
                break;
            case 7:
                paintBackgroundDisabled(graphics2D);
                break;
            case 8:
                paintBackgroundEnabled(graphics2D);
                break;
            case 9:
                paintBackgroundFocused(graphics2D);
                break;
            case 10:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 11:
                paintBackgroundMouseOverAndFocused(graphics2D);
                break;
            case 12:
                paintBackgroundPressed(graphics2D);
                break;
            case 13:
                paintBackgroundPressedAndFocused(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected Object[] getExtendedCacheKeys(JComponent jComponent) {
        Object[] objArr = null;
        switch (this.state) {
            case 1:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color4, -0.6197143f, 0.43137252f, 0), getComponentColor(jComponent, "background", this.color5, -0.5766426f, 0.38039213f, 0), getComponentColor(jComponent, "background", this.color6, -0.43866998f, 0.24705881f, 0), getComponentColor(jComponent, "background", this.color7, -0.46404046f, 0.36470586f, 0), getComponentColor(jComponent, "background", this.color8, -0.47761154f, 0.44313723f, 0)};
                break;
            case 2:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color4, -0.6197143f, 0.43137252f, 0), getComponentColor(jComponent, "background", this.color5, -0.5766426f, 0.38039213f, 0), getComponentColor(jComponent, "background", this.color6, -0.43866998f, 0.24705881f, 0), getComponentColor(jComponent, "background", this.color7, -0.46404046f, 0.36470586f, 0), getComponentColor(jComponent, "background", this.color8, -0.47761154f, 0.44313723f, 0)};
                break;
            case 3:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color12, -0.6198413f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color13, -0.5822163f, 0.40392154f, 0), getComponentColor(jComponent, "background", this.color14, -0.4555341f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color15, -0.47698414f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color16, -0.5455182f, 0.45098037f, 0)};
                break;
            case 4:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color12, -0.6198413f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color13, -0.5822163f, 0.40392154f, 0), getComponentColor(jComponent, "background", this.color14, -0.4555341f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color15, -0.47698414f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color16, -0.5455182f, 0.45098037f, 0)};
                break;
            case 5:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color20, -0.38050595f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color21, -0.29863563f, 0.1490196f, 0), getComponentColor(jComponent, "background", this.color22, 0.0f, 0.0f, 0), getComponentColor(jComponent, "background", this.color23, -0.14126986f, 0.15686274f, 0), getComponentColor(jComponent, "background", this.color24, -0.20852983f, 0.2588235f, 0)};
                break;
            case 6:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color20, -0.38050595f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color21, -0.29863563f, 0.1490196f, 0), getComponentColor(jComponent, "background", this.color22, 0.0f, 0.0f, 0), getComponentColor(jComponent, "background", this.color23, -0.14126986f, 0.15686274f, 0), getComponentColor(jComponent, "background", this.color24, -0.20852983f, 0.2588235f, 0)};
                break;
            case 8:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color35, -0.10655806f, 0.24313724f, 0), getComponentColor(jComponent, "background", this.color36, -0.09823123f, 0.2117647f, 0), getComponentColor(jComponent, "background", this.color30, -0.07016757f, 0.12941176f, 0), getComponentColor(jComponent, "background", this.color37, -0.0749532f, 0.24705881f, 0), getComponentColor(jComponent, "background", this.color38, -0.110526316f, 0.25490195f, 0)};
                break;
            case 9:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color35, -0.10655806f, 0.24313724f, 0), getComponentColor(jComponent, "background", this.color36, -0.09823123f, 0.2117647f, 0), getComponentColor(jComponent, "background", this.color30, -0.07016757f, 0.12941176f, 0), getComponentColor(jComponent, "background", this.color37, -0.0749532f, 0.24705881f, 0), getComponentColor(jComponent, "background", this.color38, -0.110526316f, 0.25490195f, 0)};
                break;
            case 10:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color41, -0.10658931f, 0.25098038f, 0), getComponentColor(jComponent, "background", this.color42, -0.098526314f, 0.2352941f, 0), getComponentColor(jComponent, "background", this.color43, -0.07333623f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color38, -0.110526316f, 0.25490195f, 0)};
                break;
            case 11:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color41, -0.10658931f, 0.25098038f, 0), getComponentColor(jComponent, "background", this.color42, -0.098526314f, 0.2352941f, 0), getComponentColor(jComponent, "background", this.color43, -0.07333623f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color38, -0.110526316f, 0.25490195f, 0)};
                break;
            case 12:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color47, -0.05960039f, 0.10196078f, 0), getComponentColor(jComponent, "background", this.color48, -0.04772438f, 0.06666666f, 0), getComponentColor(jComponent, "background", this.color49, -0.0018306673f, -0.02352941f, 0), getComponentColor(jComponent, "background", this.color50, -0.0212406f, 0.13333333f, 0), getComponentColor(jComponent, "background", this.color51, -0.030845039f, 0.23921567f, 0)};
                break;
            case 13:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color47, -0.05960039f, 0.10196078f, 0), getComponentColor(jComponent, "background", this.color48, -0.04772438f, 0.06666666f, 0), getComponentColor(jComponent, "background", this.color49, -0.0018306673f, -0.02352941f, 0), getComponentColor(jComponent, "background", this.color50, -0.0212406f, 0.13333333f, 0), getComponentColor(jComponent, "background", this.color51, -0.030845039f, 0.23921567f, 0)};
                break;
        }
        return objArr;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDefault(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundDefaultAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundMouseOverAndDefault(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect5();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundMouseOverAndDefaultAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundPressedAndDefault(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundPressedAndDefaultAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color25);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient10(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundMouseOverAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient10(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color44);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient11(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundPressedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient11(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(0.2857143f), decodeY(0.42857143f), decodeX(2.7142859f) - decodeX(0.2857143f), decodeY(2.857143f) - decodeY(0.42857143f), 12.0d, 12.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(0.2857143f), decodeY(0.2857143f), decodeX(2.7142859f) - decodeX(0.2857143f), decodeY(2.7142859f) - decodeY(0.2857143f), 9.0d, 9.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(0.42857143f), decodeY(0.42857143f), decodeX(2.5714285f) - decodeX(0.42857143f), decodeY(2.5714285f) - decodeY(0.42857143f), 7.0d, 7.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect4() {
        this.roundRect.setRoundRect(decodeX(0.08571429f), decodeY(0.08571429f), decodeX(2.914286f) - decodeX(0.08571429f), decodeY(2.914286f) - decodeY(0.08571429f), 11.0d, 11.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect5() {
        this.roundRect.setRoundRect(decodeX(0.2857143f), decodeY(0.42857143f), decodeX(2.7142859f) - decodeX(0.2857143f), decodeY(2.857143f) - decodeY(0.42857143f), 9.0d, 9.0d);
        return this.roundRect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.05f, 0.5f, 0.95f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.024f, 0.06f, 0.276f, 0.6f, 0.65f, 0.7f, 0.856f, 0.96f, 0.98399997f, 1.0f}, new Color[]{(Color) this.componentColors[0], decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f), (Color) this.componentColors[1], decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f), (Color) this.componentColors[3], decodeColor((Color) this.componentColors[3], (Color) this.componentColors[4], 0.5f), (Color) this.componentColors[4]});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.05f, 0.5f, 0.95f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.05f, 0.5f, 0.95f}, new Color[]{this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.09f, 0.52f, 0.95f}, new Color[]{this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.03f, 0.06f, 0.33f, 0.6f, 0.65f, 0.7f, 0.825f, 0.95f, 0.975f, 1.0f}, new Color[]{this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30, decodeColor(this.color30, this.color30, 0.5f), this.color30, decodeColor(this.color30, this.color31, 0.5f), this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.09f, 0.52f, 0.95f}, new Color[]{this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.03f, 0.06f, 0.33f, 0.6f, 0.65f, 0.7f, 0.825f, 0.95f, 0.975f, 1.0f}, new Color[]{(Color) this.componentColors[0], decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f), (Color) this.componentColors[1], decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f), (Color) this.componentColors[3], decodeColor((Color) this.componentColors[3], (Color) this.componentColors[4], 0.5f), (Color) this.componentColors[4]});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.09f, 0.52f, 0.95f}, new Color[]{this.color39, decodeColor(this.color39, this.color40, 0.5f), this.color40});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.024f, 0.06f, 0.276f, 0.6f, 0.65f, 0.7f, 0.856f, 0.96f, 0.98f, 1.0f}, new Color[]{(Color) this.componentColors[0], decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f), (Color) this.componentColors[1], decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f), (Color) this.componentColors[3], decodeColor((Color) this.componentColors[3], (Color) this.componentColors[3], 0.5f), (Color) this.componentColors[3]});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.05f, 0.5f, 0.95f}, new Color[]{this.color45, decodeColor(this.color45, this.color46, 0.5f), this.color46});
    }
}
