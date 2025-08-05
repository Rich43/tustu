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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/OptionPanePainter.class */
final class OptionPanePainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int ERRORICON_ENABLED = 2;
    static final int INFORMATIONICON_ENABLED = 3;
    static final int QUESTIONICON_ENABLED = 4;
    static final int WARNINGICON_ENABLED = 5;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusRed", -0.014814814f, 0.18384242f, 0.015686274f, 0);
    private Color color2 = decodeColor("nimbusRed", -0.014814814f, -0.403261f, 0.21960783f, 0);
    private Color color3 = decodeColor("nimbusRed", -0.014814814f, -0.07154381f, 0.11372548f, 0);
    private Color color4 = decodeColor("nimbusRed", -0.014814814f, 0.110274374f, 0.07058823f, 0);
    private Color color5 = decodeColor("nimbusRed", -0.014814814f, -0.05413574f, 0.2588235f, 0);
    private Color color6 = new Color(250, 250, 250, 255);
    private Color color7 = decodeColor("nimbusRed", 0.0f, -0.79881656f, 0.33725488f, -187);
    private Color color8 = new Color(255, 200, 0, 255);
    private Color color9 = decodeColor("nimbusInfoBlue", 0.0f, 0.06231594f, -0.054901958f, 0);
    private Color color10 = decodeColor("nimbusInfoBlue", 3.1620264E-4f, 0.07790506f, -0.19215685f, 0);
    private Color color11 = decodeColor("nimbusInfoBlue", -8.2296133E-4f, -0.44631243f, 0.19215685f, 0);
    private Color color12 = decodeColor("nimbusInfoBlue", 0.0012729168f, -0.0739674f, 0.043137252f, 0);
    private Color color13 = decodeColor("nimbusInfoBlue", 8.354187E-4f, -0.14148629f, 0.19999999f, 0);
    private Color color14 = decodeColor("nimbusInfoBlue", -0.0014793873f, -0.41456455f, 0.16470587f, 0);
    private Color color15 = decodeColor("nimbusInfoBlue", 3.437996E-4f, -0.14726585f, 0.043137252f, 0);
    private Color color16 = decodeColor("nimbusInfoBlue", -4.271865E-4f, -0.0055555105f, 0.0f, 0);
    private Color color17 = decodeColor("nimbusInfoBlue", 0.0f, 0.0f, 0.0f, 0);
    private Color color18 = decodeColor("nimbusInfoBlue", -7.866621E-4f, -0.12728173f, 0.17254901f, 0);
    private Color color19 = new Color(115, 120, 126, 255);
    private Color color20 = new Color(26, 34, 43, 255);
    private Color color21 = new Color(168, 173, 178, 255);
    private Color color22 = new Color(101, 109, 118, 255);
    private Color color23 = new Color(159, 163, 168, 255);
    private Color color24 = new Color(116, 122, 130, 255);
    private Color color25 = new Color(96, 104, 112, 255);
    private Color color26 = new Color(118, 128, 138, 255);
    private Color color27 = new Color(255, 255, 255, 255);
    private Color color28 = decodeColor("nimbusAlertYellow", -4.9102306E-4f, 0.1372549f, -0.15294117f, 0);
    private Color color29 = decodeColor("nimbusAlertYellow", -0.0015973002f, 0.1372549f, -0.3490196f, 0);
    private Color color30 = decodeColor("nimbusAlertYellow", 6.530881E-4f, -0.40784314f, 0.0f, 0);
    private Color color31 = decodeColor("nimbusAlertYellow", -3.9456785E-4f, -0.109803915f, 0.0f, 0);
    private Color color32 = decodeColor("nimbusAlertYellow", 0.0f, 0.0f, 0.0f, 0);
    private Color color33 = decodeColor("nimbusAlertYellow", 0.008085668f, -0.04705882f, 0.0f, 0);
    private Color color34 = decodeColor("nimbusAlertYellow", 0.026515156f, -0.18431371f, 0.0f, 0);
    private Color color35 = new Color(69, 69, 69, 255);
    private Color color36 = new Color(0, 0, 0, 255);
    private Color color37 = new Color(16, 16, 16, 255);
    private Object[] componentColors;

    public OptionPanePainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 2:
                painterrorIconEnabled(graphics2D);
                break;
            case 3:
                paintinformationIconEnabled(graphics2D);
                break;
            case 4:
                paintquestionIconEnabled(graphics2D);
                break;
            case 5:
                paintwarningIconEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void painterrorIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.path);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.ellipse);
        this.path = decodePath4();
        graphics2D.setPaint(this.color7);
        graphics2D.fill(this.path);
    }

    private void paintinformationIconEnabled(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse3();
        graphics2D.setPaint(decodeGradient2(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse4();
        graphics2D.setPaint(decodeGradient3(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse5();
        graphics2D.setPaint(decodeGradient4(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.path = decodePath5();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.path);
        this.ellipse = decodeEllipse6();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.ellipse);
    }

    private void paintquestionIconEnabled(Graphics2D graphics2D) {
        this.ellipse = decodeEllipse3();
        graphics2D.setPaint(decodeGradient5(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse4();
        graphics2D.setPaint(decodeGradient6(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse5();
        graphics2D.setPaint(decodeGradient7(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.path = decodePath6();
        graphics2D.setPaint(this.color27);
        graphics2D.fill(this.path);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(this.color27);
        graphics2D.fill(this.ellipse);
    }

    private void paintwarningIconEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.rect);
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath8();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath9();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.ellipse = decodeEllipse7();
        graphics2D.setPaint(this.color37);
        graphics2D.fill(this.ellipse);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(1.2708334f));
        this.path.lineTo(decodeX(1.2708334f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.6875f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.9583333f), decodeY(1.2708334f));
        this.path.lineTo(decodeX(1.9583333f), decodeY(1.6875f));
        this.path.lineTo(decodeX(1.6875f), decodeY(1.9583333f));
        this.path.lineTo(decodeX(1.2708334f), decodeY(1.9583333f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.6875f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.2708334f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0208334f), decodeY(1.2916666f));
        this.path.lineTo(decodeX(1.2916666f), decodeY(1.0208334f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.0208334f));
        this.path.lineTo(decodeX(1.9375f), decodeY(1.2916666f));
        this.path.lineTo(decodeX(1.9375f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.9375f));
        this.path.lineTo(decodeX(1.2916666f), decodeY(1.9375f));
        this.path.lineTo(decodeX(1.0208334f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.0208334f), decodeY(1.2916666f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(1.4166666f), decodeY(1.2291666f));
        this.path.curveTo(decodeAnchorX(1.4166666f, 0.0f), decodeAnchorY(1.2291666f, -2.0f), decodeAnchorX(1.4791666f, -2.0f), decodeAnchorY(1.1666666f, 0.0f), decodeX(1.4791666f), decodeY(1.1666666f));
        this.path.curveTo(decodeAnchorX(1.4791666f, 2.0f), decodeAnchorY(1.1666666f, 0.0f), decodeAnchorX(1.5416667f, 0.0f), decodeAnchorY(1.2291666f, -2.0f), decodeX(1.5416667f), decodeY(1.2291666f));
        this.path.curveTo(decodeAnchorX(1.5416667f, 0.0f), decodeAnchorY(1.2291666f, 2.0f), decodeAnchorX(1.5f, 0.0f), decodeAnchorY(1.6041667f, 0.0f), decodeX(1.5f), decodeY(1.6041667f));
        this.path.lineTo(decodeX(1.4583334f), decodeY(1.6041667f));
        this.path.curveTo(decodeAnchorX(1.4583334f, 0.0f), decodeAnchorY(1.6041667f, 0.0f), decodeAnchorX(1.4166666f, 0.0f), decodeAnchorY(1.2291666f, 2.0f), decodeX(1.4166666f), decodeY(1.2291666f));
        this.path.closePath();
        return this.path;
    }

    private Ellipse2D decodeEllipse1() {
        this.ellipse.setFrame(decodeX(1.4166666f), decodeY(1.6666667f), decodeX(1.5416667f) - decodeX(1.4166666f), decodeY(1.7916667f) - decodeY(1.6666667f));
        return this.ellipse;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0208334f), decodeY(1.2851562f));
        this.path.lineTo(decodeX(1.2799479f), decodeY(1.0208334f));
        this.path.lineTo(decodeX(1.6783855f), decodeY(1.0208334f));
        this.path.lineTo(decodeX(1.9375f), decodeY(1.28125f));
        this.path.lineTo(decodeX(1.9375f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.9375f));
        this.path.lineTo(decodeX(1.2851562f), decodeY(1.936198f));
        this.path.lineTo(decodeX(1.0221354f), decodeY(1.673177f));
        this.path.lineTo(decodeX(1.0208334f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.0416666f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.0416666f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.2916666f), decodeY(1.9166667f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.9166667f));
        this.path.lineTo(decodeX(1.9166667f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.9166667f), decodeY(1.2916666f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.0416666f));
        this.path.lineTo(decodeX(1.2916666f), decodeY(1.0416666f));
        this.path.lineTo(decodeX(1.0416666f), decodeY(1.2916666f));
        this.path.lineTo(decodeX(1.0416666f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.0208334f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.0208334f), decodeY(1.2851562f));
        this.path.closePath();
        return this.path;
    }

    private Ellipse2D decodeEllipse2() {
        this.ellipse.setFrame(decodeX(1.0f), decodeY(1.0f), decodeX(1.0f) - decodeX(1.0f), decodeY(1.0f) - decodeY(1.0f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse3() {
        this.ellipse.setFrame(decodeX(1.0f), decodeY(1.0f), decodeX(1.9583333f) - decodeX(1.0f), decodeY(1.9583333f) - decodeY(1.0f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse4() {
        this.ellipse.setFrame(decodeX(1.0208334f), decodeY(1.0208334f), decodeX(1.9375f) - decodeX(1.0208334f), decodeY(1.9375f) - decodeY(1.0208334f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse5() {
        this.ellipse.setFrame(decodeX(1.0416666f), decodeY(1.0416666f), decodeX(1.9166667f) - decodeX(1.0416666f), decodeY(1.9166667f) - decodeY(1.0416666f));
        return this.ellipse;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(1.375f), decodeY(1.375f));
        this.path.curveTo(decodeAnchorX(1.375f, 2.5f), decodeAnchorY(1.375f, 0.0f), decodeAnchorX(1.5f, -1.1875f), decodeAnchorY(1.375f, 0.0f), decodeX(1.5f), decodeY(1.375f));
        this.path.curveTo(decodeAnchorX(1.5f, 1.1875f), decodeAnchorY(1.375f, 0.0f), decodeAnchorX(1.5416667f, 0.0f), decodeAnchorY(1.4375f, -2.0f), decodeX(1.5416667f), decodeY(1.4375f));
        this.path.curveTo(decodeAnchorX(1.5416667f, 0.0f), decodeAnchorY(1.4375f, 2.0f), decodeAnchorX(1.5416667f, 0.0f), decodeAnchorY(1.6875f, 0.0f), decodeX(1.5416667f), decodeY(1.6875f));
        this.path.curveTo(decodeAnchorX(1.5416667f, 0.0f), decodeAnchorY(1.6875f, 0.0f), decodeAnchorX(1.6028645f, -2.5625f), decodeAnchorY(1.6875f, 0.0625f), decodeX(1.6028645f), decodeY(1.6875f));
        this.path.curveTo(decodeAnchorX(1.6028645f, 2.5625f), decodeAnchorY(1.6875f, -0.0625f), decodeAnchorX(1.6041667f, 2.5625f), decodeAnchorY(1.7708333f, 0.0f), decodeX(1.6041667f), decodeY(1.7708333f));
        this.path.curveTo(decodeAnchorX(1.6041667f, -2.5625f), decodeAnchorY(1.7708333f, 0.0f), decodeAnchorX(1.3567709f, 2.5f), decodeAnchorY(1.7708333f, 0.0625f), decodeX(1.3567709f), decodeY(1.7708333f));
        this.path.curveTo(decodeAnchorX(1.3567709f, -2.5f), decodeAnchorY(1.7708333f, -0.0625f), decodeAnchorX(1.3541666f, -2.4375f), decodeAnchorY(1.6875f, 0.0f), decodeX(1.3541666f), decodeY(1.6875f));
        this.path.curveTo(decodeAnchorX(1.3541666f, 2.4375f), decodeAnchorY(1.6875f, 0.0f), decodeAnchorX(1.4166666f, 0.0f), decodeAnchorY(1.6875f, 0.0f), decodeX(1.4166666f), decodeY(1.6875f));
        this.path.lineTo(decodeX(1.4166666f), decodeY(1.4583334f));
        this.path.curveTo(decodeAnchorX(1.4166666f, 0.0f), decodeAnchorY(1.4583334f, 0.0f), decodeAnchorX(1.375f, 2.75f), decodeAnchorY(1.4583334f, 0.0f), decodeX(1.375f), decodeY(1.4583334f));
        this.path.curveTo(decodeAnchorX(1.375f, -2.75f), decodeAnchorY(1.4583334f, 0.0f), decodeAnchorX(1.375f, -2.5f), decodeAnchorY(1.375f, 0.0f), decodeX(1.375f), decodeY(1.375f));
        this.path.closePath();
        return this.path;
    }

    private Ellipse2D decodeEllipse6() {
        this.ellipse.setFrame(decodeX(1.4166666f), decodeY(1.1666666f), decodeX(1.5416667f) - decodeX(1.4166666f), decodeY(1.2916666f) - decodeY(1.1666666f));
        return this.ellipse;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(1.3125f), decodeY(1.3723959f));
        this.path.curveTo(decodeAnchorX(1.3125f, 1.5f), decodeAnchorY(1.3723959f, 1.375f), decodeAnchorX(1.3997396f, -0.75f), decodeAnchorY(1.3580729f, 1.1875f), decodeX(1.3997396f), decodeY(1.3580729f));
        this.path.curveTo(decodeAnchorX(1.3997396f, 0.75f), decodeAnchorY(1.3580729f, -1.1875f), decodeAnchorX(1.46875f, -1.8125f), decodeAnchorY(1.2903646f, 0.0f), decodeX(1.46875f), decodeY(1.2903646f));
        this.path.curveTo(decodeAnchorX(1.46875f, 1.8125f), decodeAnchorY(1.2903646f, 0.0f), decodeAnchorX(1.5351562f, 0.0f), decodeAnchorY(1.3502604f, -1.5625f), decodeX(1.5351562f), decodeY(1.3502604f));
        this.path.curveTo(decodeAnchorX(1.5351562f, 0.0f), decodeAnchorY(1.3502604f, 1.5625f), decodeAnchorX(1.4700521f, 1.25f), decodeAnchorY(1.4283854f, -1.1875f), decodeX(1.4700521f), decodeY(1.4283854f));
        this.path.curveTo(decodeAnchorX(1.4700521f, -1.25f), decodeAnchorY(1.4283854f, 1.1875f), decodeAnchorX(1.4179688f, -0.0625f), decodeAnchorY(1.5442708f, -1.5f), decodeX(1.4179688f), decodeY(1.5442708f));
        this.path.curveTo(decodeAnchorX(1.4179688f, 0.0625f), decodeAnchorY(1.5442708f, 1.5f), decodeAnchorX(1.4765625f, -1.3125f), decodeAnchorY(1.6028645f, 0.0f), decodeX(1.4765625f), decodeY(1.6028645f));
        this.path.curveTo(decodeAnchorX(1.4765625f, 1.3125f), decodeAnchorY(1.6028645f, 0.0f), decodeAnchorX(1.5403645f, 0.0f), decodeAnchorY(1.546875f, 1.625f), decodeX(1.5403645f), decodeY(1.546875f));
        this.path.curveTo(decodeAnchorX(1.5403645f, 0.0f), decodeAnchorY(1.546875f, -1.625f), decodeAnchorX(1.6132812f, -1.1875f), decodeAnchorY(1.4648438f, 1.25f), decodeX(1.6132812f), decodeY(1.4648438f));
        this.path.curveTo(decodeAnchorX(1.6132812f, 1.1875f), decodeAnchorY(1.4648438f, -1.25f), decodeAnchorX(1.6666667f, 0.0625f), decodeAnchorY(1.3463541f, 3.3125f), decodeX(1.6666667f), decodeY(1.3463541f));
        this.path.curveTo(decodeAnchorX(1.6666667f, -0.0625f), decodeAnchorY(1.3463541f, -3.3125f), decodeAnchorX(1.4830729f, 6.125f), decodeAnchorY(1.1679688f, -0.0625f), decodeX(1.4830729f), decodeY(1.1679688f));
        this.path.curveTo(decodeAnchorX(1.4830729f, -6.125f), decodeAnchorY(1.1679688f, 0.0625f), decodeAnchorX(1.3046875f, 0.4375f), decodeAnchorY(1.2890625f, -1.25f), decodeX(1.3046875f), decodeY(1.2890625f));
        this.path.curveTo(decodeAnchorX(1.3046875f, -0.4375f), decodeAnchorY(1.2890625f, 1.25f), decodeAnchorX(1.3125f, -1.5f), decodeAnchorY(1.3723959f, -1.375f), decodeX(1.3125f), decodeY(1.3723959f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(1.0f) - decodeX(1.0f), decodeY(1.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Path2D decodePath7() {
        this.path.reset();
        this.path.moveTo(decodeX(1.5f), decodeY(1.0208334f));
        this.path.curveTo(decodeAnchorX(1.5f, 2.0f), decodeAnchorY(1.0208334f, 0.0f), decodeAnchorX(1.5664062f, 0.0f), decodeAnchorY(1.0820312f, 0.0f), decodeX(1.5664062f), decodeY(1.0820312f));
        this.path.lineTo(decodeX(1.9427083f), decodeY(1.779948f));
        this.path.curveTo(decodeAnchorX(1.9427083f, 0.0f), decodeAnchorY(1.779948f, 0.0f), decodeAnchorX(1.9752605f, 0.0f), decodeAnchorY(1.8802083f, -2.375f), decodeX(1.9752605f), decodeY(1.8802083f));
        this.path.curveTo(decodeAnchorX(1.9752605f, 0.0f), decodeAnchorY(1.8802083f, 2.375f), decodeAnchorX(1.9166667f, 0.0f), decodeAnchorY(1.9375f, 0.0f), decodeX(1.9166667f), decodeY(1.9375f));
        this.path.lineTo(decodeX(1.0833334f), decodeY(1.9375f));
        this.path.curveTo(decodeAnchorX(1.0833334f, 0.0f), decodeAnchorY(1.9375f, 0.0f), decodeAnchorX(1.0247396f, 0.125f), decodeAnchorY(1.8815105f, 2.25f), decodeX(1.0247396f), decodeY(1.8815105f));
        this.path.curveTo(decodeAnchorX(1.0247396f, -0.125f), decodeAnchorY(1.8815105f, -2.25f), decodeAnchorX(1.0598959f, 0.0f), decodeAnchorY(1.78125f, 0.0f), decodeX(1.0598959f), decodeY(1.78125f));
        this.path.lineTo(decodeX(1.4375f), decodeY(1.0833334f));
        this.path.curveTo(decodeAnchorX(1.4375f, 0.0f), decodeAnchorY(1.0833334f, 0.0f), decodeAnchorX(1.5f, -2.0f), decodeAnchorY(1.0208334f, 0.0f), decodeX(1.5f), decodeY(1.0208334f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath8() {
        this.path.reset();
        this.path.moveTo(decodeX(1.4986979f), decodeY(1.0429688f));
        this.path.curveTo(decodeAnchorX(1.4986979f, 1.75f), decodeAnchorY(1.0429688f, 0.0f), decodeAnchorX(1.5546875f, 0.0f), decodeAnchorY(1.0950521f, 0.0f), decodeX(1.5546875f), decodeY(1.0950521f));
        this.path.lineTo(decodeX(1.9322917f), decodeY(1.8007812f));
        this.path.curveTo(decodeAnchorX(1.9322917f, 0.0f), decodeAnchorY(1.8007812f, 0.0f), decodeAnchorX(1.9570312f, 0.0f), decodeAnchorY(1.875f, -1.4375f), decodeX(1.9570312f), decodeY(1.875f));
        this.path.curveTo(decodeAnchorX(1.9570312f, 0.0f), decodeAnchorY(1.875f, 1.4375f), decodeAnchorX(1.8841145f, 0.0f), decodeAnchorY(1.9166667f, 0.0f), decodeX(1.8841145f), decodeY(1.9166667f));
        this.path.lineTo(decodeX(1.1002604f), decodeY(1.9166667f));
        this.path.curveTo(decodeAnchorX(1.1002604f, 0.0f), decodeAnchorY(1.9166667f, 0.0f), decodeAnchorX(1.0455729f, 0.0625f), decodeAnchorY(1.8723958f, 1.625f), decodeX(1.0455729f), decodeY(1.8723958f));
        this.path.curveTo(decodeAnchorX(1.0455729f, -0.0625f), decodeAnchorY(1.8723958f, -1.625f), decodeAnchorX(1.0755209f, 0.0f), decodeAnchorY(1.7903645f, 0.0f), decodeX(1.0755209f), decodeY(1.7903645f));
        this.path.lineTo(decodeX(1.4414062f), decodeY(1.1028646f));
        this.path.curveTo(decodeAnchorX(1.4414062f, 0.0f), decodeAnchorY(1.1028646f, 0.0f), decodeAnchorX(1.4986979f, -1.75f), decodeAnchorY(1.0429688f, 0.0f), decodeX(1.4986979f), decodeY(1.0429688f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath9() {
        this.path.reset();
        this.path.moveTo(decodeX(1.5f), decodeY(1.2291666f));
        this.path.curveTo(decodeAnchorX(1.5f, 2.0f), decodeAnchorY(1.2291666f, 0.0f), decodeAnchorX(1.5625f, 0.0f), decodeAnchorY(1.3125f, -2.0f), decodeX(1.5625f), decodeY(1.3125f));
        this.path.curveTo(decodeAnchorX(1.5625f, 0.0f), decodeAnchorY(1.3125f, 2.0f), decodeAnchorX(1.5f, 1.3125f), decodeAnchorY(1.6666667f, 0.0f), decodeX(1.5f), decodeY(1.6666667f));
        this.path.curveTo(decodeAnchorX(1.5f, -1.3125f), decodeAnchorY(1.6666667f, 0.0f), decodeAnchorX(1.4375f, 0.0f), decodeAnchorY(1.3125f, 2.0f), decodeX(1.4375f), decodeY(1.3125f));
        this.path.curveTo(decodeAnchorX(1.4375f, 0.0f), decodeAnchorY(1.3125f, -2.0f), decodeAnchorX(1.5f, -2.0f), decodeAnchorY(1.2291666f, 0.0f), decodeX(1.5f), decodeY(1.2291666f));
        this.path.closePath();
        return this.path;
    }

    private Ellipse2D decodeEllipse7() {
        this.ellipse.setFrame(decodeX(1.4375f), decodeY(1.7291667f), decodeX(1.5625f) - decodeX(1.4375f), decodeY(1.8541667f) - decodeY(1.7291667f));
        return this.ellipse;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.17258064f, 0.3451613f, 0.5145161f, 0.683871f, 0.9f, 1.0f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.24143836f, 0.48287672f, 0.7414384f, 1.0f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.14212328f, 0.28424656f, 0.39212328f, 0.5f, 0.60958904f, 0.7191781f, 0.85958904f, 1.0f}, new Color[]{this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.15239726f, 0.30479452f, 0.47945207f, 0.6541096f, 0.8270548f, 1.0f}, new Color[]{this.color23, decodeColor(this.color23, this.color24, 0.5f), this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.1729452f, 0.3458904f, 0.49315068f, 0.64041096f, 0.7328767f, 0.8253425f, 0.9126712f, 1.0f}, new Color[]{this.color30, decodeColor(this.color30, this.color31, 0.5f), this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32, decodeColor(this.color32, this.color33, 0.5f), this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color35, decodeColor(this.color35, this.color36, 0.5f), this.color36});
    }
}
