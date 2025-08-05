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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SliderTrackPainter.class */
final class SliderTrackPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -245);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.061265234f, 0.05098039f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.01010108f, -0.059835073f, 0.10588235f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.061982628f, 0.062745094f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", -0.00505054f, -0.058639523f, 0.086274505f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, ClassFileConstants.opc_i2b);
    private Color color7 = decodeColor("nimbusBlueGrey", 0.0f, -0.034093194f, -0.12941176f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.01111114f, -0.023821115f, -0.06666666f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", -0.008547008f, -0.03314536f, -0.086274505f, 0);
    private Color color10 = decodeColor("nimbusBlueGrey", 0.004273474f, -0.040256046f, -0.019607842f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", 0.0f, -0.03626889f, 0.04705882f, 0);
    private Object[] componentColors;

    public SliderTrackPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient1(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect3();
        graphics2D.setPaint(decodeGradient2(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.roundRect = decodeRoundRect4();
        graphics2D.setPaint(this.color6);
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect2();
        graphics2D.setPaint(decodeGradient3(this.roundRect));
        graphics2D.fill(this.roundRect);
        this.roundRect = decodeRoundRect5();
        graphics2D.setPaint(decodeGradient4(this.roundRect));
        graphics2D.fill(this.roundRect);
    }

    private RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(decodeX(0.2f), decodeY(1.6f), decodeX(2.8f) - decodeX(0.2f), decodeY(2.8333333f) - decodeY(1.6f), 8.70588207244873d, 8.70588207244873d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(decodeX(0.0f), decodeY(1.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(2.0f) - decodeY(1.0f), 4.941176414489746d, 4.941176414489746d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(decodeX(0.29411763f), decodeY(1.2f), decodeX(2.7058823f) - decodeX(0.29411763f), decodeY(2.0f) - decodeY(1.2f), 4.0d, 4.0d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect4() {
        this.roundRect.setRoundRect(decodeX(0.2f), decodeY(1.6f), decodeX(2.8f) - decodeX(0.2f), decodeY(2.1666667f) - decodeY(1.6f), 8.70588207244873d, 8.70588207244873d);
        return this.roundRect;
    }

    private RoundRectangle2D decodeRoundRect5() {
        this.roundRect.setRoundRect(decodeX(0.28823528f), decodeY(1.2f), decodeX(2.7f) - decodeX(0.28823528f), decodeY(2.0f) - decodeY(1.2f), 4.0d, 4.0d);
        return this.roundRect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.07647059f * height) + y2, (0.25f * width) + x2, (0.9117647f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.13770053f, 0.27540106f, 0.63770056f, 1.0f}, new Color[]{this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color3, 0.5f), this.color3});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.07647059f * height) + y2, (0.25f * width) + x2, (0.9117647f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.25f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.13770053f, 0.27540106f, 0.4906417f, 0.7058824f}, new Color[]{this.color9, decodeColor(this.color9, this.color10, 0.5f), this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }
}
