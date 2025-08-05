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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ScrollBarButtonPainter.class */
final class ScrollBarButtonPainter extends AbstractRegionPainter {
    static final int FOREGROUND_ENABLED = 1;
    static final int FOREGROUND_DISABLED = 2;
    static final int FOREGROUND_MOUSEOVER = 3;
    static final int FOREGROUND_PRESSED = 4;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = new Color(255, 200, 0, 255);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.07763158f, -0.1490196f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.10580933f, 0.086274505f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.102261856f, 0.20392156f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", -0.039682567f, -0.079276316f, 0.13333333f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.07382907f, 0.109803915f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", -0.039682567f, -0.08241387f, 0.23137254f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.08443936f, -0.29411766f, -136);
    private Color color9 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.09876161f, 0.25490195f, -178);
    private Color color10 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.08878718f, -0.5647059f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.080223285f, -0.4862745f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.09525914f, -0.23137254f, 0);
    private Color color13 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -165);
    private Color color14 = decodeColor("nimbusBlueGrey", -0.04444444f, -0.080223285f, -0.09803921f, 0);
    private Color color15 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, 0.10588235f, 0);
    private Color color16 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color17 = decodeColor("nimbusBlueGrey", -0.039682567f, -0.081719734f, 0.20784312f, 0);
    private Color color18 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.07677104f, 0.18431371f, 0);
    private Color color19 = decodeColor("nimbusBlueGrey", -0.04444444f, -0.080223285f, -0.09803921f, -69);
    private Color color20 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.09876161f, 0.25490195f, -39);
    private Color color21 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.0951417f, -0.49019608f, 0);
    private Color color22 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.086996906f, -0.4117647f, 0);
    private Color color23 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.09719298f, -0.15686274f, 0);
    private Color color24 = decodeColor("nimbusBlueGrey", -0.037037015f, -0.043859646f, -0.21568626f, 0);
    private Color color25 = decodeColor("nimbusBlueGrey", -0.06349206f, -0.07309316f, -0.011764705f, 0);
    private Color color26 = decodeColor("nimbusBlueGrey", -0.048611104f, -0.07296763f, 0.09019607f, 0);
    private Color color27 = decodeColor("nimbusBlueGrey", -0.03535354f, -0.05497076f, 0.031372547f, 0);
    private Color color28 = decodeColor("nimbusBlueGrey", -0.034188032f, -0.043168806f, 0.011764705f, 0);
    private Color color29 = decodeColor("nimbusBlueGrey", -0.03535354f, -0.0600676f, 0.109803915f, 0);
    private Color color30 = decodeColor("nimbusBlueGrey", -0.037037015f, -0.043859646f, -0.21568626f, -44);
    private Color color31 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, 0);
    private Object[] componentColors;

    public ScrollBarButtonPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintForegroundEnabled(graphics2D);
                break;
            case 2:
                paintForegroundDisabled(graphics2D);
                break;
            case 3:
                paintForegroundMouseOver(graphics2D);
                break;
            case 4:
                paintForegroundPressed(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintForegroundEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private void paintForegroundDisabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
    }

    private void paintForegroundMouseOver(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private void paintForegroundPressed(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.path);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(this.color31);
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(this.color13);
        graphics2D.fill(this.path);
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.6956522f), decodeY(0.0f));
        this.path.curveTo(decodeAnchorX(1.6956522f, 0.0f), decodeAnchorY(0.0f, 0.0f), decodeAnchorX(1.6956522f, -0.7058824f), decodeAnchorY(1.3076923f, -3.0294118f), decodeX(1.6956522f), decodeY(1.3076923f));
        this.path.curveTo(decodeAnchorX(1.6956522f, 0.7058824f), decodeAnchorY(1.3076923f, 3.0294118f), decodeAnchorX(1.826087f, -2.0f), decodeAnchorY(1.7692308f, -1.9411764f), decodeX(1.826087f), decodeY(1.7692308f));
        this.path.curveTo(decodeAnchorX(1.826087f, 2.0f), decodeAnchorY(1.7692308f, 1.9411764f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(2.0f, 0.0f), decodeX(3.0f), decodeY(2.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.0022625f));
        this.path.lineTo(decodeX(0.9705882f), decodeY(1.0384616f));
        this.path.lineTo(decodeX(1.0409207f), decodeY(1.0791855f));
        this.path.lineTo(decodeX(1.0409207f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.0022625f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(1.4782609f), decodeY(1.2307693f));
        this.path.lineTo(decodeX(1.4782609f), decodeY(1.7692308f));
        this.path.lineTo(decodeX(1.1713555f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.4782609f), decodeY(1.2307693f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(1.6713555f), decodeY(1.0769231f));
        this.path.curveTo(decodeAnchorX(1.6713555f, 0.7352941f), decodeAnchorY(1.0769231f, 0.0f), decodeAnchorX(1.7186701f, -0.9117647f), decodeAnchorY(1.4095023f, -2.2058823f), decodeX(1.7186701f), decodeY(1.4095023f));
        this.path.curveTo(decodeAnchorX(1.7186701f, 0.9117647f), decodeAnchorY(1.4095023f, 2.2058823f), decodeAnchorX(1.8439897f, -2.3529413f), decodeAnchorY(1.7941177f, -1.8529412f), decodeX(1.8439897f), decodeY(1.7941177f));
        this.path.curveTo(decodeAnchorX(1.8439897f, 2.3529413f), decodeAnchorY(1.7941177f, 1.8529412f), decodeAnchorX(2.5f, 0.0f), decodeAnchorY(2.2352943f, 0.0f), decodeX(2.5f), decodeY(2.2352943f));
        this.path.lineTo(decodeX(2.3529415f), decodeY(2.8235292f));
        this.path.curveTo(decodeAnchorX(2.3529415f, 0.0f), decodeAnchorY(2.8235292f, 0.0f), decodeAnchorX(1.8184143f, 1.5588236f), decodeAnchorY(1.8438914f, 1.382353f), decodeX(1.8184143f), decodeY(1.8438914f));
        this.path.curveTo(decodeAnchorX(1.8184143f, -1.5588236f), decodeAnchorY(1.8438914f, -1.382353f), decodeAnchorX(1.6943734f, 0.7941176f), decodeAnchorY(1.4841628f, 2.0f), decodeX(1.6943734f), decodeY(1.4841628f));
        this.path.curveTo(decodeAnchorX(1.6943734f, -0.7941176f), decodeAnchorY(1.4841628f, -2.0f), decodeAnchorX(1.6713555f, -0.7352941f), decodeAnchorY(1.0769231f, 0.0f), decodeX(1.6713555f), decodeY(1.0769231f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.032934133f, 0.065868266f, 0.089820355f, 0.11377245f, 0.23053892f, 0.3473054f, 0.494012f, 0.6407186f, 0.78443116f, 0.92814374f}, new Color[]{this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.5f * height) + y2, (0.5735294f * width) + x2, (0.5f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.925f * width) + x2, (0.9285714f * height) + y2, (0.925f * width) + x2, (0.004201681f * height) + y2, new float[]{0.0f, 0.2964072f, 0.5928144f, 0.79341316f, 0.994012f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.032934133f, 0.065868266f, 0.089820355f, 0.11377245f, 0.23053892f, 0.3473054f, 0.494012f, 0.6407186f, 0.78443116f, 0.92814374f}, new Color[]{this.color14, decodeColor(this.color14, this.color15, 0.5f), this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16, decodeColor(this.color16, this.color17, 0.5f), this.color17, decodeColor(this.color17, this.color18, 0.5f), this.color18, decodeColor(this.color18, this.color16, 0.5f), this.color16});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.5f * height) + y2, (0.5735294f * width) + x2, (0.5f * height) + y2, new float[]{0.19518717f, 0.5975936f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.925f * width) + x2, (0.9285714f * height) + y2, (0.925f * width) + x2, (0.004201681f * height) + y2, new float[]{0.0f, 0.2964072f, 0.5928144f, 0.79341316f, 0.994012f}, new Color[]{this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.032934133f, 0.065868266f, 0.089820355f, 0.11377245f, 0.23053892f, 0.3473054f, 0.494012f, 0.6407186f, 0.78443116f, 0.92814374f}, new Color[]{this.color24, decodeColor(this.color24, this.color25, 0.5f), this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27, decodeColor(this.color27, this.color28, 0.5f), this.color28, decodeColor(this.color28, this.color29, 0.5f), this.color29});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.5f * height) + y2, (0.5735294f * width) + x2, (0.5f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color30, decodeColor(this.color30, this.color9, 0.5f), this.color9});
    }
}
