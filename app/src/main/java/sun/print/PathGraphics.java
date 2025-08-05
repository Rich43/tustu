package sun.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.ref.SoftReference;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;
import sun.awt.image.SunWritableRaster;
import sun.awt.image.ToolkitImage;
import sun.font.CompositeFont;
import sun.font.Font2D;
import sun.font.Font2DHandle;
import sun.font.FontUtilities;
import sun.font.PhysicalFont;

/* loaded from: rt.jar:sun/print/PathGraphics.class */
public abstract class PathGraphics extends ProxyGraphics2D {
    private Printable mPainter;
    private PageFormat mPageFormat;
    private int mPageIndex;
    private boolean mCanRedraw;
    protected boolean printingGlyphVector;
    protected static SoftReference<Hashtable<Font2DHandle, Object>> fontMapRef;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract void redrawRegion(Rectangle2D rectangle2D, double d2, double d3, Shape shape, AffineTransform affineTransform) throws PrinterException;

    protected abstract void deviceFill(PathIterator pathIterator, Color color);

    protected abstract void deviceClip(PathIterator pathIterator);

    protected abstract void deviceFrameRect(int i2, int i3, int i4, int i5, Color color);

    protected abstract void deviceDrawLine(int i2, int i3, int i4, int i5, Color color);

    protected abstract void deviceFillRect(int i2, int i3, int i4, int i5, Color color);

    protected abstract boolean drawImageToPlatform(Image image, AffineTransform affineTransform, Color color, int i2, int i3, int i4, int i5, boolean z2);

    static {
        $assertionsDisabled = !PathGraphics.class.desiredAssertionStatus();
        fontMapRef = new SoftReference<>(null);
    }

    protected PathGraphics(Graphics2D graphics2D, PrinterJob printerJob, Printable printable, PageFormat pageFormat, int i2, boolean z2) {
        super(graphics2D, printerJob);
        this.mPainter = printable;
        this.mPageFormat = pageFormat;
        this.mPageIndex = i2;
        this.mCanRedraw = z2;
    }

    protected Printable getPrintable() {
        return this.mPainter;
    }

    protected PageFormat getPageFormat() {
        return this.mPageFormat;
    }

    protected int getPageIndex() {
        return this.mPageIndex;
    }

