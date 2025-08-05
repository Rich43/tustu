package sun.awt.windows;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.security.AccessController;
import java.util.Arrays;
import sun.awt.image.ByteComponentRaster;
import sun.awt.image.BytePackedRaster;
import sun.font.CompositeFont;
import sun.font.Font2D;
import sun.font.FontUtilities;
import sun.font.PhysicalFont;
import sun.font.TrueTypeFont;
import sun.print.PathGraphics;
import sun.print.ProxyGraphics2D;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/windows/WPathGraphics.class */
final class WPathGraphics extends PathGraphics {
    private static final int DEFAULT_USER_RES = 72;
    private static final float MIN_DEVICE_LINEWIDTH = 1.2f;
    private static final float MAX_THINLINE_INCHES = 0.014f;
    private static boolean useGDITextLayout;
    private static boolean preferGDITextLayout;

    static {
        useGDITextLayout = true;
        preferGDITextLayout = false;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.print.enableGDITextLayout"));
        if (str != null) {
            useGDITextLayout = Boolean.getBoolean(str);
            if (!useGDITextLayout && str.equalsIgnoreCase("prefer")) {
                useGDITextLayout = true;
                preferGDITextLayout = true;
            }
        }
    }

    WPathGraphics(Graphics2D graphics2D, PrinterJob printerJob, Printable printable, PageFormat pageFormat, int i2, boolean z2) {
        super(graphics2D, printerJob, printable, pageFormat, i2, z2);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public Graphics create() {
        return new WPathGraphics((Graphics2D) getDelegate().create(), getPrinterJob(), getPrintable(), getPageFormat(), getPageIndex(), canDoRedraws());
    }

    @Override // sun.print.PathGraphics, sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void draw(Shape shape) {
        Stroke stroke = getStroke();
        if (stroke instanceof BasicStroke) {
            Stroke basicStroke = null;
            BasicStroke basicStroke2 = (BasicStroke) stroke;
            float lineWidth = basicStroke2.getLineWidth();
            Point2D.Float r0 = new Point2D.Float(lineWidth, lineWidth);
            AffineTransform transform = getTransform();
            transform.deltaTransform(r0, r0);
            if (Math.min(Math.abs(r0.f12396x), Math.abs(r0.f12397y)) < 1.2f) {
                Point2D.Float r02 = new Point2D.Float(1.2f, 1.2f);
                try {
                    transform.createInverse().deltaTransform(r02, r02);
                    basicStroke = new BasicStroke(Math.max(Math.abs(r02.f12396x), Math.abs(r02.f12397y)), basicStroke2.getEndCap(), basicStroke2.getLineJoin(), basicStroke2.getMiterLimit(), basicStroke2.getDashArray(), basicStroke2.getDashPhase());
                    setStroke(basicStroke);
                } catch (NoninvertibleTransformException e2) {
                }
            }
            super.draw(shape);
            if (basicStroke != null) {
                setStroke(basicStroke2);
                return;
            }
            return;
        }
        super.draw(shape);
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
    protected int platformFontCount(Font font, String str) {
        AffineTransform affineTransform = new AffineTransform(getTransform());
        affineTransform.concatenate(getFont().getTransform());
        int type = affineTransform.getType();
        if (!(type != 32 && (type & 64) == 0)) {
            return 0;
        }
        Font2D font2D = FontUtilities.getFont2D(font);
        if ((font2D instanceof CompositeFont) || (font2D instanceof TrueTypeFont)) {
            return 1;
        }
        return 0;
    }

    private static boolean isXP() {
        String property = System.getProperty("os.version");
        return property != null && Float.valueOf(property).floatValue() >= 5.1f;
    }

    private boolean strNeedsTextLayout(String str, Font font) {
        char[] charArray = str.toCharArray();
        if (!FontUtilities.isComplexText(charArray, 0, charArray.length)) {
            return false;
        }
        if (!useGDITextLayout) {
            return true;
        }
        if (!preferGDITextLayout) {
            if (isXP() && FontUtilities.textLayoutIsCompatible(font)) {
                return false;
            }
            return true;
        }
        return false;
    }

    private int getAngle(Point2D.Double r6) {
        double degrees = Math.toDegrees(Math.atan2(r6.f12395y, r6.f12394x));
        if (degrees < 0.0d) {
            degrees += 360.0d;
        }
        if (degrees != 0.0d) {
            degrees = 360.0d - degrees;
        }
        return (int) Math.round(degrees * 10.0d);
    }

    private float getAwScale(double d2, double d3) {
        float f2 = (float) (d2 / d3);
        if (f2 > 0.999f && f2 < 1.001f) {
            f2 = 1.0f;
        }
        return f2;
    }

    @Override // sun.print.PathGraphics
    public void drawString(String str, float f2, float f3, Font font, FontRenderContext fontRenderContext, float f4) {
        if (str.length() == 0) {
            return;
        }
        if (WPrinterJob.shapeTextProp) {
            super.drawString(str, f2, f3, font, fontRenderContext, f4);
            return;
        }
        boolean zStrNeedsTextLayout = strNeedsTextLayout(str, font);
        if ((font.hasLayoutAttributes() || zStrNeedsTextLayout) && !this.printingGlyphVector) {
            new TextLayout(str, font, fontRenderContext).draw(this, f2, f3);
            return;
        }
        if (zStrNeedsTextLayout) {
            super.drawString(str, f2, f3, font, fontRenderContext, f4);
            return;
        }
        AffineTransform transform = getTransform();
        AffineTransform affineTransform = new AffineTransform(transform);
        affineTransform.concatenate(font.getTransform());
        int type = affineTransform.getType();
        boolean z2 = type != 32 && (type & 64) == 0;
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        try {
            wPrinterJob.setTextColor((Color) getPaint());
        } catch (ClassCastException e2) {
            z2 = false;
        }
        if (!z2) {
            super.drawString(str, f2, f3, font, fontRenderContext, f4);
            return;
        }
        Point2D.Float r0 = new Point2D.Float(f2, f3);
        Point2D.Float r02 = new Point2D.Float();
        if (font.isTransformed()) {
            AffineTransform transform2 = font.getTransform();
            float translateX = (float) transform2.getTranslateX();
            float translateY = (float) transform2.getTranslateY();
            if (Math.abs(translateX) < 1.0E-5d) {
                translateX = 0.0f;
            }
            if (Math.abs(translateY) < 1.0E-5d) {
                translateY = 0.0f;
            }
            r0.f12396x += translateX;
            r0.f12397y += translateY;
        }
        transform.transform(r0, r02);
        if (getClip() != null) {
            deviceClip(getClip().getPathIterator(transform));
        }
        float size2D = font.getSize2D();
        double xRes = wPrinterJob.getXRes();
        double yRes = wPrinterJob.getYRes();
        double d2 = yRes / 72.0d;
        int orientation = getPageFormat().getOrientation();
        if (orientation == 0 || orientation == 2) {
            xRes = yRes;
            yRes = xRes;
        }
        affineTransform.scale(1.0d / (xRes / 72.0d), 1.0d / (yRes / 72.0d));
        Point2D.Double r03 = new Point2D.Double(0.0d, 1.0d);
        affineTransform.deltaTransform(r03, r03);
        double dSqrt = Math.sqrt((r03.f12394x * r03.f12394x) + (r03.f12395y * r03.f12395y));
        float f5 = (float) (size2D * dSqrt * d2);
        Point2D.Double r04 = new Point2D.Double(1.0d, 0.0d);
        affineTransform.deltaTransform(r04, r04);
        float awScale = getAwScale(Math.sqrt((r04.f12394x * r04.f12394x) + (r04.f12395y * r04.f12395y)), dSqrt);
        int angle = getAngle(r04);
        Point2D.Double r05 = new Point2D.Double(1.0d, 0.0d);
        transform.deltaTransform(r05, r05);
        double dSqrt2 = Math.sqrt((r05.f12394x * r05.f12394x) + (r05.f12395y * r05.f12395y));
        Point2D.Double r06 = new Point2D.Double(0.0d, 1.0d);
        transform.deltaTransform(r06, r06);
        double dSqrt3 = Math.sqrt((r06.f12394x * r06.f12394x) + (r06.f12395y * r06.f12395y));
        Font2D font2D = FontUtilities.getFont2D(font);
        if (font2D instanceof TrueTypeFont) {
            textOut(str, font, (TrueTypeFont) font2D, fontRenderContext, f5, angle, awScale, dSqrt2, dSqrt3, f2, f3, r02.f12396x, r02.f12397y, f4);
            return;
        }
        if (font2D instanceof CompositeFont) {
            CompositeFont compositeFont = (CompositeFont) font2D;
            float f6 = f2;
            float f7 = r02.f12396x;
            float f8 = r02.f12397y;
            char[] charArray = str.toCharArray();
            int length = charArray.length;
            int[] iArr = new int[length];
            compositeFont.getMapper().charsToGlyphs(length, charArray, iArr);
            int i2 = 0;
            while (i2 < length) {
                int i3 = i2;
                int i4 = iArr[i3] >>> 24;
                while (i2 < length && (iArr[i2] >>> 24) == i4) {
                    i2++;
                }
                String str2 = new String(charArray, i3, i2 - i3);
                textOut(str2, font, compositeFont.getSlotFont(i4), fontRenderContext, f5, angle, awScale, dSqrt2, dSqrt3, f6, f3, f7, f8, 0.0f);
                float width = (float) font.getStringBounds(str2, fontRenderContext).getWidth();
                f6 += width;
                r0.f12396x += width;
                transform.transform(r0, r02);
                f7 = r02.f12396x;
                f8 = r02.f12397y;
            }
            return;
        }
        super.drawString(str, f2, f3, font, fontRenderContext, f4);
    }

    @Override // sun.print.PathGraphics
    protected boolean printGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        if ((glyphVector.getLayoutFlags() & 1) != 0) {
            return false;
        }
        if (glyphVector.getNumGlyphs() == 0) {
            return true;
        }
        AffineTransform transform = getTransform();
        AffineTransform affineTransform = new AffineTransform(transform);
        Font font = glyphVector.getFont();
        affineTransform.concatenate(font.getTransform());
        int type = affineTransform.getType();
        boolean z2 = type != 32 && (type & 64) == 0;
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        try {
            wPrinterJob.setTextColor((Color) getPaint());
        } catch (ClassCastException e2) {
            z2 = false;
        }
        if (WPrinterJob.shapeTextProp || !z2) {
            return false;
        }
        Point2D.Float r0 = new Point2D.Float(f2, f3);
        Point2D glyphPosition = glyphVector.getGlyphPosition(0);
        r0.f12396x += (float) glyphPosition.getX();
        r0.f12397y += (float) glyphPosition.getY();
        Point2D.Float r02 = new Point2D.Float();
        if (font.isTransformed()) {
            AffineTransform transform2 = font.getTransform();
            float translateX = (float) transform2.getTranslateX();
            float translateY = (float) transform2.getTranslateY();
            if (Math.abs(translateX) < 1.0E-5d) {
                translateX = 0.0f;
            }
            if (Math.abs(translateY) < 1.0E-5d) {
                translateY = 0.0f;
            }
            r0.f12396x += translateX;
            r0.f12397y += translateY;
        }
        transform.transform(r0, r02);
        if (getClip() != null) {
            deviceClip(getClip().getPathIterator(transform));
        }
        float size2D = font.getSize2D();
        double xRes = wPrinterJob.getXRes();
        double yRes = wPrinterJob.getYRes();
        double d2 = yRes / 72.0d;
        int orientation = getPageFormat().getOrientation();
        if (orientation == 0 || orientation == 2) {
            xRes = yRes;
            yRes = xRes;
        }
        affineTransform.scale(1.0d / (xRes / 72.0d), 1.0d / (yRes / 72.0d));
        Point2D.Double r03 = new Point2D.Double(0.0d, 1.0d);
        affineTransform.deltaTransform(r03, r03);
        double dSqrt = Math.sqrt((r03.f12394x * r03.f12394x) + (r03.f12395y * r03.f12395y));
        float f4 = (float) (size2D * dSqrt * d2);
        Point2D.Double r04 = new Point2D.Double(1.0d, 0.0d);
        affineTransform.deltaTransform(r04, r04);
        float awScale = getAwScale(Math.sqrt((r04.f12394x * r04.f12394x) + (r04.f12395y * r04.f12395y)), dSqrt);
        int angle = getAngle(r04);
        Point2D.Double r05 = new Point2D.Double(1.0d, 0.0d);
        transform.deltaTransform(r05, r05);
        double dSqrt2 = Math.sqrt((r05.f12394x * r05.f12394x) + (r05.f12395y * r05.f12395y));
        Point2D.Double r06 = new Point2D.Double(0.0d, 1.0d);
        transform.deltaTransform(r06, r06);
        double dSqrt3 = Math.sqrt((r06.f12394x * r06.f12394x) + (r06.f12395y * r06.f12395y));
        int numGlyphs = glyphVector.getNumGlyphs();
        int[] glyphCodes = glyphVector.getGlyphCodes(0, numGlyphs, null);
        float[] glyphPositions = glyphVector.getGlyphPositions(0, numGlyphs, null);
        int i2 = 0;
        for (int i3 = 0; i3 < numGlyphs; i3++) {
            if ((glyphCodes[i3] & 65535) >= 65534) {
                i2++;
            }
        }
        if (i2 > 0) {
            int i4 = numGlyphs - i2;
            int[] iArr = new int[i4];
            float[] fArr = new float[i4 * 2];
            int i5 = 0;
            for (int i6 = 0; i6 < numGlyphs; i6++) {
                if ((glyphCodes[i6] & 65535) < 65534) {
                    iArr[i5] = glyphCodes[i6];
                    fArr[i5 * 2] = glyphPositions[i6 * 2];
                    fArr[(i5 * 2) + 1] = glyphPositions[(i6 * 2) + 1];
                    i5++;
                }
            }
            numGlyphs = i4;
            glyphCodes = iArr;
            glyphPositions = fArr;
        }
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(dSqrt2, dSqrt3);
        float[] fArr2 = new float[glyphPositions.length];
        scaleInstance.transform(glyphPositions, 0, fArr2, 0, glyphPositions.length / 2);
        Font2D font2D = FontUtilities.getFont2D(font);
        if (font2D instanceof TrueTypeFont) {
            if (!wPrinterJob.setFont(font2D.getFamilyName(null), f4, font.getStyle() | font2D.getStyle(), angle, awScale)) {
                return false;
            }
            wPrinterJob.glyphsOut(glyphCodes, r02.f12396x, r02.f12397y, fArr2);
            return true;
        }
        if (font2D instanceof CompositeFont) {
            CompositeFont compositeFont = (CompositeFont) font2D;
            float f5 = r02.f12396x;
            float f6 = r02.f12397y;
            int i7 = 0;
            while (i7 < numGlyphs) {
                int i8 = i7;
                int i9 = glyphCodes[i8] >>> 24;
                while (i7 < numGlyphs && (glyphCodes[i7] >>> 24) == i9) {
                    i7++;
                }
                PhysicalFont slotFont = compositeFont.getSlotFont(i9);
                if (!(slotFont instanceof TrueTypeFont) || !wPrinterJob.setFont(slotFont.getFamilyName(null), f4, font.getStyle() | slotFont.getStyle(), angle, awScale)) {
                    return false;
                }
                int[] iArrCopyOfRange = Arrays.copyOfRange(glyphCodes, i8, i7);
                float[] fArrCopyOfRange = Arrays.copyOfRange(fArr2, i8 * 2, i7 * 2);
                if (i8 != 0) {
                    Point2D.Float r07 = new Point2D.Float(f2 + glyphPositions[i8 * 2], f3 + glyphPositions[(i8 * 2) + 1]);
                    transform.transform(r07, r07);
                    f5 = r07.f12396x;
                    f6 = r07.f12397y;
                }
                wPrinterJob.glyphsOut(iArrCopyOfRange, f5, f6, fArrCopyOfRange);
            }
            return true;
        }
        return false;
    }

