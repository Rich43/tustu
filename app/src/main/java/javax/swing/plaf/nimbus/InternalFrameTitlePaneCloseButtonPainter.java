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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InternalFrameTitlePaneCloseButtonPainter.class */
final class InternalFrameTitlePaneCloseButtonPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
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
    private Color color1 = decodeColor("nimbusRed", 0.5893519f, -0.75736576f, 0.09411764f, 0);
    private Color color2 = decodeColor("nimbusRed", 0.5962963f, -0.71005917f, 0.0f, 0);
    private Color color3 = decodeColor("nimbusRed", 0.6005698f, -0.7200287f, -0.015686274f, ClassFileConstants.opc_i2f);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.062449392f, 0.07058823f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.0029994324f, -0.38039216f, -185);
    private Color color6 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.4431373f, 0);
    private Color color7 = decodeColor("nimbusRed", -2.7342606E-4f, 0.13829035f, -0.039215684f, 0);
    private Color color8 = decodeColor("nimbusRed", 6.890595E-4f, -0.36665577f, 0.11764705f, 0);
    private Color color9 = decodeColor("nimbusRed", -0.001021713f, 0.101804554f, -0.031372547f, 0);
    private Color color10 = decodeColor("nimbusRed", -2.7342606E-4f, 0.13243341f, -0.035294116f, 0);
    private Color color11 = decodeColor("nimbusRed", -2.7342606E-4f, 0.002258718f, 0.06666666f, 0);
    private Color color12 = decodeColor("nimbusRed", 0.0056530247f, 0.0040003657f, -0.38431373f, ClassFileConstants.opc_i2f);
    private Color color13 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color14 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.3882353f, 0);
    private Color color15 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.13333333f, 0);
    private Color color16 = decodeColor("nimbusRed", 6.890595E-4f, -0.38929275f, 0.1607843f, 0);
    private Color color17 = decodeColor("nimbusRed", 2.537202E-5f, 0.012294531f, 0.043137252f, 0);
    private Color color18 = decodeColor("nimbusRed", -2.7342606E-4f, 0.033585668f, 0.039215684f, 0);
    private Color color19 = decodeColor("nimbusRed", -2.7342606E-4f, -0.07198727f, 0.14117646f, 0);
    private Color color20 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, 0.0039215684f, ClassFileConstants.opc_i2f);
    private Color color21 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -140);
    private Color color22 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.49411768f, 0);
    private Color color23 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.20392159f, 0);
    private Color color24 = decodeColor("nimbusRed", -0.014814814f, -0.21260965f, 0.019607842f, 0);
    private Color color25 = decodeColor("nimbusRed", -0.014814814f, 0.17340565f, -0.09803921f, 0);
    private Color color26 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.10588235f, 0);
    private Color color27 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.04705882f, 0);
    private Color color28 = decodeColor("nimbusRed", -0.014814814f, 0.20118344f, -0.31764707f, ClassFileConstants.opc_i2f);
    private Color color29 = decodeColor("nimbusRed", 0.5962963f, -0.6994788f, -0.07058823f, 0);
    private Color color30 = decodeColor("nimbusRed", 0.5962963f, -0.66245294f, -0.23137257f, 0);
    private Color color31 = decodeColor("nimbusRed", 0.58518517f, -0.77649516f, 0.21568626f, 0);
    private Color color32 = decodeColor("nimbusRed", 0.5962963f, -0.7372781f, 0.10196078f, 0);
    private Color color33 = decodeColor("nimbusRed", 0.5962963f, -0.73911506f, 0.12549019f, 0);
    private Color color34 = decodeColor("nimbusBlueGrey", 0.0f, -0.027957506f, -0.31764707f, 0);
    private Object[] componentColors;

    public InternalFrameTitlePaneCloseButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color3);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(this.color5);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(this.color5);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color20);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(this.color21);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundEnabledAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath2();
        graphics2D.setPaint(this.color34);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOverAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(this.color5);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color20);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundPressedAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(this.color21);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient7(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color28);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(1.9444444f) - decodeY(1.0f), 8.600000381469727d, 8.600000381469727d);
        return this.roundRect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.25f), decodeY(1.7373737f));
        this.path.lineTo(decodeX(1.3002392f), decodeY(1.794192f));
        this.path.lineTo(decodeX(1.5047847f), decodeY(1.5909091f));
        this.path.lineTo(decodeX(1.6842105f), decodeY(1.7954545f));
        this.path.lineTo(decodeX(1.7595694f), decodeY(1.719697f));
        this.path.lineTo(decodeX(1.5956938f), decodeY(1.5239899f));
        this.path.lineTo(decodeX(1.7535884f), decodeY(1.3409091f));
        this.path.lineTo(decodeX(1.6830144f), decodeY(1.2537879f));
        this.path.lineTo(decodeX(1.5083733f), decodeY(1.4406565f));
        this.path.lineTo(decodeX(1.3301436f), decodeY(1.2563131f));
        this.path.lineTo(decodeX(1.257177f), decodeY(1.3320707f));
        this.path.lineTo(decodeX(1.4270334f), decodeY(1.5252526f));
        this.path.lineTo(decodeX(1.25f), decodeY(1.7373737f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.257177f), decodeY(1.2828283f));
        this.path.lineTo(decodeX(1.3217703f), decodeY(1.2133838f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.4040405f));
        this.path.lineTo(decodeX(1.673445f), decodeY(1.2108586f));
        this.path.lineTo(decodeX(1.7440192f), decodeY(1.2853535f));
        this.path.lineTo(decodeX(1.5669856f), decodeY(1.4709597f));
        this.path.lineTo(decodeX(1.7488039f), decodeY(1.6527778f));
        this.path.lineTo(decodeX(1.673445f), decodeY(1.7398989f));
        this.path.lineTo(decodeX(1.4988039f), decodeY(1.5416667f));
        this.path.lineTo(decodeX(1.3313397f), decodeY(1.7424242f));
        this.path.lineTo(decodeX(1.2523923f), decodeY(1.6565657f));
        this.path.lineTo(decodeX(1.4366028f), decodeY(1.4722222f));
        this.path.lineTo(decodeX(1.257177f), decodeY(1.2828283f));
        this.path.closePath();
        return this.path;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(1.0f), decodeY(1.6111112f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.6111112f), 6.0d, 6.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(1.0526316f), decodeY(1.0530303f), decodeX(1.9473684f) - decodeX(1.0526316f), decodeY(1.8863636f) - decodeY(1.0530303f), 6.75d, 6.75d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect4() {
        this.roundRect.setRoundRect(decodeX(1.0526316f), decodeY(1.0517677f), decodeX(1.9473684f) - decodeX(1.0526316f), decodeY(1.8851011f) - decodeY(1.0517677f), 6.75d, 6.75d);
        return this.roundRect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.81480503f, 0.97904193f}, new Color[]{this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.81630206f, 0.98203593f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.24101797f, 0.48203593f, 0.5838324f, 0.6856288f, 0.8428144f, 1.0f}, new Color[]{this.color31, decodeColor(this.color31, this.color32, 0.5f), this.color32, decodeColor(this.color32, this.color32, 0.5f), this.color32, decodeColor(this.color32, this.color33, 0.5f), this.color33});
    }
}
