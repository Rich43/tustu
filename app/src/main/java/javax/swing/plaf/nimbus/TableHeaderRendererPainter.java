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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TableHeaderRendererPainter.class */
final class TableHeaderRendererPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_ENABLED_FOCUSED = 3;
    static final int BACKGROUND_MOUSEOVER = 4;
    static final int BACKGROUND_PRESSED = 5;
    static final int BACKGROUND_ENABLED_SORTED = 6;
    static final int BACKGROUND_ENABLED_FOCUSED_SORTED = 7;
    static final int BACKGROUND_DISABLED_SORTED = 8;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBorder", -0.013888836f, 5.823001E-4f, -0.12941176f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.08625447f, 0.062745094f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", -0.013888836f, -0.028334536f, -0.17254901f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", -0.013888836f, -0.029445238f, -0.16470587f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", -0.02020204f, -0.053531498f, 0.011764705f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10655806f, 0.24313724f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", 0.0f, -0.08455229f, 0.1607843f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.0f, -0.07466974f, 0.23921567f, 0);
    private Color color10 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10658931f, 0.25098038f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", 0.0f, -0.08613607f, 0.21960783f, 0);
    private Color color13 = decodeColor("nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color15 = decodeColor("nimbusBlueGrey", -0.00505054f, -0.05960039f, 0.10196078f, 0);
    private Color color16 = decodeColor("nimbusBlueGrey", 0.0f, -0.017742813f, 0.015686274f, 0);
    private Color color17 = decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
    private Color color18 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.020436227f, 0.12549019f, 0);
    private Color color19 = decodeColor("nimbusBase", -0.023096085f, -0.62376213f, 0.4352941f, 0);
    private Color color20 = decodeColor("nimbusBase", -0.0012707114f, -0.50901747f, 0.31764704f, 0);
    private Color color21 = decodeColor("nimbusBase", -0.002461195f, -0.47139505f, 0.2862745f, 0);
    private Color color22 = decodeColor("nimbusBase", -0.0051222444f, -0.49103343f, 0.372549f, 0);
    private Color color23 = decodeColor("nimbusBase", -8.738637E-4f, -0.49872798f, 0.3098039f, 0);
    private Color color24 = decodeColor("nimbusBase", -2.2029877E-4f, -0.4916465f, 0.37647057f, 0);
    private Object[] componentColors;

    public TableHeaderRendererPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
                paintBackgroundEnabledAndFocused(graphics2D);
                break;
            case 4:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 5:
                paintBackgroundPressed(graphics2D);
                break;
            case 6:
                paintBackgroundEnabledAndSorted(graphics2D);
                break;
            case 7:
                paintBackgroundEnabledAndFocusedAndSorted(graphics2D);
                break;
            case 8:
                paintBackgroundDisabledAndSorted(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundDisabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndFocused(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient3(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundPressed(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient4(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndSorted(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient5(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabledAndFocusedAndSorted(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient6(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath1();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
    }

    private void paintBackgroundDisabledAndSorted(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
        this.rect = decodeRect2();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect3();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.0f), decodeY(2.8f), decodeX(3.0f) - decodeX(0.0f), decodeY(3.0f) - decodeY(2.8f));
        return this.rect;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(2.8f), decodeY(0.0f), decodeX(3.0f) - decodeX(2.8f), decodeY(2.8f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(2.8f) - decodeX(0.0f), decodeY(2.8f) - decodeY(0.0f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.24000001f), decodeY(0.24000001f));
        this.path.lineTo(decodeX(2.7599998f), decodeY(0.24000001f));
        this.path.lineTo(decodeX(2.7599998f), decodeY(2.7599998f));
        this.path.lineTo(decodeX(0.24000001f), decodeY(2.7599998f));
        this.path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
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
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.14441223f, 0.43703705f, 0.59444445f, 0.75185186f, 0.8759259f, 1.0f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.8518518f, 1.0f}, new Color[]{this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.7919203f, 0.88013697f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.8518518f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.08049711f, 0.32534248f, 0.56267816f, 0.7037037f, 0.83986557f, 0.97602737f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20, decodeColor(this.color20, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.8518518f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color23, 0.5f), this.color23, decodeColor(this.color23, this.color21, 0.5f), this.color21, decodeColor(this.color21, this.color24, 0.5f), this.color24});
    }
}
