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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ComboBoxPainter.class */
final class ComboBoxPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_DISABLED_PRESSED = 2;
    static final int BACKGROUND_ENABLED = 3;
    static final int BACKGROUND_FOCUSED = 4;
    static final int BACKGROUND_MOUSEOVER_FOCUSED = 5;
    static final int BACKGROUND_MOUSEOVER = 6;
    static final int BACKGROUND_PRESSED_FOCUSED = 7;
    static final int BACKGROUND_PRESSED = 8;
    static final int BACKGROUND_ENABLED_SELECTED = 9;
    static final int BACKGROUND_DISABLED_EDITABLE = 10;
    static final int BACKGROUND_ENABLED_EDITABLE = 11;
    static final int BACKGROUND_FOCUSED_EDITABLE = 12;
    static final int BACKGROUND_MOUSEOVER_EDITABLE = 13;
    static final int BACKGROUND_PRESSED_EDITABLE = 14;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -247);
    private Color color2 = decodeColor("nimbusBase", 0.032459438f, -0.5928571f, 0.2745098f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.032459438f, -0.590029f, 0.2235294f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.032459438f, -0.60996324f, 0.36470586f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.040395975f, -0.60474086f, 0.33725488f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.032459438f, -0.5953556f, 0.32549018f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.032459438f, -0.5957143f, 0.3333333f, 0);
    private Color color8 = decodeColor("nimbusBase", 0.021348298f, -0.56289876f, 0.2588235f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.010237217f, -0.55799407f, 0.20784312f, 0);
    private Color color10 = decodeColor("nimbusBase", 0.021348298f, -0.59223604f, 0.35294116f, 0);
    private Color color11 = decodeColor("nimbusBase", 0.02391243f, -0.5774183f, 0.32549018f, 0);
    private Color color12 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.021348298f, -0.567841f, 0.31764704f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", 0.0f, 0.0f, -0.22f, -176);
    private Color color15 = decodeColor("nimbusBase", 0.032459438f, -0.5787523f, 0.07058823f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.032459438f, -0.5399696f, -0.18039218f, 0);
    private Color color17 = decodeColor("nimbusBase", 0.08801502f, -0.63174605f, 0.43921566f, 0);
    private Color color18 = decodeColor("nimbusBase", 0.040395975f, -0.6054113f, 0.35686272f, 0);
    private Color color19 = decodeColor("nimbusBase", 0.032459438f, -0.5998577f, 0.4352941f, 0);
    private Color color20 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    private Color color21 = decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    private Color color22 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    private Color color23 = decodeColor("nimbusBase", -0.0028941035f, -0.4800539f, 0.28235292f, 0);
    private Color color24 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    private Color color25 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);
    private Color color26 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color27 = decodeColor("nimbusBase", 0.032459438f, -0.54616207f, -0.02352941f, 0);
    private Color color28 = decodeColor("nimbusBase", 0.032459438f, -0.41349208f, -0.33725494f, 0);
    private Color color29 = decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);
    private Color color30 = decodeColor("nimbusBase", 0.032459438f, -0.6113241f, 0.41568625f, 0);
    private Color color31 = decodeColor("nimbusBase", 0.032459438f, -0.5985242f, 0.39999998f, 0);
    private Color color32 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Color color33 = decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    private Color color34 = decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    private Color color35 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color36 = decodeColor("nimbusBase", -8.738637E-4f, -0.50527954f, 0.35294116f, 0);
    private Color color37 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    private Color color38 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4757143f, 0.43137252f, 0);
    private Color color39 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.52156866f, 0);
    private Color color40 = decodeColor("nimbusBase", 0.032459438f, -0.5246032f, -0.12549022f, 0);
    private Color color41 = decodeColor("nimbusBase", 0.027408898f, -0.5847884f, 0.2980392f, 0);
    private Color color42 = decodeColor("nimbusBase", 0.026611507f, -0.53623784f, 0.19999999f, 0);
    private Color color43 = decodeColor("nimbusBase", 0.029681683f, -0.52701867f, 0.17254901f, 0);
    private Color color44 = decodeColor("nimbusBase", 0.03801495f, -0.5456242f, 0.3215686f, 0);
    private Color color45 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color46 = decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    private Color color47 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    private Color color48 = decodeColor("nimbusBase", 4.081726E-4f, -0.12922078f, 0.054901958f, 0);
    private Color color49 = decodeColor("nimbusBase", 0.0f, -0.00895375f, 0.007843137f, 0);
    private Color color50 = decodeColor("nimbusBase", -0.0015907288f, -0.1436508f, 0.19215685f, 0);
    private Color color51 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -83);
    private Color color52 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -88);
    private Color color53 = decodeColor("nimbusBlueGrey", 0.0f, -0.005263157f, -0.52156866f, -191);
    private Object[] componentColors;

    public ComboBoxPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundDisabledAndPressed(graphics2D);
                break;
            case 3:
                paintBackgroundEnabled(graphics2D);
                break;
            case 4:
                paintBackgroundFocused(graphics2D);
                break;
            case 5:
                paintBackgroundMouseOverAndFocused(graphics2D);
                break;
            case 6:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 7:
                paintBackgroundPressedAndFocused(graphics2D);
                break;
            case 8:
                paintBackgroundPressed(graphics2D);
                break;
            case 9:
                paintBackgroundEnabledAndSelected(graphics2D);
                break;
            case 10:
                paintBackgroundDisabledAndEditable(graphics2D);
                break;
            case 11:
                paintBackgroundEnabledAndEditable(graphics2D);
                break;
            case 12:
                paintBackgroundFocusedAndEditable(graphics2D);
                break;
            case 13:
                paintBackgroundMouseOverAndEditable(graphics2D);
                break;
            case 14:
                paintBackgroundPressedAndEditable(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected Object[] getExtendedCacheKeys(JComponent jComponent) {
        Object[] objArr = null;
        switch (this.state) {
            case 3:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color17, -0.63174605f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color18, -0.6054113f, 0.35686272f, 0), getComponentColor(jComponent, "background", this.color6, -0.5953556f, 0.32549018f, 0), getComponentColor(jComponent, "background", this.color19, -0.5998577f, 0.4352941f, 0), getComponentColor(jComponent, "background", this.color22, -0.6197143f, 0.43137252f, 0), getComponentColor(jComponent, "background", this.color23, -0.4800539f, 0.28235292f, 0), getComponentColor(jComponent, "background", this.color24, -0.43866998f, 0.24705881f, 0), getComponentColor(jComponent, "background", this.color25, -0.4625541f, 0.35686272f, 0)};
                break;
            case 4:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color17, -0.63174605f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color18, -0.6054113f, 0.35686272f, 0), getComponentColor(jComponent, "background", this.color6, -0.5953556f, 0.32549018f, 0), getComponentColor(jComponent, "background", this.color19, -0.5998577f, 0.4352941f, 0), getComponentColor(jComponent, "background", this.color22, -0.6197143f, 0.43137252f, 0), getComponentColor(jComponent, "background", this.color23, -0.4800539f, 0.28235292f, 0), getComponentColor(jComponent, "background", this.color24, -0.43866998f, 0.24705881f, 0), getComponentColor(jComponent, "background", this.color25, -0.4625541f, 0.35686272f, 0)};
                break;
            case 5:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color29, -0.6317773f, 0.4470588f, 0), getComponentColor(jComponent, "background", this.color30, -0.6113241f, 0.41568625f, 0), getComponentColor(jComponent, "background", this.color31, -0.5985242f, 0.39999998f, 0), getComponentColor(jComponent, "background", this.color32, -0.6357143f, 0.45098037f, 0), getComponentColor(jComponent, "background", this.color35, -0.6198413f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color36, -0.50527954f, 0.35294116f, 0), getComponentColor(jComponent, "background", this.color37, -0.4555341f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color25, -0.4625541f, 0.35686272f, 0), getComponentColor(jComponent, "background", this.color38, -0.4757143f, 0.43137252f, 0)};
                break;
            case 6:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color29, -0.6317773f, 0.4470588f, 0), getComponentColor(jComponent, "background", this.color30, -0.6113241f, 0.41568625f, 0), getComponentColor(jComponent, "background", this.color31, -0.5985242f, 0.39999998f, 0), getComponentColor(jComponent, "background", this.color32, -0.6357143f, 0.45098037f, 0), getComponentColor(jComponent, "background", this.color35, -0.6198413f, 0.43921566f, 0), getComponentColor(jComponent, "background", this.color36, -0.50527954f, 0.35294116f, 0), getComponentColor(jComponent, "background", this.color37, -0.4555341f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color25, -0.4625541f, 0.35686272f, 0), getComponentColor(jComponent, "background", this.color38, -0.4757143f, 0.43137252f, 0)};
                break;
            case 7:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color41, -0.5847884f, 0.2980392f, 0), getComponentColor(jComponent, "background", this.color42, -0.53623784f, 0.19999999f, 0), getComponentColor(jComponent, "background", this.color43, -0.52701867f, 0.17254901f, 0), getComponentColor(jComponent, "background", this.color44, -0.5456242f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color47, -0.38050595f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color48, -0.12922078f, 0.054901958f, 0), getComponentColor(jComponent, "background", this.color49, -0.00895375f, 0.007843137f, 0), getComponentColor(jComponent, "background", this.color50, -0.1436508f, 0.19215685f, 0)};
                break;
            case 8:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color41, -0.5847884f, 0.2980392f, 0), getComponentColor(jComponent, "background", this.color42, -0.53623784f, 0.19999999f, 0), getComponentColor(jComponent, "background", this.color43, -0.52701867f, 0.17254901f, 0), getComponentColor(jComponent, "background", this.color44, -0.5456242f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color47, -0.38050595f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color48, -0.12922078f, 0.054901958f, 0), getComponentColor(jComponent, "background", this.color49, -0.00895375f, 0.007843137f, 0), getComponentColor(jComponent, "background", this.color50, -0.1436508f, 0.19215685f, 0)};
                break;
            case 9:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color41, -0.5847884f, 0.2980392f, 0), getComponentColor(jComponent, "background", this.color42, -0.53623784f, 0.19999999f, 0), getComponentColor(jComponent, "background", this.color43, -0.52701867f, 0.17254901f, 0), getComponentColor(jComponent, "background", this.color44, -0.5456242f, 0.3215686f, 0), getComponentColor(jComponent, "background", this.color47, -0.38050595f, 0.20392156f, 0), getComponentColor(jComponent, "background", this.color48, -0.12922078f, 0.054901958f, 0), getComponentColor(jComponent, "background", this.color49, -0.00895375f, 0.007843137f, 0), getComponentColor(jComponent, "background", this.color50, -0.1436508f, 0.19215685f, 0)};
                break;
        }
        return objArr;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundDisabledAndPressed(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color14);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.roundRect);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOverAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.roundRect);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color14);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressedAndFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.roundRect);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color51);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabledAndSelected(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color52);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundDisabledAndEditable(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color53);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndEditable(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color53);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundFocusedAndEditable(Graphics2D graphics2D) {
        this.path = decodePath6();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOverAndEditable(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color53);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundPressedAndEditable(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color53);
        graphics2D.fill(this.rect);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.22222222f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.22222222f), decodeY(2.25f));
        this.path.curveTo(decodeAnchorX(0.22222222f, 0.0f), decodeAnchorY(2.25f, 3.0f), decodeAnchorX(0.7777778f, -3.0f), decodeAnchorY(2.875f, 0.0f), decodeX(0.7777778f), decodeY(2.875f));
        this.path.lineTo(decodeX(2.631579f), decodeY(2.875f));
        this.path.curveTo(decodeAnchorX(2.631579f, 3.0f), decodeAnchorY(2.875f, 0.0f), decodeAnchorX(2.8947368f, 0.0f), decodeAnchorY(2.25f, 3.0f), decodeX(2.8947368f), decodeY(2.25f));
        this.path.lineTo(decodeX(2.8947368f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.22222222f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.22222222f), decodeY(0.875f));
        this.path.lineTo(decodeX(0.22222222f), decodeY(2.125f));
        this.path.curveTo(decodeAnchorX(0.22222222f, 0.0f), decodeAnchorY(2.125f, 3.0f), decodeAnchorX(0.7777778f, -3.0f), decodeAnchorY(2.75f, 0.0f), decodeX(0.7777778f), decodeY(2.75f));
        this.path.lineTo(decodeX(2.0f), decodeY(2.75f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.25f));
        this.path.lineTo(decodeX(0.7777778f), decodeY(0.25f));
        this.path.curveTo(decodeAnchorX(0.7777778f, -3.0f), decodeAnchorY(0.25f, 0.0f), decodeAnchorX(0.22222222f, 0.0f), decodeAnchorY(0.875f, -3.0f), decodeX(0.22222222f), decodeY(0.875f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.8888889f), decodeY(0.375f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.375f));
        this.path.lineTo(decodeX(2.0f), decodeY(2.625f));
        this.path.lineTo(decodeX(0.8888889f), decodeY(2.625f));
        this.path.curveTo(decodeAnchorX(0.8888889f, -4.0f), decodeAnchorY(2.625f, 0.0f), decodeAnchorX(0.33333334f, 0.0f), decodeAnchorY(2.0f, 4.0f), decodeX(0.33333334f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.33333334f), decodeY(0.875f));
        this.path.curveTo(decodeAnchorX(0.33333334f, 0.0f), decodeAnchorY(0.875f, -3.0f), decodeAnchorX(0.8888889f, -4.0f), decodeAnchorY(0.375f, 0.0f), decodeX(0.8888889f), decodeY(0.375f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(2.0f), decodeY(0.25f));
        this.path.lineTo(decodeX(2.631579f), decodeY(0.25f));
        this.path.curveTo(decodeAnchorX(2.631579f, 3.0f), decodeAnchorY(0.25f, 0.0f), decodeAnchorX(2.8947368f, 0.0f), decodeAnchorY(0.875f, -3.0f), decodeX(2.8947368f), decodeY(0.875f));
        this.path.lineTo(decodeX(2.8947368f), decodeY(2.125f));
        this.path.curveTo(decodeAnchorX(2.8947368f, 0.0f), decodeAnchorY(2.125f, 3.0f), decodeAnchorX(2.631579f, 3.0f), decodeAnchorY(2.75f, 0.0f), decodeX(2.631579f), decodeY(2.75f));
        this.path.lineTo(decodeX(2.0f), decodeY(2.75f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.25f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(2.0131578f), decodeY(0.375f));
        this.path.lineTo(decodeX(2.5789473f), decodeY(0.375f));
        this.path.curveTo(decodeAnchorX(2.5789473f, 4.0f), decodeAnchorY(0.375f, 0.0f), decodeAnchorX(2.8421054f, 0.0f), decodeAnchorY(1.0f, -4.0f), decodeX(2.8421054f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8421054f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(2.8421054f, 0.0f), decodeAnchorY(2.0f, 4.0f), decodeAnchorX(2.5789473f, 4.0f), decodeAnchorY(2.625f, 0.0f), decodeX(2.5789473f), decodeY(2.625f));
        this.path.lineTo(decodeX(2.0131578f), decodeY(2.625f));
        this.path.lineTo(decodeX(2.0131578f), decodeY(0.375f));
        this.path.closePath();
        return this.path;
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(0.06666667f), decodeY(0.075f), decodeX(2.9684212f) - decodeX(0.06666667f), decodeY(2.925f) - decodeY(0.075f), 13.0d, 13.0d);
        return this.roundRect;
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.4385965f), decodeY(1.4444444f), decodeX(1.4385965f) - decodeX(1.4385965f), decodeY(1.4444444f) - decodeY(1.4444444f));
        return this.rect;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(0.120000005f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(1.9954545f), decodeY(0.120000005f));
        this.path.curveTo(decodeAnchorX(1.9954545f, 3.0f), decodeAnchorY(0.120000005f, 0.0f), decodeAnchorX(2.8799987f, 0.0f), decodeAnchorY(1.0941176f, -3.0f), decodeX(2.8799987f), decodeY(1.0941176f));
        this.path.lineTo(decodeX(2.8799987f), decodeY(1.964706f));
        this.path.curveTo(decodeAnchorX(2.8799987f, 0.0f), decodeAnchorY(1.964706f, 3.0f), decodeAnchorX(1.9954545f, 3.0f), decodeAnchorY(2.8799999f, 0.0f), decodeX(1.9954545f), decodeY(2.8799999f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(2.8799999f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(0.120000005f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(1.4385965f), decodeY(1.5f), decodeX(1.4385965f) - decodeX(1.4385965f), decodeY(1.5f) - decodeY(1.5f));
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
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.2002841f, 0.4005682f, 0.5326705f, 0.66477275f, 0.8323864f, 1.0f}, new Color[]{this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.171875f, 0.34375f, 0.4815341f, 0.6193182f, 0.8096591f, 1.0f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.2002841f, 0.4005682f, 0.5326705f, 0.66477275f, 0.8323864f, 1.0f}, new Color[]{(Color) this.componentColors[0], decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f), (Color) this.componentColors[1], decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f), (Color) this.componentColors[2], decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f), (Color) this.componentColors[3]});
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
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.171875f, 0.34375f, 0.4815341f, 0.6193182f, 0.8096591f, 1.0f}, new Color[]{(Color) this.componentColors[4], decodeColor((Color) this.componentColors[4], (Color) this.componentColors[5], 0.5f), (Color) this.componentColors[5], decodeColor((Color) this.componentColors[5], (Color) this.componentColors[6], 0.5f), (Color) this.componentColors[6], decodeColor((Color) this.componentColors[6], (Color) this.componentColors[7], 0.5f), (Color) this.componentColors[7]});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color27, decodeColor(this.color27, this.color28, 0.5f), this.color28});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color39, decodeColor(this.color39, this.color40, 0.5f), this.color40});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color45, decodeColor(this.color45, this.color46, 0.5f), this.color46});
    }
}
