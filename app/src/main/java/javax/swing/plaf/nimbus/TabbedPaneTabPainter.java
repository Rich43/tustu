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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TabbedPaneTabPainter.class */
final class TabbedPaneTabPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BACKGROUND_ENABLED_MOUSEOVER = 2;
    static final int BACKGROUND_ENABLED_PRESSED = 3;
    static final int BACKGROUND_DISABLED = 4;
    static final int BACKGROUND_SELECTED_DISABLED = 5;
    static final int BACKGROUND_SELECTED = 6;
    static final int BACKGROUND_SELECTED_MOUSEOVER = 7;
    static final int BACKGROUND_SELECTED_PRESSED = 8;
    static final int BACKGROUND_SELECTED_FOCUSED = 9;
    static final int BACKGROUND_SELECTED_MOUSEOVER_FOCUSED = 10;
    static final int BACKGROUND_SELECTED_PRESSED_FOCUSED = 11;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBase", 0.032459438f, -0.55535716f, -0.109803945f, 0);
    private Color color2 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.4784314f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.08801502f, -0.63174605f, 0.43921566f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.05468172f, -0.6145278f, 0.37647057f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.032459438f, -0.5953556f, 0.32549018f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.032459438f, -0.54616207f, -0.02352941f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);
    private Color color8 = decodeColor("nimbusBase", 0.021348298f, -0.61547136f, 0.41960782f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.032459438f, -0.5985242f, 0.39999998f, 0);
    private Color color10 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.52156866f, 0);
    private Color color11 = decodeColor("nimbusBase", 0.027408898f, -0.5847884f, 0.2980392f, 0);
    private Color color12 = decodeColor("nimbusBase", 0.035931647f, -0.5553123f, 0.23137254f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.029681683f, -0.5281874f, 0.18039215f, 0);
    private Color color14 = decodeColor("nimbusBase", 0.03801495f, -0.5456242f, 0.3215686f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.032459438f, -0.59181184f, 0.25490195f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.05468172f, -0.58308274f, 0.19607842f, 0);
    private Color color17 = decodeColor("nimbusBase", 0.046348333f, -0.6006266f, 0.34509802f, 0);
    private Color color18 = decodeColor("nimbusBase", 0.046348333f, -0.60015875f, 0.3333333f, 0);
    private Color color19 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    private Color color20 = decodeColor("nimbusBase", 7.13408E-4f, -0.543609f, 0.34509802f, 0);
    private Color color21 = decodeColor("nimbusBase", -0.0020751357f, -0.45610264f, 0.2588235f, 0);
    private Color color22 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    private Color color23 = decodeColor("nimbusBase", 5.1498413E-4f, -0.44879842f, 0.29019606f, 0);
    private Color color24 = decodeColor("nimbusBase", 5.1498413E-4f, -0.08776909f, -0.2627451f, 0);
    private Color color25 = decodeColor("nimbusBase", 0.06332368f, 0.3642857f, -0.4431373f, 0);
    private Color color26 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color27 = decodeColor("nimbusBase", -0.0022627711f, -0.5335866f, 0.372549f, 0);
    private Color color28 = decodeColor("nimbusBase", -0.0017285943f, -0.4608264f, 0.32549018f, 0);
    private Color color29 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    private Color color30 = decodeColor("nimbusBase", 5.1498413E-4f, -0.46404046f, 0.36470586f, 0);
    private Color color31 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color32 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    private Color color33 = decodeColor("nimbusBase", 0.0013483167f, -0.16401619f, 0.0745098f, 0);
    private Color color34 = decodeColor("nimbusBase", -0.0010001659f, -0.01599598f, 0.007843137f, 0);
    private Color color35 = decodeColor("nimbusBase", 0.0f, 0.0f, 0.0f, 0);
    private Color color36 = decodeColor("nimbusBase", 0.0018727183f, -0.038398862f, 0.035294116f, 0);
    private Color color37 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Object[] componentColors;

    public TabbedPaneTabPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundEnabledAndMouseOver(graphics2D);
                break;
            case 3:
                paintBackgroundEnabledAndPressed(graphics2D);
                break;
            case 4:
                paintBackgroundDisabled(graphics2D);
                break;
            case 5:
                paintBackgroundSelectedAndDisabled(graphics2D);
                break;
            case 6:
                paintBackgroundSelected(graphics2D);
                break;
            case 7:
                paintBackgroundSelectedAndMouseOver(graphics2D);
                break;
            case 8:
                paintBackgroundSelectedAndPressed(graphics2D);
                break;
            case 9:
                paintBackgroundSelectedAndFocused(graphics2D);
                break;
            case 10:
                paintBackgroundSelectedAndMouseOverAndFocused(graphics2D);
                break;
            case 11:
                paintBackgroundSelectedAndPressedAndFocused(graphics2D);
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
    }

    private void paintBackgroundEnabledAndMouseOver(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabledAndPressed(Graphics2D graphics2D) {
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath6();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndDisabled(Graphics2D graphics2D) {
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelected(Graphics2D graphics2D) {
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndMouseOver(Graphics2D graphics2D) {
        this.path = decodePath8();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath9();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndPressed(Graphics2D graphics2D) {
        this.path = decodePath8();
        graphics2D.setPaint(decodeGradient13(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath9();
        graphics2D.setPaint(decodeGradient14(this.path));
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndFocused(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath10();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath11();
        graphics2D.setPaint(this.color37);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndMouseOverAndFocused(Graphics2D graphics2D) {
        this.path = decodePath12();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath13();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath14();
        graphics2D.setPaint(this.color37);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundSelectedAndPressedAndFocused(Graphics2D graphics2D) {
        this.path = decodePath12();
        graphics2D.setPaint(decodeGradient13(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath13();
        graphics2D.setPaint(decodeGradient14(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath14();
        graphics2D.setPaint(this.color37);
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeAnchorX(0.71428573f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(0.71428573f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(0.71428573f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(2.2857144f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(2.2857144f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(2.2857144f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeX(3.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeX(0.0f), decodeY(0.71428573f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.14285715f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(0.85714287f, 3.5555556f), decodeX(0.14285715f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(0.85714287f, -3.5555556f), decodeAnchorX(0.85714287f, -3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeX(0.85714287f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(0.85714287f, 3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.142857f, -3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeX(2.142857f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(2.142857f, 3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(0.85714287f, -3.2777777f), decodeX(2.857143f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(0.85714287f, 3.2777777f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(2.857143f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.14285715f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.055555556f), decodeAnchorY(0.71428573f, 2.6111112f), decodeAnchorX(0.8333333f, -2.5f), decodeAnchorY(0.0f, 0.0f), decodeX(0.8333333f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(0.8333333f, 2.5f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(2.2857144f, -2.7222223f), decodeAnchorY(0.0f, 0.0f), decodeX(2.2857144f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(2.2857144f, 2.7222223f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(3.0f, -0.055555556f), decodeAnchorY(0.71428573f, -2.7222223f), decodeX(3.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.055555556f), decodeAnchorY(0.71428573f, 2.7222223f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeAnchorX(0.0f, -0.055555556f), decodeAnchorY(0.71428573f, -2.6111112f), decodeX(0.0f), decodeY(0.71428573f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(0.16666667f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.16666667f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(0.16666667f, 0.0f), decodeAnchorY(0.85714287f, 3.6666667f), decodeX(0.16666667f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(0.16666667f, 0.0f), decodeAnchorY(0.85714287f, -3.6666667f), decodeAnchorX(1.0f, -3.5555556f), decodeAnchorY(0.14285715f, 0.0f), decodeX(1.0f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(1.0f, 3.5555556f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.142857f, -3.5f), decodeAnchorY(0.14285715f, 0.055555556f), decodeX(2.142857f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(2.142857f, 3.5f), decodeAnchorY(0.14285715f, -0.055555556f), decodeAnchorX(2.857143f, 0.055555556f), decodeAnchorY(0.85714287f, -3.6666667f), decodeX(2.857143f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(2.857143f, -0.055555556f), decodeAnchorY(0.85714287f, 3.6666667f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(2.857143f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.16666667f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.8333333f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.8333333f, -3.0f), decodeAnchorX(0.71428573f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(0.71428573f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(0.71428573f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(2.2857144f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(2.2857144f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(2.2857144f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.8333333f, -3.0f), decodeX(3.0f), decodeY(0.8333333f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.8333333f, 3.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.8333333f, 3.0f), decodeX(0.0f), decodeY(0.8333333f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(0.14285715f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(1.0f, 3.5555556f), decodeX(0.14285715f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(1.0f, -3.5555556f), decodeAnchorX(0.85714287f, -3.4444444f), decodeAnchorY(0.16666667f, 0.0f), decodeX(0.85714287f), decodeY(0.16666667f));
        this.path.curveTo(decodeAnchorX(0.85714287f, 3.4444444f), decodeAnchorY(0.16666667f, 0.0f), decodeAnchorX(2.142857f, -3.3333333f), decodeAnchorY(0.16666667f, 0.0f), decodeX(2.142857f), decodeY(0.16666667f));
        this.path.curveTo(decodeAnchorX(2.142857f, 3.3333333f), decodeAnchorY(0.16666667f, 0.0f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(1.0f, -3.2777777f), decodeX(2.857143f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(1.0f, 3.2777777f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(2.857143f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.14285715f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath7() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeAnchorX(0.71428573f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(0.71428573f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(0.71428573f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(2.2857144f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(2.2857144f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(2.2857144f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeX(3.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(3.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeX(0.0f), decodeY(0.71428573f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath8() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeAnchorX(0.5555556f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(0.5555556f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(0.5555556f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(2.4444444f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(2.4444444f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(2.4444444f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeX(3.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(3.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeX(0.0f), decodeY(0.71428573f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath9() {
        this.path.reset();
        this.path.moveTo(decodeX(0.11111111f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.11111111f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeAnchorX(0.11111111f, 0.0f), decodeAnchorY(0.85714287f, 3.5555556f), decodeX(0.11111111f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(0.11111111f, 0.0f), decodeAnchorY(0.85714287f, -3.5555556f), decodeAnchorX(0.6666667f, -3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeX(0.6666667f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(0.6666667f, 3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.3333333f, -3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeX(2.3333333f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(2.3333333f, 3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.8888888f, 0.0f), decodeAnchorY(0.85714287f, -3.2777777f), decodeX(2.8888888f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(2.8888888f, 0.0f), decodeAnchorY(0.85714287f, 3.2777777f), decodeAnchorX(2.8888888f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(2.8888888f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.11111111f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath10() {
        this.path.reset();
        this.path.moveTo(decodeX(0.14285715f), decodeY(3.0f));
        this.path.curveTo(decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(0.85714287f, 3.5555556f), decodeX(0.14285715f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(0.14285715f, 0.0f), decodeAnchorY(0.85714287f, -3.5555556f), decodeAnchorX(0.85714287f, -3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeX(0.85714287f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(0.85714287f, 3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.142857f, -3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeX(2.142857f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(2.142857f, 3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(0.85714287f, -3.2777777f), decodeX(2.857143f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(0.85714287f, 3.2777777f), decodeAnchorX(2.857143f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeX(2.857143f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.14285715f), decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath11() {
        this.path.reset();
        this.path.moveTo(decodeX(1.4638889f), decodeY(2.25f));
        this.path.lineTo(decodeX(1.4652778f), decodeY(2.777778f));
        this.path.lineTo(decodeX(0.3809524f), decodeY(2.777778f));
        this.path.lineTo(decodeX(0.375f), decodeY(0.88095236f));
        this.path.curveTo(decodeAnchorX(0.375f, 0.0f), decodeAnchorY(0.88095236f, -2.25f), decodeAnchorX(0.8452381f, -1.9166666f), decodeAnchorY(0.3809524f, 0.0f), decodeX(0.8452381f), decodeY(0.3809524f));
        this.path.lineTo(decodeX(2.1011903f), decodeY(0.3809524f));
        this.path.curveTo(decodeAnchorX(2.1011903f, 2.125f), decodeAnchorY(0.3809524f, 0.0f), decodeAnchorX(2.6309526f, 0.0f), decodeAnchorY(0.8630952f, -2.5833333f), decodeX(2.6309526f), decodeY(0.8630952f));
        this.path.lineTo(decodeX(2.625f), decodeY(2.7638886f));
        this.path.lineTo(decodeX(1.4666667f), decodeY(2.777778f));
        this.path.lineTo(decodeX(1.4638889f), decodeY(2.2361114f));
        this.path.lineTo(decodeX(2.3869045f), decodeY(2.222222f));
        this.path.lineTo(decodeX(2.375f), decodeY(0.86904764f));
        this.path.curveTo(decodeAnchorX(2.375f, -7.1054274E-15f), decodeAnchorY(0.86904764f, -0.9166667f), decodeAnchorX(2.0952382f, 1.0833334f), decodeAnchorY(0.60714287f, -1.7763568E-15f), decodeX(2.0952382f), decodeY(0.60714287f));
        this.path.lineTo(decodeX(0.8333334f), decodeY(0.6130952f));
        this.path.curveTo(decodeAnchorX(0.8333334f, -1.0f), decodeAnchorY(0.6130952f, 0.0f), decodeAnchorX(0.625f, 0.041666668f), decodeAnchorY(0.86904764f, -0.9583333f), decodeX(0.625f), decodeY(0.86904764f));
        this.path.lineTo(decodeX(0.6130952f), decodeY(2.2361114f));
        this.path.lineTo(decodeX(1.4638889f), decodeY(2.25f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath12() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeAnchorX(0.5555556f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(0.5555556f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(0.5555556f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(2.4444444f, -3.0f), decodeAnchorY(0.0f, 0.0f), decodeX(2.4444444f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(2.4444444f, 3.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, -3.0f), decodeX(3.0f), decodeY(0.71428573f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeAnchorX(0.0f, 0.0f), decodeAnchorY(0.71428573f, 3.0f), decodeX(0.0f), decodeY(0.71428573f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath13() {
        this.path.reset();
        this.path.moveTo(decodeX(0.11111111f), decodeY(3.0f));
        this.path.curveTo(decodeAnchorX(0.11111111f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeAnchorX(0.11111111f, 0.0f), decodeAnchorY(0.85714287f, 3.5555556f), decodeX(0.11111111f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(0.11111111f, 0.0f), decodeAnchorY(0.85714287f, -3.5555556f), decodeAnchorX(0.6666667f, -3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeX(0.6666667f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(0.6666667f, 3.4444444f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.3333333f, -3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeX(2.3333333f), decodeY(0.14285715f));
        this.path.curveTo(decodeAnchorX(2.3333333f, 3.3333333f), decodeAnchorY(0.14285715f, 0.0f), decodeAnchorX(2.8888888f, 0.0f), decodeAnchorY(0.85714287f, -3.2777777f), decodeX(2.8888888f), decodeY(0.85714287f));
        this.path.curveTo(decodeAnchorX(2.8888888f, 0.0f), decodeAnchorY(0.85714287f, 3.2777777f), decodeAnchorX(2.8888888f, 0.0f), decodeAnchorY(3.0f, 0.0f), decodeX(2.8888888f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.11111111f), decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath14() {
        this.path.reset();
        this.path.moveTo(decodeX(1.4583333f), decodeY(2.25f));
        this.path.lineTo(decodeX(1.4599359f), decodeY(2.777778f));
        this.path.lineTo(decodeX(0.2962963f), decodeY(2.777778f));
        this.path.lineTo(decodeX(0.29166666f), decodeY(0.88095236f));
        this.path.curveTo(decodeAnchorX(0.29166666f, 0.0f), decodeAnchorY(0.88095236f, -2.25f), decodeAnchorX(0.6574074f, -1.9166666f), decodeAnchorY(0.3809524f, 0.0f), decodeX(0.6574074f), decodeY(0.3809524f));
        this.path.lineTo(decodeX(2.3009257f), decodeY(0.3809524f));
        this.path.curveTo(decodeAnchorX(2.3009257f, 2.125f), decodeAnchorY(0.3809524f, 0.0f), decodeAnchorX(2.712963f, 0.0f), decodeAnchorY(0.8630952f, -2.5833333f), decodeX(2.712963f), decodeY(0.8630952f));
        this.path.lineTo(decodeX(2.7083333f), decodeY(2.7638886f));
        this.path.lineTo(decodeX(1.4615384f), decodeY(2.777778f));
        this.path.lineTo(decodeX(1.4583333f), decodeY(2.2361114f));
        this.path.lineTo(decodeX(2.523148f), decodeY(2.222222f));
        this.path.lineTo(decodeX(2.5138888f), decodeY(0.86904764f));
        this.path.curveTo(decodeAnchorX(2.5138888f, -7.1054274E-15f), decodeAnchorY(0.86904764f, -0.9166667f), decodeAnchorX(2.2962964f, 1.0833334f), decodeAnchorY(0.60714287f, -1.7763568E-15f), decodeX(2.2962964f), decodeY(0.60714287f));
        this.path.lineTo(decodeX(0.6481482f), decodeY(0.6130952f));
        this.path.curveTo(decodeAnchorX(0.6481482f, -1.0f), decodeAnchorY(0.6130952f, 0.0f), decodeAnchorX(0.4861111f, 0.041666668f), decodeAnchorY(0.86904764f, -0.9583333f), decodeX(0.4861111f), decodeY(0.86904764f));
        this.path.lineTo(decodeX(0.47685182f), decodeY(2.2361114f));
        this.path.lineTo(decodeX(1.4583333f), decodeY(2.25f));
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
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.1f, 0.2f, 0.6f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color6, decodeColor(this.color6, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.1f, 0.2f, 0.6f, 1.0f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color10, decodeColor(this.color10, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.1f, 0.2f, 0.42096776f, 0.64193547f, 0.82096773f, 1.0f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.1f, 0.2f, 0.6f, 1.0f}, new Color[]{this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18, decodeColor(this.color18, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12419355f, 0.2483871f, 0.42580646f, 0.6032258f, 0.6854839f, 0.7677419f, 0.88387096f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color25, decodeColor(this.color25, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12419355f, 0.2483871f, 0.42580646f, 0.6032258f, 0.6854839f, 0.7677419f, 0.86774194f, 0.9677419f}, new Color[]{this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27, decodeColor(this.color27, this.color28, 0.5f), this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30});
    }

    private Paint decodeGradient13(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color25, decodeColor(this.color25, this.color31, 0.5f), this.color31});
    }

    private Paint decodeGradient14(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12419355f, 0.2483871f, 0.42580646f, 0.6032258f, 0.6854839f, 0.7677419f, 0.8548387f, 0.9419355f}, new Color[]{this.color32, decodeColor(this.color32, this.color33, 0.5f), this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34, decodeColor(this.color34, this.color35, 0.5f), this.color35, decodeColor(this.color35, this.color36, 0.5f), this.color36});
    }
}
