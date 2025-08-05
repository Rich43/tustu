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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TreePainter.class */
final class TreePainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_ENABLED_SELECTED = 3;
    static final int LEAFICON_ENABLED = 4;
    static final int CLOSEDICON_ENABLED = 5;
    static final int OPENICON_ENABLED = 6;
    static final int COLLAPSEDICON_ENABLED = 7;
    static final int COLLAPSEDICON_ENABLED_SELECTED = 8;
    static final int EXPANDEDICON_ENABLED = 9;
    static final int EXPANDEDICON_ENABLED_SELECTED = 10;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.065654516f, -0.13333333f, 0);
    private Color color2 = new Color(97, 98, 102, 255);
    private Color color3 = decodeColor("nimbusBlueGrey", -0.032679737f, -0.043332636f, 0.24705881f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.0077680945f, -0.51781034f, 0.3490196f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.013940871f, -0.599277f, 0.41960782f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.004681647f, -0.4198052f, 0.14117646f, 0);
    private Color color8 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -127);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.0f, 0.0f, -0.21f, -99);
    private Color color10 = decodeColor("nimbusBase", 2.9569864E-4f, -0.45978838f, 0.2980392f, 0);
    private Color color11 = decodeColor("nimbusBase", 0.0015952587f, -0.34848025f, 0.18823528f, 0);
    private Color color12 = decodeColor("nimbusBase", 0.0015952587f, -0.30844158f, 0.09803921f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.0015952587f, -0.27329817f, 0.035294116f, 0);
    private Color color14 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -125);
    private Color color16 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -50);
    private Color color17 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -100);
    private Color color18 = decodeColor("nimbusBase", 0.0012094378f, -0.23571429f, -0.0784314f, 0);
    private Color color19 = decodeColor("nimbusBase", 2.9569864E-4f, -0.115166366f, -0.2627451f, 0);
    private Color color20 = decodeColor("nimbusBase", 0.0027436614f, -0.335015f, 0.011764705f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.0024294257f, -0.3857143f, 0.031372547f, 0);
    private Color color22 = decodeColor("nimbusBase", 0.0018081069f, -0.3595238f, -0.13725492f, 0);
    private Color color23 = new Color(255, 200, 0, 255);
    private Color color24 = decodeColor("nimbusBase", 0.004681647f, -0.33496243f, -0.027450979f, 0);
    private Color color25 = decodeColor("nimbusBase", 0.0019934773f, -0.361378f, -0.10588238f, 0);
    private Color color26 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.34509805f, 0);
    private Object[] componentColors;

    public TreePainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 4:
                paintleafIconEnabled(graphics2D);
                break;
            case 5:
                paintclosedIconEnabled(graphics2D);
                break;
            case 6:
                paintopenIconEnabled(graphics2D);
                break;
            case 7:
                paintcollapsedIconEnabled(graphics2D);
                break;
            case 8:
                paintcollapsedIconEnabledAndSelected(graphics2D);
                break;
            case 9:
                paintexpandedIconEnabled(graphics2D);
                break;
            case 10:
                paintexpandedIconEnabledAndSelected(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintleafIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.rect);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(this.color7);
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.path);
    }

    private void paintclosedIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath6();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath8();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color15);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.rect);
        this.path = decodePath9();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath10();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath11();
        graphics2D.setPaint(this.color23);
        graphics2D.fill(this.path);
    }

    private void paintopenIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath6();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
        this.path = decodePath12();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath13();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color15);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.rect);
        this.path = decodePath14();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath15();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath11();
        graphics2D.setPaint(this.color23);
        graphics2D.fill(this.path);
    }

    private void paintcollapsedIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath16();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.path);
    }

    private void paintcollapsedIconEnabledAndSelected(Graphics2D graphics2D) {
        this.path = decodePath16();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
    }

    private void paintexpandedIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath17();
        graphics2D.setPaint(this.color26);
        graphics2D.fill(this.path);
    }

    private void paintexpandedIconEnabledAndSelected(Graphics2D graphics2D) {
        this.path = decodePath17();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.2f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.9197531f), decodeY(0.2f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.9f));
        this.path.lineTo(decodeX(2.6f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(0.88888896f));
        this.path.lineTo(decodeX(1.9537036f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.4f), decodeY(2.8f), decodeX(2.6f) - decodeX(0.4f), decodeY(3.0f) - decodeY(2.8f));
        return this.rect;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.6234567f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.6296296f), decodeY(1.2037038f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.2006173f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.4f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.4f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.4f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.4f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath7() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6037037f), decodeY(1.8425925f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath8() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.40833336f), decodeY(1.8645833f));
        this.path.lineTo(decodeX(0.79583335f), decodeY(0.8f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.8f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.6f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.2f), decodeY(0.6f), decodeX(0.4f) - decodeX(0.2f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.6f), decodeY(0.2f), decodeX(1.3333334f) - decodeX(0.6f), decodeY(0.4f) - decodeY(0.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(1.5f), decodeY(0.6f), decodeX(2.4f) - decodeX(1.5f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Path2D decodePath9() {
        this.path.reset();
        this.path.moveTo(decodeX(3.0f), decodeY(0.8f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.5888889f), decodeY(0.20370372f));
        this.path.lineTo(decodeX(0.5962963f), decodeY(0.34814817f));
        this.path.lineTo(decodeX(0.34814817f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.774074f), decodeY(1.1604939f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8925927f), decodeY(1.1882716f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.65185183f));
        this.path.lineTo(decodeX(0.63703704f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.5925925f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.8f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.8f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath10() {
        this.path.reset();
        this.path.moveTo(decodeX(2.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.74814814f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.4037037f), decodeY(1.8425925f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.5925926f), decodeY(2.225926f));
        this.path.lineTo(decodeX(0.916f), decodeY(0.996f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath11() {
        this.path.reset();
        this.path.moveTo(decodeX(2.2f), decodeY(2.2f));
        this.path.lineTo(decodeX(2.2f), decodeY(2.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath12() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.2f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath13() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.6f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath14() {
        this.path.reset();
        this.path.moveTo(decodeX(3.0f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.5888889f), decodeY(0.20370372f));
        this.path.lineTo(decodeX(0.5962963f), decodeY(0.34814817f));
        this.path.lineTo(decodeX(0.34814817f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(2.916f), decodeY(1.3533334f));
        this.path.lineTo(decodeX(2.98f), decodeY(1.3766667f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.65185183f));
        this.path.lineTo(decodeX(0.63703704f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.5925925f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.1666666f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath15() {
        this.path.reset();
        this.path.moveTo(decodeX(2.4f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(0.74f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.5925926f), decodeY(2.225926f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.3333334f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath16() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.2397541f), decodeY(0.70163935f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath17() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.25f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.70819676f), decodeY(2.9901638f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.046296295f * width) + x2, (0.9675926f * height) + y2, (0.4861111f * width) + x2, (0.5324074f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.04191617f, 0.10329342f, 0.16467066f, 0.24550897f, 0.3263473f, 0.6631737f, 1.0f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color5, decodeColor(this.color5, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12724552f, 0.25449103f, 0.62724555f, 1.0f}, new Color[]{this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25});
    }
}
