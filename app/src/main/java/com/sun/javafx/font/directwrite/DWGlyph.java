package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/DWGlyph.class */
public class DWGlyph implements Glyph {
    private DWFontStrike strike;
    private DWRITE_GLYPH_METRICS metrics;
    private DWRITE_GLYPH_RUN run;
    private float pixelXAdvance;
    private float pixelYAdvance;
    private RECT rect;
    private boolean drawShapes;
    private byte[][] pixelData;
    private RECT[] rects;
    private static final boolean CACHE_TARGET = true;
    private static IWICBitmap cachedBitmap;
    private static ID2D1RenderTarget cachedTarget;
    private static final int BITMAP_WIDTH = 256;
    private static final int BITMAP_HEIGHT = 256;
    private static final int BITMAP_PIXEL_FORMAT = 8;
    private static D2D1_COLOR_F BLACK = new D2D1_COLOR_F(0.0f, 0.0f, 0.0f, 1.0f);
    private static D2D1_COLOR_F WHITE = new D2D1_COLOR_F(1.0f, 1.0f, 1.0f, 1.0f);
    private static D2D1_MATRIX_3X2_F D2D2_MATRIX_IDENTITY = new D2D1_MATRIX_3X2_F(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);

    /* JADX WARN: Type inference failed for: r1v3, types: [byte[], byte[][]] */
    DWGlyph(DWFontStrike strike, int glyphCode, boolean drawShapes) {
        this.strike = strike;
        this.drawShapes = drawShapes;
        int size = DWFontStrike.SUBPIXEL_Y ? 9 : 3;
        this.pixelData = new byte[size];
        this.rects = new RECT[size];
        IDWriteFontFace face = strike.getFontFace();
        this.run = new DWRITE_GLYPH_RUN();
        this.run.fontFace = face != null ? face.ptr : 0L;
        this.run.fontEmSize = strike.getSize();
        this.run.glyphIndices = (short) glyphCode;
        this.run.glyphAdvances = 0.0f;
        this.run.advanceOffset = 0.0f;
        this.run.ascenderOffset = 0.0f;
        this.run.bidiLevel = 0;
        this.run.isSideways = false;
    }

    void checkMetrics() {
        IDWriteFontFace face;
        if (this.metrics == null && (face = this.strike.getFontFace()) != null) {
            this.metrics = face.GetDesignGlyphMetrics(this.run.glyphIndices, false);
            if (this.metrics != null) {
                float upem = this.strike.getUpem();
                this.pixelXAdvance = (this.metrics.advanceWidth * this.strike.getSize()) / upem;
                this.pixelYAdvance = 0.0f;
                if (this.strike.matrix != null) {
                    Point2D pt = new Point2D(this.pixelXAdvance, this.pixelYAdvance);
                    this.strike.getTransform().transform(pt, pt);
                    this.pixelXAdvance = pt.f11907x;
                    this.pixelYAdvance = pt.f11908y;
                }
            }
        }
    }

    void checkBounds() {
        if (this.rect != null) {
            return;
        }
        IDWriteGlyphRunAnalysis runAnalysis = createAnalysis(0.0f, 0.0f);
        if (runAnalysis != null) {
            this.rect = runAnalysis.GetAlphaTextureBounds(1);
            if (this.rect == null || this.rect.right - this.rect.left == 0 || this.rect.bottom - this.rect.top == 0) {
                this.rect = runAnalysis.GetAlphaTextureBounds(0);
            }
            runAnalysis.Release();
        }
        if (this.rect == null) {
            this.rect = new RECT();
            return;
        }
        this.rect.left--;
        this.rect.top--;
        this.rect.right++;
        this.rect.bottom++;
    }

    byte[] getLCDMask(float subPixelX, float subPixelY) {
        IDWriteGlyphRunAnalysis runAnalysis = createAnalysis(subPixelX, subPixelY);
        byte[] buffer = null;
        if (runAnalysis != null) {
            this.rect = runAnalysis.GetAlphaTextureBounds(1);
            if (this.rect != null && this.rect.right - this.rect.left != 0 && this.rect.bottom - this.rect.top != 0) {
                buffer = runAnalysis.CreateAlphaTexture(1, this.rect);
            } else {
                this.rect = runAnalysis.GetAlphaTextureBounds(0);
                if (this.rect != null && this.rect.right - this.rect.left != 0 && this.rect.bottom - this.rect.top != 0) {
                    buffer = getD2DMask(subPixelX, subPixelY, true);
                }
            }
            runAnalysis.Release();
        }
        if (buffer == null) {
            buffer = new byte[0];
            this.rect = new RECT();
        }
        return buffer;
    }

