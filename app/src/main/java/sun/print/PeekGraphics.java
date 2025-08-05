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
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.awt.print.PrinterGraphics;
import java.awt.print.PrinterJob;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import sun.java2d.Spans;

/* loaded from: rt.jar:sun/print/PeekGraphics.class */
public class PeekGraphics extends Graphics2D implements PrinterGraphics, ImageObserver, Cloneable {
    Graphics2D mGraphics;
    PrinterJob mPrinterJob;
    private Spans mDrawingArea = new Spans();
    private PeekMetrics mPrintMetrics = new PeekMetrics();
    private boolean mAWTDrawingOnly = false;

    public PeekGraphics(Graphics2D graphics2D, PrinterJob printerJob) {
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

    public void setAWTDrawingOnly() {
        this.mAWTDrawingOnly = true;
    }

    public boolean getAWTDrawingOnly() {
        return this.mAWTDrawingOnly;
    }

    public Spans getDrawingArea() {
        return this.mDrawingArea;
    }

    @Override // java.awt.Graphics2D
    public GraphicsConfiguration getDeviceConfiguration() {
        return ((RasterPrinterJob) this.mPrinterJob).getPrinterGraphicsConfig();
    }

    @Override // java.awt.Graphics
    public Graphics create() {
        PeekGraphics peekGraphics = null;
        try {
            peekGraphics = (PeekGraphics) clone();
            peekGraphics.mGraphics = (Graphics2D) this.mGraphics.create();
        } catch (CloneNotSupportedException e2) {
        }
        return peekGraphics;
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
    }

    @Override // java.awt.Graphics
    public void drawLine(int i2, int i3, int i4, int i5) {
        addStrokeShape(new Line2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.draw(this);
    }

    @Override // java.awt.Graphics
    public void fillRect(int i2, int i3, int i4, int i5) {
        addDrawingRect(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.fill(this);
    }

    @Override // java.awt.Graphics
    public void clearRect(int i2, int i3, int i4, int i5) {
        addDrawingRect(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.clear(this);
    }

    @Override // java.awt.Graphics
    public void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        addStrokeShape(new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
        this.mPrintMetrics.draw(this);
    }

    @Override // java.awt.Graphics
    public void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        addDrawingRect(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.fill(this);
    }

    @Override // java.awt.Graphics
    public void drawOval(int i2, int i3, int i4, int i5) {
        addStrokeShape(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.draw(this);
    }

    @Override // java.awt.Graphics
    public void fillOval(int i2, int i3, int i4, int i5) {
        addDrawingRect(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.fill(this);
    }

    @Override // java.awt.Graphics
    public void drawArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        addStrokeShape(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.draw(this);
    }

    @Override // java.awt.Graphics
    public void fillArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        addDrawingRect(new Rectangle2D.Float(i2, i3, i4, i5));
        this.mPrintMetrics.fill(this);
    }

    @Override // java.awt.Graphics
    public void drawPolyline(int[] iArr, int[] iArr2, int i2) {
        if (i2 > 0) {
            int i3 = iArr[0];
            int i4 = iArr2[0];
            for (int i5 = 1; i5 < i2; i5++) {
                drawLine(i3, i4, iArr[i5], iArr2[i5]);
                i3 = iArr[i5];
                i4 = iArr2[i5];
            }
        }
    }

    @Override // java.awt.Graphics
    public void drawPolygon(int[] iArr, int[] iArr2, int i2) {
        if (i2 > 0) {
            drawPolyline(iArr, iArr2, i2);
            drawLine(iArr[i2 - 1], iArr2[i2 - 1], iArr[0], iArr2[0]);
        }
    }

    @Override // java.awt.Graphics
    public void fillPolygon(int[] iArr, int[] iArr2, int i2) {
        if (i2 > 0) {
            int i3 = iArr[0];
            int i4 = iArr2[0];
            int i5 = iArr[0];
            int i6 = iArr2[0];
            for (int i7 = 1; i7 < i2; i7++) {
                if (iArr[i7] < i3) {
                    i3 = iArr[i7];
                } else if (iArr[i7] > i5) {
                    i5 = iArr[i7];
                }
                if (iArr2[i7] < i4) {
                    i4 = iArr2[i7];
                } else if (iArr2[i7] > i6) {
                    i6 = iArr2[i7];
                }
            }
            addDrawingRect(i3, i4, i5 - i3, i6 - i4);
        }
        this.mPrintMetrics.fill(this);
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        drawString(str, i2, i3);
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        drawString(attributedCharacterIterator, i2, i3);
    }

    @Override // java.awt.Graphics2D
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, float f2, float f3) {
        if (attributedCharacterIterator == null) {
            throw new NullPointerException("AttributedCharacterIterator is null");
        }
        new TextLayout(attributedCharacterIterator, getFontRenderContext()).draw(this, f2, f3);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        ImageWaiter imageWaiter = new ImageWaiter(image);
        addDrawingRect(i2, i3, imageWaiter.getWidth(), imageWaiter.getHeight());
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        addDrawingRect(i2, i3, i4, i5);
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, i2, i3, i4, i5, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        ImageWaiter imageWaiter = new ImageWaiter(image);
        addDrawingRect(i2, i3, imageWaiter.getWidth(), imageWaiter.getHeight());
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, i2, i3, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        addDrawingRect(i2, i3, i4, i5);
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, i2, i3, i4, i5, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        addDrawingRect(i2, i3, i4 - i2, i5 - i3);
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        addDrawingRect(i2, i3, i4 - i2, i5 - i3);
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
    }

    @Override // java.awt.Graphics2D
    public void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform) {
        if (renderedImage == null) {
            return;
        }
        this.mPrintMetrics.drawImage(this, renderedImage);
        this.mDrawingArea.addInfinite();
    }

    @Override // java.awt.Graphics2D
    public void drawRenderableImage(RenderableImage renderableImage, AffineTransform affineTransform) {
        if (renderableImage == null) {
            return;
        }
        this.mPrintMetrics.drawImage(this, renderableImage);
        this.mDrawingArea.addInfinite();
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
        addStrokeShape(shape);
        this.mPrintMetrics.draw(this);
    }

    @Override // java.awt.Graphics2D
    public boolean drawImage(Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        this.mDrawingArea.addInfinite();
        this.mPrintMetrics.drawImage(this, image);
        return this.mGraphics.drawImage(image, affineTransform, imageObserver);
    }

    @Override // java.awt.Graphics2D
    public void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        if (bufferedImage == null) {
            return;
        }
        this.mPrintMetrics.drawImage((Graphics2D) this, (RenderedImage) bufferedImage);
        this.mDrawingArea.addInfinite();
    }

    @Override // java.awt.Graphics2D
    public void drawString(String str, float f2, float f3) {
        if (str.length() == 0) {
            return;
        }
        addDrawingRect(getFont().getStringBounds(str, getFontRenderContext()), f2, f3);
        this.mPrintMetrics.drawText(this);
    }

    @Override // java.awt.Graphics2D
    public void drawGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        addDrawingRect(glyphVector.getLogicalBounds(), f2, f3);
        this.mPrintMetrics.drawText(this);
    }

    @Override // java.awt.Graphics2D
    public void fill(Shape shape) {
        addDrawingRect(shape.getBounds());
        this.mPrintMetrics.fill(this);
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

    public boolean hitsDrawingArea(Rectangle rectangle) {
        return this.mDrawingArea.intersects((float) rectangle.getMinY(), (float) rectangle.getMaxY());
    }

    public PeekMetrics getMetrics() {
        return this.mPrintMetrics;
    }

    private void addDrawingRect(Rectangle2D rectangle2D, float f2, float f3) {
        addDrawingRect((float) (rectangle2D.getX() + f2), (float) (rectangle2D.getY() + f3), (float) rectangle2D.getWidth(), (float) rectangle2D.getHeight());
    }

    private void addDrawingRect(float f2, float f3, float f4, float f5) {
        addDrawingRect(new Rectangle2D.Float(f2, f3, f4, f5));
    }

    private void addDrawingRect(Rectangle2D rectangle2D) {
        Rectangle2D bounds2D = getTransform().createTransformedShape(rectangle2D).getBounds2D();
        this.mDrawingArea.add((float) bounds2D.getMinY(), (float) bounds2D.getMaxY());
    }

    private void addStrokeShape(Shape shape) {
        addDrawingRect(getStroke().createStrokedShape(shape).getBounds2D());
    }

    @Override // java.awt.image.ImageObserver
    public synchronized boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        boolean z2 = false;
        if ((i2 & 3) != 0) {
            z2 = true;
            notify();
        }
        return z2;
    }

    private synchronized int getImageWidth(Image image) {
        while (image.getWidth(this) == -1) {
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
        return image.getWidth(this);
    }

    private synchronized int getImageHeight(Image image) {
        while (image.getHeight(this) == -1) {
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
        return image.getHeight(this);
    }

    /* loaded from: rt.jar:sun/print/PeekGraphics$ImageWaiter.class */
    protected class ImageWaiter implements ImageObserver {
        private int mWidth;
        private int mHeight;
        private boolean badImage = false;

        ImageWaiter(Image image) {
            waitForDimensions(image);
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        private synchronized void waitForDimensions(Image image) {
            this.mHeight = image.getHeight(this);
            this.mWidth = image.getWidth(this);
            while (!this.badImage && (this.mWidth < 0 || this.mHeight < 0)) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                }
                this.mHeight = image.getHeight(this);
                this.mWidth = image.getWidth(this);
            }
            if (this.badImage) {
                this.mHeight = 0;
                this.mWidth = 0;
            }
        }

        @Override // java.awt.image.ImageObserver
        public synchronized boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
            boolean z2 = (i2 & 194) != 0;
            this.badImage = (i2 & 192) != 0;
            return z2;
        }
    }
}
