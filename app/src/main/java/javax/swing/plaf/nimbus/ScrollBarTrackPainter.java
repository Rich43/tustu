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

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ScrollBarTrackPainter.class */
final class ScrollBarTrackPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.10016362f, 0.011764705f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.100476064f, 0.035294116f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10606203f, 0.13333333f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, 0.24705881f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.02222228f, -0.06465475f, -0.31764707f, 0);
    private Color color6 = decodeColor("nimbusBlueGrey", 0.0f, -0.06766917f, -0.19607842f, 0);
    private Color color7 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.0655825f, -0.04705882f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", 0.0138888955f, -0.071117446f, 0.05098039f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    private Color color10 = decodeColor("nimbusBlueGrey", 0.0f, -0.05967886f, -0.5137255f, 0);
    private Color color11 = decodeColor("nimbusBlueGrey", 0.0f, -0.05967886f, -0.5137255f, -255);
    private Color color12 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.07826825f, -0.5019608f, -255);
    private Color color13 = decodeColor("nimbusBlueGrey", -0.015872955f, -0.06731644f, -0.109803915f, 0);
    private Color color14 = decodeColor("nimbusBlueGrey", 0.0f, -0.06924191f, 0.109803915f, 0);
    private Color color15 = decodeColor("nimbusBlueGrey", -0.015872955f, -0.06861015f, -0.09019607f, 0);
    private Color color16 = decodeColor("nimbusBlueGrey", 0.0f, -0.06766917f, 0.07843137f, 0);
    private Object[] componentColors;

    public ScrollBarTrackPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
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
        this.rect = decodeRect1();
        graphics2D.setPaint(decodeGradient1(this.rect));
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(decodeGradient2(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath1();
        graphics2D.setPaint(decodeGradient3(this.path));
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
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(3.0f) - decodeY(0.0f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.7f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.2f));
        this.path.curveTo(decodeAnchorX(0.0f, 0.0f), decodeAnchorY(1.2f, 0.0f), decodeAnchorX(0.3f, -1.0f), decodeAnchorY(2.2f, -1.0f), decodeX(0.3f), decodeY(2.2f));
        this.path.curveTo(decodeAnchorX(0.3f, 1.0f), decodeAnchorY(2.2f, 1.0f), decodeAnchorX(0.6785714f, 0.0f), decodeAnchorY(2.8f, 0.0f), decodeX(0.6785714f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.7f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(3.0f), decodeY(0.0f));
        this.path.lineTo(decodeX(2.2222223f), decodeY(0.0f));
        this.path.lineTo(decodeX(2.2222223f), decodeY(2.8f));
        this.path.curveTo(decodeAnchorX(2.2222223f, 0.0f), decodeAnchorY(2.8f, 0.0f), decodeAnchorX(2.6746032f, -1.0f), decodeAnchorY(2.1857142f, 1.0f), decodeX(2.6746032f), decodeY(2.1857142f));
        this.path.curveTo(decodeAnchorX(2.6746032f, 1.0f), decodeAnchorY(2.1857142f, -1.0f), decodeAnchorX(3.0f, 0.0f), decodeAnchorY(1.2f, 0.0f), decodeX(3.0f), decodeY(1.2f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(0.11428572f), decodeY(1.3714286f));
        this.path.curveTo(decodeAnchorX(0.11428572f, 0.78571427f), decodeAnchorY(1.3714286f, -0.5714286f), decodeAnchorX(0.4642857f, -1.3571428f), decodeAnchorY(2.0714285f, -1.5714285f), decodeX(0.4642857f), decodeY(2.0714285f));
        this.path.curveTo(decodeAnchorX(0.4642857f, 1.3571428f), decodeAnchorY(2.0714285f, 1.5714285f), decodeAnchorX(0.8714286f, 0.21428572f), decodeAnchorY(2.7285714f, -1.0f), decodeX(0.8714286f), decodeY(2.7285714f));
        this.path.curveTo(decodeAnchorX(0.8714286f, -0.21428572f), decodeAnchorY(2.7285714f, 1.0f), decodeAnchorX(0.35714287f, 1.5f), decodeAnchorY(2.3142858f, 1.6428572f), decodeX(0.35714287f), decodeY(2.3142858f));
        this.path.curveTo(decodeAnchorX(0.35714287f, -1.5f), decodeAnchorY(2.3142858f, -1.6428572f), decodeAnchorX(0.11428572f, -0.78571427f), decodeAnchorY(1.3714286f, 0.5714286f), decodeX(0.11428572f), decodeY(1.3714286f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(2.1111112f), decodeY(2.7f));
        this.path.curveTo(decodeAnchorX(2.1111112f, 0.42857143f), decodeAnchorY(2.7f, 0.64285713f), decodeAnchorX(2.6269841f, -1.5714285f), decodeAnchorY(2.2f, 1.6428572f), decodeX(2.6269841f), decodeY(2.2f));
        this.path.curveTo(decodeAnchorX(2.6269841f, 1.5714285f), decodeAnchorY(2.2f, -1.6428572f), decodeAnchorX(2.84127f, 0.71428573f), decodeAnchorY(1.3857143f, 0.64285713f), decodeX(2.84127f), decodeY(1.3857143f));
        this.path.curveTo(decodeAnchorX(2.84127f, -0.71428573f), decodeAnchorY(1.3857143f, -0.64285713f), decodeAnchorX(2.5238094f, 0.71428573f), decodeAnchorY(2.0571427f, -0.85714287f), decodeX(2.5238094f), decodeY(2.0571427f));
        this.path.curveTo(decodeAnchorX(2.5238094f, -0.71428573f), decodeAnchorY(2.0571427f, 0.85714287f), decodeAnchorX(2.1111112f, -0.42857143f), decodeAnchorY(2.7f, -0.64285713f), decodeX(2.1111112f), decodeY(2.7f));
        this.path.closePath();
        return this.path;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.016129032f, 0.038709678f, 0.061290324f, 0.16091082f, 0.26451612f, 0.4378071f, 0.88387096f}, new Color[]{this.color1, decodeColor(this.color1, this.color2, 0.5f), this.color2, decodeColor(this.color2, this.color3, 0.5f), this.color3, decodeColor(this.color3, this.color4, 0.5f), this.color4});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.030645162f, 0.061290324f, 0.09677419f, 0.13225806f, 0.22096774f, 0.30967742f, 0.47434634f, 0.82258064f}, new Color[]{this.color5, decodeColor(this.color5, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7, decodeColor(this.color7, this.color8, 0.5f), this.color8, decodeColor(this.color8, this.color9, 0.5f), this.color9});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.0f * height) + y2, (0.9285714f * width) + x2, (0.12244898f * height) + y2, new float[]{0.0f, 0.1f, 1.0f}, new Color[]{this.color10, decodeColor(this.color10, this.color11, 0.5f), this.color11});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient(((-0.045918368f) * width) + x2, (0.18336426f * height) + y2, (0.872449f * width) + x2, (0.04050711f * height) + y2, new float[]{0.0f, 0.87096775f, 1.0f}, new Color[]{this.color12, decodeColor(this.color12, this.color10, 0.5f), this.color10});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.12719299f * width) + x2, (0.13157894f * height) + y2, (0.90789473f * width) + x2, (0.877193f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.86458343f * width) + x2, (0.20952381f * height) + y2, (0.020833189f * width) + x2, (0.95238096f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color15, decodeColor(this.color15, this.color16, 0.5f), this.color16});
    }
}