    private void textOut(String str, Font font, PhysicalFont physicalFont, FontRenderContext fontRenderContext, float f2, int i2, float f3, double d2, double d3, float f4, float f5, float f6, float f7, float f8) {
        String familyName = physicalFont.getFamilyName(null);
        int style = font.getStyle() | physicalFont.getStyle();
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        if (!wPrinterJob.setFont(familyName, f2, style, i2, f3)) {
            super.drawString(str, f4, f5, font, fontRenderContext, f8);
            return;
        }
        float[] fArr = null;
        if (!okGDIMetrics(str, font, fontRenderContext, d2)) {
            str = wPrinterJob.removeControlChars(str);
            char[] charArray = str.toCharArray();
            int length = charArray.length;
            GlyphVector glyphVectorCreateGlyphVector = null;
            if (!FontUtilities.isComplexText(charArray, 0, length)) {
                glyphVectorCreateGlyphVector = font.createGlyphVector(fontRenderContext, str);
            }
            if (glyphVectorCreateGlyphVector == null) {
                super.drawString(str, f4, f5, font, fontRenderContext, f8);
                return;
            }
            float[] glyphPositions = glyphVectorCreateGlyphVector.getGlyphPositions(0, length, null);
            glyphVectorCreateGlyphVector.getGlyphPosition(glyphVectorCreateGlyphVector.getNumGlyphs());
            AffineTransform scaleInstance = AffineTransform.getScaleInstance(d2, d3);
            float[] fArr2 = new float[glyphPositions.length];
            scaleInstance.transform(glyphPositions, 0, fArr2, 0, glyphPositions.length / 2);
            fArr = fArr2;
        }
        wPrinterJob.textOut(str, f6, f7, fArr);
    }

