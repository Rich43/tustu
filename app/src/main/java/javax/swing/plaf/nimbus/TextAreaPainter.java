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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TextAreaPainter.class */
final class TextAreaPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_DISABLED_NOTINSCROLLPANE = 3;
    static final int BACKGROUND_ENABLED_NOTINSCROLLPANE = 4;
    static final int BACKGROUND_SELECTED = 5;
    static final int BORDER_DISABLED_NOTINSCROLLPANE = 6;
    static final int BORDER_FOCUSED_NOTINSCROLLPANE = 7;
    static final int BORDER_ENABLED_NOTINSCROLLPANE = 8;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", -0.015872955f, -0.07995863f, 0.15294117f, 0);
    private Color color2 = decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07856284f, 0.11372548f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07796818f, 0.09803921f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);
    private Color color9 = decodeColor("nimbusLightBackground", 0.6666667f, 0.004901961f, -0.19999999f, 0);
    private Color color10 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10512091f, -0.019607842f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.105344966f, 0.011764705f, 0);
    private Color color12 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Object[] componentColors;

    public TextAreaPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundDisabledAndNotInScrollPane(graphics2D);
                break;
            case 4:
                paintBackgroundEnabledAndNotInScrollPane(graphics2D);
                break;
            case 5:
                paintBackgroundSelected(graphics2D);
                break;
            case 6:
                paintBorderDisabledAndNotInScrollPane(graphics2D);
                break;
            case 7:
                paintBorderFocusedAndNotInScrollPane(graphics2D);
                break;
            case 8:
                paintBorderEnabledAndNotInScrollPane(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected Object[] getExtendedCacheKeys(JComponent jComponent) {
        Object[] objArr = null;
        switch (this.state) {
            case 2:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color2, 0.0f, 0.0f, 0)};
                break;
            case 4:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color2, 0.0f, 0.0f, 0)};
                break;
            case 7:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color9, 0.004901961f, -0.19999999f, 0), getComponentColor(jComponent, "background", this.color2, 0.0f, 0.0f, 0)};
                break;
            case 8:
                objArr = new Object[]{getComponentColor(jComponent, "background", this.color9, 0.004901961f, -0.19999999f, 0), getComponentColor(jComponent, "background", this.color2, 0.0f, 0.0f, 0)};
                break;
        }
        return objArr;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint((Color) this.componentColors[0]);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundDisabledAndNotInScrollPane(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndNotInScrollPane(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint((Color) this.componentColors[0]);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundSelected(Graphics2D graphics2D) {
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.rect);
    }

    private void paintBorderDisabledAndNotInScrollPane(Graphics2D graphics2D) {
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.rect);
        this.rect = decodeRect6();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.rect);
        this.rect = decodeRect7();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.rect);
    }

    private void paintBorderFocusedAndNotInScrollPane(Graphics2D graphics2D) {
        this.rect = decodeRect8();
        graphics2D.setPaint(decodeGradient3(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect9();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect10();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.rect);
        this.rect = decodeRect11();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.rect);
        this.rect = decodeRect12();
        graphics2D.setPaint(this.color11);
        graphics2D.fill(this.rect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color12);
        graphics2D.fill(this.path);
    }

    private void paintBorderEnabledAndNotInScrollPane(Graphics2D graphics2D) {
        this.rect = decodeRect8();
        graphics2D.setPaint(decodeGradient5(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect9();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect10();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.rect);
        this.rect = decodeRect11();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.rect);
        this.rect = decodeRect12();
        graphics2D.setPaint(this.color11);
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(0.4f), decodeX(2.3333333f) - decodeX(0.6666667f), decodeY(1.0f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.6f), decodeX(2.0f) - decodeX(1.0f), decodeY(1.0f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect5() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(1.0f), decodeX(1.0f) - decodeX(0.6666667f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect6() {
        this.rect.setRect(decodeX(0.6666667f), decodeY(2.3333333f), decodeX(2.3333333f) - decodeX(0.6666667f), decodeY(2.0f) - decodeY(2.3333333f));
        return this.rect;
    }

    private Rectangle2D decodeRect7() {
        this.rect.setRect(decodeX(2.0f), decodeY(1.0f), decodeX(2.3333333f) - decodeX(2.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect8() {
        this.rect.setRect(decodeX(0.4f), decodeY(0.4f), decodeX(2.6f) - decodeX(0.4f), decodeY(1.0f) - decodeY(0.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect9() {
        this.rect.setRect(decodeX(0.6f), decodeY(0.6f), decodeX(2.4f) - decodeX(0.6f), decodeY(1.0f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect10() {
        this.rect.setRect(decodeX(0.4f), decodeY(1.0f), decodeX(0.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect11() {
        this.rect.setRect(decodeX(2.4f), decodeY(1.0f), decodeX(2.6f) - decodeX(2.4f), decodeY(2.6f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect12() {
        this.rect.setRect(decodeX(0.6f), decodeY(2.4f), decodeX(2.4f) - decodeX(0.6f), decodeY(2.6f) - decodeY(2.4f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.4f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.4f));
        this.path.curveTo(decodeAnchorX(2.6f, 0.0f), decodeAnchorY(0.4f, 0.0f), decodeAnchorX(2.8800004f, 0.1f), decodeAnchorY(0.4f, 0.0f), decodeX(2.8800004f), decodeY(0.4f));
        this.path.curveTo(decodeAnchorX(2.8800004f, 0.1f), decodeAnchorY(0.4f, 0.0f), decodeAnchorX(2.8800004f, 0.0f), decodeAnchorY(2.8799999f, 0.0f), decodeX(2.8800004f), decodeY(2.8799999f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(2.8799999f));
        this.path.lineTo(decodeX(0.120000005f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(2.8800004f), decodeY(0.120000005f));
        this.path.lineTo(decodeX(2.8800004f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.4f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color5, decodeColor(this.color5, this.color1, 0.5f), this.color1});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (0.1625f * height) + y2, new float[]{0.1f, 0.49999997f, 0.9f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.1f, 0.49999997f, 0.9f}, new Color[]{(Color) this.componentColors[0], decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f), (Color) this.componentColors[1]});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.1f, 0.49999997f, 0.9f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }
}
