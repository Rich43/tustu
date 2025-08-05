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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InternalFrameTitlePaneIconifyButtonPainter.class */
final class InternalFrameTitlePaneIconifyButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BACKGROUND_DISABLED = 2;
    static final int BACKGROUND_MOUSEOVER = 3;
    static final int BACKGROUND_PRESSED = 4;
    static final int BACKGROUND_ENABLED_WINDOWNOTFOCUSED = 5;
    static final int BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED = 6;
    static final int BACKGROUND_PRESSED_WINDOWNOTFOCUSED = 7;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.0029994324f, -0.38039216f, -185);
    private Color color2 = decodeColor("nimbusOrange", -0.08377897f, 0.02094239f, -0.40392157f, 0);
    private Color color3 = decodeColor("nimbusOrange", 0.0f, 0.0f, 0.0f, 0);
    private Color color4 = decodeColor("nimbusOrange", -4.4563413E-4f, -0.48364475f, 0.10588235f, 0);
    private Color color5 = decodeColor("nimbusOrange", 0.0f, -0.0050992966f, 0.0039215684f, 0);
    private Color color6 = decodeColor("nimbusOrange", 0.0f, -0.12125945f, 0.10588235f, 0);
    private Color color7 = decodeColor("nimbusOrange", -0.08377897f, 0.02094239f, -0.40392157f, -106);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color9 = decodeColor("nimbusOrange", 0.5203877f, -0.9376068f, 0.007843137f, 0);
    private Color color10 = decodeColor("nimbusOrange", 0.5273321f, -0.8903002f, -0.086274505f, 0);
    private Color color11 = decodeColor("nimbusOrange", 0.5273321f, -0.93313926f, 0.019607842f, 0);
    private Color color12 = decodeColor("nimbusOrange", 0.53526866f, -0.8995122f, -0.058823526f, 0);
    private Color color13 = decodeColor("nimbusOrange", 0.5233639f, -0.8971863f, -0.07843137f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", -0.0808081f, 0.015910469f, -0.40392157f, -216);
    private Color color15 = decodeColor("nimbusBlueGrey", -0.003968239f, -0.03760965f, 0.007843137f, 0);
    private Color color16 = new Color(255, 200, 0, 255);
    private Color color17 = decodeColor("nimbusOrange", -0.08377897f, 0.02094239f, -0.31764707f, 0);
    private Color color18 = decodeColor("nimbusOrange", -0.02758849f, 0.02094239f, -0.062745094f, 0);
    private Color color19 = decodeColor("nimbusOrange", -4.4563413E-4f, -0.5074419f, 0.1490196f, 0);
    private Color color20 = decodeColor("nimbusOrange", 9.745359E-6f, -0.11175901f, 0.07843137f, 0);
    private Color color21 = decodeColor("nimbusOrange", 0.0f, -0.09280169f, 0.07843137f, 0);
    private Color color22 = decodeColor("nimbusOrange", 0.0f, -0.19002807f, 0.18039215f, 0);
    private Color color23 = decodeColor("nimbusOrange", -0.025772434f, 0.02094239f, 0.05098039f, 0);
    private Color color24 = decodeColor("nimbusOrange", -0.08377897f, 0.02094239f, -0.4f, 0);
    private Color color25 = decodeColor("nimbusOrange", -0.053104125f, 0.02094239f, -0.109803915f, 0);
    private Color color26 = decodeColor("nimbusOrange", -0.017887495f, -0.33726656f, 0.039215684f, 0);
    private Color color27 = decodeColor("nimbusOrange", -0.018038228f, 0.02094239f, -0.043137252f, 0);
    private Color color28 = decodeColor("nimbusOrange", -0.015844189f, 0.02094239f, -0.027450979f, 0);
    private Color color29 = decodeColor("nimbusOrange", -0.010274701f, 0.02094239f, 0.015686274f, 0);
    private Color color30 = decodeColor("nimbusOrange", -0.08377897f, 0.02094239f, -0.14509803f, -91);
    private Color color31 = decodeColor("nimbusOrange", 0.5273321f, -0.87971985f, -0.15686274f, 0);
    private Color color32 = decodeColor("nimbusOrange", 0.5273321f, -0.842694f, -0.31764707f, 0);
    private Color color33 = decodeColor("nimbusOrange", 0.516221f, -0.9567362f, 0.12941176f, 0);
    private Color color34 = decodeColor("nimbusOrange", 0.5222816f, -0.9229352f, 0.019607842f, 0);
    private Color color35 = decodeColor("nimbusOrange", 0.5273321f, -0.91751915f, 0.015686274f, 0);
    private Color color36 = decodeColor("nimbusOrange", 0.5273321f, -0.9193561f, 0.039215684f, 0);
    private Color color37 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.017933726f, -0.32156864f, 0);
    private Object[] componentColors;

    public InternalFrameTitlePaneIconifyButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundMouseOver(graphics2D);
                break;
            case 4:
                paintBackgroundPressed(graphics2D);
                break;
            case 5:
                paintBackgroundEnabledAndWindowNotFocused(graphics2D);
                break;
            case 6:
                paintBackgroundMouseOverAndWindowNotFocused(graphics2D);
                break;
            case 7:
                paintBackgroundPressedAndWindowNotFocused(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color7);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color14);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color15);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color23);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color30);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient10(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color14);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color37);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundMouseOverAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color23);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundPressedAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color30);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(1.0f), decodeY(1.6111112f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.6111112f), 6.0d, 6.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(1.9444444f) - decodeY(1.0f), 8.600000381469727d, 8.600000381469727d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(1.0526316f), decodeY(1.0555556f), decodeX(1.9473684f) - decodeX(1.0526316f), decodeY(1.8888888f) - decodeY(1.0555556f), 6.75d, 6.75d);
        return this.roundRect;
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.25f), decodeY(1.6628788f), decodeX(1.75f) - decodeX(1.25f), decodeY(1.7487373f) - decodeY(1.6628788f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(1.2870814f), decodeY(1.6123737f), decodeX(1.7165072f) - decodeX(1.2870814f), decodeY(1.7222222f) - decodeY(1.6123737f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(1.0f) - decodeX(1.0f), decodeY(1.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(1.25f), decodeY(1.6527778f), decodeX(1.7511961f) - decodeX(1.25f), decodeY(1.7828283f) - decodeY(1.6527778f));
        return this.rect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color4, decodeColor(this.color4, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color10, 0.5f), this.color10});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27, decodeColor(this.color27, this.color28, 0.5f), this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.78336793f, 0.9161677f}, new Color[]{this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34, decodeColor(this.color34, this.color35, 0.5f), this.color35, decodeColor(this.color35, this.color36, 0.5f), this.color36});
    }
}
