package java.awt;

import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/* loaded from: rt.jar:java/awt/Graphics2D.class */
public abstract class Graphics2D extends Graphics {
    public abstract void draw(Shape shape);

    public abstract boolean drawImage(Image image, AffineTransform affineTransform, ImageObserver imageObserver);

    public abstract void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3);

    public abstract void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform);

    public abstract void drawRenderableImage(RenderableImage renderableImage, AffineTransform affineTransform);

    @Override // java.awt.Graphics
    public abstract void drawString(String str, int i2, int i3);

    public abstract void drawString(String str, float f2, float f3);

    @Override // java.awt.Graphics
    public abstract void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3);

    public abstract void drawString(AttributedCharacterIterator attributedCharacterIterator, float f2, float f3);

    public abstract void drawGlyphVector(GlyphVector glyphVector, float f2, float f3);

    public abstract void fill(Shape shape);

    public abstract boolean hit(Rectangle rectangle, Shape shape, boolean z2);

    public abstract GraphicsConfiguration getDeviceConfiguration();

    public abstract void setComposite(Composite composite);

    public abstract void setPaint(Paint paint);

    public abstract void setStroke(Stroke stroke);

    public abstract void setRenderingHint(RenderingHints.Key key, Object obj);

    public abstract Object getRenderingHint(RenderingHints.Key key);

    public abstract void setRenderingHints(Map<?, ?> map);

    public abstract void addRenderingHints(Map<?, ?> map);

    public abstract RenderingHints getRenderingHints();

    @Override // java.awt.Graphics
    public abstract void translate(int i2, int i3);

    public abstract void translate(double d2, double d3);

    public abstract void rotate(double d2);

    public abstract void rotate(double d2, double d3, double d4);

    public abstract void scale(double d2, double d3);

    public abstract void shear(double d2, double d3);

    public abstract void transform(AffineTransform affineTransform);

    public abstract void setTransform(AffineTransform affineTransform);

    public abstract AffineTransform getTransform();

    public abstract Paint getPaint();

    public abstract Composite getComposite();

    public abstract void setBackground(Color color);

    public abstract Color getBackground();

    public abstract Stroke getStroke();

    public abstract void clip(Shape shape);

    public abstract FontRenderContext getFontRenderContext();

    protected Graphics2D() {
    }

    @Override // java.awt.Graphics
    public void draw3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        Paint paint = getPaint();
        Color color = getColor();
        Color colorBrighter = color.brighter();
        Color colorDarker = color.darker();
        setColor(z2 ? colorBrighter : colorDarker);
        fillRect(i2, i3, 1, i5 + 1);
        fillRect(i2 + 1, i3, i4 - 1, 1);
        setColor(z2 ? colorDarker : colorBrighter);
        fillRect(i2 + 1, i3 + i5, i4, 1);
        fillRect(i2 + i4, i3, 1, i5);
        setPaint(paint);
    }

    @Override // java.awt.Graphics
    public void fill3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        Paint paint = getPaint();
        Color color = getColor();
        Color colorBrighter = color.brighter();
        Color colorDarker = color.darker();
        if (!z2) {
            setColor(colorDarker);
        } else if (paint != color) {
            setColor(color);
        }
        fillRect(i2 + 1, i3 + 1, i4 - 2, i5 - 2);
        setColor(z2 ? colorBrighter : colorDarker);
        fillRect(i2, i3, 1, i5);
        fillRect(i2 + 1, i3, i4 - 2, 1);
        setColor(z2 ? colorDarker : colorBrighter);
        fillRect(i2 + 1, (i3 + i5) - 1, i4 - 1, 1);
        fillRect((i2 + i4) - 1, i3, 1, i5 - 1);
        setPaint(paint);
    }
}