    private boolean okGDIMetrics(String str, Font font, FontRenderContext fontRenderContext, double d2) {
        double dRound = Math.round(font.getStringBounds(str, fontRenderContext).getWidth() * d2);
        int gDIAdvance = ((WPrinterJob) getPrinterJob()).getGDIAdvance(str);
        if (dRound > 0.0d && gDIAdvance > 0) {
            double dAbs = Math.abs(gDIAdvance - dRound);
            double d3 = gDIAdvance / dRound;
            if (d3 < 1.0d) {
                d3 = 1.0d / d3;
            }
            return dAbs <= 1.0d || d3 < 1.01d;
        }
        return true;
    }

    @Override // sun.print.PathGraphics
    protected boolean drawImageToPlatform(Image image, AffineTransform affineTransform, Color color, int i2, int i3, int i4, int i5, boolean z2) {
        BufferedImage bufferedImage;
        byte[] dataStorage;
        BufferedImage bufferedImage2 = getBufferedImage(image);
        if (bufferedImage2 == null) {
            return true;
        }
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
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
        double xRes = wPrinterJob.getXRes();
        double yRes = wPrinterJob.getYRes();
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
            Rectangle2D.Float r04 = new Rectangle2D.Float(i2, i3, i4, i5);
            Rectangle2D bounds2D = affineTransform2.createTransformedShape(r04).getBounds2D();
            bounds2D.setRect(bounds2D.getX(), bounds2D.getY(), bounds2D.getWidth() + 0.001d, bounds2D.getHeight() + 0.001d);
            int width = (int) bounds2D.getWidth();
            int height = (int) bounds2D.getHeight();
            if (width > 0 && height > 0) {
                boolean z3 = true;
                if (!z2 && hasTransparentPixels(bufferedImage2)) {
                    z3 = false;
                    if (isBitmaskTransparency(bufferedImage2)) {
                        if (color == null) {
                            if (drawBitmaskImage(bufferedImage2, affineTransform, color, i2, i3, i4, i5)) {
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
                if ((i2 + i4 > bufferedImage2.getWidth(null) || i3 + i5 > bufferedImage2.getHeight(null)) && canDoRedraws()) {
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
                    wPrinterJob.saveState(getTransform(), getClip(), bounds2D2, d5, d5);
                    return true;
                }
                int i9 = 5;
                IndexColorModel indexColorModel = null;
                ColorModel colorModel = bufferedImage2.getColorModel();
                int type = bufferedImage2.getType();
                if ((colorModel instanceof IndexColorModel) && colorModel.getPixelSize() <= 8 && (type == 12 || type == 13)) {
                    indexColorModel = (IndexColorModel) colorModel;
                    i9 = type;
                    if (type == 12 && colorModel.getPixelSize() == 2) {
                        int[] iArr = new int[16];
                        indexColorModel.getRGBs(iArr);
                        indexColorModel = new IndexColorModel(4, 16, iArr, 0, indexColorModel.getTransparency() != 1, indexColorModel.getTransparentPixel(), 0);
                    }
                }
                int width3 = (int) bounds2D.getWidth();
                int height3 = (int) bounds2D.getHeight();
                if (1 != 0) {
                    if (indexColorModel == null) {
                        bufferedImage = new BufferedImage(width3, height3, i9);
                    } else {
                        bufferedImage = new BufferedImage(width3, height3, i9, indexColorModel);
                    }
                    Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
                    graphics2DCreateGraphics.clipRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
                    graphics2DCreateGraphics.translate(-bounds2D.getX(), -bounds2D.getY());
                    graphics2DCreateGraphics.transform(affineTransform2);
                    if (color == null) {
                        color = Color.white;
                    }
                    graphics2DCreateGraphics.drawImage(bufferedImage2, i2, i3, i2 + i4, i3 + i5, i2, i3, i2 + i4, i3 + i5, color, null);
                    graphics2DCreateGraphics.dispose();
                } else {
                    bufferedImage = bufferedImage2;
                }
                Rectangle2D.Float r05 = new Rectangle2D.Float((float) (bounds2D.getX() * dDistance), (float) (bounds2D.getY() * dDistance2), (float) (bounds2D.getWidth() * dDistance), (float) (bounds2D.getHeight() * dDistance2));
                WritableRaster raster = bufferedImage.getRaster();
                if (raster instanceof ByteComponentRaster) {
                    dataStorage = ((ByteComponentRaster) raster).getDataStorage();
                } else if (raster instanceof BytePackedRaster) {
                    dataStorage = ((BytePackedRaster) raster).getDataStorage();
                } else {
                    return false;
                }
                int length = 24;
                SampleModel sampleModel = bufferedImage.getSampleModel();
                if (sampleModel instanceof ComponentSampleModel) {
                    length = ((ComponentSampleModel) sampleModel).getPixelStride() * 8;
                } else if (sampleModel instanceof MultiPixelPackedSampleModel) {
                    length = ((MultiPixelPackedSampleModel) sampleModel).getPixelBitStride();
                } else if (indexColorModel != null) {
                    int width4 = bufferedImage.getWidth();
                    int height4 = bufferedImage.getHeight();
                    if (width4 > 0 && height4 > 0) {
                        length = ((dataStorage.length * 8) / width4) / height4;
                    }
                }
                Shape clip = getClip();
                clip(affineTransform.createTransformedShape(r04));
                deviceClip(getClip().getPathIterator(getTransform()));
                wPrinterJob.drawDIBImage(dataStorage, r05.f12404x, r05.f12405y, (float) Math.rint(r05.width + 0.5d), (float) Math.rint(r05.height + 0.5d), 0.0f, 0.0f, bufferedImage.getWidth(), bufferedImage.getHeight(), length, indexColorModel);
                setClip(clip);
                return true;
            }
            return true;
        }
        return true;
    }

    @Override // sun.print.PathGraphics
    public void redrawRegion(Rectangle2D rectangle2D, double d2, double d3, Shape shape, AffineTransform affineTransform) throws PrinterException {
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        Printable printable = getPrintable();
        PageFormat pageFormat = getPageFormat();
        int pageIndex = getPageIndex();
        BufferedImage bufferedImage = new BufferedImage((int) rectangle2D.getWidth(), (int) rectangle2D.getHeight(), 5);
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        ProxyGraphics2D proxyGraphics2D = new ProxyGraphics2D(graphics2DCreateGraphics, wPrinterJob);
        proxyGraphics2D.setColor(Color.white);
        proxyGraphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        proxyGraphics2D.clipRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        proxyGraphics2D.translate(-rectangle2D.getX(), -rectangle2D.getY());
        proxyGraphics2D.scale(((float) (wPrinterJob.getXRes() / d2)) / 72.0f, ((float) (wPrinterJob.getYRes() / d3)) / 72.0f);
        proxyGraphics2D.translate(((-wPrinterJob.getPhysicalPrintableX(pageFormat.getPaper())) / wPrinterJob.getXRes()) * 72.0d, ((-wPrinterJob.getPhysicalPrintableY(pageFormat.getPaper())) / wPrinterJob.getYRes()) * 72.0d);
        proxyGraphics2D.transform(new AffineTransform(getPageFormat().getMatrix()));
        proxyGraphics2D.setPaint(Color.black);
        printable.print(proxyGraphics2D, pageFormat, pageIndex);
        graphics2DCreateGraphics.dispose();
        if (shape != null) {
            deviceClip(shape.getPathIterator(affineTransform));
        }
        Rectangle2D.Float r0 = new Rectangle2D.Float((float) (rectangle2D.getX() * d2), (float) (rectangle2D.getY() * d3), (float) (rectangle2D.getWidth() * d2), (float) (rectangle2D.getHeight() * d3));
        wPrinterJob.drawImage3ByteBGR(((ByteComponentRaster) bufferedImage.getRaster()).getDataStorage(), r0.f12404x, r0.f12405y, r0.width, r0.height, 0.0f, 0.0f, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override // sun.print.PathGraphics
    protected void deviceFill(PathIterator pathIterator, Color color) {
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        convertToWPath(pathIterator);
        wPrinterJob.selectSolidBrush(color);
        wPrinterJob.fillPath();
    }

    @Override // sun.print.PathGraphics
    protected void deviceClip(PathIterator pathIterator) {
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        convertToWPath(pathIterator);
        wPrinterJob.selectClipPath();
    }

    @Override // sun.print.PathGraphics
    protected void deviceFrameRect(int i2, int i3, int i4, int i5, Color color) {
        AffineTransform transform = getTransform();
        if ((transform.getType() & 48) != 0) {
            draw(new Rectangle2D.Float(i2, i3, i4, i5));
            return;
        }
        Stroke stroke = getStroke();
        if (stroke instanceof BasicStroke) {
            BasicStroke basicStroke = (BasicStroke) stroke;
            int endCap = basicStroke.getEndCap();
            int lineJoin = basicStroke.getLineJoin();
            if (endCap == 2 && lineJoin == 0 && basicStroke.getMiterLimit() == 10.0f) {
                float lineWidth = basicStroke.getLineWidth();
                Point2D.Float r0 = new Point2D.Float(lineWidth, lineWidth);
                transform.deltaTransform(r0, r0);
                float fMin = Math.min(Math.abs(r0.f12396x), Math.abs(r0.f12397y));
                Point2D.Float r02 = new Point2D.Float(i2, i3);
                transform.transform(r02, r02);
                Point2D.Float r03 = new Point2D.Float(i2 + i4, i3 + i5);
                transform.transform(r03, r03);
                float x2 = (float) (r03.getX() - r02.getX());
                float y2 = (float) (r03.getY() - r02.getY());
                WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
                if (wPrinterJob.selectStylePen(endCap, lineJoin, fMin, color)) {
                    wPrinterJob.frameRect((float) r02.getX(), (float) r02.getY(), x2, y2);
                    return;
                }
                if (fMin / Math.min(wPrinterJob.getXRes(), wPrinterJob.getYRes()) < 0.014000000432133675d) {
                    wPrinterJob.selectPen(fMin, color);
                    wPrinterJob.frameRect((float) r02.getX(), (float) r02.getY(), x2, y2);
                    return;
                } else {
                    draw(new Rectangle2D.Float(i2, i3, i4, i5));
                    return;
                }
            }
            draw(new Rectangle2D.Float(i2, i3, i4, i5));
        }
    }

    @Override // sun.print.PathGraphics
    protected void deviceFillRect(int i2, int i3, int i4, int i5, Color color) {
        AffineTransform transform = getTransform();
        if ((transform.getType() & 48) != 0) {
            fill(new Rectangle2D.Float(i2, i3, i4, i5));
            return;
        }
        Point2D.Float r0 = new Point2D.Float(i2, i3);
        transform.transform(r0, r0);
        Point2D.Float r02 = new Point2D.Float(i2 + i4, i3 + i5);
        transform.transform(r02, r02);
        ((WPrinterJob) getPrinterJob()).fillRect((float) r0.getX(), (float) r0.getY(), (float) (r02.getX() - r0.getX()), (float) (r02.getY() - r0.getY()), color);
    }

    @Override // sun.print.PathGraphics
    protected void deviceDrawLine(int i2, int i3, int i4, int i5, Color color) {
        Stroke stroke = getStroke();
        if (stroke instanceof BasicStroke) {
            BasicStroke basicStroke = (BasicStroke) stroke;
            if (basicStroke.getDashArray() != null) {
                draw(new Line2D.Float(i2, i3, i4, i5));
                return;
            }
            float lineWidth = basicStroke.getLineWidth();
            Point2D.Float r0 = new Point2D.Float(lineWidth, lineWidth);
            AffineTransform transform = getTransform();
            transform.deltaTransform(r0, r0);
            float fMin = Math.min(Math.abs(r0.f12396x), Math.abs(r0.f12397y));
            Point2D.Float r02 = new Point2D.Float(i2, i3);
            transform.transform(r02, r02);
            Point2D.Float r03 = new Point2D.Float(i4, i5);
            transform.transform(r03, r03);
            int endCap = basicStroke.getEndCap();
            int lineJoin = basicStroke.getLineJoin();
            if (r03.getX() == r02.getX() && r03.getY() == r02.getY()) {
                endCap = 1;
            }
            WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
            if (wPrinterJob.selectStylePen(endCap, lineJoin, fMin, color)) {
                wPrinterJob.moveTo((float) r02.getX(), (float) r02.getY());
                wPrinterJob.lineTo((float) r03.getX(), (float) r03.getY());
                return;
            }
            double dMin = Math.min(wPrinterJob.getXRes(), wPrinterJob.getYRes());
            if (endCap == 1 || ((i2 == i4 || i3 == i5) && fMin / dMin < 0.014000000432133675d)) {
                wPrinterJob.selectPen(fMin, color);
                wPrinterJob.moveTo((float) r02.getX(), (float) r02.getY());
                wPrinterJob.lineTo((float) r03.getX(), (float) r03.getY());
                return;
            }
            draw(new Line2D.Float(i2, i3, i4, i5));
        }
    }

    private void convertToWPath(PathIterator pathIterator) {
        int i2;
        float[] fArr = new float[6];
        WPrinterJob wPrinterJob = (WPrinterJob) getPrinterJob();
        if (pathIterator.getWindingRule() == 0) {
            i2 = 1;
        } else {
            i2 = 2;
        }
        wPrinterJob.setPolyFillMode(i2);
        wPrinterJob.beginPath();
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(fArr)) {
                case 0:
                    wPrinterJob.moveTo(fArr[0], fArr[1]);
                    break;
                case 1:
                    wPrinterJob.lineTo(fArr[0], fArr[1]);
                    break;
                case 2:
                    int penX = wPrinterJob.getPenX();
                    int penY = wPrinterJob.getPenY();
                    wPrinterJob.polyBezierTo(penX + (((fArr[0] - penX) * 2.0f) / 3.0f), penY + (((fArr[1] - penY) * 2.0f) / 3.0f), fArr[2] - (((fArr[2] - fArr[0]) * 2.0f) / 3.0f), fArr[3] - (((fArr[3] - fArr[1]) * 2.0f) / 3.0f), fArr[2], fArr[3]);
                    break;
                case 3:
                    wPrinterJob.polyBezierTo(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                    break;
                case 4:
                    wPrinterJob.closeFigure();
                    break;
            }
            pathIterator.next();
        }
        wPrinterJob.endPath();
    }
}
