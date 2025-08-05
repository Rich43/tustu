package sun.print;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.awt.print.PrinterGraphics;
import java.awt.print.PrinterJob;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/* loaded from: rt.jar:sun/print/ProxyGraphics2D.class */
public class ProxyGraphics2D extends Graphics2D implements PrinterGraphics {
    Graphics2D mGraphics;
    PrinterJob mPrinterJob;

    public ProxyGraphics2D(Graphics2D graphics2D, PrinterJob printerJob) {
        this.mGraphics = graphics2D;
        this.mPrinterJob = printerJob;
    }

    public Graphics2D getDelegate() {
        return this.mGraphics;
    }

    public void setDelegate(Graphics2D graphics2D) {
        this.mGraphics = graphics2D;
    }

    @Override // java.awt.print.PrinterGraphics
    public PrinterJob getPrinterJob() {
        return this.mPrinterJob;
    }

    @Override // java.awt.Graphics2D
    public GraphicsConfiguration getDeviceConfiguration() {
        return ((RasterPrinterJob) this.mPrinterJob).getPrinterGraphicsConfig();
    }

    @Override // java.awt.Graphics
    public Graphics create() {
        return new ProxyGraphics2D((Graphics2D) this.mGraphics.create(), this.mPrinterJob);
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void translate(int i2, int i3) {
        this.mGraphics.translate(i2, i3);
    }

    @Override // java.awt.Graphics2D
    public void translate(double d2, double d3) {
        this.mGraphics.translate(d2, d3);
    }

    @Override // java.awt.Graphics2D
    public void rotate(double d2) {
        this.mGraphics.rotate(d2);
    }

    @Override // java.awt.Graphics2D
    public void rotate(double d2, double d3, double d4) {
        this.mGraphics.rotate(d2, d3, d4);
    }

    @Override // java.awt.Graphics2D
    public void scale(double d2, double d3) {
        this.mGraphics.scale(d2, d3);
    }

    @Override // java.awt.Graphics2D
    public void shear(double d2, double d3) {
        this.mGraphics.shear(d2, d3);
    }

    @Override // java.awt.Graphics
    public Color getColor() {
        return this.mGraphics.getColor();
    }

    @Override // java.awt.Graphics
    public void setColor(Color color) {
        this.mGraphics.setColor(color);
    }

    @Override // java.awt.Graphics
    public void setPaintMode() {
        this.mGraphics.setPaintMode();
    }

    @Override // java.awt.Graphics
    public void setXORMode(Color color) {
        this.mGraphics.setXORMode(color);
    }

    @Override // java.awt.Graphics
    public Font getFont() {
        return this.mGraphics.getFont();
    }

    @Override // java.awt.Graphics
    public void setFont(Font font) {
        this.mGraphics.setFont(font);
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics(Font font) {
        return this.mGraphics.getFontMetrics(font);
    }

    @Override // java.awt.Graphics2D
    public FontRenderContext getFontRenderContext() {
        return this.mGraphics.getFontRenderContext();
    }

    @Override // java.awt.Graphics
    public Rectangle getClipBounds() {
        return this.mGraphics.getClipBounds();
    }

    @Override // java.awt.Graphics
    public void clipRect(int i2, int i3, int i4, int i5) {
        this.mGraphics.clipRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void setClip(int i2, int i3, int i4, int i5) {
        this.mGraphics.setClip(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public Shape getClip() {
        return this.mGraphics.getClip();
    }

    @Override // java.awt.Graphics
    public void setClip(Shape shape) {
        this.mGraphics.setClip(shape);
    }

    @Override // java.awt.Graphics
    public void copyArea(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.mGraphics.copyArea(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawLine(int i2, int i3, int i4, int i5) {
        this.mGraphics.drawLine(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void fillRect(int i2, int i3, int i4, int i5) {
        this.mGraphics.fillRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void clearRect(int i2, int i3, int i4, int i5) {
        this.mGraphics.clearRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.mGraphics.drawRoundRect(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.mGraphics.fillRoundRect(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawOval(int i2, int i3, int i4, int i5) {
        this.mGraphics.drawOval(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void fillOval(int i2, int i3, int i4, int i5) {
        this.mGraphics.fillOval(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.mGraphics.drawArc(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void fillArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.mGraphics.fillArc(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawPolyline(int[] iArr, int[] iArr2, int i2) {
        this.mGraphics.drawPolyline(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void drawPolygon(int[] iArr, int[] iArr2, int i2) {
        this.mGraphics.drawPolygon(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void fillPolygon(int[] iArr, int[] iArr2, int i2) {
        this.mGraphics.fillPolygon(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        this.mGraphics.drawString(str, i2, i3);
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        this.mGraphics.drawString(attributedCharacterIterator, i2, i3);
    }

    @Override // java.awt.Graphics2D
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, float f2, float f3) {
        this.mGraphics.drawString(attributedCharacterIterator, f2, f3);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return this.mGraphics.drawImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver) {
        return this.mGraphics.drawImage(image, i2, i3, i4, i5, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        boolean zDrawImage;
        if (image == null) {
            return true;
        }
        if (needToCopyBgColorImage(image)) {
            zDrawImage = this.mGraphics.drawImage(getBufferedImageCopy(image, color), i2, i3, (ImageObserver) null);
        } else {
            zDrawImage = this.mGraphics.drawImage(image, i2, i3, color, imageObserver);
        }
        return zDrawImage;
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        boolean zDrawImage;
        if (image == null) {
            return true;
        }
        if (needToCopyBgColorImage(image)) {
            zDrawImage = this.mGraphics.drawImage(getBufferedImageCopy(image, color), i2, i3, i4, i5, null);
        } else {
            zDrawImage = this.mGraphics.drawImage(image, i2, i3, i4, i5, color, imageObserver);
        }
        return zDrawImage;
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver) {
        return this.mGraphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        boolean zDrawImage;
        if (image == null) {
            return true;
        }
        if (needToCopyBgColorImage(image)) {
            zDrawImage = this.mGraphics.drawImage(getBufferedImageCopy(image, color), i2, i3, i4, i5, i7, i7, i8, i9, null);
        } else {
            zDrawImage = this.mGraphics.drawImage(image, i2, i3, i4, i5, i7, i7, i8, i9, color, imageObserver);
        }
        return zDrawImage;
    }

    private boolean needToCopyBgColorImage(Image image) {
        return (getTransform().getType() & 48) != 0;
    }

    private BufferedImage getBufferedImageCopy(Image image, Color color) {
        BufferedImage bufferedImage;
        int type;
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width > 0 && height > 0) {
            if (image instanceof BufferedImage) {
                type = ((BufferedImage) image).getType();
            } else {
                type = 2;
            }
            bufferedImage = new BufferedImage(width, height, type);
            Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
            graphics2DCreateGraphics.drawImage(image, 0, 0, color, null);
            graphics2DCreateGraphics.dispose();
        } else {
            bufferedImage = null;
        }
        return bufferedImage;
    }

    @Override // java.awt.Graphics2D
    public void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform) {
        this.mGraphics.drawRenderedImage(renderedImage, affineTransform);
    }

    @Override // java.awt.Graphics2D
    public void drawRenderableImage(RenderableImage renderableImage, AffineTransform affineTransform) {
        AffineTransform affineTransform2;
        if (renderableImage == null) {
            return;
        }
        AffineTransform transform = getTransform();
        AffineTransform affineTransform3 = new AffineTransform(affineTransform);
        affineTransform3.concatenate(transform);
        RenderContext renderContext = new RenderContext(affineTransform3);
        try {
            affineTransform2 = transform.createInverse();
        } catch (NoninvertibleTransformException e2) {
            renderContext = new RenderContext(transform);
            affineTransform2 = new AffineTransform();
        }
        drawRenderedImage(renderableImage.createRendering(renderContext), affineTransform2);
    }

    @Override // java.awt.Graphics
    public void dispose() {
        this.mGraphics.dispose();
    }

    @Override // java.awt.Graphics
    public void finalize() {
    }

    @Override // java.awt.Graphics2D
    public void draw(Shape shape) {
        this.mGraphics.draw(shape);
    }

    @Override // java.awt.Graphics2D
    public boolean drawImage(Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        return this.mGraphics.drawImage(image, affineTransform, imageObserver);
    }

    @Override // java.awt.Graphics2D
    public void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        this.mGraphics.drawImage(bufferedImage, bufferedImageOp, i2, i3);
    }

    @Override // java.awt.Graphics2D
    public void drawString(String str, float f2, float f3) {
        this.mGraphics.drawString(str, f2, f3);
    }

    @Override // java.awt.Graphics2D
    public void drawGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        this.mGraphics.drawGlyphVector(glyphVector, f2, f3);
    }

    @Override // java.awt.Graphics2D
    public void fill(Shape shape) {
        this.mGraphics.fill(shape);
    }

    @Override // java.awt.Graphics2D
    public boolean hit(Rectangle rectangle, Shape shape, boolean z2) {
        return this.mGraphics.hit(rectangle, shape, z2);
    }

    @Override // java.awt.Graphics2D
    public void setComposite(Composite composite) {
        this.mGraphics.setComposite(composite);
    }

    @Override // java.awt.Graphics2D
    public void setPaint(Paint paint) {
        this.mGraphics.setPaint(paint);
    }

    @Override // java.awt.Graphics2D
    public void setStroke(Stroke stroke) {
        this.mGraphics.setStroke(stroke);
    }

    @Override // java.awt.Graphics2D
    public void setRenderingHint(RenderingHints.Key key, Object obj) {
        this.mGraphics.setRenderingHint(key, obj);
    }

    @Override // java.awt.Graphics2D
    public Object getRenderingHint(RenderingHints.Key key) {
        return this.mGraphics.getRenderingHint(key);
    }

    @Override // java.awt.Graphics2D
    public void setRenderingHints(Map<?, ?> map) {
        this.mGraphics.setRenderingHints(map);
    }

    @Override // java.awt.Graphics2D
    public void addRenderingHints(Map<?, ?> map) {
        this.mGraphics.addRenderingHints(map);
    }

    @Override // java.awt.Graphics2D
    public RenderingHints getRenderingHints() {
        return this.mGraphics.getRenderingHints();
    }

    @Override // java.awt.Graphics2D
    public void transform(AffineTransform affineTransform) {
        this.mGraphics.transform(affineTransform);
    }

    @Override // java.awt.Graphics2D
    public void setTransform(AffineTransform affineTransform) {
        this.mGraphics.setTransform(affineTransform);
    }

    @Override // java.awt.Graphics2D
    public AffineTransform getTransform() {
        return this.mGraphics.getTransform();
    }

    @Override // java.awt.Graphics2D
    public Paint getPaint() {
        return this.mGraphics.getPaint();
    }

    @Override // java.awt.Graphics2D
    public Composite getComposite() {
        return this.mGraphics.getComposite();
    }

    @Override // java.awt.Graphics2D
    public void setBackground(Color color) {
        this.mGraphics.setBackground(color);
    }

    @Override // java.awt.Graphics2D
    public Color getBackground() {
        return this.mGraphics.getBackground();
    }

    @Override // java.awt.Graphics2D
    public Stroke getStroke() {
        return this.mGraphics.getStroke();
    }

    @Override // java.awt.Graphics2D
    public void clip(Shape shape) {
        this.mGraphics.clip(shape);
    }
}