    byte[] getD2DMask(float subPixelX, float subPixelY, boolean lcd) {
        IWICBitmap bitmap;
        ID2D1RenderTarget target;
        D2D1_MATRIX_3X2_F transform;
        float glyphX;
        float glyphY;
        checkBounds();
        if (getWidth() == 0 || getHeight() == 0 || this.run.fontFace == 0) {
            return new byte[0];
        }
        float glyphX2 = this.rect.left;
        float glyphY2 = this.rect.top;
        int w2 = this.rect.right - this.rect.left;
        int h2 = this.rect.bottom - this.rect.top;
        boolean cache = 256 >= w2 && 256 >= h2;
        if (cache) {
            bitmap = getCachedBitmap();
            target = getCachedRenderingTarget();
        } else {
            bitmap = createBitmap(w2, h2);
            target = createRenderingTarget(bitmap);
        }
        if (bitmap == null || target == null) {
            return new byte[0];
        }
        DWRITE_MATRIX matrix = this.strike.matrix;
        if (matrix != null) {
            transform = new D2D1_MATRIX_3X2_F(matrix.m11, matrix.m12, matrix.m21, matrix.m22, (-glyphX2) + subPixelX, (-glyphY2) + subPixelY);
            glyphY = 0.0f;
            glyphX = 0.0f;
        } else {
            transform = D2D2_MATRIX_IDENTITY;
            glyphX = glyphX2 - subPixelX;
            glyphY = glyphY2 - subPixelY;
        }
        target.BeginDraw();
        target.SetTransform(transform);
        target.Clear(WHITE);
        D2D1_POINT_2F pt = new D2D1_POINT_2F(-glyphX, -glyphY);
        ID2D1Brush brush = target.CreateSolidColorBrush(BLACK);
        if (!lcd) {
            target.SetTextAntialiasMode(2);
        }
        target.DrawGlyphRun(pt, this.run, brush, 0);
        int hr = target.EndDraw();
        brush.Release();
        if (hr != 0) {
            bitmap.Release();
            cachedBitmap = null;
            target.Release();
            cachedTarget = null;
            if (PrismFontFactory.debugFonts) {
                System.err.println("Rendering failed=" + hr);
            }
            RECT rect = this.rect;
            RECT rect2 = this.rect;
            RECT rect3 = this.rect;
            this.rect.bottom = 0;
            rect3.right = 0;
            rect2.top = 0;
            rect.left = 0;
            return null;
        }
        byte[] result = null;
        IWICBitmapLock lock = bitmap.Lock(0, 0, w2, h2, 1);
        if (lock != null) {
            byte[] buffer = lock.GetDataPointer();
            if (buffer != null) {
                int stride = lock.GetStride();
                int i2 = 0;
                int j2 = 0;
                if (lcd) {
                    result = new byte[w2 * h2 * 3];
                    for (int y2 = 0; y2 < h2; y2++) {
                        int row = j2;
                        for (int x2 = 0; x2 < w2; x2++) {
                            int i3 = i2;
                            int i4 = i2 + 1;
                            int i5 = row;
                            int row2 = row + 1;
                            result[i3] = (byte) ((-1) - buffer[i5]);
                            int i6 = i4 + 1;
                            int row3 = row2 + 1;
                            result[i4] = (byte) ((-1) - buffer[row2]);
                            i2 = i6 + 1;
                            result[i6] = (byte) ((-1) - buffer[row3]);
                            row = row3 + 1 + 1;
                        }
                        j2 += stride;
                    }
                } else {
                    result = new byte[w2 * h2];
                    for (int y3 = 0; y3 < h2; y3++) {
                        int row4 = j2;
                        for (int x3 = 0; x3 < w2; x3++) {
                            int i7 = i2;
                            i2++;
                            result[i7] = (byte) ((-1) - buffer[row4]);
                            row4 += 4;
                        }
                        j2 += stride;
                    }
                }
            }
            lock.Release();
        }
        if (!cache) {
            bitmap.Release();
            target.Release();
        }
        return result;
    }

