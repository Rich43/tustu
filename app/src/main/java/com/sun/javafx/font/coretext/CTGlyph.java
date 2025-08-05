package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/font/coretext/CTGlyph.class */
class CTGlyph implements Glyph {
    private CTFontStrike strike;
    private int glyphCode;
    private CGRect bounds;
    private double xAdvance;
    private double yAdvance;
    private boolean drawShapes;
    private static long cachedContextRef;
    private static final int BITMAP_WIDTH = 256;
    private static final int BITMAP_HEIGHT = 256;
    private static final int MAX_SIZE = 320;
    private static boolean LCD_CONTEXT = true;
    private static boolean CACHE_CONTEXT = true;
    private static final long GRAY_COLORSPACE = OS.CGColorSpaceCreateDeviceGray();
    private static final long RGB_COLORSPACE = OS.CGColorSpaceCreateDeviceRGB();

    CTGlyph(CTFontStrike strike, int glyphCode, boolean drawShapes) {
        this.strike = strike;
        this.glyphCode = glyphCode;
        this.drawShapes = drawShapes;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getGlyphCode() {
        return this.glyphCode;
    }

    @Override // com.sun.javafx.font.Glyph
    public RectBounds getBBox() {
        CGRect rect = this.strike.getBBox(this.glyphCode);
        return rect == null ? new RectBounds() : new RectBounds((float) rect.origin.f11884x, (float) rect.origin.f11885y, (float) (rect.origin.f11884x + rect.size.width), (float) (rect.origin.f11885y + rect.size.height));
    }

    /* JADX WARN: Type inference failed for: r1v30, types: [com.sun.javafx.font.coretext.CGPoint, double] */
    /* JADX WARN: Type inference failed for: r2v17, types: [com.sun.javafx.font.coretext.CGSize, double] */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.sun.javafx.font.coretext.CGPoint] */
    private void checkBounds() {
        if (this.bounds != null) {
            return;
        }
        this.bounds = new CGRect();
        if (this.strike.getSize() == 0.0f) {
            return;
        }
        long fontRef = this.strike.getFontRef();
        if (fontRef == 0) {
            return;
        }
        CGSize size = new CGSize();
        OS.CTFontGetAdvancesForGlyphs(fontRef, 0, (short) this.glyphCode, size);
        this.xAdvance = size.width;
        this.yAdvance = -size.height;
        if (this.drawShapes) {
            return;
        }
        CTFontFile fr = this.strike.getFontResource();
        float[] bb2 = new float[4];
        fr.getGlyphBoundingBox((short) this.glyphCode, this.strike.getSize(), bb2);
        this.bounds.origin.f11884x = bb2[0];
        this.bounds.origin.f11885y = bb2[1];
        this.bounds.size.width = bb2[2] - bb2[0];
        this.bounds.size.height = bb2[3] - bb2[1];
        if (this.strike.matrix != null) {
            OS.CGRectApplyAffineTransform(this.bounds, this.strike.matrix);
        }
        if (this.bounds.size.width < 0.0d || this.bounds.size.height < 0.0d || this.bounds.size.width > 320.0d || this.bounds.size.height > 320.0d) {
            CGPoint cGPoint = this.bounds.origin;
            ?? r1 = this.bounds.origin;
            ?? r2 = this.bounds.size;
            ?? r4 = 0;
            this.bounds.size.height = 0.0d;
            r2.width = 0.0d;
            r4.f11885y = r1;
            r1.f11884x = r2;
            return;
        }
        this.bounds.origin.f11884x = ((int) Math.floor(this.bounds.origin.f11884x)) - 1;
        this.bounds.origin.f11885y = ((int) Math.floor(this.bounds.origin.f11885y)) - 1;
        this.bounds.size.width = ((int) Math.ceil(this.bounds.size.width)) + 1 + 1 + 1;
        this.bounds.size.height = ((int) Math.ceil(this.bounds.size.height)) + 1 + 1 + 1;
    }