    public boolean canDoRedraws() {
        return this.mCanRedraw;
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void drawLine(int i2, int i3, int i4, int i5) {
        Paint paint = getPaint();
        try {
            AffineTransform transform = getTransform();
            if (getClip() != null) {
                deviceClip(getClip().getPathIterator(transform));
            }
            deviceDrawLine(i2, i3, i4, i5, (Color) paint);
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException("Expected a Color instance");
        }
    }

    @Override // java.awt.Graphics
    public void drawRect(int i2, int i3, int i4, int i5) {
        Paint paint = getPaint();
        try {
            AffineTransform transform = getTransform();
            if (getClip() != null) {
                deviceClip(getClip().getPathIterator(transform));
            }
            deviceFrameRect(i2, i3, i4, i5, (Color) paint);
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException("Expected a Color instance");
        }
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void fillRect(int i2, int i3, int i4, int i5) {
        Paint paint = getPaint();
        try {
            AffineTransform transform = getTransform();
            if (getClip() != null) {
                deviceClip(getClip().getPathIterator(transform));
            }
            deviceFillRect(i2, i3, i4, i5, (Color) paint);
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException("Expected a Color instance");
        }
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void clearRect(int i2, int i3, int i4, int i5) {
        fill(new Rectangle2D.Float(i2, i3, i4, i5), getBackground());
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        draw(new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        fill(new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void drawOval(int i2, int i3, int i4, int i5) {
        draw(new Ellipse2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void fillOval(int i2, int i3, int i4, int i5) {
        fill(new Ellipse2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void drawArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        draw(new Arc2D.Float(i2, i3, i4, i5, i6, i7, 0));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void fillArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        fill(new Arc2D.Float(i2, i3, i4, i5, i6, i7, 2));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void drawPolyline(int[] iArr, int[] iArr2, int i2) {
        if (i2 > 0) {
            float f2 = iArr[0];
            float f3 = iArr2[0];
            for (int i3 = 1; i3 < i2; i3++) {
                float f4 = iArr[i3];
                float f5 = iArr2[i3];
                draw(new Line2D.Float(f2, f3, f4, f5));
                f2 = f4;
                f3 = f5;
            }
        }
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void drawPolygon(int[] iArr, int[] iArr2, int i2) {
        draw(new Polygon(iArr, iArr2, i2));
    }

    @Override // java.awt.Graphics
    public void drawPolygon(Polygon polygon) {
        draw(polygon);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public void fillPolygon(int[] iArr, int[] iArr2, int i2) {
        fill(new Polygon(iArr, iArr2, i2));
    }

    @Override // java.awt.Graphics
    public void fillPolygon(Polygon polygon) {
        fill(polygon);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D, java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        drawString(str, i2, i3);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void drawString(String str, float f2, float f3) {
        if (str.length() == 0) {
            return;
        }
        new TextLayout(str, getFont(), getFontRenderContext()).draw(this, f2, f3);
    }

    protected void drawString(String str, float f2, float f3, Font font, FontRenderContext fontRenderContext, float f4) {
        fill(new TextLayout(str, font, fontRenderContext).getOutline(AffineTransform.getTranslateInstance(f2, f3)));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D, java.awt.Graphics
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        drawString(attributedCharacterIterator, i2, i3);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, float f2, float f3) {
        if (attributedCharacterIterator == null) {
            throw new NullPointerException("attributedcharacteriterator is null");
        }
        new TextLayout(attributedCharacterIterator, getFontRenderContext()).draw(this, f2, f3);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void drawGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        if (this.printingGlyphVector) {
            if (!$assertionsDisabled && this.printingGlyphVector) {
                throw new AssertionError();
            }
            fill(glyphVector.getOutline(f2, f3));
            return;
        }
        try {
            this.printingGlyphVector = true;
            if (RasterPrinterJob.shapeTextProp || !printedSimpleGlyphVector(glyphVector, f2, f3)) {
                fill(glyphVector.getOutline(f2, f3));
            }
        } finally {
            this.printingGlyphVector = false;
        }
    }

    protected int platformFontCount(Font font, String str) {
        return 0;
    }

    protected boolean printGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v225, types: [char[]] */
    boolean printedSimpleGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        Hashtable<Font2DHandle, Object> hashtable;
        char c2;
        char c3;
        int layoutFlags = glyphVector.getLayoutFlags();
        if (layoutFlags != 0 && layoutFlags != 2) {
            return printGlyphVector(glyphVector, f2, f3);
        }
        Font font = glyphVector.getFont();
        Font2D font2D = FontUtilities.getFont2D(font);
        if (font2D.handle.font2D != font2D) {
            return false;
        }
        synchronized (PathGraphics.class) {
            hashtable = fontMapRef.get();
            if (hashtable == null) {
                hashtable = new Hashtable<>();
                fontMapRef = new SoftReference<>(hashtable);
            }
        }
        int numGlyphs = glyphVector.getNumGlyphs();
        int[] glyphCodes = glyphVector.getGlyphCodes(0, numGlyphs, null);
        char[] glyphToCharMapForFont = null;
        char[][] cArr = (char[][]) null;
        CompositeFont compositeFont = null;
        synchronized (hashtable) {
            if (font2D instanceof CompositeFont) {
                compositeFont = (CompositeFont) font2D;
                int numSlots = compositeFont.getNumSlots();
                cArr = (char[][]) hashtable.get(font2D.handle);
                if (cArr == null) {
                    cArr = new char[numSlots];
                    hashtable.put(font2D.handle, cArr);
                }
                for (int i2 = 0; i2 < numGlyphs; i2++) {
                    int i3 = glyphCodes[i2] >>> 24;
                    if (i3 >= numSlots) {
                        return false;
                    }
                    if (cArr[i3] == null) {
                        PhysicalFont slotFont = compositeFont.getSlotFont(i3);
                        char[] glyphToCharMapForFont2 = (char[]) hashtable.get(slotFont.handle);
                        if (glyphToCharMapForFont2 == null) {
                            glyphToCharMapForFont2 = getGlyphToCharMapForFont(slotFont);
                        }
                        cArr[i3] = glyphToCharMapForFont2;
                    }
                }
            } else {
                glyphToCharMapForFont = (char[]) hashtable.get(font2D.handle);
                if (glyphToCharMapForFont == null) {
                    glyphToCharMapForFont = getGlyphToCharMapForFont(font2D);
                    hashtable.put(font2D.handle, glyphToCharMapForFont);
                }
            }
            char[] cArr2 = new char[numGlyphs];
            if (compositeFont != null) {
                for (int i4 = 0; i4 < numGlyphs; i4++) {
                    int i5 = glyphCodes[i4];
                    char[] cArr3 = cArr[i5 >>> 24];
                    int i6 = i5 & 16777215;
                    if (cArr3 == null) {
                        return false;
                    }
                    if (i6 == 65535) {
                        c3 = '\n';
                    } else {
                        if (i6 < 0 || i6 >= cArr3.length) {
                            return false;
                        }
                        c3 = cArr3[i6];
                    }
                    if (c3 != 65535) {
                        cArr2[i4] = c3;
                    } else {
                        return false;
                    }
                }
            } else {
                for (int i7 = 0; i7 < numGlyphs; i7++) {
                    int i8 = glyphCodes[i7];
                    if (i8 == 65535) {
                        c2 = '\n';
                    } else {
                        if (i8 < 0 || i8 >= glyphToCharMapForFont.length) {
                            return false;
                        }
                        c2 = glyphToCharMapForFont[i8];
                    }
                    if (c2 != 65535) {
                        cArr2[i7] = c2;
                    } else {
                        return false;
                    }
                }
            }
            FontRenderContext fontRenderContext = glyphVector.getFontRenderContext();
            GlyphVector glyphVectorCreateGlyphVector = font.createGlyphVector(fontRenderContext, cArr2);
            if (glyphVectorCreateGlyphVector.getNumGlyphs() != numGlyphs) {
                return printGlyphVector(glyphVector, f2, f3);
            }
            int[] glyphCodes2 = glyphVectorCreateGlyphVector.getGlyphCodes(0, numGlyphs, null);
            for (int i9 = 0; i9 < numGlyphs; i9++) {
                if (glyphCodes[i9] != glyphCodes2[i9]) {
                    return printGlyphVector(glyphVector, f2, f3);
                }
            }
            FontRenderContext fontRenderContext2 = getFontRenderContext();
            boolean zEquals = fontRenderContext.equals(fontRenderContext2);
            if (!zEquals && fontRenderContext.usesFractionalMetrics() == fontRenderContext2.usesFractionalMetrics()) {
                AffineTransform transform = fontRenderContext.getTransform();
                AffineTransform transform2 = getTransform();
                double[] dArr = new double[4];
                double[] dArr2 = new double[4];
                transform.getMatrix(dArr);
                transform2.getMatrix(dArr2);
                zEquals = true;
                int i10 = 0;
                while (true) {
                    if (i10 >= 4) {
                        break;
                    }
                    if (dArr[i10] == dArr2[i10]) {
                        i10++;
                    } else {
                        zEquals = false;
                        break;
                    }
                }
            }
            String str = new String(cArr2, 0, numGlyphs);
            int iPlatformFontCount = platformFontCount(font, str);
            if (iPlatformFontCount == 0) {
                return false;
            }
            float[] glyphPositions = glyphVector.getGlyphPositions(0, numGlyphs, null);
            boolean z2 = (layoutFlags & 2) == 0 || samePositions(glyphVectorCreateGlyphVector, glyphCodes2, glyphCodes, glyphPositions);
            float x2 = (float) glyphVector.getGlyphPosition(numGlyphs).getX();
            boolean z3 = false;
            if (font.hasLayoutAttributes() && this.printingGlyphVector && z2) {
                Object obj = font.getAttributes().get(TextAttribute.TRACKING);
                if ((obj == null || !(obj instanceof Number) || ((Number) obj).floatValue() == 0.0f) ? false : true) {
                    z2 = false;
                } else if (Math.abs(((float) font.getStringBounds(str, fontRenderContext).getWidth()) - x2) > 1.0E-5d) {
                    z3 = true;
                }
            }
            if (zEquals && z2 && !z3) {
                drawString(str, f2, f3, font, fontRenderContext, 0.0f);
                return true;
            }
            if (iPlatformFontCount == 1 && canDrawStringToWidth() && z2) {
                drawString(str, f2, f3, font, fontRenderContext, x2);
                return true;
            }
            if (FontUtilities.isComplexText(cArr2, 0, cArr2.length)) {
                return printGlyphVector(glyphVector, f2, f3);
            }
            if (numGlyphs > 10 && printGlyphVector(glyphVector, f2, f3)) {
                return true;
            }
            for (int i11 = 0; i11 < numGlyphs; i11++) {
                drawString(new String(cArr2, i11, 1), f2 + glyphPositions[i11 * 2], f3 + glyphPositions[(i11 * 2) + 1], font, fontRenderContext, 0.0f);
            }
            return true;
        }
    }

    private boolean samePositions(GlyphVector glyphVector, int[] iArr, int[] iArr2, float[] fArr) {
        int numGlyphs = glyphVector.getNumGlyphs();
        float[] glyphPositions = glyphVector.getGlyphPositions(0, numGlyphs, null);
        if (numGlyphs != iArr.length || iArr2.length != iArr.length || fArr.length != glyphPositions.length) {
            return false;
        }
        for (int i2 = 0; i2 < numGlyphs; i2++) {
            if (iArr[i2] != iArr2[i2] || glyphPositions[i2] != fArr[i2]) {
                return false;
            }
        }
        return true;
    }

    protected boolean canDrawStringToWidth() {
        return false;
    }

    private static char[] getGlyphToCharMapForFont(Font2D font2D) {
        int iCharToGlyph;
        int numGlyphs = font2D.getNumGlyphs();
        int missingGlyphCode = font2D.getMissingGlyphCode();
        char[] cArr = new char[numGlyphs];
        for (int i2 = 0; i2 < numGlyphs; i2++) {
            cArr[i2] = 65535;
        }
        char c2 = 0;
        while (true) {
            char c3 = c2;
            if (c3 < 65535) {
                if ((c3 < 55296 || c3 > 57343) && (iCharToGlyph = font2D.charToGlyph(c3)) != missingGlyphCode && iCharToGlyph >= 0 && iCharToGlyph < numGlyphs && cArr[iCharToGlyph] == 65535) {
                    cArr[iCharToGlyph] = c3;
                }
                c2 = (char) (c3 + 1);
            } else {
                return cArr;
            }
        }
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void draw(Shape shape) {
        fill(getStroke().createStrokedShape(shape));
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void fill(Shape shape) {
        try {
            fill(shape, (Color) getPaint());
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException("Expected a Color instance");
        }
    }

    public void fill(Shape shape, Color color) {
        AffineTransform transform = getTransform();
        if (getClip() != null) {
            deviceClip(getClip().getPathIterator(transform));
        }
        deviceFill(shape.getPathIterator(transform), color);
    }

    protected BufferedImage getBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        if (image instanceof ToolkitImage) {
            return ((ToolkitImage) image).getBufferedImage();
        }
        if (image instanceof VolatileImage) {
            return ((VolatileImage) image).getSnapshot();
        }
        return null;
    }

    protected boolean hasTransparentPixels(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean z2 = colorModel == null || colorModel.getTransparency() != 1;
        boolean z3 = z2;
        if (z3 && bufferedImage != null && (bufferedImage.getType() == 2 || bufferedImage.getType() == 3)) {
            DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
            SampleModel sampleModel = bufferedImage.getRaster().getSampleModel();
            if ((dataBuffer instanceof DataBufferInt) && (sampleModel instanceof SinglePixelPackedSampleModel)) {
                SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
                int[] iArrStealData = SunWritableRaster.stealData((DataBufferInt) dataBuffer, 0);
                int minX = bufferedImage.getMinX();
                int minY = bufferedImage.getMinY();
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                int scanlineStride = singlePixelPackedSampleModel.getScanlineStride();
                boolean z4 = false;
                for (int i2 = minY; i2 < minY + height; i2++) {
                    int i3 = i2 * scanlineStride;
                    int i4 = minX;
                    while (true) {
                        if (i4 >= minX + width) {
                            break;
                        }
                        if ((iArrStealData[i3 + i4] & (-16777216)) == -16777216) {
                            i4++;
                        } else {
                            z4 = true;
                            break;
                        }
                    }
                    if (z4) {
                        break;
                    }
                }
                if (!z4) {
                    z3 = false;
                }
            }
        }
        return z3;
    }

    protected boolean isBitmaskTransparency(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        return colorModel != null && colorModel.getTransparency() == 2;
    }

    protected boolean drawBitmaskImage(BufferedImage bufferedImage, AffineTransform affineTransform, Color color, int i2, int i3, int i4, int i5) {
        int i6;
        int i7;
        int i8;
        ColorModel colorModel = bufferedImage.getColorModel();
        if (!(colorModel instanceof IndexColorModel)) {
            return false;
        }
        IndexColorModel indexColorModel = (IndexColorModel) colorModel;
        if (colorModel.getTransparency() != 2) {
            return false;
        }
        if ((color != null && color.getAlpha() < 128) || (affineTransform.getType() & (-12)) != 0 || (getTransform().getType() & (-12)) != 0) {
            return false;
        }
        WritableRaster raster = bufferedImage.getRaster();
        int transparentPixel = indexColorModel.getTransparentPixel();
        byte[] bArr = new byte[indexColorModel.getMapSize()];
        indexColorModel.getAlphas(bArr);
        if (transparentPixel >= 0) {
            bArr[transparentPixel] = 0;
        }
        int width = raster.getWidth();
        int height = raster.getHeight();
        if (i2 > width || i3 > height) {
            return false;
        }
        if (i2 + i4 > width) {
            i6 = width;
            i7 = i6 - i2;
        } else {
            i6 = i2 + i4;
            i7 = i4;
        }
        if (i3 + i5 > height) {
            i8 = height;
            int i9 = i8 - i3;
        } else {
            i8 = i3 + i5;
        }
        int[] iArr = new int[i7];
        for (int i10 = i3; i10 < i8; i10++) {
            int i11 = -1;
            raster.getPixels(i2, i10, i7, 1, iArr);
            for (int i12 = i2; i12 < i6; i12++) {
                if (bArr[iArr[i12 - i2]] == 0) {
                    if (i11 >= 0) {
                        BufferedImage subimage = bufferedImage.getSubimage(i11, i10, i12 - i11, 1);
                        affineTransform.translate(i11, i10);
                        drawImageToPlatform(subimage, affineTransform, color, 0, 0, i12 - i11, 1, true);
                        affineTransform.translate(-i11, -i10);
                        i11 = -1;
                    }
                } else if (i11 < 0) {
                    i11 = i12;
                }
            }
            if (i11 >= 0) {
                BufferedImage subimage2 = bufferedImage.getSubimage(i11, i10, i6 - i11, 1);
                affineTransform.translate(i11, i10);
                drawImageToPlatform(subimage2, affineTransform, color, 0, 0, i6 - i11, 1, true);
                affineTransform.translate(-i11, -i10);
            }
        }
        return true;
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return drawImage(image, i2, i3, null, imageObserver);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver) {
        return drawImage(image, i2, i3, i4, i5, null, imageObserver);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        boolean zDrawImage;
        if (image == null) {
            return true;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width < 0 || height < 0) {
            zDrawImage = false;
        } else {
            zDrawImage = drawImage(image, i2, i3, width, height, color, imageObserver);
        }
        return zDrawImage;
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        boolean zDrawImage;
        if (image == null) {
            return true;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width < 0 || height < 0) {
            zDrawImage = false;
        } else {
            zDrawImage = drawImage(image, i2, i3, i2 + i4, i3 + i5, 0, 0, width, height, imageObserver);
        }
        return zDrawImage;
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver) {
        return drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, null, imageObserver);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width < 0 || height < 0) {
            return true;
        }
        float f2 = (i4 - i2) / (i8 - i6);
        float f3 = (i5 - i3) / (i9 - i7);
        AffineTransform affineTransform = new AffineTransform(f2, 0.0f, 0.0f, f3, i2 - (i6 * f2), i3 - (i7 * f3));
        if (i8 < i6) {
            i6 = i8;
            i8 = i6;
        }
        if (i9 < i7) {
            i7 = i9;
            i9 = i7;
        }
        if (i6 < 0) {
            i6 = 0;
        } else if (i6 > width) {
            i6 = width;
        }
        if (i8 < 0) {
            i8 = 0;
        } else if (i8 > width) {
            i8 = width;
        }
        if (i7 < 0) {
            i7 = 0;
        } else if (i7 > height) {
            i7 = height;
        }
        if (i9 < 0) {
            i9 = 0;
        } else if (i9 > height) {
            i9 = height;
        }
        int i10 = i8 - i6;
        int i11 = i9 - i7;
        if (i10 <= 0 || i11 <= 0) {
            return true;
        }
        return drawImageToPlatform(image, affineTransform, color, i6, i7, i10, i11, false);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public boolean drawImage(Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        boolean zDrawImageToPlatform;
        if (image == null) {
            return true;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width < 0 || height < 0) {
            zDrawImageToPlatform = false;
        } else {
            zDrawImageToPlatform = drawImageToPlatform(image, affineTransform, null, 0, 0, width, height, false);
        }
        return zDrawImageToPlatform;
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        if (bufferedImage == null) {
            return;
        }
        int width = bufferedImage.getWidth(null);
        int height = bufferedImage.getHeight(null);
        if (bufferedImageOp != null) {
            bufferedImage = bufferedImageOp.filter(bufferedImage, null);
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        drawImageToPlatform(bufferedImage, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, i2, i3), null, 0, 0, width, height, false);
    }

    @Override // sun.print.ProxyGraphics2D, java.awt.Graphics2D
    public void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform) {
        BufferedImage bufferedImage;
        if (renderedImage == null) {
            return;
        }
        int width = renderedImage.getWidth();
        int height = renderedImage.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (renderedImage instanceof BufferedImage) {
            bufferedImage = (BufferedImage) renderedImage;
        } else {
            bufferedImage = new BufferedImage(width, height, 2);
            bufferedImage.createGraphics().drawRenderedImage(renderedImage, affineTransform);
        }
        drawImageToPlatform(bufferedImage, affineTransform, null, 0, 0, width, height, false);
    }
}