    IDWriteGlyphRunAnalysis createAnalysis(float x2, float y2) {
        if (this.run.fontFace == 0) {
            return null;
        }
        IDWriteFactory factory = DWFactory.getDWriteFactory();
        int renderingMode = DWFontStrike.SUBPIXEL_Y ? 5 : 4;
        DWRITE_MATRIX matrix = this.strike.matrix;
        return factory.CreateGlyphRunAnalysis(this.run, 1.0f, matrix, renderingMode, 0, x2, y2);
    }

    IWICBitmap getCachedBitmap() {
        if (cachedBitmap == null) {
            cachedBitmap = createBitmap(256, 256);
        }
        return cachedBitmap;
    }

    ID2D1RenderTarget getCachedRenderingTarget() {
        if (cachedTarget == null) {
            cachedTarget = createRenderingTarget(getCachedBitmap());
        }
        return cachedTarget;
    }

    IWICBitmap createBitmap(int width, int height) {
        IWICImagingFactory factory = DWFactory.getWICFactory();
        return factory.CreateBitmap(width, height, 8, 1);
    }

    ID2D1RenderTarget createRenderingTarget(IWICBitmap bitmap) {
        D2D1_RENDER_TARGET_PROPERTIES prop = new D2D1_RENDER_TARGET_PROPERTIES();
        prop.type = 0;
        prop.pixelFormat.format = 0;
        prop.pixelFormat.alphaMode = 0;
        prop.dpiX = 0.0f;
        prop.dpiY = 0.0f;
        prop.usage = 0;
        prop.minLevel = 0;
        ID2D1Factory factory = DWFactory.getD2DFactory();
        return factory.CreateWicBitmapRenderTarget(bitmap, prop);
    }

    @Override // com.sun.javafx.font.Glyph
    public int getGlyphCode() {
        return this.run.glyphIndices;
    }

    @Override // com.sun.javafx.font.Glyph
    public RectBounds getBBox() {
        return this.strike.getBBox(this.run.glyphIndices);
    }

    @Override // com.sun.javafx.font.Glyph
    public float getAdvance() {
        checkMetrics();
        if (this.metrics == null) {
            return 0.0f;
        }
        float upem = this.strike.getUpem();
        return (this.metrics.advanceWidth * this.strike.getSize()) / upem;
    }

    @Override // com.sun.javafx.font.Glyph
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.run.glyphIndices);
    }

    @Override // com.sun.javafx.font.Glyph
    public byte[] getPixelData() {
        return getPixelData(0);
    }

    @Override // com.sun.javafx.font.Glyph
    public byte[] getPixelData(int subPixel) {
        byte[] data = this.pixelData[subPixel];
        if (data == null) {
            float x2 = 0.0f;
            float y2 = 0.0f;
            int index = subPixel;
            if (index >= 6) {
                index -= 6;
                y2 = 0.66f;
            } else if (index >= 3) {
                index -= 3;
                y2 = 0.33f;
            }
            if (index == 1) {
                x2 = 0.33f;
            }
            if (index == 2) {
                x2 = 0.66f;
            }
            byte[][] bArr = this.pixelData;
            byte[] lCDMask = isLCDGlyph() ? getLCDMask(x2, y2) : getD2DMask(x2, y2, false);
            data = lCDMask;
            bArr[subPixel] = lCDMask;
            this.rects[subPixel] = this.rect;
        } else {
            this.rect = this.rects[subPixel];
        }
        return data;
    }

    @Override // com.sun.javafx.font.Glyph
    public float getPixelXAdvance() {
        checkMetrics();
        return this.pixelXAdvance;
    }

    @Override // com.sun.javafx.font.Glyph
    public float getPixelYAdvance() {
        checkMetrics();
        return this.pixelYAdvance;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getWidth() {
        checkBounds();
        return (this.rect.right - this.rect.left) * (isLCDGlyph() ? 3 : 1);
    }

    @Override // com.sun.javafx.font.Glyph
    public int getHeight() {
        checkBounds();
        return this.rect.bottom - this.rect.top;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getOriginX() {
        checkBounds();
        return this.rect.left;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getOriginY() {
        checkBounds();
        return this.rect.top;
    }

    @Override // com.sun.javafx.font.Glyph
    public boolean isLCDGlyph() {
        return this.strike.getAAMode() == 1;
    }
}
