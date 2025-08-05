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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SplitPaneDividerPainter.class */
final class SplitPaneDividerPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int BACKGROUND_FOCUSED = 2;
    static final int FOREGROUND_ENABLED = 3;
    static final int FOREGROUND_ENABLED_VERTICAL = 4;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.102396235f, 0.21960783f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    private Color color4 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, -0.048026316f, 0.007843137f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.06970999f, 0.21568626f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.06704806f, 0.06666666f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.0f, -0.019617222f, -0.09803921f, 0);
    private Color color10 = decodeColor("nimbusBlueGrey", 0.004273474f, -0.03790062f, -0.043137252f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.106573746f, 0.24705881f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", 0.0f, -0.049301825f, 0.02352941f, 0);
    private Color color13 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", -0.018518567f, -0.06998578f, 0.12549019f, 0);
    private Color color15 = decodeColor("nimbusBlueGrey", 0.0f, -0.050526317f, 0.039215684f, 0);
    private Object[] componentColors;

    public SplitPaneDividerPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundFocused(graphics2D);
                break;
            case 3:
                paintForegroundEnabled(graphics2D);
                break;
            case 4:
                paintForegroundEnabledAndVertical(graphics2D);
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
    }

    private void paintBackgroundFocused(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintForegroundEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintForegroundEnabledAndVertical(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient5(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient6(this.rect));
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(1.05f), decodeY(1.3f), decodeX(1.95f) - decodeX(1.05f), decodeY(1.8f) - decodeY(1.3f), 3.6666667461395264d, 3.6666667461395264d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(1.1f), decodeY(1.4f), decodeX(1.9f) - decodeX(1.1f), decodeY(1.7f) - decodeY(1.4f), 4.0d, 4.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(1.3f), decodeY(1.1428572f), decodeX(1.7f) - decodeX(1.3f), decodeY(1.8214285f) - decodeY(1.1428572f), 4.0d, 4.0d);
        return this.roundRect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(1.4f), decodeY(1.1785715f), decodeX(1.6f) - decodeX(1.4f), decodeY(1.7678571f) - decodeY(1.1785715f));
        return this.rect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.058064517f, 0.08064516f, 0.103225805f, 0.116129026f, 0.12903225f, 0.43387097f, 0.7387097f, 0.77903223f, 0.81935483f, 0.85806453f, 0.8967742f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color2, 0.5f), this.color2, decodeColor(this.color2, this.color1, 0.5f), this.color1});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.058064517f, 0.08064516f, 0.103225805f, 0.1166129f, 0.13f, 0.43f, 0.73f, 0.7746774f, 0.81935483f, 0.85806453f, 0.8967742f}, new Color[]{this.color1, decodeColor(this.color1, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color1, 0.5f), this.color1});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.20645161f, 0.5f, 0.7935484f}, new Color[]{this.color1, decodeColor(this.color1, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.090322584f, 0.2951613f, 0.5f, 0.5822581f, 0.66451615f}, new Color[]{this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.75f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.42096773f, 0.84193546f, 0.8951613f, 0.9483871f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.08064516f, 0.16129032f, 0.5129032f, 0.86451614f, 0.88548386f, 0.90645164f}, new Color[]{this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15});
    }
}