    @Override // com.sun.javafx.font.Glyph
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.glyphCode);
    }

    private long createContext(boolean lcd, int width, int height) {
        long space;
        int bpr;
        int flags;
        if (lcd) {
            space = RGB_COLORSPACE;
            bpr = width * 4;
            flags = OS.kCGBitmapByteOrder32Host | 2;
        } else {
            space = GRAY_COLORSPACE;
            bpr = width;
            flags = 0;
        }
        long context = OS.CGBitmapContextCreate(0L, width, height, 8, bpr, space, flags);
        boolean subPixel = this.strike.isSubPixelGlyph();
        OS.CGContextSetAllowsFontSmoothing(context, lcd);
        OS.CGContextSetAllowsAntialiasing(context, true);
        OS.CGContextSetAllowsFontSubpixelPositioning(context, subPixel);
        OS.CGContextSetAllowsFontSubpixelQuantization(context, subPixel);
        return context;
    }

    private long getCachedContext(boolean lcd) {
        if (cachedContextRef == 0) {
            cachedContextRef = createContext(lcd, 256, 256);
        }
        return cachedContextRef;
    }

    private synchronized byte[] getImage(double x2, double y2, int w2, int h2, int subPixel) {
        byte[] imageData;
        if (w2 == 0 || h2 == 0) {
            return new byte[0];
        }
        long fontRef = this.strike.getFontRef();
        boolean lcd = isLCDGlyph();
        boolean lcdContext = LCD_CONTEXT || lcd;
        CGAffineTransform matrix = this.strike.matrix;
        boolean cache = CACHE_CONTEXT & (256 >= w2) & (256 >= h2);
        long context = cache ? getCachedContext(lcdContext) : createContext(lcdContext, w2, h2);
        if (context == 0) {
            return new byte[0];
        }
        OS.CGContextSetRGBFillColor(context, 1.0d, 1.0d, 1.0d, 1.0d);
        CGRect rect = new CGRect();
        rect.size.width = w2;
        rect.size.height = h2;
        OS.CGContextFillRect(context, rect);
        double drawX = 0.0d;
        double drawY = 0.0d;
        if (matrix != null) {
            OS.CGContextTranslateCTM(context, -x2, -y2);
        } else {
            drawX = x2 - this.strike.getSubPixelPosition(subPixel);
            drawY = y2;
        }
        OS.CGContextSetRGBFillColor(context, 0.0d, 0.0d, 0.0d, 1.0d);
        OS.CTFontDrawGlyphs(fontRef, (short) this.glyphCode, -drawX, -drawY, context);
        if (matrix != null) {
            OS.CGContextTranslateCTM(context, x2, y2);
        }
        if (lcd) {
            imageData = OS.CGBitmapContextGetData(context, w2, h2, 24);
        } else {
            imageData = OS.CGBitmapContextGetData(context, w2, h2, 8);
        }
        if (imageData == null) {
            this.bounds = new CGRect();
            imageData = new byte[0];
        }
        if (!cache) {
            OS.CGContextRelease(context);
        }
        return imageData;
    }

    @Override // com.sun.javafx.font.Glyph
    public byte[] getPixelData() {
        return getPixelData(0);
    }

    @Override // com.sun.javafx.font.Glyph
    public byte[] getPixelData(int subPixel) {
        checkBounds();
        return getImage(this.bounds.origin.f11884x, this.bounds.origin.f11885y, (int) this.bounds.size.width, (int) this.bounds.size.height, subPixel);
    }

    @Override // com.sun.javafx.font.Glyph
    public float getAdvance() {
        checkBounds();
        return (float) this.xAdvance;
    }

    @Override // com.sun.javafx.font.Glyph
    public float getPixelXAdvance() {
        checkBounds();
        return (float) this.xAdvance;
    }

    @Override // com.sun.javafx.font.Glyph
    public float getPixelYAdvance() {
        checkBounds();
        return (float) this.yAdvance;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getWidth() {
        checkBounds();
        int w2 = (int) this.bounds.size.width;
        return isLCDGlyph() ? w2 * 3 : w2;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getHeight() {
        checkBounds();
        return (int) this.bounds.size.height;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getOriginX() {
        checkBounds();
        return (int) this.bounds.origin.f11884x;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getOriginY() {
        checkBounds();
        int h2 = (int) this.bounds.size.height;
        int y2 = (int) this.bounds.origin.f11885y;
        return (-h2) - y2;
    }

    @Override // com.sun.javafx.font.Glyph
    public boolean isLCDGlyph() {
        return this.strike.getAAMode() == 1;
    }
}
