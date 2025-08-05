package sun.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import sun.awt.image.ByteComponentRaster;

/* loaded from: rt.jar:sun/print/PSPathGraphics.class */
class PSPathGraphics extends PathGraphics {
    private static final int DEFAULT_USER_RES = 72;

    PSPathGraphics(Graphics2D graphics2D, PrinterJob printerJob, Printable printable, PageFormat pageFormat, int i2, boolean z2) {
        super(graphics2D, printerJob, printable, pageFormat, i2, z2);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public Graphics create() {
        return new PSPathGraphics((Graphics2D) getDelegate().create(), getPrinterJob(), getPrintable(), getPageFormat(), getPageIndex(), canDoRedraws());
    }

    @Override // sun.print.PathGraphics
    public void fill(Shape shape, Color color) {
        deviceFill(shape.getPathIterator(new AffineTransform()), color);
    }

    @Override // sun.print.PathGraphics, sun.print.ProxyGraphics2D, java.awt.Graphics2D, java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        drawString(str, i2, i3);
    }

    @Override // sun.print.PathGraphics, sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void drawString(String str, float f2, float f3) {
        drawString(str, f2, f3, getFont(), getFontRenderContext(), 0.0f);
    }

    @Override // sun.print.PathGraphics
    protected boolean canDrawStringToWidth() {
        return true;
    }

    @Override // sun.print.PathGraphics
    protected int platformFontCount(Font font, String str) {
        return ((PSPrinterJob) getPrinterJob()).platformFontCount(font, str);
    }

    @Override // sun.print.PathGraphics
    protected void drawString(String str, float f2, float f3, Font font, FontRenderContext fontRenderContext, float f4) {
        if (str.length() == 0) {
            return;
        }
        if (font.hasLayoutAttributes() && !this.printingGlyphVector) {
            new TextLayout(str, font, fontRenderContext).draw(this, f2, f3);
            return;
        }
        Font font2 = getFont();
        if (!font2.equals(font)) {
            setFont(font);
        } else {
            font2 = null;
        }
        boolean zTextOut = false;
        float translateX = 0.0f;
        float translateY = 0.0f;
        boolean zIsTransformed = getFont().isTransformed();
        if (zIsTransformed) {
            AffineTransform transform = getFont().getTransform();
            if (transform.getType() == 1) {
                translateX = (float) transform.getTranslateX();
                translateY = (float) transform.getTranslateY();
                if (Math.abs(translateX) < 1.0E-5d) {
                    translateX = 0.0f;
                }
                if (Math.abs(translateY) < 1.0E-5d) {
                    translateY = 0.0f;
                }
                zIsTransformed = false;
            }
        }
        boolean z2 = !zIsTransformed;
        if (!PSPrinterJob.shapeTextProp && z2) {
            PSPrinterJob pSPrinterJob = (PSPrinterJob) getPrinterJob();
            if (pSPrinterJob.setFont(getFont())) {
                try {
                    pSPrinterJob.setColor((Color) getPaint());
                    pSPrinterJob.setTransform(getTransform());
                    pSPrinterJob.setClip(getClip());
                    zTextOut = pSPrinterJob.textOut(this, str, f2 + translateX, f3 + translateY, font, fontRenderContext, f4);
                } catch (ClassCastException e2) {
                    if (font2 != null) {
                        setFont(font2);
                    }
                    throw new IllegalArgumentException("Expected a Color instance");
                }
            }
        }
        if (!zTextOut) {
            if (font2 != null) {
                setFont(font2);
                font2 = null;
            }
            super.drawString(str, f2, f3, font, fontRenderContext, f4);
        }
        if (font2 != null) {
            setFont(font2);
        }
    }

    @Override // sun.print.PathGraphics
    protected boolean drawImageToPlatform(Image image, AffineTransform affineTransform, Color color, int i2, int i3, int i4, int i5, boolean z2) {
        BufferedImage bufferedImage = getBufferedImage(image);
        if (bufferedImage == null) {
            return true;
        }
        PSPrinterJob pSPrinterJob = (PSPrinterJob) getPrinterJob();
        AffineTransform transform = getTransform();
        if (affineTransform == null) {
            affineTransform = new AffineTransform();
        }
        transform.concatenate(affineTransform);
        double[] dArr = new double[6];
        transform.getMatrix(dArr);
        Point2D.Float r0 = new Point2D.Float(1.0f, 0.0f);
        Point2D.Float r02 = new Point2D.Float(0.0f, 1.0f);
        transform.deltaTransform(r0, r0);
        transform.deltaTransform(r02, r02);
        Point2D.Float r03 = new Point2D.Float(0.0f, 0.0f);
        double dDistance = r0.distance(r03);
        double dDistance2 = r02.distance(r03);
        double xRes = pSPrinterJob.getXRes();
        double yRes = pSPrinterJob.getYRes();
        double d2 = xRes / 72.0d;
        double d3 = yRes / 72.0d;
        if ((transform.getType() & 48) != 0) {
            if (dDistance > d2) {
                dDistance = d2;
            }
            if (dDistance2 > d3) {
                dDistance2 = d3;
            }
        }
        if (dDistance != 0.0d && dDistance2 != 0.0d) {
            AffineTransform affineTransform2 = new AffineTransform(dArr[0] / dDistance, dArr[1] / dDistance2, dArr[2] / dDistance, dArr[3] / dDistance2, dArr[4] / dDistance, dArr[5] / dDistance2);
            Shape shapeCreateTransformedShape = affineTransform2.createTransformedShape(new Rectangle2D.Float(i2, i3, i4, i5));
            Rectangle2D bounds2D = shapeCreateTransformedShape.getBounds2D();
            bounds2D.setRect(bounds2D.getX(), bounds2D.getY(), bounds2D.getWidth() + 0.001d, bounds2D.getHeight() + 0.001d);
            int width = (int) bounds2D.getWidth();
            int height = (int) bounds2D.getHeight();
            if (width > 0 && height > 0) {
                boolean z3 = true;
                if (!z2 && hasTransparentPixels(bufferedImage)) {
                    z3 = false;
                    if (isBitmaskTransparency(bufferedImage)) {
                        if (color == null) {
                            if (drawBitmaskImage(bufferedImage, affineTransform, color, i2, i3, i4, i5)) {
                                return true;
                            }
                        } else if (color.getTransparency() == 1) {
                            z3 = true;
                        }
                    }
                    if (!canDoRedraws()) {
                        z3 = true;
                    }
                } else {
                    color = null;
                }
                if ((i2 + i4 > bufferedImage.getWidth(null) || i3 + i5 > bufferedImage.getHeight(null)) && canDoRedraws()) {
                    z3 = false;
                }
                if (!z3) {
                    transform.getMatrix(dArr);
                    new AffineTransform(dArr[0] / d2, dArr[1] / d3, dArr[2] / d2, dArr[3] / d3, dArr[4] / d2, dArr[5] / d3);
                    Rectangle2D bounds2D2 = transform.createTransformedShape(new Rectangle2D.Float(i2, i3, i4, i5)).getBounds2D();
                    bounds2D2.setRect(bounds2D2.getX(), bounds2D2.getY(), bounds2D2.getWidth() + 0.001d, bounds2D2.getHeight() + 0.001d);
                    int width2 = (int) bounds2D2.getWidth();
                    int height2 = (int) bounds2D2.getHeight();
                    double d4 = xRes < yRes ? xRes : yRes;
                    int i6 = (int) d4;
                    double d5 = 1.0d;
                    double d6 = width2 / width;
                    double d7 = height2 / height;
                    int i7 = (int) (i6 / (d6 > d7 ? d7 : d6));
                    if (i7 < 72) {
                        i7 = 72;
                    }
                    for (int i8 = width2 * height2 * 3; i8 > 8388608 && i6 > i7; i8 /= 4) {
                        d5 *= 2.0d;
                        i6 /= 2;
                    }
                    if (i6 < i7) {
                        d5 = d4 / i7;
                    }
                    bounds2D2.setRect(bounds2D2.getX() / d5, bounds2D2.getY() / d5, bounds2D2.getWidth() / d5, bounds2D2.getHeight() / d5);
                    pSPrinterJob.saveState(getTransform(), getClip(), bounds2D2, d5, d5);
                    return true;
                }
                BufferedImage bufferedImage2 = new BufferedImage((int) bounds2D.getWidth(), (int) bounds2D.getHeight(), 5);
                Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
                graphics2DCreateGraphics.clipRect(0, 0, bufferedImage2.getWidth(), bufferedImage2.getHeight());
                graphics2DCreateGraphics.translate(-bounds2D.getX(), -bounds2D.getY());
                graphics2DCreateGraphics.transform(affineTransform2);
                if (color == null) {
                    color = Color.white;
                }
                graphics2DCreateGraphics.drawImage(bufferedImage, i2, i3, i2 + i4, i3 + i5, i2, i3, i2 + i4, i3 + i5, color, null);
                Shape clip = getClip();
                Shape shapeCreateTransformedShape2 = getTransform().createTransformedShape(clip);
                Area area = new Area(AffineTransform.getScaleInstance(dDistance, dDistance2).createTransformedShape(shapeCreateTransformedShape));
                area.intersect(new Area(shapeCreateTransformedShape2));
                pSPrinterJob.setClip(area);
                Rectangle2D.Float r04 = new Rectangle2D.Float((float) (bounds2D.getX() * dDistance), (float) (bounds2D.getY() * dDistance2), (float) (bounds2D.getWidth() * dDistance), (float) (bounds2D.getHeight() * dDistance2));
                pSPrinterJob.drawImageBGR(((ByteComponentRaster) bufferedImage2.getRaster()).getDataStorage(), r04.f12404x, r04.f12405y, (float) Math.rint(r04.width + 0.5d), (float) Math.rint(r04.height + 0.5d), 0.0f, 0.0f, bufferedImage2.getWidth(), bufferedImage2.getHeight(), bufferedImage2.getWidth(), bufferedImage2.getHeight());
                pSPrinterJob.setClip(getTransform().createTransformedShape(clip));
                graphics2DCreateGraphics.dispose();
                return true;
            }
            return true;
        }
        return true;
    }

    @Override // sun.print.PathGraphics
    public void redrawRegion(Rectangle2D rectangle2D, double d2, double d3, Shape shape, AffineTransform affineTransform) throws PrinterException {
        PSPrinterJob pSPrinterJob = (PSPrinterJob) getPrinterJob();
        Printable printable = getPrintable();
        PageFormat pageFormat = getPageFormat();
        int pageIndex = getPageIndex();
        BufferedImage bufferedImage = new BufferedImage((int) rectangle2D.getWidth(), (int) rectangle2D.getHeight(), 5);
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        ProxyGraphics2D proxyGraphics2D = new ProxyGraphics2D(graphics2DCreateGraphics, pSPrinterJob);
        proxyGraphics2D.setColor(Color.white);
        proxyGraphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        proxyGraphics2D.clipRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        proxyGraphics2D.translate(-rectangle2D.getX(), -rectangle2D.getY());
        proxyGraphics2D.scale(((float) (pSPrinterJob.getXRes() / d2)) / 72.0f, ((float) (pSPrinterJob.getYRes() / d3)) / 72.0f);
        proxyGraphics2D.translate(((-pSPrinterJob.getPhysicalPrintableX(pageFormat.getPaper())) / pSPrinterJob.getXRes()) * 72.0d, ((-pSPrinterJob.getPhysicalPrintableY(pageFormat.getPaper())) / pSPrinterJob.getYRes()) * 72.0d);
        proxyGraphics2D.transform(new AffineTransform(getPageFormat().getMatrix()));
        proxyGraphics2D.setPaint(Color.black);
        printable.print(proxyGraphics2D, pageFormat, pageIndex);
        graphics2DCreateGraphics.dispose();
        pSPrinterJob.setClip(affineTransform.createTransformedShape(shape));
        Rectangle2D.Float r0 = new Rectangle2D.Float((float) (rectangle2D.getX() * d2), (float) (rectangle2D.getY() * d3), (float) (rectangle2D.getWidth() * d2), (float) (rectangle2D.getHeight() * d3));
        pSPrinterJob.drawImageBGR(((ByteComponentRaster) bufferedImage.getRaster()).getDataStorage(), r0.f12404x, r0.f12405y, r0.width, r0.height, 0.0f, 0.0f, bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override // sun.print.PathGraphics
    protected void deviceFill(PathIterator pathIterator, Color color) {
        ((PSPrinterJob) getPrinterJob()).deviceFill(pathIterator, color, getTransform(), getClip());
    }

    @Override // sun.print.PathGraphics
    protected void deviceFrameRect(int i2, int i3, int i4, int i5, Color color) {
        draw(new Rectangle2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.print.PathGraphics
    protected void deviceDrawLine(int i2, int i3, int i4, int i5, Color color) {
        draw(new Line2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.print.PathGraphics
    protected void deviceFillRect(int i2, int i3, int i4, int i5, Color color) {
        fill(new Rectangle2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.print.PathGraphics
    protected void deviceClip(PathIterator pathIterator) {
    }
}
