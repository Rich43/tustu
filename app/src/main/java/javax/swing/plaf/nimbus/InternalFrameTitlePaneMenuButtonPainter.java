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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InternalFrameTitlePaneMenuButtonPainter.class */
final class InternalFrameTitlePaneMenuButtonPainter extends AbstractRegionPainter {
    static final int ICON_ENABLED = 1;
    static final int ICON_DISABLED = 2;
    static final int ICON_MOUSEOVER = 3;
    static final int ICON_PRESSED = 4;
    static final int ICON_ENABLED_WINDOWNOTFOCUSED = 5;
    static final int ICON_MOUSEOVER_WINDOWNOTFOCUSED = 6;
    static final int ICON_PRESSED_WINDOWNOTFOCUSED = 7;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.0029994324f, -0.38039216f, -185);
    private Color color2 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.5019608f, 0);
    private Color color3 = decodeColor("nimbusBase", 0.030543745f, -0.3835404f, -0.09803924f, 0);
    private Color color4 = decodeColor("nimbusBase", 0.029191494f, -0.53801316f, 0.13333333f, 0);
    private Color color5 = decodeColor("nimbusBase", 0.030543745f, -0.3857143f, -0.09411767f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.030543745f, -0.43148893f, 0.007843137f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.029191494f, -0.24935067f, -0.20392159f, -132);
    private Color color8 = decodeColor("nimbusBase", 0.029191494f, -0.24935067f, -0.20392159f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.029191494f, -0.24935067f, -0.20392159f, ClassFileConstants.opc_i2l);
    private Color color10 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.0029994324f, -0.38039216f, -208);
    private Color color12 = decodeColor("nimbusBase", 0.02551502f, -0.5942635f, 0.20784312f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.032459438f, -0.5490091f, 0.12941176f, 0);
    private Color color14 = decodeColor("nimbusBase", 0.032459438f, -0.5469569f, 0.11372548f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.032459438f, -0.5760128f, 0.23921567f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.4901961f, 0);
    private Color color17 = decodeColor("nimbusBase", 0.032459438f, -0.1857143f, -0.23529413f, 0);
    private Color color18 = decodeColor("nimbusBase", 0.029191494f, -0.5438224f, 0.17647058f, 0);
    private Color color19 = decodeColor("nimbusBase", 0.030543745f, -0.41929638f, -0.02352941f, 0);
    private Color color20 = decodeColor("nimbusBase", 0.030543745f, -0.45559007f, 0.082352936f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.03409344f, -0.329408f, -0.11372551f, -132);
    private Color color22 = decodeColor("nimbusBase", 0.03409344f, -0.329408f, -0.11372551f, 0);
    private Color color23 = decodeColor("nimbusBase", 0.03409344f, -0.329408f, -0.11372551f, ClassFileConstants.opc_i2l);
    private Color color24 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color25 = decodeColor("nimbusBase", 0.031104386f, 0.12354499f, -0.33725494f, 0);
    private Color color26 = decodeColor("nimbusBase", 0.032459438f, -0.4592437f, -0.015686274f, 0);
    private Color color27 = decodeColor("nimbusBase", 0.029191494f, -0.2579365f, -0.19607845f, 0);
    private Color color28 = decodeColor("nimbusBase", 0.03409344f, -0.3149596f, -0.13333336f, 0);
    private Color color29 = decodeColor("nimbusBase", 0.029681683f, 0.07857144f, -0.3294118f, -132);
    private Color color30 = decodeColor("nimbusBase", 0.029681683f, 0.07857144f, -0.3294118f, 0);
    private Color color31 = decodeColor("nimbusBase", 0.029681683f, 0.07857144f, -0.3294118f, ClassFileConstants.opc_i2l);
    private Color color32 = decodeColor("nimbusBase", 0.032459438f, -0.53637654f, 0.043137252f, 0);
    private Color color33 = decodeColor("nimbusBase", 0.032459438f, -0.49935067f, -0.11764708f, 0);
    private Color color34 = decodeColor("nimbusBase", 0.021348298f, -0.6133929f, 0.32941175f, 0);
    private Color color35 = decodeColor("nimbusBase", 0.042560518f, -0.5804379f, 0.23137254f, 0);
    private Color color36 = decodeColor("nimbusBase", 0.032459438f, -0.57417583f, 0.21568626f, 0);
    private Color color37 = decodeColor("nimbusBase", 0.027408898f, -0.5784226f, 0.20392156f, -132);
    private Color color38 = decodeColor("nimbusBase", 0.042560518f, -0.5665319f, 0.0745098f, 0);
    private Color color39 = decodeColor("nimbusBase", 0.036732912f, -0.5642857f, 0.16470587f, ClassFileConstants.opc_i2l);
    private Color color40 = decodeColor("nimbusBase", 0.021348298f, -0.54480517f, -0.11764708f, 0);
    private Object[] componentColors;

    public InternalFrameTitlePaneMenuButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                painticonEnabled(graphics2D);
                break;
            case 2:
                painticonDisabled(graphics2D);
                break;
            case 3:
                painticonMouseOver(graphics2D);
                break;
            case 4:
                painticonPressed(graphics2D);
                break;
            case 5:
                painticonEnabledAndWindowNotFocused(graphics2D);
                break;
            case 6:
                painticonMouseOverAndWindowNotFocused(graphics2D);
                break;
            case 7:
                painticonPressedAndWindowNotFocused(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void painticonEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
    }

    private void painticonDisabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color11);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath2();
        graphics2D.setPaint(this.color15);
        graphics2D.fill(this.path);
    }

    private void painticonMouseOver(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
    }

    private void painticonPressed(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
    }

    private void painticonEnabledAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient11(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient12(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient13(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color40);
        graphics2D.fill(this.path);
    }

    private void painticonMouseOverAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient6(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
    }

    private void painticonPressedAndWindowNotFocused(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient8(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient9(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
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

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(1.3157895f), decodeY(1.4444444f));
        this.path.lineTo(decodeX(1.6842105f), decodeY(1.4444444f));
        this.path.lineTo(decodeX(1.5013158f), decodeY(1.7208333f));
        this.path.lineTo(decodeX(1.3157895f), decodeY(1.4444444f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.3157895f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(1.6842105f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.6083333f));
        this.path.lineTo(decodeX(1.3157895f), decodeY(1.3333334f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(1.3157895f), decodeY(1.3888888f));
        this.path.lineTo(decodeX(1.6842105f), decodeY(1.3888888f));
        this.path.lineTo(decodeX(1.4952153f), decodeY(1.655303f));
        this.path.lineTo(decodeX(1.3157895f), decodeY(1.3888888f));
        this.path.closePath();
        return this.path;
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
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color6, 0.5f), this.color6});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.50714284f * width) + x2, (0.095f * height) + y2, (0.49285713f * width) + x2, (0.91f * height) + y2, new float[]{0.0f, 0.24289773f, 0.48579547f, 0.74289775f, 1.0f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.31107953f, 0.62215906f, 0.8110795f, 1.0f}, new Color[]{this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color18, decodeColor(this.color18, this.color19, 0.5f), this.color19, decodeColor(this.color19, this.color19, 0.5f), this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.50714284f * width) + x2, (0.095f * height) + y2, (0.49285713f * width) + x2, (0.91f * height) + y2, new float[]{0.0f, 0.24289773f, 0.48579547f, 0.74289775f, 1.0f}, new Color[]{this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27, decodeColor(this.color27, this.color27, 0.5f), this.color27, decodeColor(this.color27, this.color28, 0.5f), this.color28});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.50714284f * width) + x2, (0.095f * height) + y2, (0.49285713f * width) + x2, (0.91f * height) + y2, new float[]{0.0f, 0.24289773f, 0.48579547f, 0.74289775f, 1.0f}, new Color[]{this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30, decodeColor(this.color30, this.color31, 0.5f), this.color31});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.24868421f * width) + x2, (0.0014705883f * height) + y2, (0.24868421f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color32, decodeColor(this.color32, this.color33, 0.5f), this.color33});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25441176f * width) + x2, (1.0016667f * height) + y2, new float[]{0.0f, 0.26988637f, 0.53977275f, 0.5951705f, 0.6505682f, 0.8252841f, 1.0f}, new Color[]{this.color34, decodeColor(this.color34, this.color35, 0.5f), this.color35, decodeColor(this.color35, this.color36, 0.5f), this.color36, decodeColor(this.color36, this.color15, 0.5f), this.color15});
    }

    private Paint decodeGradient13(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.50714284f * width) + x2, (0.095f * height) + y2, (0.49285713f * width) + x2, (0.91f * height) + y2, new float[]{0.0f, 0.24289773f, 0.48579547f, 0.74289775f, 1.0f}, new Color[]{this.color37, decodeColor(this.color37, this.color38, 0.5f), this.color38, decodeColor(this.color38, this.color39, 0.5f), this.color39});
    }
}
