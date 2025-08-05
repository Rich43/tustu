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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ProgressBarPainter.class */
final class ProgressBarPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BACKGROUND_DISABLED = 2;
    static final int FOREGROUND_ENABLED = 3;
    static final int FOREGROUND_ENABLED_FINISHED = 4;
    static final int FOREGROUND_ENABLED_INDETERMINATE = 5;
    static final int FOREGROUND_DISABLED = 6;
    static final int FOREGROUND_DISABLED_FINISHED = 7;
    static final int FOREGROUND_DISABLED_INDETERMINATE = 8;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.04845735f, -0.17647058f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.061345987f, -0.027450979f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.0f, -0.097921275f, 0.18823528f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.0138888955f, -0.0925083f, 0.12549019f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, -0.08222443f, 0.086274505f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", 0.0f, -0.08477524f, 0.16862744f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.086996906f, 0.25490195f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.0f, -0.061613273f, -0.02352941f, 0);
    private Color color10 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.061265234f, 0.05098039f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", 0.0138888955f, -0.09378991f, 0.19215685f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", 0.0f, -0.08455229f, 0.1607843f, 0);
    private Color color13 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.08362049f, 0.12941176f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
    private Color color15 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07982456f, 0.1490196f, 0);
    private Color color16 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.08099045f, 0.18431371f, 0);
    private Color color17 = decodeColor("nimbusOrange", 0.0f, 0.0f, 0.0f, -156);
    private Color color18 = decodeColor("nimbusOrange", -0.015796512f, 0.02094239f, -0.15294117f, 0);
    private Color color19 = decodeColor("nimbusOrange", -0.004321605f, 0.02094239f, -0.0745098f, 0);
    private Color color20 = decodeColor("nimbusOrange", -0.008021399f, 0.02094239f, -0.10196078f, 0);
    private Color color21 = decodeColor("nimbusOrange", -0.011706904f, -0.1790576f, -0.02352941f, 0);
    private Color color22 = decodeColor("nimbusOrange", -0.048691254f, 0.02094239f, -0.3019608f, 0);
    private Color color23 = decodeColor("nimbusOrange", 0.003940329f, -0.7375322f, 0.17647058f, 0);
    private Color color24 = decodeColor("nimbusOrange", 0.005506739f, -0.46764207f, 0.109803915f, 0);
    private Color color25 = decodeColor("nimbusOrange", 0.0042127445f, -0.18595415f, 0.04705882f, 0);
    private Color color26 = decodeColor("nimbusOrange", 0.0047626942f, 0.02094239f, 0.0039215684f, 0);
    private Color color27 = decodeColor("nimbusOrange", 0.0047626942f, -0.15147138f, 0.1607843f, 0);
    private Color color28 = decodeColor("nimbusOrange", 0.010665476f, -0.27317524f, 0.25098038f, 0);
    private Color color29 = decodeColor("nimbusBlueGrey", -0.54444444f, -0.08748484f, 0.10588235f, 0);
    private Color color30 = decodeColor("nimbusOrange", 0.0047626942f, -0.21715283f, 0.23921567f, 0);
    private Color color31 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -173);
    private Color color32 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -170);
    private Color color33 = decodeColor("nimbusOrange", 0.024554357f, -0.8873145f, 0.10588235f, -156);
    private Color color34 = decodeColor("nimbusOrange", -0.023593787f, -0.7963165f, 0.02352941f, 0);
    private Color color35 = decodeColor("nimbusOrange", -0.010608241f, -0.7760873f, 0.043137252f, 0);
    private Color color36 = decodeColor("nimbusOrange", -0.015402906f, -0.7840576f, 0.035294116f, 0);
    private Color color37 = decodeColor("nimbusOrange", -0.017112307f, -0.8091547f, 0.058823526f, 0);
    private Color color38 = decodeColor("nimbusOrange", -0.07044564f, -0.844649f, -0.019607842f, 0);
    private Color color39 = decodeColor("nimbusOrange", -0.009704903f, -0.9381485f, 0.11372548f, 0);
    private Color color40 = decodeColor("nimbusOrange", -4.4563413E-4f, -0.86742973f, 0.09411764f, 0);
    private Color color41 = decodeColor("nimbusOrange", -4.4563413E-4f, -0.79896283f, 0.07843137f, 0);
    private Color color42 = decodeColor("nimbusOrange", 0.0013274103f, -0.7530961f, 0.06666666f, 0);
    private Color color43 = decodeColor("nimbusOrange", 0.0013274103f, -0.7644457f, 0.109803915f, 0);
    private Color color44 = decodeColor("nimbusOrange", 0.009244293f, -0.78794646f, 0.13333333f, 0);
    private Color color45 = decodeColor("nimbusBlueGrey", -0.015872955f, -0.0803539f, 0.16470587f, 0);
    private Color color46 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07968931f, 0.14509803f, 0);
    private Color color47 = decodeColor("nimbusBlueGrey", 0.02222228f, -0.08779904f, 0.11764705f, 0);
    private Color color48 = decodeColor("nimbusBlueGrey", 0.0138888955f, -0.075128086f, 0.14117646f, 0);
    private Color color49 = decodeColor("nimbusBlueGrey", 0.0138888955f, -0.07604356f, 0.16470587f, 0);
    private Color color50 = decodeColor("nimbusOrange", 0.0014062226f, -0.77816474f, 0.12941176f, 0);
    private Object[] componentColors;

    public ProgressBarPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundDisabled(graphics2D);
                break;
            case 3:
                paintForegroundEnabled(graphics2D);
                break;
            case 4:
                paintForegroundEnabledAndFinished(graphics2D);
                break;
            case 5:
                paintForegroundEnabledAndIndeterminate(graphics2D);
                break;
            case 6:
                paintForegroundDisabled(graphics2D);
                break;
            case 7:
                paintForegroundDisabledAndFinished(graphics2D);
                break;
            case 8:
                paintForegroundDisabledAndIndeterminate(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(decodeGradient3(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintForegroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.path);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient5(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(decodeGradient6(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintForegroundEnabledAndFinished(Graphics2D graphics2D) {
        this.path = decodePath2();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.path);
        this.rect = decodeRect5();
        graphics2D.setPaint(decodeGradient5(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect6();
        graphics2D.setPaint(decodeGradient6(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintForegroundEnabledAndIndeterminate(Graphics2D graphics2D) {
        this.rect = decodeRect7();
        graphics2D.setPaint(decodeGradient7(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect8();
        graphics2D.setPaint(this.color31);
        graphics2D.fill(this.rect);
        this.rect = decodeRect9();
        graphics2D.setPaint(this.color32);
        graphics2D.fill(this.rect);
    }

    private void paintForegroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color33);
        graphics2D.fill(this.path);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient9(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(decodeGradient10(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintForegroundDisabledAndFinished(Graphics2D graphics2D) {
        this.path = decodePath4();
        graphics2D.setPaint(this.color33);
        graphics2D.fill(this.path);
        this.rect = decodeRect5();
        graphics2D.setPaint(decodeGradient9(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect6();
        graphics2D.setPaint(decodeGradient10(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintForegroundDisabledAndIndeterminate(Graphics2D graphics2D) {
        this.rect = decodeRect7();
        graphics2D.setPaint(decodeGradient11(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath5();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.6f), decodeY(0.6f), decodeX(2.4f) - decodeX(0.6f), decodeY(2.4f) - decodeY(0.6f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(0.21111111f));
        this.path.curveTo(decodeAnchorX(1.0f, -2.0f), decodeAnchorY(0.21111111f, 0.0f), decodeAnchorX(0.21111111f, 0.0f), decodeAnchorY(1.0f, -2.0f), decodeX(0.21111111f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(0.21111111f, 0.0f), decodeAnchorY(1.0f, 2.0f), decodeAnchorX(0.21111111f, 0.0f), decodeAnchorY(2.0f, -2.0f), decodeX(0.21111111f), decodeY(2.0f));
        this.path.curveTo(decodeAnchorX(0.21111111f, 0.0f), decodeAnchorY(2.0f, 2.0f), decodeAnchorX(1.0f, -2.0f), decodeAnchorY(2.8222225f, 0.0f), decodeX(1.0f), decodeY(2.8222225f));
        this.path.curveTo(decodeAnchorX(1.0f, 2.0f), decodeAnchorY(2.8222225f, 0.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(2.8222225f, 0.0f), decodeX(3.0f), decodeY(2.8222225f));
        this.path.lineTo(decodeX(3.0f), decodeY(2.3333333f));
        this.path.lineTo(decodeX(0.6666667f), decodeY(2.3333333f));
        this.path.lineTo(decodeX(0.6666667f), decodeY(0.6666667f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.6666667f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.2f));
        this.path.curveTo(decodeAnchorX(3.0f, 0.0f), decodeAnchorY(0.2f, 0.0f), decodeAnchorX(1.0f, 2.0f), decodeAnchorY(0.21111111f, 0.0f), decodeX(1.0f), decodeY(0.21111111f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(0.6666667f), decodeX(3.0f) - decodeX(0.6666667f), decodeY(2.3333333f) - decodeY(0.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.6666667f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.9111111f), decodeY(0.21111111f));
        this.path.curveTo(decodeAnchorX(0.9111111f, -2.0f), decodeAnchorY(0.21111111f, 0.0f), decodeAnchorX(0.2f, 0.0f), decodeAnchorY(1.0025641f, -2.0f), decodeX(0.2f), decodeY(1.0025641f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.0444443f));
        this.path.curveTo(decodeAnchorX(0.2f, 0.0f), decodeAnchorY(2.0444443f, 2.0f), decodeAnchorX(0.9666667f, -2.0f), decodeAnchorY(2.8f, 0.0f), decodeX(0.9666667f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.0f), decodeY(2.788889f));
        this.path.curveTo(decodeAnchorX(2.0f, 1.9709293f), decodeAnchorY(2.788889f, 0.01985704f), decodeAnchorX(2.777778f, -0.033333335f), decodeAnchorY(2.0555553f, 1.9333333f), decodeX(2.777778f), decodeY(2.0555553f));
        this.path.lineTo(decodeX(2.788889f), decodeY(1.8051281f));
        this.path.lineTo(decodeX(2.777778f), decodeY(1.2794871f));
        this.path.lineTo(decodeX(2.777778f), decodeY(1.0025641f));
        this.path.curveTo(decodeAnchorX(2.777778f, 0.0042173304f), decodeAnchorY(1.0025641f, -1.9503378f), decodeAnchorX(2.0999997f, 1.9659461f), decodeAnchorY(0.22222222f, 0.017122267f), decodeX(2.0999997f), decodeY(0.22222222f));
        this.path.lineTo(decodeX(0.9111111f), decodeY(0.21111111f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect5() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(0.6666667f), decodeX(2.3333333f) - decodeX(0.6666667f), decodeY(2.3333333f) - decodeY(0.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect6() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect7() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.4285715f));
        this.path.curveTo(decodeAnchorX(0.0f, 2.6785715f), decodeAnchorY(1.4285715f, 8.881784E-16f), decodeAnchorX(1.3898809f, -6.214286f), decodeAnchorY(0.3452381f, -0.035714287f), decodeX(1.3898809f), decodeY(0.3452381f));
        this.path.lineTo(decodeX(1.5535715f), decodeY(0.3452381f));
        this.path.curveTo(decodeAnchorX(1.5535715f, 8.32967f), decodeAnchorY(0.3452381f, 0.0027472528f), decodeAnchorX(2.3333333f, -5.285714f), decodeAnchorY(1.4285715f, 0.035714287f), decodeX(2.3333333f), decodeY(1.4285715f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.4285715f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.5714285f));
        this.path.lineTo(decodeX(2.3333333f), decodeY(1.5714285f));
        this.path.curveTo(decodeAnchorX(2.3333333f, -5.321429f), decodeAnchorY(1.5714285f, 0.035714287f), decodeAnchorX(1.5535715f, 8.983517f), decodeAnchorY(2.6666667f, 0.03846154f), decodeX(1.5535715f), decodeY(2.6666667f));
        this.path.lineTo(decodeX(1.4077381f), decodeY(2.6666667f));
        this.path.curveTo(decodeAnchorX(1.4077381f, -6.714286f), decodeAnchorY(2.6666667f, 0.0f), decodeAnchorX(0.0f, 2.607143f), decodeAnchorY(1.5714285f, 0.035714287f), decodeX(0.0f), decodeY(1.5714285f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.4285715f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect8() {
        this.rect.setRect(decodeX(1.2916666f), decodeY(0.0f), decodeX(1.3333334f) - decodeX(1.2916666f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect9() {
        this.rect.setRect(decodeX(1.7083333f), decodeY(0.0f), decodeX(1.75f) - decodeX(1.7083333f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(0.9888889f), decodeY(0.2f));
        this.path.curveTo(decodeAnchorX(0.9888889f, -2.0f), decodeAnchorY(0.2f, 0.0f), decodeAnchorX(0.2f, 0.0f), decodeAnchorY(0.9888889f, -2.0f), decodeX(0.2f), decodeY(0.9888889f));
        this.path.curveTo(decodeAnchorX(0.2f, 0.0f), decodeAnchorY(0.9888889f, 2.0f), decodeAnchorX(0.2f, 0.0f), decodeAnchorY(1.9974358f, -2.0f), decodeX(0.2f), decodeY(1.9974358f));
        this.path.curveTo(decodeAnchorX(0.2f, 0.0f), decodeAnchorY(1.9974358f, 2.0f), decodeAnchorX(0.9888889f, -2.0f), decodeAnchorY(2.8111107f, 0.0f), decodeX(0.9888889f), decodeY(2.8111107f));
        this.path.curveTo(decodeAnchorX(0.9888889f, 2.0f), decodeAnchorY(2.8111107f, 0.0f), decodeAnchorX(2.5f, 0.0f), decodeAnchorY(2.8f, 0.0f), decodeX(2.5f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.7444446f), decodeY(2.488889f));
        this.path.lineTo(decodeX(2.7555554f), decodeY(1.5794872f));
        this.path.lineTo(decodeX(2.7666664f), decodeY(1.4358975f));
        this.path.lineTo(decodeX(2.7666664f), decodeY(0.62222224f));
        this.path.lineTo(decodeX(2.5999997f), decodeY(0.22222222f));
        this.path.curveTo(decodeAnchorX(2.5999997f, 0.0f), decodeAnchorY(0.22222222f, 0.0f), decodeAnchorX(0.9888889f, 2.0f), decodeAnchorY(0.2f, 0.0f), decodeX(0.9888889f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.4285715f));
        this.path.curveTo(decodeAnchorX(0.0f, 2.6785715f), decodeAnchorY(1.4285715f, 8.881784E-16f), decodeAnchorX(1.3898809f, -6.357143f), decodeAnchorY(0.3452381f, -0.035714287f), decodeX(1.3898809f), decodeY(0.3452381f));
        this.path.lineTo(decodeX(1.5535715f), decodeY(0.3452381f));
        this.path.curveTo(decodeAnchorX(1.5535715f, 4.0f), decodeAnchorY(0.3452381f, 0.0f), decodeAnchorX(2.3333333f, -5.285714f), decodeAnchorY(1.4285715f, 0.035714287f), decodeX(2.3333333f), decodeY(1.4285715f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.4285715f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.5714285f));
        this.path.lineTo(decodeX(2.3333333f), decodeY(1.5714285f));
        this.path.curveTo(decodeAnchorX(2.3333333f, -5.321429f), decodeAnchorY(1.5714285f, 0.035714287f), decodeAnchorX(1.5535715f, 4.0f), decodeAnchorY(2.6666667f, 0.0f), decodeX(1.5535715f), decodeY(2.6666667f));
        this.path.lineTo(decodeX(1.4077381f), decodeY(2.6666667f));
        this.path.curveTo(decodeAnchorX(1.4077381f, -6.571429f), decodeAnchorY(2.6666667f, -0.035714287f), decodeAnchorX(0.0f, 2.607143f), decodeAnchorY(1.5714285f, 0.035714287f), decodeX(0.0f), decodeY(1.5714285f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.4285715f));
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
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038709678f, 0.05967742f, 0.08064516f, 0.23709677f, 0.3935484f, 0.41612905f, 0.43870968f, 0.67419356f, 0.90967745f, 0.91451615f, 0.91935486f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.05483871f, 0.5032258f, 0.9516129f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038709678f, 0.05967742f, 0.08064516f, 0.23709677f, 0.3935484f, 0.41612905f, 0.43870968f, 0.67419356f, 0.90967745f, 0.91612905f, 0.92258066f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038709678f, 0.05483871f, 0.07096774f, 0.28064516f, 0.4903226f, 0.6967742f, 0.9032258f, 0.9241935f, 0.9451613f}, new Color[]{this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038709678f, 0.061290324f, 0.08387097f, 0.27258065f, 0.46129033f, 0.4903226f, 0.5193548f, 0.71774197f, 0.91612905f, 0.92419356f, 0.93225807f}, new Color[]{this.color23, decodeColor(this.color23, this.color24, 0.5f), this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27, decodeColor(this.color27, this.color28, 0.5f), this.color28});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.051612902f, 0.06612903f, 0.08064516f, 0.2935484f, 0.5064516f, 0.6903226f, 0.87419355f, 0.88870966f, 0.9032258f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color29, 0.5f), this.color29, decodeColor(this.color29, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.20645161f, 0.41290322f, 0.44193548f, 0.47096774f, 0.7354839f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color30, 0.5f), this.color30});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038709678f, 0.05483871f, 0.07096774f, 0.28064516f, 0.4903226f, 0.6967742f, 0.9032258f, 0.9241935f, 0.9451613f}, new Color[]{this.color34, decodeColor(this.color34, this.color35, 0.5f), this.color35, decodeColor(this.color35, this.color36, 0.5f), this.color36, decodeColor(this.color36, this.color37, 0.5f), this.color37, decodeColor(this.color37, this.color38, 0.5f), this.color38});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.038709678f, 0.061290324f, 0.08387097f, 0.27258065f, 0.46129033f, 0.4903226f, 0.5193548f, 0.71774197f, 0.91612905f, 0.92419356f, 0.93225807f}, new Color[]{this.color39, decodeColor(this.color39, this.color40, 0.5f), this.color40, decodeColor(this.color40, this.color41, 0.5f), this.color41, decodeColor(this.color41, this.color42, 0.5f), this.color42, decodeColor(this.color42, this.color43, 0.5f), this.color43, decodeColor(this.color43, this.color44, 0.5f), this.color44});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.051612902f, 0.06612903f, 0.08064516f, 0.2935484f, 0.5064516f, 0.6903226f, 0.87419355f, 0.88870966f, 0.9032258f}, new Color[]{this.color45, decodeColor(this.color45, this.color46, 0.5f), this.color46, decodeColor(this.color46, this.color47, 0.5f), this.color47, decodeColor(this.color47, this.color48, 0.5f), this.color48, decodeColor(this.color48, this.color49, 0.5f), this.color49});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.20645161f, 0.41290322f, 0.44193548f, 0.47096774f, 0.7354839f, 1.0f}, new Color[]{this.color40, decodeColor(this.color40, this.color41, 0.5f), this.color41, decodeColor(this.color41, this.color42, 0.5f), this.color42, decodeColor(this.color42, this.color50, 0.5f), this.color50});
    }
}
